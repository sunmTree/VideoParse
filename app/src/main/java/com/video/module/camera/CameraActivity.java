package com.video.module.camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.video.parse.R;

public class CameraActivity extends AppCompatActivity {
    private PreView mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mPreview = findViewById(R.id.camera);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPreview.safeCameraOpen()) {
        }
    }
}
