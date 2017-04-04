package bj4.yhh.changewp.imagesource;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bj4.yhh.changewp.R;

/**
 * Created by s011208 on 2017/4/4.
 */

public class ImageSourceAdapter extends RecyclerView.Adapter<ImageSourceViewHolder> {
    public static final int SOURCE_FROM_EXTERNAL_STORAGE = 0;
    public static final int SOURCE_FROM_GOOGLE_DRIVE = 1;

    private final List<String> mSourceList = new ArrayList<>();
    private WeakReference<Context> mContext;
    private WeakReference<Callback> mCallback;

    public ImageSourceAdapter(Context context, Callback cb) {
        mContext = new WeakReference<>(context);
        mCallback = new WeakReference<>(cb);
        mSourceList.add(context.getString(R.string.source_name_from_external_storage));
        mSourceList.add(context.getString(R.string.source_name_from_google_Drive));
    }

    @Override
    public ImageSourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = mContext.get();
        if (context == null) return null;
        return new ImageSourceViewHolder(LayoutInflater.from(context).inflate(R.layout.image_resource_adapter_item, null));
    }

    @Override
    public void onBindViewHolder(ImageSourceViewHolder holder, final int position) {
        if (position == SOURCE_FROM_EXTERNAL_STORAGE) {
            holder.getImageSourceIcon().setImageResource(R.drawable.source_external_storage);
        } else if (position == SOURCE_FROM_GOOGLE_DRIVE) {
            holder.getImageSourceIcon().setImageResource(R.drawable.source_google_drive);
        }
        holder.getImageSourceText().setText(mSourceList.get(position));
        holder.getContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callback cb = mCallback.get();
                if (cb == null) return;
                cb.imageSourceFrom(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSourceList.size();
    }

    public interface Callback {
        void imageSourceFrom(int from);
    }
}
