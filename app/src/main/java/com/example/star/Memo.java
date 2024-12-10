package com.example.star;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Memo extends AppCompatActivity {

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_memo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText editText = (EditText) findViewById(R.id.edit_text);
        Button saveButton = (Button) findViewById(R.id.save_button);
        Button clearButton = (Button) findViewById(R.id.clear_button);
        final ListView listView = (ListView) findViewById(R.id.memo_list);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        String[] saveMemoList = readMemo();
        if (saveMemoList != null) {
            adapter.addAll(saveMemoList);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memo = editText.getText().toString();

                if (memo.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "メモ内容が入力されていません", Toast.LENGTH_SHORT).show();
                    return;
                }

                adapter.add(memo);
                editText.setText("");

                String memoTmp = "";
                for (int i = 0; i < adapter.getCount(); i++) {
                    memoTmp += adapter.getItem(i) + splitStr;
                }
                saveMemo(memoTmp.substring(0, memoTmp.length() - splitStr.length()));
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });

        ImageButton back01 = (ImageButton) findViewById(R.id.imageButton6);
        back01.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Memo.this, star_sky.class);
                startActivity(intent);
            }
        });
    }

    private String fileName = "memo.txt";
    private String splitStr = "///";

    private String[] readMemo() {
        BufferedReader reader = null;
        String memoTxt = "";
        try {
            reader = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
            String str;
            while ((str = reader.readLine()) != null) {
                memoTxt = memoTxt + str;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (memoTxt == "") {
            return null;
        }

        return memoTxt.split(splitStr);
    }

    private void saveMemo(String str) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE)));
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
