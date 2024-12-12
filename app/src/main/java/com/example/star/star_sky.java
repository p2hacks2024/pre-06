package com.example.star;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class star_sky extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_star_sky);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton toMain = (ImageButton) findViewById(R.id.imageButton);
        toMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(star_sky.this,MainActivity.class);
                startActivity(intent);
            }
        });
        ImageButton toCamera = (ImageButton) findViewById(R.id.imageButton2);
        toCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(star_sky.this,Camera.class);
                startActivity(intent);
            }
        });
        ImageButton toMemo = (ImageButton) findViewById(R.id.imageButton3);
        toMemo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(star_sky.this,Memo.class);
                startActivity(intent);
            }
        });

    }
}