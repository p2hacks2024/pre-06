package com.example.star;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri photoUri;
    private int memoIdCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // star_skyクラスからmemoIdCounterを受け取る
        memoIdCounter = getIntent().getIntExtra("MEMO_ID_COUNTER", 0);

        ImageButton captureButton = findViewById(R.id.capture_button);
        captureButton.setOnClickListener(v -> dispatchTakePictureIntent());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Camera.this, star_sky.class);
            startActivity(intent);
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            if (imageBitmap != null) {
                saveImageToGallery(imageBitmap);
            } else {
                Toast.makeText(this, "Failed to capture image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageToGallery(Bitmap finalBitmap) {
        ContentValues values = new ContentValues();
        String fileName = "Image-" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";

        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        if (uri != null) {
            try {
                OutputStream out = getContentResolver().openOutputStream(uri);
                if (out != null) {
                    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    Toast.makeText(this, "Image saved successfully: " + uri, Toast.LENGTH_SHORT).show();
                    photoUri = uri;

                    // memoIdCounterをインクリメント
                    memoIdCounter++;

                    // 3秒後にAnimationTwoクラスに遷移するためのHandlerを使用
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Camera.this, AnimationTwo.class);
                            intent.putExtra("PHOTO_URI", photoUri.toString());
                            intent.putExtra("MEMO_ID", memoIdCounter);
                            startActivity(intent);

                            // 現在のアクティビティを終了
                            //finish();
                        }
                    }, 100); // 100ミリ秒（0.1秒）の遅延

                    // 結果を返すIntent
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("PHOTO_URI", photoUri.toString());
                    resultIntent.putExtra("MEMO_ID", memoIdCounter);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to create MediaStore entry.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
