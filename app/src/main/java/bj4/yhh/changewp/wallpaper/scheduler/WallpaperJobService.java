package bj4.yhh.changewp.wallpaper.scheduler;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import bj4.yhh.changewp.wallpaper.ChangeWallpaperService;

/**
 * Created by yenhsunhuang on 2017/4/5.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class WallpaperJobService extends JobService {
    public static final String JOB_CHANGE_WALLPAPER = "Change-wallpaper";
    private static final boolean DEBUG = false;
    private static final String TAG = "WallpaperJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        if (DEBUG) {
            Log.d(TAG, "onStartJob");
        }
        if (JOB_CHANGE_WALLPAPER.equals(params.getTag())) {
            Intent intent = new Intent(this, ChangeWallpaperService.class);
            intent.putExtra(ChangeWallpaperService.EXTRA_FROM_JOB_SCHEDULER, ChangeWallpaperService.FROM_JOB_SCHEDULER);
            startService(intent);
            return false;
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
