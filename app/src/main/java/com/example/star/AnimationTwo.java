package com.example.star;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class AnimationTwo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_two);

        // ImageViewの参照を取得
        ImageView starImage = findViewById(R.id.starImage);

        // ImageViewを最初に非表示（描画のみオフ）に設定
        starImage.setVisibility(View.INVISIBLE);

        // スケールアニメーションを作成
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.01f, 6.0f, // X軸の開始スケールと終了スケール
                0.01f, 6.0f, // Y軸の開始スケールと終了スケール
                Animation.RELATIVE_TO_SELF, 0.5f, // ピボットX（中央）
                Animation.RELATIVE_TO_SELF, 0.5f  // ピボットY（中央）
        );
        scaleAnimation.setDuration(3000); // アニメーションの持続時間（ミリ秒）
        scaleAnimation.setFillAfter(true); // アニメーション終了後も状態を保持

        // アニメーションのリスナーを設定
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // アニメーション開始時にImageViewを表示
                starImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // アニメーション終了時の処理
                Intent intent = new Intent(AnimationTwo.this, Memo.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // 現在のアクティビティを終了
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // アニメーション繰り返し時の処理
            }
        });

        // アニメーションをImageViewに適用
        starImage.startAnimation(scaleAnimation);
    }
}
