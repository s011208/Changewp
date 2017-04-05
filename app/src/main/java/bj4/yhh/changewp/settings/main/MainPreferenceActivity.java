package bj4.yhh.changewp.settings.main;

import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;

import bj4.yhh.albumview.ImageData;
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
        private static final String PREFERENCE_KEY_VERSION_CODE = "VERSION_CODE";
        private static final String PREFERENCE_KEY_VERSION_NAME = "VERSION_NAME";

        private static final String PREFERENCE_KEY_WALLPAPER_RESOURCE_FOLDER = "WALLPAPER_RESOURCE_FOLDER";

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.main_preference);
            updateChangeWallpaperIntervalSummary();
            updateVersionInfo();
            updateWallpaperResourceFolder();
        }

        private void updateWallpaperResourceFolder() {
            Preference preference = findPreference(PREFERENCE_KEY_WALLPAPER_RESOURCE_FOLDER);
            if (preference != null) {
                final int sourceType = PreferenceHelper.getSharedPreference(getActivity()).getInt(PreferenceHelper.KEY_SOURCE_TYPE, -1);
                if (sourceType == -1) return;
                final String sourceFolder = PreferenceHelper.getSharedPreference(getActivity()).getString(PreferenceHelper.KEY_SOURCE_FOLDER, null);
                if (ImageData.SOURCE_TYPE_EXTERNAL_STORAGE == sourceType) {
                    preference.setSummary(getString(bj4.yhh.albumview.R.string.source_type_external_storage_text) + " - " + sourceFolder);
                } else if (ImageData.SOURCE_TYPE_GOOGLE_DRIVE == sourceType) {
                    preference.setSummary(getString(bj4.yhh.albumview.R.string.source_type_google_drive_text) + " - " + sourceFolder);
                } else if (ImageData.SOURCE_TYPE_OTHER_CLOUD == sourceType) {
                    preference.setSummary(getString(bj4.yhh.albumview.R.string.source_type_other_cloud_text) + " - " + sourceFolder);
                } else {
                    throw new RuntimeException("unexpected source type: " + sourceType);
                }
            }
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

        private void updateVersionInfo() {
            Preference preference = findPreference(PREFERENCE_KEY_VERSION_CODE);
            if (preference != null) {
                try {
                    preference.setSummary(String.valueOf(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            preference = findPreference(PREFERENCE_KEY_VERSION_NAME);
            if (preference != null) {
                try {
                    preference.setSummary(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            final String preferenceKey = preference.getKey();
            if (PREFERENCE_KEY_CHANGE_WALLPAPER_INTERVAL.equals(preferenceKey)) {
                if (DEBUG) Log.d(TAG, "PREFERENCE_KEY_CHANGE_WALLPAPER_INTERVAL");
                WallpaperTimeInterval.DialogFragmentImp dialogFragment = new WallpaperTimeInterval.DialogFragmentImp();
                Bundle argument = new Bundle();
                argument.putInt(WallpaperTimeInterval.EXTRA_KEY_SELECTION, PreferenceHelper.getSharedPreference(getActivity()).getInt(PreferenceHelper.KEY_CHANGE_WALLPAPER_INTERVAL, WallpaperTimeInterval.ONE_MINUTE));
                dialogFragment.setArguments(argument);
                dialogFragment.show(getFragmentManager(), WallpaperTimeInterval.DialogFragmentImp.class.getSimpleName());
                return true;
            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
    }
}
