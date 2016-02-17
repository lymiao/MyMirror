package hou.mymirror.camera;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import hou.mymirror.utils.CameraUtils;

/**
 * Created by hou on 2016-1-15.
 */
public class FaceDetector implements Camera.FaceDetectionListener {

    private static final String TAG = "FaceDetector";

    private Handler mHandler;

    public FaceDetector(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        Log.v(TAG,"onFaceDetection");
        if (faces != null){
            Message msg = mHandler.obtainMessage();
            msg.what = CameraUtils.UPDATE_FACE_RECT;
            msg.obj = faces;
            msg.sendToTarget();
        }
    }
}
