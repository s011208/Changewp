package bj4.yhh.changewp.wallpaper;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by yenhsunhuang on 2017/4/5.
 */

public class ChangeWallpaperService extends IntentService {
    private static final String TAG = "ChangeWallpaperService";
    private static final boolean DEBUG = true;

    public static final String EXTRA_FROM_JOB_SCHEDULER = "extra_from";
    public static final int FROM_UNKNOWN = -1;
    public static final int FROM_JOB_SCHEDULER = 1000;
    public static final int FROM_ALARM_MANAGER = 1001;

    public ChangeWallpaperService() {
        super(ChangeWallpaperService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (DEBUG) {
            Log.d(TAG, "ChangeWallpaperService onHandleIntent");
        }
        final int from = intent.getIntExtra(EXTRA_FROM_JOB_SCHEDULER, FROM_UNKNOWN);
        if (DEBUG) {
            Log.v(TAG, "from: " + from);
        }
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        if (DEBUG) {
            Log.d(TAG, "w: " + wallpaperManager.getDesiredMinimumWidth() + ", h: " + wallpaperManager.getDesiredMinimumHeight());
        }
    }
}
