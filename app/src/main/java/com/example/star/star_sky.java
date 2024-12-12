package com.example.star;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class star_sky extends AppCompatActivity {
    private Context context;
    private RelativeLayout buttonContainer;
    private Random random = new Random();
    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "button_prefs";
    private static final String BUTTON_COUNT_KEY = "button_count";

    public void setContext(Context context) {
        this.context = context;
    }

    public final ActivityResultLauncher<Intent> cameraActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    boolean receivedValue = result.getData().getBooleanExtra("buttonAdded", false);
                    Log.i("receivedValue", "received value: "+receivedValue+"");
                    if(receivedValue){

                        PlusStar.addNewButton(this, buttonContainer);
                    }
                    receivedValue = false;
                    PlusStar.loadButtons(this, buttonContainer);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        buttonContainer = findViewById(R.id.buttonContainer);
        PlusStar.loadButtons(this, buttonContainer);

        setContentView(R.layout.activity_star_sky);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton toMain = (ImageButton) findViewById(R.id.imageButton);
        toMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(star_sky.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ImageButton toCamera = (ImageButton) findViewById(R.id.imageButton2);
        toCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //PlusStar.addNewButton(context,buttonContainer);
                Intent intent = new Intent(star_sky.this, Camera.class);
                cameraActivityLauncher.launch(intent);
            }
        });
        ImageButton toMemo = (ImageButton) findViewById(R.id.imageButton3);
        toMemo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(star_sky.this, Memo.class);
                startActivity(intent);

            }
        });




        // 保存されたボタンを再表示
        PlusStar.loadButtons(this, buttonContainer);
    }

    /*private boolean addNewButtonCheck(boolean value){
        if(value){
            PlusStar.addNewButton(this, buttonContainer);
        }
        return false;
    }*/

}