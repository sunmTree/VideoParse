package com.video.module.circle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.video.parse.R;

public class SelfViewActivity extends AppCompatActivity {
    private CircleImageView mCircleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_view);

        mCircleView = findViewById(R.id.circle_img);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.banner);
        mCircleView.setBitmap(bitmap);
        mCircleView.setRepeatCount(4);

        findViewById(R.id.image).setOnClickListener(v -> {
            mCircleView.startAnim();
        });

        findViewById(R.id.button).setOnClickListener(v -> {
            mCircleView.setCircleModel(CircleImageView.CircleModel.LEFT);
            mCircleView.startAnim();
        });

        findViewById(R.id.button2).setOnClickListener(v -> {
            mCircleView.setCircleModel(CircleImageView.CircleModel.RIGHT);
            mCircleView.startAnim();
        });

        findViewById(R.id.button3).setOnClickListener(v -> {
            mCircleView.setCircleModel(CircleImageView.CircleModel.TOP);
            mCircleView.startAnim();
        });

        findViewById(R.id.button4).setOnClickListener(v -> {
            mCircleView.setCircleModel(CircleImageView.CircleModel.BOTTOM);
            mCircleView.startAnim();
        });
    }
}
