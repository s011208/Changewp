package bj4.yhh.changewp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by s011208 on 2017/4/4.
 */

public class PreferenceHelper {
    private static final String KEY_CHANGE_WALLPAPER_INTERVAL = "key_change_wallpaper_interval";

    private static final String KEY_FOLDER_SOURCE_TYPE = "key_folder_source_type";
    private static final String KEY_SOURCE_FOLDER = "key_source_folder";
    private static final String KEY_WALLPAPER_QUEUE_LIST = "key_wallpaper_file_path";


    private static final String SHARED_PREFERENCE_KEY = "bj4.yhh.changewp.utilities.pref";

    private static SharedPreferences sSharedPreference;

    public static synchronized SharedPreferences getSharedPreference(Context context) {
        if (sSharedPreference == null) {
            sSharedPreference = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        }
        return sSharedPreference;
    }

    public static int getWallpaperInterval(Context context, int defaultValue) {
        return getSharedPreference(context).getInt(KEY_CHANGE_WALLPAPER_INTERVAL, defaultValue);
    }

    public static void setWallpaperInterval(Context context, int value) {
        getSharedPreference(context).edit().putInt(KEY_CHANGE_WALLPAPER_INTERVAL, value).commit();
    }

    public static int getFolderSourceType(Context context, int defaultValue) {
        return getSharedPreference(context).getInt(KEY_FOLDER_SOURCE_TYPE, defaultValue);
    }

    public static void setFolderSourceType(Context context, int value) {
        getSharedPreference(context).edit().putInt(KEY_FOLDER_SOURCE_TYPE, value).commit();
    }

    public static List<String> getFolderList(Context context) {
        return getList(context, KEY_SOURCE_FOLDER);
    }

    public static void removeFolderFromFolderList(Context context, String folder) {
        removeFromList(context, KEY_SOURCE_FOLDER, folder);
    }

    public static void setFolderList(Context context, List<String> values) {
        setList(context, KEY_SOURCE_FOLDER, values);
    }

    public static List<String> getWallpaperQueueList(Context context) {
        return getList(context, KEY_WALLPAPER_QUEUE_LIST);
    }

    public static void setWallpaperQueueList(Context context, List<String> items) {
        setList(context, KEY_WALLPAPER_QUEUE_LIST, items);
    }

    public static List<String> getList(Context context, String key) {
        List<String> rtn = new ArrayList<>();
        rtn.addAll(getSharedPreference(context).getStringSet(key, new HashSet<String>()));
        return rtn;
    }

    public static void removeFromList(Context context, String key, String item) {
        Set<String> folderList = getSharedPreference(context).getStringSet(key, new HashSet<String>());
        folderList.remove(item);
        getSharedPreference(context).edit().putStringSet(item, new HashSet<>(folderList)).commit();
    }

    public static void setList(Context context, String key, List<String> values) {
        Set<String> setValue = new HashSet<>();
        setValue.addAll(values);
        getSharedPreference(context).edit().putStringSet(key, setValue).commit();
    }
}
