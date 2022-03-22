package com.android.artbook;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.artbook.databinding.ActivityArtaddBinding;
import com.android.artbook.databinding.ActivityDetailsBinding;

public class Details extends AppCompatActivity {
    private ImageView image;
    private TextView bookName, bookAuthor, bookSummary;
    private String bookname, bookauthor, booksummary;
    private Bitmap bookImage;
private ActivityDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        bookName.setText(bookname);
        bookAuthor.setText(bookauthor);
        bookSummary.setText(booksummary);
        image.setImageBitmap(bookImage);
        init();
    }
    private  void init(){
      image=findViewById(R.id.artImage);
        bookName=findViewById(R.id.bookName);
        bookSummary=findViewById(R.id.bookSummary);
        bookAuthor=findViewById(R.id.bookAuthor);
    }
}