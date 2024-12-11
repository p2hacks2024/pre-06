package com.example.star;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Memo extends AppCompatActivity {

    private EditText editText;
    private String memoFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        ImageView imageView = findViewById(R.id.imageView);
        editText = findViewById(R.id.editText);
        Button buttonBack = findViewById(R.id.button_back);
        Button buttonSave = findViewById(R.id.button_save);

        String photoFileName = getIntent().getStringExtra("photoFileName");
        if (photoFileName != null) {
            File photoFile = new File(getFilesDir(), photoFileName);
            if (photoFile.exists()) {
                imageView.setImageURI(Uri.fromFile(photoFile));
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }

        memoFileName = getIntent().getStringExtra("memoFileName");
        if (memoFileName != null) {
            loadMemo(memoFileName);
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo(memoFileName);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadMemo(String fileName) {
        File memoFile = new File(getFilesDir(), fileName);
        if (memoFile.exists()) {
            StringBuilder memoContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(memoFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    memoContent.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            editText.setText(memoContent.toString());
        }
    }

    private void saveMemo(String fileName) {
        File memoFile = new File(getFilesDir(), fileName);
        try (FileWriter writer = new FileWriter(memoFile)) {
            writer.write(editText.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
