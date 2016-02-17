package hou.mymirror.modules;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;

import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import hou.mymirror.requests.MoodDetectResult;

/**
 * Created by hou on 2016-1-18.
 */
public class MoodModule {

    private static final String API_KEY = "15a1093823b987f153190b335ccb110c";
    private static final String API_SECRET = "o1XJ6Nj8H8kX3Jid53BPlnur4sMT9cr_ ";


    public interface MoodDetectDoneListener {
        void onMoodDetectDone(List<MoodDetectResult> results);
    }

    public static void getSmileDegree(final Bitmap bm, final MoodDetectDoneListener listener) {
        new AsyncTask<Void, Void, List<MoodDetectResult>>() {

            @Override
            protected List<MoodDetectResult> doInBackground(Void... params) {
                List<MoodDetectResult> results = new ArrayList<>();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                float scale = Math.min(1, Math.max(600f / bm.getWidth(), 600f / bm.getHeight()));
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                Bitmap imgSmall = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, false);
                imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] array = baos.toByteArray();
                HttpRequests httpRequests = new HttpRequests(API_KEY, API_SECRET, true, false);
                PostParameters postParameters = new PostParameters().setImg(array);

                JSONObject jsonObject;
                try {
                    jsonObject = httpRequests.detectionDetect(postParameters);
                    if (jsonObject != null) {
                        int count = jsonObject.getJSONArray("face").length();
                        for (int i = 0; i < count; i++) {
                            MoodDetectResult result = new MoodDetectResult();
                            result.setAge(jsonObject.getJSONArray("face")
                                    .getJSONObject(i).getJSONObject("attribute").getJSONObject("age").getInt("value"));
                            result.setGender(jsonObject.getJSONArray("face")
                                    .getJSONObject(i).getJSONObject("attribute").getJSONObject("gender").getString("value"));
                            result.setSmileDegree(jsonObject.getJSONArray("face")
                                    .getJSONObject(i).getJSONObject("attribute").getJSONObject("smiling").getDouble("value"));
                            results.add(result);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return results;
            }

            @Override
            protected void onPostExecute(List<MoodDetectResult> results) {
                listener.onMoodDetectDone(results);
                bm.recycle();
            }
        }.execute();

    }
}
