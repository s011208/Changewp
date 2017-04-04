package bj4.yhh.changewp.settings.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import bj4.yhh.changewp.R;
import bj4.yhh.changewp.settings.DialogCallback;
import bj4.yhh.changewp.utilities.PreferenceHelper;

/**
 * Created by s011208 on 2017/4/5.
 */

public class WallpaperTimeInterval {
    public static final String EXTRA_KEY_SELECTION = "key_selection";

    public static final int TEN_SECONDS = 0;
    public static final int THIRTY_SECONDS = 1;
    public static final int ONE_MINUTE = 2;
    public static final int THREE_MINUTES = 3;
    public static final int FIVE_MINUTES = 4;
    public static final int TEN_MINUTES = 5;
    public static final int THIRTY_MINUTES = 6;
    public static final int ONE_HOUR = 7;
    public static final int SIX_HOUR = 8;
    public static final int ONE_DAY = 9;

    public static String getIntervalString(Context context, int index) {
        String[] items = context.getResources().getStringArray(R.array.preference_change_wallpaper_interval_selection);
        return items[index];
    }

    public static class DialogFragmentImp extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int selection = ONE_MINUTE;
            Bundle bundle = getArguments();
            if (bundle != null) {
                bundle.getInt(EXTRA_KEY_SELECTION, ONE_MINUTE);
            }
            return new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                    .setTitle(R.string.main_preference_change_wallpaper_interval)
                    .setSingleChoiceItems(R.array.preference_change_wallpaper_interval_selection, selection, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            PreferenceHelper.getSharedPreference(getActivity()).edit()
                                    .putInt(PreferenceHelper.KEY_CHANGE_WALLPAPER_INTERVAL, i).commit();
                            dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            if (getActivity() instanceof DialogCallback) {
                ((DialogCallback) getActivity()).onDismiss(DialogFragmentImp.this);
            }
        }
    }
}
