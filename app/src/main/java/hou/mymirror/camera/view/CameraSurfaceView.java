package hou.mymirror.camera.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import hou.mymirror.camera.CameraInterface;

/**
 * Created by hou on 2016-1-14.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static String TAG = "CameraSurfaceView";

    private Context mContext;

    private SurfaceHolder mSurfaceHolder;


    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(TAG,"surfaceCreated");
        CameraInterface.getInstance(mContext).openCamera(null, Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v(TAG,"surfaceChanged");
        CameraInterface.getInstance(mContext).startPreview(mSurfaceHolder,1.333f);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG,"surfaceDestroyed");
        CameraInterface.getInstance(mContext).stopCamera();
    }

    public SurfaceHolder getSurfaceHolder() {
        return mSurfaceHolder;
    }
}
