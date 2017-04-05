package bj4.yhh.changewp.wallpaper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import bj4.yhh.changewp.settings.main.WallpaperTimeInterval;
import bj4.yhh.changewp.wallpaper.scheduler.WallpaperJobService;

/**
 * Created by yenhsunhuang on 2017/4/5.
 */

public class WallpaperUtility {
    private static final boolean DEBUG = true;
    private static final String TAG = "WallpaperUtility";

    public static void scheduleWallpaperTask(Context context) {
        final long interval = WallpaperTimeInterval.getTimeInterval(context);
        if (DEBUG) {
            Log.v(TAG, "scheduleWallpaperTask, interval: " + interval);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder builder = new JobInfo.Builder(WallpaperJobService.JOB_CHANGE_WALLPAPER,
                    new ComponentName(context.getPackageName(), WallpaperJobService.class.getName()))
                    .setPersisted(true)
                    .setPeriodic(interval);

            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            scheduler.cancelAll();
            scheduler.schedule(builder.build());
        } else {
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ChangeWallpaperService.class);
            intent.putExtra(ChangeWallpaperService.EXTRA_FROM_JOB_SCHEDULER, ChangeWallpaperService.FROM_ALARM_MANAGER);
            PendingIntent alarmIntent = PendingIntent.getService(context, 3000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.cancel(alarmIntent);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, alarmIntent);
        }
    }
}
