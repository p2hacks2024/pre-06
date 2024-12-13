package com.example.star;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Memo extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    ImageView imageView;
    EditText editTextNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        imageView = findViewById(R.id.imageView);
        editTextNote = findViewById(R.id.editTextNote);
        Button selectImageButton = findViewById(R.id.selectImageButton);
        Button goToTitleButton = findViewById(R.id.goToTitleButton);

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        goToTitleButton.setOnClickListener(v -> {
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
            FileOutputStream noteStream = openFileOutput("note.txt", Context.MODE_PRIVATE);
            noteStream.write(note.getBytes());
            noteStream.close();

            FileOutputStream imageStream = openFileOutput("image.png", Context.MODE_PRIVATE);
            imageStream.write(image);
            imageStream.close();

            Toast.makeText(this, "メモを保存しました", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "メモの保存に失敗しました", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMemo() {
        try {
            FileInputStream noteStream = openFileInput("note.txt");
            byte[] noteBytes = new byte[noteStream.available()];
            noteStream.read(noteBytes);
            noteStream.close();
            String note = new String(noteBytes);
            editTextNote.setText(note);

            FileInputStream imageStream = openFileInput("image.png");
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageStream.close();
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(this, "メモの読み込みに失敗しました", Toast.LENGTH_SHORT).show();
        }
    }
}
