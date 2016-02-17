package hou.mymirror.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;

/**
 * Created by hou on 2016-1-15.
 */
public class CameraUtils {
    private static String TAG = "CameraUtils";


    public static final int UPDATE_FACE_RECT = 0;
    public static final int CAMERA_HAS_STARTED_PREVIEW = 1;
    public static final int CAMERA_HAS_TAKE_PICTURE_DONE = 3;


    private static final File sdcardPath = Environment.getExternalStorageDirectory();

    private static String storagePath = "";
    private static final String DST_FOLDER_NAME = "PlayCamera";


    private static String getStoragePath() {
        if (storagePath.equals("")) {
            storagePath = sdcardPath.getAbsolutePath() + "/" + DST_FOLDER_NAME;
            File file = new File(storagePath);
            if (!file.exists()) {
                file.mkdir();
            }
        }
        return storagePath;
    }


    public static String saveBitmap(Context context, Bitmap bitmap) {

      /*  String path = getStoragePath();
        long timeMillis = System.currentTimeMillis();
        String picName = path + "/" + timeMillis + ".jpg";
        Log.v(TAG, "save bitmap: picture name is" + picName);
        try {
            FileOutputStream fos = new FileOutputStream(picName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.v(TAG, "save bitmap done!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, "save bitmap failed!");
        }*/

        String picPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null);

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(picPath));
        intent.setData(uri);
        context.sendBroadcast(intent);

        return getPathFromUri(context, picPath);
    }

    //根据uri获得文件的实际路径
    private static String getPathFromUri(Context context, String url) {
        Uri uri = Uri.parse(url);
        String[] pro = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, pro, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String picPath = cursor.getString(index);
        cursor.close();
        return picPath;
    }


    /**
     * 获取屏幕宽度和高度，单位为px
     *
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        Log.i(TAG, "Screen---Width = " + w_screen + " Height = " + h_screen + " densityDpi = " + dm.densityDpi);
        return new Point(w_screen, h_screen);

    }

    /**
     * 获取屏幕长宽比
     *
     * @param context
     * @return
     */
    public static float getScreenRate(Context context) {
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H / W);
    }


    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
                                     int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }


}
