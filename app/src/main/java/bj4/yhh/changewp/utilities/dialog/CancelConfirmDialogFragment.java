package bj4.yhh.changewp.utilities.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import bj4.yhh.changewp.R;

/**
 * Created by s011208 on 2017/4/9.
 */

public class CancelConfirmDialogFragment extends DialogFragment {
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MSG = "message";

    public WeakReference<Callback> mCallback;

    public void setCallback(Callback cb) {
        mCallback = new WeakReference<>(cb);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String title = null, message = null;
        if (arguments != null) {
            title = arguments.getString(EXTRA_TITLE);
            message = arguments.getString(EXTRA_MSG);
        }

        if (title == null) {
            title = getString(R.string.cancel_confirm_dialog_default_title);
        }
        if (message == null) {
            message = getString(R.string.cancel_confirm_dialog_default_message);
        }
        return new AlertDialog.Builder(getActivity())
                .setTitle(title).setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (getActivity() instanceof Callback) {
                            ((Callback) getActivity()).onPositiveClick();
                        }
                        final Callback cb = mCallback.get();
                        if (cb != null) {
                            cb.onPositiveClick();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (getActivity() instanceof Callback) {
                            ((Callback) getActivity()).onNegativeClick();
                        }
                        final Callback cb = mCallback.get();
                        if (cb != null) {
                            cb.onNegativeClick();
                        }
                    }
                }).create();
    }

    public interface Callback {
        void onPositiveClick();

        void onNegativeClick();
    }
}
