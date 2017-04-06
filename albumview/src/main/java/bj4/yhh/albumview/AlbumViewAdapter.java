package bj4.yhh.albumview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by s011208 on 2017/4/2.
 */

public class AlbumViewAdapter extends RecyclerView.Adapter<AlbumViewViewHolder> {
    private final WeakReference<Context> mContext;
    private final WeakReference<Callback> mCallback;
    private final List<ImageData> mDataList = new ArrayList<>();
    private final List<Integer> mSelectedItem = new ArrayList<>();

    private int mMaximumItemSize;

    public AlbumViewAdapter(Context context, Callback cb) {
        mContext = new WeakReference(context);
        mCallback = new WeakReference<>(cb);
        mMaximumItemSize = context.getResources().getDisplayMetrics().widthPixels / context.getResources().getInteger(R.integer.album_view_span_size);
    }

    @Override
    public AlbumViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = mContext.get();
        if (context == null) return null;
        return new AlbumViewViewHolder(LayoutInflater.from(context).inflate(R.layout.album_item, null));
    }

    public void setSelectedItem(int position) {
        if (mSelectedItem.contains(position)) mSelectedItem.remove(mSelectedItem.indexOf(position));
        else mSelectedItem.add(position);
    }

    public List<Integer> getSelectedItems() {
        return mSelectedItem;
    }

    public void setSelectedItems(List<Integer> positions) {
        mSelectedItem.clear();
        mSelectedItem.addAll(positions);
    }

    @Override
    public void onBindViewHolder(final AlbumViewViewHolder holder, final int position) {
        final Context context = mContext.get();
        if (context == null) return;
        final ImageData imageData = getItem(position);
        final View eventHandler = holder.getEventHandler();
        final ImageView imageView = holder.getImageHolderView();
        final ProgressBar loadingView = holder.getLoadingView();
        final ImageView iconSourceImageView = holder.getSourceTypeIcon();
        final TextView descriptionTextView = holder.getDescriptionTextView();
        final RelativeLayout selectionBar = holder.getSelectionBar();

        eventHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Callback cb = mCallback.get();
                if (cb == null) return;
                cb.onItemClick(position);
            }
        });

        eventHandler.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Callback cb = mCallback.get();
                if (cb == null) return false;
                cb.onItemLongClick(position);
                return true;
            }
        });
        loadingView.setVisibility(View.VISIBLE);
        Glide.with(context).load(new File(imageData.getDataPath())).override(mMaximumItemSize, mMaximumItemSize)
                .listener(new RequestListener<File, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                        loadingView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        loadingView.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(android.R.drawable.ic_dialog_alert).centerCrop().into(imageView);

        if (imageData.getSourceType() == ImageData.SOURCE_TYPE_EXTERNAL_STORAGE) {
            iconSourceImageView.setImageResource(R.drawable.source_external_storage);
            descriptionTextView.setText(R.string.source_type_external_storage_text);
        } else if (imageData.getSourceType() == ImageData.SOURCE_TYPE_GOOGLE_DRIVE) {
            iconSourceImageView.setImageResource(R.drawable.source_google_drive);
            descriptionTextView.setText(R.string.source_type_google_drive_text);
        } else if (imageData.getSourceType() == ImageData.SOURCE_TYPE_OTHER_CLOUD) {
            iconSourceImageView.setImageResource(R.drawable.source_cloud_other);
            descriptionTextView.setText(R.string.source_type_other_cloud_text);
        } else {
            iconSourceImageView.setImageResource(0);
            descriptionTextView.setText(null);
        }

        if (!TextUtils.isEmpty(imageData.getDescription())) {
            descriptionTextView.setText(imageData.getDescription());
        }

        selectionBar.setBackgroundColor(mSelectedItem.contains(position) ? Color.argb(0x90, 0xff, 0x3e, 0xff) : Color.argb(0x90, 0x00, 0x00, 0x00));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public ImageData getItem(int position) {
        return mDataList.get(position);
    }

    public void setDataList(List<ImageData> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
    }

    interface Callback {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }
}
