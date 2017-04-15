package bj4.yhh.googledrivehelper.query;

import android.support.annotation.Nullable;

import com.google.api.services.drive.model.FileList;

/**
 * Created by s011208 on 2017/4/15.
 */

public interface QueryCallback {
    void onQueryError(Exception e);

    @Nullable
    void onQueryResult(FileList fileList);
}
