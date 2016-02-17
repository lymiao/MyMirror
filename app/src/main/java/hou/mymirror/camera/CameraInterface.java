package hou.mymirror.camera;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

import hou.mymirror.utils.CameraParamUtils;
import hou.mymirror.utils.CameraUtils;
import hou.mymirror.utils.Utils;

/**
 * Created by hou on 2016-1-14.
 */
public class CameraInterface {

    private static final String TAG = "CameraInterface";

    private Camera mCamera;
    private Camera.Parameters mParams;

    private boolean isPreviewing = false;
    private int mCameraId = -1;

    private Context mContext;

    private static CameraInterface mCameraInterface;

    private float mPreviwRate = -1f;

    private TakePictureDoneListener mTakePictureDoneListener;

    public interface CameraOpenDoneCallback {
        void cameraHasOpened();
    }


    public interface TakePictureDoneListener {
        void takePictureDone(String picPath);
    }


    private CameraInterface(Context context) {
        mContext = context;
    }

    public static CameraInterface getInstance(Context context) {
        if (mCameraInterface == null) {
            synchronized (CameraInterface.class) {
                if (mCameraInterface == null) {
                    mCameraInterface = new CameraInterface(context);
                }
            }
        }
        return mCameraInterface;
    }


    /**
     * 开启Camera
     *
     * @param callback 是否开启完成接口
     * @param cameraId 前置或后置摄像头
     */
    public void openCamera(CameraOpenDoneCallback callback, int cameraId) {
        mCamera = Camera.open(cameraId);
        mCameraId = cameraId;
        if (callback != null) {
            callback.cameraHasOpened();
        }
    }


    /**
     * 开启Camera预览
     *
     * @param holder      SurfaceHolder
     * @param previewRate
     */
    public void startPreview(SurfaceHolder holder, float previewRate) {
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            mParams = mCamera.getParameters();
            //拍照后存储的图片格式
            mParams.setPictureFormat(PixelFormat.JPEG);
            //设置PreviewSize和PictureSize
            Camera.Size previewSize = CameraParamUtils.getInstance()
                    .getPropPreviewSize(mParams.getSupportedPreviewSizes(), previewRate, 800);
            mParams.setPreviewSize(previewSize.width, previewSize.height);
            Camera.Size pictureSize = CameraParamUtils.getInstance()
                    .getPropPreviewSize(mParams.getSupportedPictureSizes(), previewRate, 800);
            mParams.setPreviewSize(pictureSize.width, pictureSize.height);

            mCamera.setDisplayOrientation(90);


            CameraParamUtils.getInstance().printSupportFocusMode(mParams);
            List<String> focusModes = mParams.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);

            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();//开启预览
            } catch (IOException e) {
                e.printStackTrace();
            }

            isPreviewing = true;
            mPreviwRate = previewRate;

            mParams = mCamera.getParameters(); //重新get一次
            Log.v(TAG, "最终设置:PreviewSize--With = " + mParams.getPreviewSize().width
                    + "Height = " + mParams.getPreviewSize().height);
            Log.v(TAG, "最终设置:PictureSize--With = " + mParams.getPictureSize().width
                    + "Height = " + mParams.getPictureSize().height);
        }
    }

    /**
     * 停止预览，关闭Camera
     */
    public void stopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照片
     */
    public void takePicture(TakePictureDoneListener listener) {
        if (isPreviewing && mCamera != null) {
            mTakePictureDoneListener = listener;
            mCamera.takePicture(mShutterCallback, null, mPictureCallback);
        }
    }

    public Camera getCamera() {
        return mCamera;
    }

    public Camera.Parameters getParams() {
        if (mCamera != null) {
            return mCamera.getParameters();
        }
        return null;
    }

    public int getCameraId() {
        return mCameraId;
    }


    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.v(TAG, "mPictureCallback ");
            String picPath = "";
            Bitmap bm = null;
            if (data != null) {
                bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                mCamera.stopPreview();
                isPreviewing = false;
            }
            if (bm != null) {
                Bitmap rotaBitmap;
                if (mCameraId == 0) {
                    rotaBitmap = Utils.getRotateBitmap(bm, 90.0f);
                } else {
                    rotaBitmap = Utils.getRotateBitmap(bm, -90.0f);
                }
                picPath = CameraUtils.saveBitmap(mContext, rotaBitmap);
            }
            mCamera.startPreview();
            isPreviewing = true;
            Log.v(TAG, "Picture Path = " + picPath);
            mTakePictureDoneListener.takePictureDone(picPath);

        }
    };


}
