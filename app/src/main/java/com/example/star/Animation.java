package com.example.star;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class Animation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation1);

        ConstraintLayout mainLayout = findViewById(R.id.main);

        // 星を1つだけ描画
        ImageView star = new ImageView(this);
        star.setId(View.generateViewId());
        star.setImageResource(R.drawable.star); // 星の画像を設定

        // 星の初期サイズを設定
        int initialSize = 900; // 初期サイズを適切な大きさに設定
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(initialSize, initialSize);
        star.setLayoutParams(params);

        // 動的にレイアウトに追加
        mainLayout.addView(star);

        // 制約設定
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);

        // 星の初期位置を画面中央に設定
        constraintSet.connect(
                star.getId(),
                ConstraintSet.TOP,
                mainLayout.getId(),
                ConstraintSet.TOP,
                0 // 初期位置 (画面の上部)
        );
        constraintSet.connect(star.getId(), ConstraintSet.START, mainLayout.getId(), ConstraintSet.START, 0);
        constraintSet.connect(star.getId(), ConstraintSet.END, mainLayout.getId(), ConstraintSet.END, 0);

        // 制約を適用
        constraintSet.applyTo(mainLayout);

        // 星のアニメーションを実行
        animateStarMovementAndResize(star);

        // 次のアクティビティに遷移
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Animation.this, AnimationThree.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // 現在のアクティビティを終了
            }
        }, 3000); // 3000ミリ秒（3秒）の遅延
    }

    private void animateStarMovementAndResize(ImageView star) {
        // 画面の高さを取得
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        // 星の高さを取得
        int starHeight = star.getLayoutParams().height;

        // 星が画面下側中央に止まるように位置を計算
        int finalTopMargin = screenHeight - (starHeight / 2) - 245; // 余白を100px残す

        ValueAnimator animator = ValueAnimator.ofInt(0, finalTopMargin); // 初期位置から最終位置まで
        animator.setDuration(1000); // 2秒で移動
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                float fraction = animation.getAnimatedFraction();

                // 星の位置を更新
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) star.getLayoutParams();
                params.topMargin = value;
                star.setLayoutParams(params);

                // 星のサイズを縮小
                float scale = 1 - (0.67f * fraction); // サイズを50%まで縮小
                star.setScaleX(scale);
                star.setScaleY(scale);
            }
        });

        // アニメーションの終了時処理
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // アニメーション終了時も星はそのまま残す
            }
        });

        animator.start();
    }
}