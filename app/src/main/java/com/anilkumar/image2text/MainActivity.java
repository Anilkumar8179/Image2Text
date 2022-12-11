package com.anilkumar.image2text;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView book_image;
    TextView textView_Welcome;
    Button button_scanning;

     Bitmap bitmap;
     static final int REQUEST_IMAGE_CAPTURE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        book_image=findViewById(R.id.book_image);
        textView_Welcome=findViewById(R.id.textView_welcome);
        button_scanning=findViewById(R.id.button_scanning);
        button_scanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(MainActivity.this,Activity_Scanner.class);
                startActivity(intent);

            }
        });

    }
}