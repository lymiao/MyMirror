package hou.mymirror.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("msg")){
            Log.v("bmob", "客户端收到推送内容：" + intent.getStringExtra("msg"));
        }
    }
}
