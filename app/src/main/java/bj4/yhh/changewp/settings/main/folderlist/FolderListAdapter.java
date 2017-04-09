package bj4.yhh.changewp.settings.main.folderlist;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bj4.yhh.changewp.R;
import bj4.yhh.changewp.utilities.PreferenceHelper;
import bj4.yhh.changewp.utilities.dialog.CancelConfirmDialogFragment;

/**
 * Created by s011208 on 2017/4/9.
 */

public class FolderListAdapter extends RecyclerView.Adapter<FolderListViewHolder> implements CancelConfirmDialogFragment.Callback {
    private final WeakReference<Activity> mActivity;
    private final WeakReference<Callback> mCallback;
    private final List<String> mData = new ArrayList<>();
    private int mSelectedPosition = -1;

    public FolderListAdapter(Activity context, Callback cb) {
        mActivity = new WeakReference<>(context);
        mData.addAll(PreferenceHelper.getFolderList(context));
        mCallback = new WeakReference<>(cb);
    }

    @Override
    public FolderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Activity activity = mActivity.get();
        if (activity == null) return null;
        return new FolderListViewHolder(LayoutInflater.from(activity).inflate(R.layout.folder_source_item, null));
    }

    @Override
    public void onBindViewHolder(FolderListViewHolder holder, final int position) {
        holder.getFolderPath().setText(mData.get(position));
        holder.getDeleteIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedPosition = position;
                final Activity activity = mActivity.get();
                if (activity == null) return;
                CancelConfirmDialogFragment dialog = new CancelConfirmDialogFragment();
                dialog.setCallback(FolderListAdapter.this);
                dialog.show(activity.getFragmentManager(), CancelConfirmDialogFragment.class.getSimpleName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onPositiveClick() {
        if (mSelectedPosition == -1) return;
        final Context context = mActivity.get();
        if (context == null) return;
        PreferenceHelper.removeFolderFromFolderList(context, mData.get(mSelectedPosition));
        mData.remove(mSelectedPosition);

        Callback cb = mCallback.get();
        if (mData.isEmpty() && cb != null) {
            cb.onEmptyAdapter();
        } else {
            notifyItemRemoved(mSelectedPosition);
            notifyItemRangeChanged(mSelectedPosition, getItemCount());
        }
    }

    @Override
    public void onNegativeClick() {
        mSelectedPosition = -1;
    }

    public interface Callback {
        void onEmptyAdapter();
    }
}
