package hou.mymirror;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import hou.mymirror.configuration.ConfigurationSetting;

public class SetUpActivity extends AppCompatActivity {

    private ConfigurationSetting mConfiguration;

    private CheckBox mMoodDetectionCheckBox;

    private TextView mMoodGuideTextView;

    private EditText mLatitude;
    private EditText mLongitude;
    private EditText mCityLocation;
    private TextView mLocationDescription;


    // 定位相关
    private LocationClient mLocationClient;
    private BDLocation mLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置百度地图SDK
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_set_up);
        mConfiguration = new ConfigurationSetting(this);

        mMoodDetectionCheckBox = (CheckBox) findViewById(R.id.mood_detection_checkbox);
        mMoodDetectionCheckBox.setChecked(mConfiguration.isShowMoodDetection());

        mMoodGuideTextView = (TextView) findViewById(R.id.mood_guide_text);
        setTextUnderline();

        mLocationDescription = (TextView) findViewById(R.id.location_description);
        mLatitude = (EditText) findViewById(R.id.latitude);
        mLongitude = (EditText) findViewById(R.id.longitude);
        mCityLocation = (EditText) findViewById(R.id.city);

        mLatitude.setText(String.valueOf(mConfiguration.getLatitude()));
        mLongitude.setText(String.valueOf(mConfiguration.getLongitude()));
        mCityLocation.setText(String.valueOf(mConfiguration.getDetailAddress()));

        setUpLocationMonitoring();


        findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveField();
                Intent intent = new Intent(SetUpActivity.this, MirrorActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 为moodDetection指引textView添加下划线和跳转
     */
    private void setTextUnderline() {
        String text = getResources().getString(R.string.mood_guide_detail);
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SetUpActivity.this,CameraActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        },0,text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mMoodGuideTextView.setHighlightColor(Color.TRANSPARENT);
        mMoodGuideTextView.append(spannableString);
        mMoodGuideTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setUpLocationMonitoring() {
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null) {
                    mLocation = bdLocation;
                    mConfiguration.setLatLon(String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()));
                    mConfiguration.setCity(String.valueOf(mLocation.getCity()));
                    mConfiguration.setDetailAddress(String.valueOf(mLocation.getAddrStr()));
                    mLatitude.setText(String.valueOf(mConfiguration.getLatitude()));
                    mLongitude.setText(String.valueOf(mConfiguration.getLongitude()));
                    mCityLocation.setText(String.valueOf(mConfiguration.getDetailAddress()));
//                    mLatitude.setEnabled(false);
//                    mLongitude.setEnabled(false);
//                    mCityLocation.setEnabled(false);
                    mLocationDescription.setText("自动定位的位置信息是来查天气预报的");
                }
            }

        });

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);

        mLocationClient.setLocOption(option);

    }

    private void saveField() {
        mConfiguration.setShowMoodDetection(mMoodDetectionCheckBox.isChecked());
        if (mLocation != null){
            mConfiguration.setCity(mLocation.getCity());
        }
        mConfiguration.setLatLon(mLatitude.getText().toString(), mLongitude.getText().toString());
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!mLocationClient.isStarted())
            mLocationClient.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }
}
