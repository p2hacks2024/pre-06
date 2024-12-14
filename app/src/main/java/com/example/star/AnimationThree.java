
package com.example.star;

import static android.system.Os.close;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;

import androidx.appcompat.app.AppCompatActivity;

public class AnimationThree extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_three);

        // ImageView を取得
        ImageView imageView = findViewById(R.id.flyingImageView);

        // ImageView にタッチリスナーを設定
        imageView.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                moveImageOffScreen(view);
                return true;
            }
            return false;
        });
    }

    // ImageView を画面外に移動させるメソッド
    private void moveImageOffScreen(View view) {
        // 画面の高さを取得
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        // アニメーションを設定
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(view, "translationY", -screenHeight);
        moveUp.setDuration(500); // 速度を設定 (ミリ秒)

        // アニメーションセットの開始
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(moveUp);
        animatorSet.start();

        // アニメーション終了後に View を非表示にする
        moveUp.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                view.setVisibility(View.GONE);
                // Memoクラスに遷移
                Intent intent = new Intent(AnimationThree.this, star_sky.class);
                startActivity(intent);
                finish();
                // 現在のアクティビティを終了
            }
        });
    }
}