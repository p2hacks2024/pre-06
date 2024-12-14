package com.example.star;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Memo extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    ImageView imageView;
    EditText editTextNote;
    private int memoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        imageView = findViewById(R.id.imageView);
        editTextNote = findViewById(R.id.editTextNote);

        ImageButton kanryouButton = findViewById(R.id.kanryouButton);
        ImageButton modoruButton = findViewById(R.id.modoruButton);

        // メモIDと写真のURIを取得
        memoId = getIntent().getIntExtra("MEMO_ID", -1);
        String photoUriString = getIntent().getStringExtra("PHOTO_URI");
        if (photoUriString != null) {
            Uri photoUri = Uri.parse(photoUriString);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "画像の読み込みに失敗しました", Toast.LENGTH_SHORT).show();
            }
        }


        kanryouButton.setOnClickListener(v -> {
            saveMemo();
            /*
            Intent intent = new Intent(Memo.this, star_sky.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            */
            // 3秒後に画面を切り替える
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Memo.this, Animation.class);
                    // Animationクラスに変更
                    startActivity(intent);
                }
            }, 2000); // 2000ミリ秒（2秒）の遅延
        });

        modoruButton.setOnClickListener(v -> {
            saveMemo();
            Intent intent = new Intent(Memo.this, star_sky.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        loadMemo(); // ここでメモを読み込みます
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "画像の読み込みに失敗しました", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveMemo() {
        String note = editTextNote.getText().toString();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();

        try {
            // メモIDを使ってファイル名を一意にする
            FileOutputStream noteStream = openFileOutput("note_" + memoId + ".txt", Context.MODE_PRIVATE);
            noteStream.write(note.getBytes());
            noteStream.close();

            FileOutputStream imageStream = openFileOutput("image_" + memoId + ".png", Context.MODE_PRIVATE);
            imageStream.write(image);
            imageStream.close();

            Toast.makeText(this, "メモを保存しました", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "メモの保存に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMemo() {
        try {
            // メモIDを使ってファイル名を一意にする
            FileInputStream noteStream = openFileInput("note_" + memoId + ".txt");
            byte[] noteBytes = new byte[noteStream.available()];
            noteStream.read(noteBytes);
            noteStream.close();
            String note = new String(noteBytes);
            editTextNote.setText(note);

            FileInputStream imageStream = openFileInput("image_" + memoId + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageStream.close();
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(this, "メモの読み込みに失敗しました", Toast.LENGTH_SHORT).show();
        }
    }
}