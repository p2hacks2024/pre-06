package com.example.star;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsCompat.Type;

import java.util.Random;

public class star_sky extends AppCompatActivity {

    private RelativeLayout buttonContainer;
    private Random random = new Random();
    private static final String PREFS_NAME = "button_prefs";
    private static final String BUTTON_COUNT_KEY = "button_count";
    private static final String MEMO_ID_COUNTER_KEY = "memo_id_counter";
    private int memoIdCounter;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_sky);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonContainer = findViewById(R.id.buttonContainer);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        memoIdCounter = prefs.getInt(MEMO_ID_COUNTER_KEY, 0);

        //Button addButton = findViewById(R.id.addButton);
        //addButton.setOnClickListener(v -> addNewButton(null));  // URI引数なしで呼び出し

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(star_sky.this, Animation.class);
                startActivity(intent);
            }
        });


        ImageButton toFile01 = findViewById(R.id.cameraButton);
        toFile01.setOnClickListener(v -> {
            Intent intent = new Intent(star_sky.this, Camera.class);
            intent.putExtra("MEMO_ID_COUNTER", memoIdCounter);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        });

        loadButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String photoUriString = data.getStringExtra("PHOTO_URI");
            int memoId = data.getIntExtra("MEMO_ID", -1);

            if (photoUriString != null && memoId != -1) {
                Uri photoUri = Uri.parse(photoUriString);
                addNewButton(photoUri);  // ボタンの追加

                // メモ画面に遷移する
                openMemoScreen(memoId, photoUri);
            }
        }
    }

    private void addNewButton(@Nullable Uri photoUri) {
        ImageButton newImageButton = new ImageButton(this);
        newImageButton.setImageResource(R.drawable.demo); // デフォルトの画像

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                100,  // 幅を100ピクセルに設定
                100   // 高さを100ピクセルに設定
        );

        int[] position = findNonOverlappingPosition(params.width, params.height);
        params.leftMargin = position[0];
        params.topMargin = position[1];
        newImageButton.setLayoutParams(params);

        final int memoId = memoIdCounter; // 更新前のカウンター値を使う
        newImageButton.setOnClickListener(v -> openMemoScreen(memoId, photoUri));

        buttonContainer.addView(newImageButton);
        saveButtonInfo(position[0], position[1], memoId);
    }

    private void openMemoScreen(int memoId, @Nullable Uri photoUri) {
        Intent intent = new Intent(star_sky.this, Memo.class);
        intent.putExtra("MEMO_ID", memoId);
        if (photoUri != null) {
            intent.putExtra("PHOTO_URI", photoUri.toString());
        }
        startActivity(intent);
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

            for (int i = 0; i < buttonContainer.getChildCount(); i++) {
                View child = buttonContainer.getChildAt(i);
                int childLeft = child.getLeft();
                int childTop = child.getTop();
                int childRight = childLeft + child.getWidth();
                int childBottom = childTop + child.getHeight();

                if (leftMargin < childRight && leftMargin + buttonWidth > childLeft &&
                        topMargin < childBottom && topMargin + buttonHeight > childTop) {
                    isOverlapping = true;
                    break;
                }
            }
        } while (isOverlapping);

        return new int[]{leftMargin, topMargin};
    }

    private void saveButtonInfo(int leftMargin, int topMargin, int memoId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int buttonCount = prefs.getInt(BUTTON_COUNT_KEY, 0);
        buttonCount++;
        editor.putInt(BUTTON_COUNT_KEY, buttonCount);
        editor.putInt("button_left_" + buttonCount, leftMargin);
        editor.putInt("button_top_" + buttonCount, topMargin);
        editor.putInt("button_memo_id_" + buttonCount, memoId);
        editor.putInt(MEMO_ID_COUNTER_KEY, memoIdCounter);
        editor.apply();
    }

    private void loadButtons() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int buttonCount = prefs.getInt(BUTTON_COUNT_KEY, 0);

        for (int i = 1; i <= buttonCount; i++) {
            int leftMargin = prefs.getInt("button_left_" + i, 0);
            int topMargin = prefs.getInt("button_top_" + i, 0);
            int memoId = prefs.getInt("button_memo_id_" + i, i);

            ImageButton newImageButton = new ImageButton(this);
            newImageButton.setImageResource(R.drawable.demo);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    100,  // 幅を100ピクセルに設定
                    100   // 高さを100ピクセルに設定
            );
            params.leftMargin = leftMargin;
            params.topMargin = topMargin;
            newImageButton.setLayoutParams(params);

            newImageButton.setOnClickListener(v -> openMemoScreen(memoId, null));

            buttonContainer.addView(newImageButton);

            if (memoId > memoIdCounter) {
                memoIdCounter = memoId;
            }
        }
    }
}
