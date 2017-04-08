package bj4.yhh.changewp.settings.main.folderlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bj4.yhh.changewp.R;

/**
 * Created by s011208 on 2017/4/9.
 */

public class FolderListViewHolder extends RecyclerView.ViewHolder {
    private ImageView mDeleteIcon;
    private TextView mFolderPath;

    public FolderListViewHolder(View itemView) {
        super(itemView);
        mDeleteIcon = (ImageView) itemView.findViewById(R.id.delete);
        mFolderPath = (TextView) itemView.findViewById(R.id.folder_path);
    }

    public ImageView getDeleteIcon() {
        return mDeleteIcon;
    }

    public TextView getFolderPath() {
        return mFolderPath;
    }
}
