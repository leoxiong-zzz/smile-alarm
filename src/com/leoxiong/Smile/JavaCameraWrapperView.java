package com.leoxiong.Smile;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import org.opencv.android.JavaCameraView;

/**
 * Created by Leo on 6/12/2014.
 */
public class JavaCameraWrapperView extends JavaCameraView implements Camera.PictureCallback {

    public JavaCameraWrapperView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void enableTorch(boolean value) {
        Camera.Parameters params = mCamera.getParameters();
        params.setFlashMode(value ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(params);
    }

    public boolean setFocusMode(String mode) {
        Camera.Parameters params = mCamera.getParameters();
        if (!params.getSupportedFocusModes().contains(mode))
            return false;
        params.setFocusMode(mode);
        mCamera.setParameters(params);
        return true;
    }

    public void autoFocus(Camera.AutoFocusCallback callback) {
        mCamera.autoFocus(callback);
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {

    }
}
