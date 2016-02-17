package hou.mymirror.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import hou.mymirror.MirrorActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int REQUEST_CODE = 101;
    private static final String TAG = "MirrorWakeLock";
    public static final long MINUTES_10 = 10 * 60 * 1000;

    public static void startMirrorUpdates(Context context) {
        Log.v(TAG, "============startMirrorUpdates==========");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + MINUTES_10, MINUTES_10, alarmIntent);
    }


    public static void stopMirrorUpdates(Context context) {
        Log.v(TAG, "============stopMirrorUpdates==========");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);

        alarmManager.cancel(alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v(TAG, "============onReceive===========");
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wakeLock.acquire();

        Intent mirrorActivityIntent = new Intent(context, MirrorActivity.class);
        mirrorActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mirrorActivityIntent);

        wakeLock.release();

    }
}
