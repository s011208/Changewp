package bj4.yhh.changewp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by s011208 on 2017/4/4.
 */

public class PreferenceHelper {
    public static final String KEY_CHANGE_WALLPAPER_INTERVAL = "key_change_wallpaper_interval";

    private static final String SHARED_PREFERENCE_KEY = "bj4.yhh.changewp.utilities.pref";

    private static SharedPreferences sSharedPreference;

    public static synchronized SharedPreferences getSharedPreference(Context context) {
        if (sSharedPreference == null) {
            sSharedPreference = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        }
        return sSharedPreference;
    }
}
