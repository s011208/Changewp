package bj4.yhh.changewp.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import bj4.yhh.albumview.ImageData;

/**
 * Created by s011208 on 2017/4/2.
 */

public class Utility {
    public static List<ImageData> getAllExternalStorageImageData(Context context) {
        List<ImageData> rtn = new ArrayList<>();
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor == null) return rtn;
        ContentValues cv = new ContentValues();
        try {
            while (cursor.moveToNext()) {
                cv.clear();
                DatabaseUtils.cursorRowToContentValues(cursor, cv);
                rtn.add(new ImageData(cv));
            }
            return rtn;
        } finally {
            cursor.close();
        }
    }

}
