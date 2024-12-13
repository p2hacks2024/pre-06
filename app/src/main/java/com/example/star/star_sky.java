package com.example.star;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class star_sky extends AppCompatActivity {

    private RelativeLayout buttonContainer;
    private Random random = new Random();
    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "button_prefs";
    private static final String BUTTON_COUNT_KEY = "button_count";

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
        buttonContainer = findViewById(R.id.buttonContainer);

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewButton();
            }
        });
        loadButtons();
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

    private void addNewButton() {
        // 新しいボタンを作成
        ImageButton newImageButton = new ImageButton(this);
        newImageButton.setImageResource(R.drawable.demo);

        // 画像ボタンのレイアウトパラメータを設定
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                100,  // 幅を100ピクセルに設定
                100   // 高さを100ピクセルに設定
        );

        // ボタンの位置を重ならないようにランダムに設定
        int[] position = findNonOverlappingPosition(params.width, params.height);
        params.leftMargin = position[0];
        params.topMargin = position[1];

        newImageButton.setLayoutParams(params);

        // 追加したボタンにクリックイベントを設定
        newImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ボタンクリック時の処理
                Log.d(TAG, "New button clicked!");
            }
        });

        // ボタンをコンテナに追加
        buttonContainer.addView(newImageButton);
        saveButtonInfo(position[0], position[1]);
    }

    private int[] findNonOverlappingPosition(int buttonWidth, int buttonHeight) {
        int maxWidth = buttonContainer.getWidth() - buttonWidth;
        int maxHeight = buttonContainer.getHeight() - buttonHeight;
        int leftMargin, topMargin;

        boolean isOverlapping;
        do {
            leftMargin = random.nextInt(maxWidth);
            topMargin = random.nextInt(maxHeight);
            isOverlapping = false;

            // 既存のボタンと重なりを確認
            for (int i = 0; i < buttonContainer.getChildCount(); i++) {
                View child = buttonContainer.getChildAt(i);
                int childLeft = child.getLeft();
                int childTop = child.getTop();
                int childRight = childLeft + child.getWidth();
                int childBottom = childTop + child.getHeight();

                // ボタンが重なっているか確認
                if (leftMargin < childRight && leftMargin + buttonWidth > childLeft &&
                        topMargin < childBottom && topMargin + buttonHeight > childTop) {
                    isOverlapping = true;
                    break;
                }
            }
        } while (isOverlapping);

        return new int[]{leftMargin, topMargin};
    }

    private void saveButtonInfo(int leftMargin, int topMargin) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit(); int buttonCount = prefs.getInt(BUTTON_COUNT_KEY, 0);
        buttonCount++; editor.putInt(BUTTON_COUNT_KEY, buttonCount);
        editor.putInt("button_left_" + buttonCount, leftMargin); editor.putInt("button_top_" + buttonCount, topMargin);
        editor.apply();
    }

    private void loadButtons() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int buttonCount = prefs.getInt(BUTTON_COUNT_KEY, 0);

        for (int i = 1; i <= buttonCount; i++) {
            int leftMargin = prefs.getInt("button_left_" + i, 0);
            int topMargin = prefs.getInt("button_top_" + i, 0);

            // 保存されたボタンを再追加
            ImageButton newImageButton = new ImageButton(this);
            newImageButton.setImageResource(R.drawable.demo);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    100,  // 幅を100ピクセルに設定
                    100   // 高さを100ピクセルに設定
            );
            params.leftMargin = leftMargin;
            params.topMargin = topMargin;
            newImageButton.setLayoutParams(params);

            newImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ボタンクリック時の処理
                    Log.d(TAG, "New button clicked!");
                }
            });

            buttonContainer.addView(newImageButton);
        }
    }


}