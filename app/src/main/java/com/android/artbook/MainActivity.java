package com.android.artbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.artbook.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    ArrayList<Art> artList;
    ArtAdapter artadapter;
    static public ArtDetails artDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        artList = new ArrayList<Art>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        artadapter = new ArtAdapter(artList);
        binding.recyclerView.setAdapter(artadapter);
        getData();

    }

    private void getData() {
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("select*from arts", null);
            int nameIndex = cursor.getColumnIndex("artName");
            int idIndex = cursor.getColumnIndex("id");
            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIndex);
                int id = cursor.getInt(idIndex);
                Art art = new Art(name, id);
                artList.add(art);
            }
            artadapter.notifyDataSetChanged();
            cursor.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sanat_menusu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.eserEkle) {
            Intent intent = new Intent(this, ArtAdd.class);
            intent.putExtra("info", "new");

            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}