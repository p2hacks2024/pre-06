package com.example.star;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        int totalButtons = 15;

        //初期画面配列作成

        for(int i = 0; i<totalButtons; i++){
            ImageButton imageButton = new ImageButton(this);


            imageButton.setLayoutParams(new ViewGroup.LayoutParams(350,350));
            imageButton.setImageResource(R.drawable.demo1);
            imageButton.setId(View.generateViewId());
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ボタンがクリックされたときの処理をここに記述
                    Toast.makeText(MainActivity.this, "Button clicked!", Toast.LENGTH_SHORT).show();
                }

        });
            gridLayout.addView(imageButton);
        }

        }
    }


