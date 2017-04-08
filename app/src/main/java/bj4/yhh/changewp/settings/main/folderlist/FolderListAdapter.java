package bj4.yhh.changewp.settings.main.folderlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bj4.yhh.changewp.R;
import bj4.yhh.changewp.utilities.PreferenceHelper;

/**
 * Created by s011208 on 2017/4/9.
 */

public class FolderListAdapter extends RecyclerView.Adapter<FolderListViewHolder> {
    private final WeakReference<Context> mContext;
    private final List<String> mData = new ArrayList<>();

    public FolderListAdapter(Context context) {
        mContext = new WeakReference<>(context);
        mData.addAll(PreferenceHelper.getFolderList(context));
    }

    @Override
    public FolderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = mContext.get();
        if (context == null) return null;
        return new FolderListViewHolder(LayoutInflater.from(context).inflate(R.layout.folder_source_item, null));
    }

    @Override
    public void onBindViewHolder(FolderListViewHolder holder, int position) {
        holder.getFolderPath().setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
