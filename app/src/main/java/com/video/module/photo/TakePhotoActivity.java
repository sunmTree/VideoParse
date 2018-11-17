package com.video.module.photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.video.parse.BuildConfig;
import com.video.parse.R;

import java.io.File;

public class TakePhotoActivity extends AppCompatActivity {
    private static final String TAG = "TakePhoto";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PHOTO = 2;

    private ImageView mPhotoImg1, mPhotoImg2, mPhotoImg3;
    private ProgressBar mPro;
    private Button mTakePhoto;
    private Button mTakeP;

    private String mPhotoFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        initView();
    }

    private void initView() {
        mPhotoImg1 = findViewById(R.id.photo_img1);
        mPhotoImg2 = findViewById(R.id.photo_img2);
        mPhotoImg3 = findViewById(R.id.photo_img3);

        mPro = findViewById(R.id.photo_pro);
        mTakePhoto = findViewById(R.id.take_photo);
        mTakeP = findViewById(R.id.take_photo2);

        mTakePhoto.setOnClickListener(view -> dispatchTakePicIntent());
        mTakeP.setOnClickListener(view -> dispatchTakePIntent());
    }

    private void dispatchTakePicIntent() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "user decide to select a photo");
        }
        // get a thumbnail, not save photo file
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicIntent.resolveActivity(getPackageManager()) != null) {
            mPro.setVisibility(View.VISIBLE);
            startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakePIntent() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "user decide to take a photo file");
        }
        Intent takePIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = FileUtils.createImageFile(TakePhotoActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                mPhotoFilePath = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(TakePhotoActivity.this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePIntent, REQUEST_IMAGE_PHOTO);
            }
        }
    }

    private void setPic() {
        int targetW = mPhotoImg2.getWidth();
        int targetH = mPhotoImg2.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPhotoFilePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFilePath, bmOptions);
        mPhotoImg2.setImageBitmap(bitmap);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mPhotoFilePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            mPro.setVisibility(View.GONE);
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            mPhotoImg1.setImageBitmap(bitmap);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onActivityResult " + extras);
            }
        }

        if (requestCode == REQUEST_IMAGE_PHOTO && resultCode == Activity.RESULT_OK) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "take photo finish ");
            }
            galleryAddPic();
            setPic();
        }
    }
}
