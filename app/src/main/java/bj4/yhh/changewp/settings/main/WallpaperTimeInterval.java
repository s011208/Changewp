package bj4.yhh.changewp.settings.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseArray;

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

    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    private static final SparseArray<Long> INTERVAL_ARRAY = new SparseArray<>();

    static {
        INTERVAL_ARRAY.put(TEN_SECONDS, 10 * SECOND);
        INTERVAL_ARRAY.put(THIRTY_SECONDS, 30 * SECOND);
        INTERVAL_ARRAY.put(ONE_MINUTE, MINUTE);
        INTERVAL_ARRAY.put(THREE_MINUTES, 3 * MINUTE);
        INTERVAL_ARRAY.put(FIVE_MINUTES, 5 * MINUTE);
        INTERVAL_ARRAY.put(TEN_MINUTES, 10 * MINUTE);
        INTERVAL_ARRAY.put(THIRTY_MINUTES, 30 * MINUTE);
        INTERVAL_ARRAY.put(ONE_HOUR, HOUR);
        INTERVAL_ARRAY.put(SIX_HOUR, 6 * HOUR);
        INTERVAL_ARRAY.put(ONE_DAY, DAY);
    }

    public static long getTimeInterval(Context context) {
        return INTERVAL_ARRAY.get(PreferenceHelper.getWallpaperInterval(context, ONE_DAY));
    }

    public static String getIntervalString(Context context, int index) {
        String[] items = context.getResources().getStringArray(R.array.preference_change_wallpaper_interval_selection);
        return items[index];
    }

    public static class DialogFragmentImp extends DialogFragment {

        private int mSelectedIndex;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            mSelectedIndex = ONE_MINUTE;
            Bundle bundle = getArguments();
            if (bundle != null) {
                mSelectedIndex = bundle.getInt(EXTRA_KEY_SELECTION, ONE_MINUTE);
            }
            return new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                    .setTitle(R.string.main_preference_change_wallpaper_interval)
                    .setSingleChoiceItems(R.array.preference_change_wallpaper_interval_selection, mSelectedIndex, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mSelectedIndex = i;
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
            PreferenceHelper.setWallpaperInterval(getActivity(), mSelectedIndex);
            if (getActivity() instanceof DialogCallback) {
                ((DialogCallback) getActivity()).onDismiss(DialogFragmentImp.this);
            }
        }
    }
}
