package bj4.yhh.albumview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by s011208 on 2017/4/2.
 */

public class AlbumViewViewHolder extends RecyclerView.ViewHolder {

    private ImageView mImageHolderView;
    private RelativeLayout mContainer;
    private ProgressBar mLoadingView;
    private View mEventHandler;
    private ImageView mSourceTypeIcon;
    private TextView mDescription;
    private RelativeLayout mSelectionBar;

    public AlbumViewViewHolder(View itemView) {
        super(itemView);
        mContainer = (RelativeLayout) itemView;
        mImageHolderView = (ImageView) mContainer.findViewById(R.id.image_holder);
        mLoadingView = (ProgressBar) mContainer.findViewById(R.id.loading_view);
        mEventHandler = mContainer.findViewById(R.id.event_handler);
        mSourceTypeIcon = (ImageView) mContainer.findViewById(R.id.source_type_icon);
        mDescription = (TextView) mContainer.findViewById(R.id.item_description);
        mSelectionBar = (RelativeLayout) mContainer.findViewById(R.id.selection_bar);
    }

    public TextView getDescriptionTextView() {
        return mDescription;
    }

    public ImageView getImageHolderView() {
        return mImageHolderView;
    }

    public RelativeLayout getContainer() {
        return mContainer;
    }

    public ProgressBar getLoadingView() {
        return mLoadingView;
    }

    public View getEventHandler() {
        return mEventHandler;
    }

    public ImageView getSourceTypeIcon() {
        return mSourceTypeIcon;
    }

    public RelativeLayout getSelectionBar() {
        return mSelectionBar;
    }
}
