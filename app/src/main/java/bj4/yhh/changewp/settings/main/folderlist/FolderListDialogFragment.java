package bj4.yhh.changewp.settings.main.folderlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import bj4.yhh.changewp.R;
import bj4.yhh.changewp.settings.DialogCallback;

/**
 * Created by s011208 on 2017/4/8.
 */

public class FolderListDialogFragment extends DialogFragment implements FolderListAdapter.Callback {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity();
        RecyclerView recyclerView = new RecyclerView(context);
        FolderListAdapter folderListAdapter = new FolderListAdapter(getActivity(), this);
        recyclerView.setAdapter(folderListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));
        return new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                .setTitle(R.string.folder_list_dialog_title)
                .setView(recyclerView).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() instanceof DialogCallback) {
            ((DialogCallback) getActivity()).onDismiss(FolderListDialogFragment.this);
        }
    }

    @Override
    public void onEmptyAdapter() {
        dismiss();
    }
}
