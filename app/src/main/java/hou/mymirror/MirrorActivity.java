package hou.mymirror;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.bmob.push.BmobPush;
import hou.mymirror.configuration.ConfigurationSetting;
import hou.mymirror.modules.BirthdayModule;
import hou.mymirror.modules.DayModule;
import hou.mymirror.modules.ForecastModule;
import hou.mymirror.modules.OneModule;
import hou.mymirror.receiver.AlarmReceiver;
import hou.mymirror.utils.Utils;

public class MirrorActivity extends AppCompatActivity {
    private static String TAG = "MirrorActivity";

    private static final String BMOB_ID = "1d0878b609a976c1d70aabb338a51836";

    private ConfigurationSetting mConfigurationSetting;

    private TextView mBirthdayText;

    private TextView mDateText;
    private TextView mForecastText;
    private TextView mPM25Text;
    private TextView mOneContentText;


    private ForecastModule.ForecastListener forecastListener = new ForecastModule.ForecastListener() {
        @Override
        public void onWeatherToday(String weatherToday, String pm, int pmWarning) {
            if (!TextUtils.isEmpty(weatherToday) && !TextUtils.isEmpty(pm)) {
                mForecastText.setVisibility(View.VISIBLE);
                mForecastText.setText(weatherToday);


                mPM25Text.setVisibility(View.VISIBLE);
                mPM25Text.setText("PM2.5:  " + pm);

            }
        }

    };


    private OneModule.OneContentListener oneContentListener = new OneModule.OneContentListener() {
        @Override
        public void onOneContent(String content) {
            if (!TextUtils.isEmpty(content)) {
                mOneContentText.setVisibility(View.VISIBLE);
                mOneContentText.setText(content);
                mOneContentText.setSelected(true);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);

        mConfigurationSetting = new ConfigurationSetting(this);
        AlarmReceiver.startMirrorUpdates(this);

        //保持屏幕一直出于亮着的状态
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 使用推送服务时的初始化操作
//        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, BMOB_ID);

        initView();

        setViewState();
        Log.v(TAG, "============onCreate=======");

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setViewState();
    }

    private void setViewState() {
        String name = BirthdayModule.getBirthday();
        if (TextUtils.isEmpty(name)) {
            mBirthdayText.setVisibility(View.GONE);
        } else {
            mBirthdayText.setText(name + "生日快乐~");
            mBirthdayText.setVisibility(View.VISIBLE);
        }
        mDateText.setText(DayModule.getDate());

        if (Utils.isNetAvailable(this)) {
            ForecastModule.getCurrentForecast(getResources(),
                    mConfigurationSetting.getLatitude(), mConfigurationSetting.getLongitude(), forecastListener);

            OneModule.getOneContent(oneContentListener);
        }

    }

    private void initView() {
        mBirthdayText = (TextView) findViewById(R.id.birthday_text);
        mDateText = (TextView) findViewById(R.id.date_text);
        mForecastText = (TextView) findViewById(R.id.forecast_text);
        mOneContentText = (TextView) findViewById(R.id.one_content_text);
        mPM25Text = (TextView) findViewById(R.id.pm25_text);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "============onStart=======");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "============onResume=======");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlarmReceiver.stopMirrorUpdates(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
