package com.android.artbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.artbook.databinding.ActivityArtaddBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class ArtAdd extends AppCompatActivity {
    private ActivityArtaddBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionResultLauncher;
    Bitmap selectedImage;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtaddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        registerLauncher();
        database = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null);

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        if (info.matches("new")) {
            binding.artName.setText("");
            binding.artistName.setText("");
            binding.makeAge.setText("");
            binding.save.setVisibility(View.VISIBLE);
            Bitmap selectImage = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.image);
            binding.imageView.setImageBitmap(selectImage);
        } else {
            int artId = intent.getIntExtra("artId", 1);
            binding.save.setVisibility(View.INVISIBLE);
            try {
                Cursor cursor = database.rawQuery("select*from arts where id=?", new String[]{String.valueOf(artId)});
                int artNameIndex = cursor.getColumnIndex("artName");
                int painterNameIndex = cursor.getColumnIndex("artistName");
                int yearIndex = cursor.getColumnIndex("year");
                int imageIndex = cursor.getColumnIndex("image");
                while (cursor.moveToNext()) {
                    binding.artName.setText(cursor.getString(artNameIndex));
                    binding.artistName.setText(cursor.getString(painterNameIndex));
                    binding.makeAge.setText(cursor.getString(yearIndex));
                    byte[] bytes = cursor.getBlob(imageIndex);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    binding.imageView.setImageBitmap(bitmap);
                }
                cursor.close();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    }

    public void save(View view) {
        String artName = binding.artName.getText().toString();
        String artistName = binding.artistName.getText().toString();
        String year = binding.makeAge.getText().toString();
        Bitmap smallImage = makeSmallerImage(selectedImage, 300);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        try {//veritabanı işlemleri
            database = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null);
            database.execSQL("create table if not exists arts(id Integer primary key,artName varchar,artistName varchar,year varchar,image Blob)");
            String sql = "insert into arts(artName,artistName,year,image)values(?,?,?,?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sql);
            sqLiteStatement.bindString(1, artName);
            sqLiteStatement.bindString(2, artistName);
            sqLiteStatement.bindString(3, year);
            sqLiteStatement.bindBlob(4, byteArray);
            sqLiteStatement.execute();


        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public Bitmap makeSmallerImage(Bitmap image, int maximumSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRation = (float) width / (float) height;
        if (bitmapRation > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRation);
        } else {
            height = maximumSize;
            width = (int) (height * bitmapRation);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void selectImage(View view) {
        //izin isteme kodları
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view, "Resim seçmek için izin vermelisiniz", Snackbar.LENGTH_INDEFINITE).setAction("izin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            } else {
                permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }

    }

    public void registerLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Uri image = intent.getData();
                        //  binding.imageView.setImageURI(image);
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                ImageDecoder.Source source = ImageDecoder.createSource(ArtAdd.this.getContentResolver(), image);
                                selectedImage = ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(selectedImage);
                            } else {
                                selectedImage = MediaStore.Images.Media.getBitmap(ArtAdd.this.getContentResolver(), image);
                            }

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
        });
        permissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Intent galeriIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(galeriIntent);
                } else {
                    Toast.makeText(ArtAdd.this, "izin gerekli", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}