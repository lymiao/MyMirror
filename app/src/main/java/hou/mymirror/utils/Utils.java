package hou.mymirror.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by hou on 2016-1-14.
 */
public class Utils {


    public static boolean isNetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
        }
        return false;
    }

    public static Bitmap decodeSampledBitmap(String filePath){
        Bitmap bm;
        //just read size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bm = BitmapFactory.decodeFile(filePath, options);

        //scale size to read
        options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max((double)options.outWidth / 1024f, (double)options.outHeight / 1024f)));
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(filePath, options);
        return bm;
    }


    public static Bitmap setThumbnailPicture(String picPath, ImageView imageView) {
        ImageSize imageSize = getImageViewSize(imageView);
        Bitmap bm = decodeSampledBitmapFromPath(picPath, imageSize);
        imageView.setImageBitmap(bm);
        Log.v("Utils", "setThumbnailPicture ,done!" + picPath);
        return bm;
    }


    private static Bitmap decodeSampledBitmapFromPath(String picPath, ImageSize imageSize) {
        //获得图片的宽和高，但并不加载到内存中去
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picPath, options);

        options.inSampleSize = caculateInSampleSize(options, imageSize.width, imageSize.height);

        //根据拿到的inSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picPath, options);

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();

        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();

         /*
        * 获取图片压缩的宽和高
        * */
        int width = imageView.getWidth();//获取imageView的实际宽度
        if (width <= 0) {
            width = lp.width;//获取imageView在布局中设置的宽度
        }
        if (width <= 0) {
            width = imageView.getMaxWidth();
        }
        if (width <= 0) {
            width = displayMetrics.widthPixels;
        }
        imageSize.width = width;
        int height = imageView.getHeight();//获取imageView的实际宽度
        if (height <= 0) {
            height = lp.height;//获取imageView在布局中设置的宽度
        }
        if (height <= 0) {
            height = imageView.getMaxHeight();
        }
        if (height <= 0) {
            height = displayMetrics.heightPixels;
        }
        imageSize.height = height;
        return imageSize;
    }

    //根据图片实际的宽度高度和需求的宽度高度，计算合适的压缩比
    private static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqHeight);
            int heightRadio = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }

        return inSampleSize;

    }


    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }

    private static class ImageSize {
        int width;
        int height;
    }
}
