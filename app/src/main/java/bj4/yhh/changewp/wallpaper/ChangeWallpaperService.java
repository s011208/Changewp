package bj4.yhh.changewp.wallpaper;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bj4.yhh.albumview.ImageData;
import bj4.yhh.changewp.utilities.PreferenceHelper;
import bj4.yhh.changewp.utilities.Utility;

/**
 * Created by yenhsunhuang on 2017/4/5.
 */

public class ChangeWallpaperService extends IntentService {
    public static final String EXTRA_FROM_JOB_SCHEDULER = "extra_from";
    public static final int FROM_UNKNOWN = -1;
    public static final int FROM_JOB_SCHEDULER = 1000;
    private static final String TAG = "ChangeWallpaperService";
    private static final boolean DEBUG = false;

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

        String nextWallpaperPath = popNextWallpaperPath();

        if (nextWallpaperPath == null) return;
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        if (DEBUG) {
            Log.d(TAG, "w: " + wallpaperManager.getDesiredMinimumWidth() + ", h: " + wallpaperManager.getDesiredMinimumHeight());
        }

        try {
            wallpaperManager.setWallpaperOffsetSteps(1, 1);
            Bitmap bitmap = Glide.with(this).load(new File(nextWallpaperPath)).asBitmap().into(getResources().getConfiguration().screenWidthDp, getResources().getConfiguration().screenHeightDp).get();
            wallpaperManager.suggestDesiredDimensions(bitmap.getWidth(), bitmap.getHeight());
            wallpaperManager.setBitmap(bitmap);
        } catch (Exception e) {
            Log.w(TAG, "failed to set wallpaper", e);
        }
    }

    @Nullable
    private String popNextWallpaperPath() {
        List<String> wallpaperQueue = PreferenceHelper.getWallpaperQueueList(this);
        if (wallpaperQueue.isEmpty()) {
            createWallpaperQueue();
            wallpaperQueue.addAll(PreferenceHelper.getWallpaperQueueList(this));
        }
        if (wallpaperQueue.isEmpty()) {
            Log.w(TAG, "cannot find wallpaper");
            return null;
        }
        final String rtn = wallpaperQueue.remove(0);
        PreferenceHelper.setWallpaperQueueList(this, wallpaperQueue);

        return rtn;
    }

    private void createWallpaperQueue() {
        List<String> wallpaperFolders = PreferenceHelper.getFolderList(this);
        if (wallpaperFolders.isEmpty()) return;
        List<String> wallpaperQueue = new ArrayList<>();
        List<ImageData> allImageDataList = Utility.getAllExternalStorageImageData(this);
        for (ImageData imageData : allImageDataList) {
            for (String wallpaperFolder : wallpaperFolders) {
                if (imageData.getDataPath().startsWith(wallpaperFolder)) {
                    if (DEBUG) {
                        Log.v(TAG, "Add path: " + imageData.getDataPath() + " into wallpaper queue");
                    }
                    wallpaperQueue.add(imageData.getDataPath());
                    break;
                }
            }
        }
        if (wallpaperQueue.isEmpty()) return;
        PreferenceHelper.setWallpaperQueueList(this, wallpaperQueue);
    }
}
