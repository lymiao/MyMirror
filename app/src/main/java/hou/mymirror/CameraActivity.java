package hou.mymirror;

import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import hou.mymirror.camera.CameraInterface;
import hou.mymirror.camera.FaceDetector;
import hou.mymirror.camera.view.CameraSurfaceView;
import hou.mymirror.camera.view.FaceView;
import hou.mymirror.camera.view.RoundImageView;
import hou.mymirror.configuration.ConfigurationSetting;
import hou.mymirror.utils.CameraUtils;
import hou.mymirror.utils.Utils;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CameraActivity";

    private CameraSurfaceView mCameraSurfaceView;
    private FaceView mFaceView;

    private ImageButton mSwitchButton;
    private ImageButton mShutterButton;

    private RoundImageView mThumbnailPicture;
    private String mCurrentPicturePath;

    private float previewRate = -1f;

    private MainHandler mHander;
    private FaceDetector mFaceDetector;


    private ConfigurationSetting mConfigurationSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initView();

        initViewParams();

        mHander = new MainHandler();

        mFaceDetector = new FaceDetector(mHander);

        mShutterButton.setOnClickListener(this);
        mSwitchButton.setOnClickListener(this);

        mConfigurationSetting = new ConfigurationSetting(this);
        mCurrentPicturePath = mConfigurationSetting.getLastPicturePath();

        if (!mCurrentPicturePath.equals("")) {
            Utils.setThumbnailPicture(mCurrentPicturePath, mThumbnailPicture);
            Log.v(TAG, mCurrentPicturePath);
        }

        Log.v(TAG, "============onCreate===========");

        mThumbnailPicture.setClickable(true);
        mThumbnailPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Click picture thumbnail");
                Intent intent = new Intent(CameraActivity.this, SmileDetectActivity.class);
                intent.putExtra("picPath", mCurrentPicturePath);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHander.sendEmptyMessageDelayed(CameraUtils.CAMERA_HAS_STARTED_PREVIEW, 1500);
        Log.v(TAG, "============onStart===========");
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CameraUtils.UPDATE_FACE_RECT:
                    Camera.Face[] faces = (Camera.Face[]) msg.obj;
                    mFaceView.setFaces(faces);
                    break;
                case CameraUtils.CAMERA_HAS_STARTED_PREVIEW:
                    startFaceDetect();
                    break;
                case CameraUtils.CAMERA_HAS_TAKE_PICTURE_DONE:
                    mShutterButton.setEnabled(true);
                    mCurrentPicturePath = (String) msg.obj;
                    Utils.setThumbnailPicture(mCurrentPicturePath, mThumbnailPicture);
                    startFaceDetect();
                    break;
            }
            super.handleMessage(msg);
        }

    }


    private void startFaceDetect() {
        Camera.Parameters params = CameraInterface.getInstance(this).getParams();
        if (params.getMaxNumDetectedFaces() > 0) {
            if (mFaceView != null) {
                mFaceView.clearFaces();
                mFaceView.setVisibility(View.VISIBLE);
            }
            CameraInterface.getInstance(this).getCamera().setFaceDetectionListener(mFaceDetector);
            CameraInterface.getInstance(this).getCamera().startFaceDetection();
            mSwitchButton.setEnabled(true);
            Log.v(TAG, "============startFaceDetect===========");
        }
    }

    private void stopFaceDetect() {
        Camera.Parameters params = CameraInterface.getInstance(this).getParams();
        if (params.getMaxNumDetectedFaces() > 0) {
            CameraInterface.getInstance(this).getCamera().setFaceDetectionListener(null);
            CameraInterface.getInstance(this).getCamera().stopFaceDetection();
            mFaceView.clearFaces();
            Log.v(TAG, "============stopFaceDetect===========");
        }
    }

    private void initViewParams() {
        ViewGroup.LayoutParams params = mCameraSurfaceView.getLayoutParams();
        Point point = CameraUtils.getScreenMetrics(this);
        params.height = point.y;
        params.width = point.x;
        previewRate = CameraUtils.getScreenRate(this);
        mCameraSurfaceView.setLayoutParams(params);
    }

    private void initView() {
        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.camera_surfaceview);
        mFaceView = (FaceView) findViewById(R.id.face_view);
        mShutterButton = (ImageButton) findViewById(R.id.btn_shutter);
        mSwitchButton = (ImageButton) findViewById(R.id.btn_switch);
        mThumbnailPicture = (RoundImageView) findViewById(R.id.pic_thumbnail);
    }


    @Override
    public void onClick(View v) {
        Log.v(TAG, v.getId() + "");
        switch (v.getId()) {
            case R.id.btn_shutter:
                takePicture();
                break;
            case R.id.btn_switch:
                switchCamera();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onStop() {
        Log.v(TAG, "============onStop===========");
        super.onStop();
        mFaceView.clearFaces();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "============onDestroy===========");
        mConfigurationSetting.setLastPicturePath(mCurrentPicturePath);
    }

    private void switchCamera() {
        mSwitchButton.setEnabled(false);
        stopFaceDetect();
        int newId = (CameraInterface.getInstance(this).getCameraId() + 1) % 2;
        CameraInterface.getInstance(this).stopCamera();
        CameraInterface.getInstance(this).openCamera(null, newId);
        CameraInterface.getInstance(this).startPreview(mCameraSurfaceView.getSurfaceHolder(), previewRate);
        mHander.sendEmptyMessageDelayed(CameraUtils.CAMERA_HAS_STARTED_PREVIEW, 1500);
    }

    private void takePicture() {
        mShutterButton.setEnabled(false);
        CameraInterface.getInstance(this).takePicture(new CameraInterface.TakePictureDoneListener() {
            @Override
            public void takePictureDone(String picPath) {
                Message msg = mHander.obtainMessage();
                msg.what = CameraUtils.CAMERA_HAS_TAKE_PICTURE_DONE;
                msg.obj = picPath;
                mHander.sendMessageDelayed(msg, 1500);
            }
        });
    }


}
