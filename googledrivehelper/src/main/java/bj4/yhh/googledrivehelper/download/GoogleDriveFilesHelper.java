package bj4.yhh.googledrivehelper.download;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import bj4.yhh.googledrivehelper.GoogleDriveWrapper;
import bj4.yhh.googledrivehelper.query.GoogleDriveTask;
import bj4.yhh.googledrivehelper.query.QueryCallback;

/**
 * Created by s011208 on 2017/4/16.
 */

public class GoogleDriveFilesHelper {
    private static final boolean DEBUG = true;
    private static final String TAG = "GoogleDriveFilesHelper";

    private static GoogleDriveFilesHelper sInstance;
    private WeakReference<Context> mContext;

    private GoogleDriveFilesHelper(Context context) {
        mContext = new WeakReference<>(context.getApplicationContext());
    }

    public static synchronized GoogleDriveFilesHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GoogleDriveFilesHelper(context);
        }
        return sInstance;
    }


    public void saveFile(GoogleDriveWrapper mGoogleDriveWrapper, String folderId, String fileId) {
        new GoogleDriveTask(mGoogleDriveWrapper, new QueryCallback() {
            @Override
            public void onQueryError(Exception e) {

            }

            @Nullable
            @Override
            public void onQueryResult(FileList fileList) {
                Log.w(TAG, "finish");
            }
        }, "change wp") {
            @Override
            protected FileList doInBackground(Void... voids) {
                String fileId = "0BxipNGTIBjveNU5nc3hTM1RfXzA";
                OutputStream outputStream = new ByteArrayOutputStream();
                try {
                    getDriveService().files().get(fileId)
                            .executeMediaAndDownloadTo(outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.setQueryTrash(false).execute();
    }
}
