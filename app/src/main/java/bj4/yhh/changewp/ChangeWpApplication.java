package bj4.yhh.changewp;

import android.app.Application;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Build;

import bj4.yhh.changewp.wallpaper.BootCompletedReceiver;
import bj4.yhh.changewp.wallpaper.WallpaperUtility;

/**
 * Created by s011208 on 2017/4/3.
 */

public class ChangeWpApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        WallpaperUtility.scheduleWallpaperTask(this);
    }
}
