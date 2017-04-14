package bj4.yhh.changewp.wallpaper;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

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
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job job = dispatcher.newJobBuilder().setService(WallpaperJobService.class)
                .setTag(WallpaperJobService.JOB_CHANGE_WALLPAPER)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow((int) (interval / 1000), 1 + (int) (interval / 1000)))
                .build();

        dispatcher.mustSchedule(job);
    }
}
