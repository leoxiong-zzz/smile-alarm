package com.leoxiong.Smile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.*;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Leo on 6/8/2014.
 */
public class OpenCVActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "OpenCVActivity";
    private static final long HAPPY_THRESHOLD = 1000;

    private CascadeClassifier mFrontalFaceClassifier;
    private CascadeClassifier mSmileClassifier;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case SUCCESS:
                    mFrontalFaceClassifier = getCascadeClassifier(R.raw.lbpcascade_frontalface);
                    mSmileClassifier = getCascadeClassifier(R.raw.haarcascade_smile_1);

                    mOpenCvCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };
    private Ringtone mRingtone;
    private ImageView mImageViewSwitch;
    private ImageView mImageViewFlash;
    private JavaCameraWrapperView mOpenCvCameraView;
    private float mAbsoluteFaceSize = 0;
    private float mRelativeFaceSize = 0.2f;
    private LinkedList<Integer> mCameraIds;
    private LinkedList<Integer> mFlashMode;
    private Long mHappySince;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opencv_activity);

        mCameraIds = new LinkedList<Integer>() {{
            add(CameraBridgeViewBase.CAMERA_ID_BACK);
            add(CameraBridgeViewBase.CAMERA_ID_FRONT);
        }};
        mFlashMode = new LinkedList<Integer>() {{
            add(R.drawable.ic_action_flash_off);
            add(R.drawable.ic_action_flash_on);
            // add(R.drawable.ic_action_flash_automatic);
        }};


        mOpenCvCameraView = (JavaCameraWrapperView) findViewById(R.id.opencv_view);
        mImageViewSwitch = (ImageView) findViewById(R.id.imageView_switch);
        mImageViewFlash = (ImageView) findViewById(R.id.imageView_flash);
        mRingtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        mImageViewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOpenCvCameraView.disableView();
                mCameraIds.addLast(mCameraIds.removeFirst());
                mOpenCvCameraView.setCameraIndex(mCameraIds.getFirst());
                mOpenCvCameraView.enableView();
            }
        });
        mImageViewFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlashMode.addLast(mFlashMode.removeFirst());
                mImageViewFlash.setImageResource(mFlashMode.getFirst());
                switch (mFlashMode.getFirst()) {
                    case R.drawable.ic_action_flash_on:
                        switch (mCameraIds.getFirst()) {
                            case CameraBridgeViewBase.CAMERA_ID_BACK:
                                mOpenCvCameraView.enableTorch(true);
                                break;
                            case CameraBridgeViewBase.CAMERA_ID_FRONT:
                                mOpenCvCameraView.setBackgroundColor(Color.WHITE);
                                WindowManager.LayoutParams params = getWindow().getAttributes();
                                params.screenBrightness = 1;
                                getWindow().setAttributes(params);
                                break;
                        }
                        break;
                    case R.drawable.ic_action_flash_off:
                        switch (mCameraIds.getFirst()) {
                            case CameraBridgeViewBase.CAMERA_ID_BACK:
                                mOpenCvCameraView.enableTorch(false);
                                break;
                            case CameraBridgeViewBase.CAMERA_ID_FRONT:
                                WindowManager.LayoutParams params = getWindow().getAttributes();
                                params.screenBrightness = -1;
                                getWindow().setAttributes(params);
                                mOpenCvCameraView.setBackground(null);
                                break;
                        }
                        break;
                }
            }
        });
        mOpenCvCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Attempting to autofocus camera.");
                mOpenCvCameraView.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean b, Camera camera) {
                        Log.v(TAG, b ? "Camera successfully focused." : "Camera is unable to focus.");
                    }
                });
            }
        });
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        //mRingtone.setStreamType(AudioManager.STREAM_ALARM);
        mRingtone.setStreamType(AudioManager.STREAM_MUSIC);
    }

    private CascadeClassifier getCascadeClassifier(int resId) {
        CascadeClassifier cc = null;
        try {
            InputStream is = getResources().openRawResource(resId);
            File file = new File(getDir("cascade", Context.MODE_PRIVATE), "tmp.xml");
            FileOutputStream os = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int read;
            while ((read = is.read(buffer)) != -1)
                os.write(buffer, 0, read);
            is.close();
            os.close();

            cc = new CascadeClassifier(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cc;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        if (mRingtone.isPlaying())
            mRingtone.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRingtone.play();
        mOpenCvCameraView.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba();
        Mat gray = inputFrame.gray();

        MatOfRect faces = new MatOfRect();

        mAbsoluteFaceSize = Math.round(gray.rows() * mRelativeFaceSize);
        mFrontalFaceClassifier.detectMultiScale(gray, faces, 1.1, 8, 0,
                new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());

        for (Rect face : faces.toArray()) {
            Core.rectangle(frame, face.tl(), face.br(), new Scalar(0, 255, 0, 255), 2);

            int mouthX1 = 0;
            int mouthY1 = (int) Math.round(face.height * 0.6);
            Mat gray1 = new Mat(gray.submat(face), new Rect(mouthX1, mouthY1, face.width - mouthX1, face.height - mouthY1));
            MatOfRect smiles = new MatOfRect();
            mSmileClassifier.detectMultiScale(gray1, smiles, 1.2, 10, 0, new Size(30, 30), new Size());
            if (smiles.toArray().length == 0)
                mHappySince = null;
            for (Rect smile : smiles.toArray()) {
                Point pt1 = new Point(face.tl().x + mouthX1 + smile.x, face.tl().y + mouthY1 + smile.y);
                Point pt2 = new Point(pt1.x + smile.width, pt1.y + smile.height);
                Core.rectangle(frame, pt1, pt2, new Scalar(0, 0, 255, 255), 2);

                if (mHappySince == null) {
                    mHappySince = System.currentTimeMillis();
                } else if (mHappySince != null) {
                    if (System.currentTimeMillis() - mHappySince > HAPPY_THRESHOLD) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(OpenCVActivity.this, R.string.smile, Toast.LENGTH_SHORT).show();
                            }
                        });
                        if (mRingtone.isPlaying())
                            mRingtone.stop();
                        //finish();
                    }
                }
                break; // only one smile for now
            }
        }
        return frame;
    }
}