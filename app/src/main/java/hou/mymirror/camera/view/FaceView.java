package hou.mymirror.camera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.util.AttributeSet;
import android.widget.ImageView;

import hou.mymirror.R;
import hou.mymirror.camera.CameraInterface;
import hou.mymirror.utils.CameraUtils;

/**
 * Created by hou on 2016-1-14.
 */
public class FaceView extends ImageView {

    private static final String TAG = "FaceView";


    private Paint mLinePaint;
    private Face[] mFaces;
    private Matrix mMatrix = new Matrix();
    private RectF mRect = new RectF();
    private Drawable mFaceIndicator = null;

    private Context mContext;

    public FaceView(Context context) {
        super(context);
        mContext = context;
    }

    public void setFaces(Face[] faces) {
        mFaces = faces;
        invalidate();
    }

    public void clearFaces() {
        mFaces = null;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mFaces == null || mFaces.length == 0) {
            return;
        }
        boolean isMirror = false;
        int id = CameraInterface.getInstance(mContext).getCameraId();
        if (id == Camera.CameraInfo.CAMERA_FACING_BACK) {
            isMirror = false;
        } else if (id == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            isMirror = true;
        }
        CameraUtils.prepareMatrix(mMatrix, isMirror, 90, getWidth(), getHeight());
        canvas.save();
        mMatrix.postRotate(0);
        canvas.rotate(-0);
        for (Face mFace : mFaces) {
            mRect.set(mFace.rect);
            mMatrix.mapRect(mRect);
            mFaceIndicator.setBounds(Math.round(mRect.left),
                    Math.round(mRect.top), Math.round(mRect.right), Math.round(mRect.bottom));
            mFaceIndicator.draw(canvas);
        }
        canvas.restore();

        super.onDraw(canvas);
    }

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mFaceIndicator = getResources().getDrawable(R.drawable.ic_face_find_2);
    }

    private void initPaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int color = Color.rgb(98, 212, 68);
        mLinePaint.setColor(color);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5f);
        mLinePaint.setAlpha(180);
    }


}
