package com.example.star;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button captureButton = findViewById(R.id.capture_button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Camera.this, star_sky.class);
            startActivity(intent);
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.image_view);
            imageView.setImageBitmap(imageBitmap);

            // Save the image
            saveImageToGallery(imageBitmap);
        }
    }

    private void saveImageToGallery(Bitmap finalBitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "Image-" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream out = getContentResolver().openOutputStream(uri);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            if (out != null) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 新たにファイル保存場所をログに表示
        Log.d("CameraActivity", "Image saved at: " + uri.toString());
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // requestCode は、どのリクエストに対する応答かを識別します
        if (requestCode == 1) {

            // grantResults 配列は、各権限のリクエスト結果を含みます
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // ユーザーが権限を許可した場合の処理
                // ここに権限が許可された場合の処理を追加します
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {

                // ユーザーが権限を拒否した場合の処理
                // ここに権限が拒否された場合の処理を追加します
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
