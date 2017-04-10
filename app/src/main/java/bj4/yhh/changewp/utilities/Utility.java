package bj4.yhh.changewp.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
                ImageData data = new ImageData(cv);
                data.setSourceType(ImageData.SOURCE_TYPE_EXTERNAL_STORAGE);
                rtn.add(data);
            }
            return rtn;
        } finally {
            cursor.close();
        }
    }

    public static Map<String, List<ImageData>> groupImageDataByFolder(List<ImageData> imageDataList) {
        Map<String, List<ImageData>> rtn = new HashMap<>();
        for (ImageData imageData : imageDataList) {
            final String mapKey = getFolderPath(imageData.getDataPath());
            List<ImageData> imageDatas = rtn.get(mapKey);
            if (imageDatas == null) {
                imageDatas = new ArrayList<>();
            }
            imageDatas.add(imageData);
            rtn.put(mapKey, imageDatas);
        }
        return rtn;
    }

    public static String getFolderPath(String path) {
        return path.substring(0, path.lastIndexOf(File.separator));
    }

    public static String getFolderName(String path) {
        List<String> paths = Uri.parse(path).getPathSegments();
        return paths.get(paths.size() - 2);
    }

    public static List<ImageData> getFirstImageDataFromGroup(Map<String, List<ImageData>> imageDataGroup) {
        List<ImageData> rtn = new ArrayList<>();
        Iterator<String> groupKeyIterator = imageDataGroup.keySet().iterator();
        while (groupKeyIterator.hasNext()) {
            final String key = groupKeyIterator.next();
            List<ImageData> groupList = imageDataGroup.get(key);
            ImageData imageData = groupList.get(0);
            imageData.setDescription(getFolderName(imageData.getDataPath()) + "(" + groupList.size() + ")");
            rtn.add(imageData);
        }
        Collections.sort(rtn, new Comparator<ImageData>() {

            @Override
            public int compare(ImageData imageData1, ImageData imageData2) {
                return getFolderName(imageData1.getDataPath()).compareTo(getFolderName(imageData2.getDataPath()));
            }
        });

        return rtn;
    }
}
