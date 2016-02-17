package hou.mymirror;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hou.mymirror.modules.MoodModule;
import hou.mymirror.requests.MoodDetectResult;
import hou.mymirror.utils.Utils;

public class SmileDetectActivity extends AppCompatActivity {


    private static String TAG = "SmileDetectActivity";
    private String picPath;

    private ImageView imageView;

    private Button mChoosePictureButton;
    private Button mDetectButton;

    private TextView mSmileDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_smile);
        picPath = getIntent().getStringExtra("picPath");
        Log.d(TAG, "Picture:" + picPath);


        imageView = (ImageView) findViewById(R.id.picture);
        mSmileDescription = (TextView) findViewById(R.id.smile_description);
        mChoosePictureButton = (Button) findViewById(R.id.choose_pic);
        mDetectButton = (Button) findViewById(R.id.detect);


        Utils.setThumbnailPicture(picPath, imageView);
        Bitmap bm = Utils.decodeSampledBitmap(picPath);

        if (Utils.isNetAvailable(this)) {
            MoodModule.getSmileDegree(bm, new MoodModule.MoodDetectDoneListener() {
                @Override
                public void onMoodDetectDone(List<MoodDetectResult> results) {
                    int count = results.size();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("共检测到").append(count).append("个人.\n");
                    for (int i = 0; i < count; i++) {
                        MoodDetectResult result = results.get(i);
                        stringBuilder.append("性别：").append(result.getGender());
                        stringBuilder.append("年龄：").append(result.getAge());
                        stringBuilder.append("微笑程度：").append(result.getSmileDegree()).append("\n");
                    }
                    mSmileDescription.setText(stringBuilder);
                }
            });
        }

        mChoosePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


        mDetectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSmileDescription.setText(getResources().getString(R.string.please_wait));

                Bitmap bm = Utils.decodeSampledBitmap(picPath);

                if (Utils.isNetAvailable(SmileDetectActivity.this)) {
                    MoodModule.getSmileDegree(bm, new MoodModule.MoodDetectDoneListener() {
                        @Override
                        public void onMoodDetectDone(List<MoodDetectResult> results) {
                            int count = results.size();
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("共检测到").append(count).append("个人.\n");
                            for (int i = 0; i < count; i++) {
                                MoodDetectResult result = results.get(i);
                                stringBuilder.append("性别：").append(result.getGender());
                                stringBuilder.append("年龄：").append(result.getAge());
                                stringBuilder.append("微笑程度：").append(result.getSmileDegree()).append("\n");
                            }
                            mSmileDescription.setText(stringBuilder);
                        }
                    });
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                cursor.moveToFirst();
                int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                picPath = cursor.getString(id);
                Utils.setThumbnailPicture(picPath, imageView);
                mDetectButton.setVisibility(View.VISIBLE);
                cursor.close();
            }
        }
    }
}
