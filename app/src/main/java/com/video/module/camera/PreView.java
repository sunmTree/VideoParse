package com.video.module.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.video.parse.BuildConfig;

import java.io.IOException;
import java.util.List;

public class PreView extends ViewGroup implements SurfaceHolder.Callback {
    private static final String TAG = "PreView";

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private List<Camera.Size> mSupportPreViewSizes;

    public PreView(Context context) {
        this(context, null);
    }

    public PreView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

    }

    public boolean safeCameraOpen() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "safeCameraOpen ");
        }

        boolean qOpened = false;
        try {
            releaseCameraAndPreview();
            mCamera = Camera.open();
            qOpened = mCamera != null;
            if (qOpened) {
                setCamera(mCamera);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qOpened;
    }

    private void releaseCameraAndPreview() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "releaseCameraAndPreview " + mCamera);
        }

        setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void setCamera(Camera camera) {
        if (mCamera == camera) {
            return;
        }

        stopPreviewAndFreeCamera();

        if (mCamera != null) {
            List<Camera.Size> sizeList = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportPreViewSizes = sizeList;
            requestLayout();

            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture
            mCamera.startPreview();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // Now that the size is known, set up the camera parameters and begin the preview
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(i1, i2);
        requestLayout();
        mCamera.setParameters(parameters);

        // Important: Call startPreview() to start updating the preview surface
        // Preview must be started before you can take a picture
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Surface will be destroyed when we return, so stop the preview
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface
            mCamera.stopPreview();
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    private void stopPreviewAndFreeCamera() {
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface
            mCamera.stopPreview();

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()
            mCamera.release();

            mCamera = null;
        }
    }
}
