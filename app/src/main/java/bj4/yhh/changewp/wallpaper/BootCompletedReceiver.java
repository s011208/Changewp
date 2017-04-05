package bj4.yhh.changewp.wallpaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yenhsunhuang on 2017/4/5.
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompletedReceiver";
    private static final boolean DEBUG = true;


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (DEBUG) {
            Log.d(TAG, "BootCompletedReceiver onReceive, action: " + action);
        }
        if (!Intent.ACTION_BOOT_COMPLETED.equals(action)) return;
        WallpaperUtility.scheduleWallpaperTask(context);
    }
}
