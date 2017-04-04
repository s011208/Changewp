package bj4.yhh.changewp.imagesource;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bj4.yhh.changewp.R;

/**
 * Created by s011208 on 2017/4/4.
 */

public class ImageSourceViewHolder extends RecyclerView.ViewHolder {
    private TextView mImageSourceText;
    private ImageView mImageSourceIcon;
    private View mContainer;

    public ImageSourceViewHolder(View itemView) {
        super(itemView);
        mContainer = itemView;
        mImageSourceIcon = (ImageView) itemView.findViewById(R.id.source_icon);
        mImageSourceText = (TextView) itemView.findViewById(R.id.source_text);
    }

    public View getContainer() {
        return mContainer;
    }

    public TextView getImageSourceText() {
        return mImageSourceText;
    }

    public ImageView getImageSourceIcon() {
        return mImageSourceIcon;
    }
}
