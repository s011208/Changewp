package bj4.yhh.albumview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by s011208 on 2017/4/2.
 */

public class AlbumView extends RecyclerView implements AlbumViewAdapter.Callback {
    private static final String TAG = "AlbumView";
    private static final boolean DEBUG = true;

    // data list
    private final List<ImageData> mImageDataList = new ArrayList<>();

    // views
    private AlbumViewAdapter mAlbumViewAdapter;
    private LayoutManager mLayoutManager;
    private ItemDecoration mItemDecoration;

    // config
    private boolean mEnableSpan = false;
    private boolean mEnableGridMargin = true;
    private int mSpanSize;

    // other
    private WeakReference<Callback> mCallback;

    public AlbumView(@NonNull Context context) {
        this(context, null);
    }

    public AlbumView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlbumView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSpanSize = getResources().getInteger(R.integer.album_view_span_size);
        setBackgroundColor(Color.BLACK);
        setHasFixedSize(true);
        mItemDecoration = new MarginDecoration(getContext());
    }

    public AlbumView setSpanSize(int spanSize) {
        mSpanSize = spanSize;
        return this;
    }

    public AlbumView setEnableSpan(boolean enableSpan) {
        mEnableSpan = enableSpan;
        return this;
    }

    public AlbumView setEnableGridMargin(boolean enableGridMargin) {
        mEnableGridMargin = enableGridMargin;
        return this;
    }

    public void buildAlbumView() {
        mAlbumViewAdapter = new AlbumViewAdapter(getContext(), this);
        mLayoutManager = new GridLayoutManager(getContext(), mSpanSize);
        if (mEnableGridMargin) {
            removeItemDecoration(mItemDecoration);
            addItemDecoration(mItemDecoration);
        } else {
            removeItemDecoration(mItemDecoration);
        }
        if (mEnableSpan) {
            ((GridLayoutManager) mLayoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position % 3 == 0) {
                        return mSpanSize;
                    } else {
                        return 1;
                    }
                }
            });
        }
        setLayoutManager(mLayoutManager);
        mAlbumViewAdapter.setDataList(mImageDataList);
        setAdapter(mAlbumViewAdapter);
    }

    public void setImageDataList(List<ImageData> imageDataList) {
        mImageDataList.clear();
        mImageDataList.addAll(imageDataList);
        mAlbumViewAdapter.setDataList(mImageDataList);
        mAlbumViewAdapter.notifyDataSetChanged();
    }

    public void setSelections(List<Integer> position) {
        mAlbumViewAdapter.setSelectedItems(position);
        mAlbumViewAdapter.notifyDataSetChanged();
    }

    public void setSelection(int position) {
        mAlbumViewAdapter.setSelectedItem(position);
        mAlbumViewAdapter.notifyItemChanged(position);
    }

    public List<Integer> getSelections() {
        return mAlbumViewAdapter.getSelectedItems();
    }

    @Override
    public void onItemClick(int position) {
        if (DEBUG) {
            Log.d(TAG, "onItemClick, position: " + position);
        }
        final Callback cb = mCallback.get();
        if (cb == null) return;
        cb.onItemClick(position);
    }

    @Override
    public void onItemLongClick(int position) {
        if (DEBUG) {
            Log.d(TAG, "onItemLongClick, position: " + position);
        }
        final Callback cb = mCallback.get();
        if (cb == null) return;
        cb.onItemLongClick(position);
    }

    public void setCallback(Callback cb) {
        mCallback = new WeakReference<>(cb);
    }

    public interface Callback {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }
}
