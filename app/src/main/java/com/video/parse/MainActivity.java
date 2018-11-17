package com.video.parse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.video.module.camera.CameraActivity;
import com.video.module.circle.SelfViewActivity;
import com.video.module.photo.TakePhotoActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        findViewById(R.id.take_photo).setOnClickListener(view -> {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "take photo");
            }
            Intent intent = new Intent(MainActivity.this, TakePhotoActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.start_camera).setOnClickListener(v -> {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "start camera");
            }

            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.start_view).setOnClickListener(v -> {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "start circle view");
            }
            Intent intent = new Intent(MainActivity.this, SelfViewActivity.class);
            startActivity(intent);
        });
    }
}
