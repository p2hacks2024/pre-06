package com.example.star;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class AnimationTwo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_two);

        // ImageViewの参照を取得
        ImageView starImage = findViewById(R.id.starImage);

        // スケールアニメーションを作成
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.01f, 6.0f, // X軸の開始スケールと終了スケール
                0.01f, 6.0f, // Y軸の開始スケールと終了スケール
                Animation.RELATIVE_TO_SELF, 0.5f, // ピボットX（中央）
                Animation.RELATIVE_TO_SELF, 0.5f  // ピボットY（中央）
        );
        scaleAnimation.setDuration(3000); // アニメーションの持続時間（ミリ秒）
        scaleAnimation.setFillAfter(true); // アニメーション終了後も状態を保持

        // アニメーションをImageViewに適用
        starImage.startAnimation(scaleAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(com.example.star.AnimationTwo.this, Memo.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // 現在のアクティビティを終了
            }
        }, 3000); // 3000ミリ秒（3秒）の遅延

    }
}