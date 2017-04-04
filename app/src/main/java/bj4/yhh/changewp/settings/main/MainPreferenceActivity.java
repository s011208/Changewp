package bj4.yhh.changewp.settings.main;

import android.app.DialogFragment;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;

import bj4.yhh.changewp.R;
import bj4.yhh.changewp.settings.AppCompatPreferenceActivity;
import bj4.yhh.changewp.settings.DialogCallback;
import bj4.yhh.changewp.utilities.PreferenceHelper;

/**
 * Created by s011208 on 2017/4/4.
 */

public class MainPreferenceActivity extends AppCompatPreferenceActivity implements DialogCallback {

    private static final String TAG = "MainPreferenceActivity";
    private static final boolean DEBUG = true;

    private MyPreferenceFragment mMyPreferenceFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyPreferenceFragment = new MyPreferenceFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, mMyPreferenceFragment).commit();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDismiss(DialogFragment instance) {
        if (instance instanceof WallpaperTimeInterval.DialogFragmentImp) {
            if (DEBUG) {
                Log.d(TAG, "WallpaperTimeInterval.DialogFragmentImp onDismiss");
            }
            mMyPreferenceFragment.updateChangeWallpaperIntervalSummary();
        }
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        private static final String PREFERENCE_KEY_CHANGE_WALLPAPER_INTERVAL = "CHANGE_WALLPAPER_INTERVAL";

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.main_preference);
            updateChangeWallpaperIntervalSummary();
        }

        private void updateChangeWallpaperIntervalSummary() {
            Preference preference = findPreference(PREFERENCE_KEY_CHANGE_WALLPAPER_INTERVAL);
            if (preference != null) {
                int index = PreferenceHelper.getSharedPreference(getActivity()).getInt(PreferenceHelper.KEY_CHANGE_WALLPAPER_INTERVAL, -1);
                if (index != -1) {
                    preference.setSummary(WallpaperTimeInterval.getIntervalString(getActivity(), index));
                }
            }
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            final String preferenceKey = preference.getKey();
            if (PREFERENCE_KEY_CHANGE_WALLPAPER_INTERVAL.equals(preferenceKey)) {
                if (DEBUG) Log.d(TAG, "PREFERENCE_KEY_CHANGE_WALLPAPER_INTERVAL");
                WallpaperTimeInterval.DialogFragmentImp dialogFragment = new WallpaperTimeInterval.DialogFragmentImp();
                dialogFragment.show(getFragmentManager(), WallpaperTimeInterval.DialogFragmentImp.class.getSimpleName());
                return true;
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
    }
}
