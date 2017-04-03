package bj4.yhh.albumview;

import android.content.ContentValues;
import android.provider.MediaStore;

/**
 * Created by s011208 on 2017/4/2.
 */

public class ImageData {
    public static final int SOURCE_TYPE_NONE = 0;
    public static final int SOURCE_TYPE_EXTERNAL_STORAGE = 1;
    public static final int SOURCE_TYPE_GOOGLE_DRIVE = 2;
    public static final int SOURCE_TYPE_OTHER_CLOUD = 3;

    private final String mDataPath;
    private final String mDisplayName;
    private final long mDateTaken;
    private String mDescription;
    private int mSourceType = SOURCE_TYPE_EXTERNAL_STORAGE;

    public ImageData(String dataPath, String displayName, long dateTaken) {
        mDataPath = dataPath;
        mDisplayName = displayName;
        mDateTaken = dateTaken;
    }

    /**
     * @param cv from MediaStore.Images.ImageColumns;
     */
    public ImageData(ContentValues cv) {
        mDataPath = cv.getAsString(MediaStore.Images.ImageColumns.DATA);
        mDisplayName = cv.getAsString(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        mDateTaken = cv.getAsLong(MediaStore.Images.ImageColumns.DATE_TAKEN);
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getSourceType() {
        return mSourceType;
    }

    public void setSourceType(int sourceType) {
        mSourceType = sourceType;
    }

    public String getDataPath() {
        return mDataPath;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public long getDateTaken() {
        return mDateTaken;
    }
}
