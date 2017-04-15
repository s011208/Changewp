package bj4.yhh.googledrivehelper.query;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bj4.yhh.googledrivehelper.GoogleDriveWrapper;
import bj4.yhh.googledrivehelper.QUtility;

/**
 * Created by s011208 on 2017/4/4.
 */

public class QueryAllFoldersTask extends AsyncTask<Void, Void, FileList> {
    public static final String DRIVE_FILE_ROOT = "root";

    private String mDriveFolderId = DRIVE_FILE_ROOT;
    private Drive mDriveService;
    private Exception mLastError = null;
    private WeakReference<GoogleDriveWrapper> mGoogleDriveWrapper;
    private WeakReference<QueryCallback> mCallback;
    private boolean mIncludeTrash = false;

    public QueryAllFoldersTask(GoogleDriveWrapper wrapper, QueryCallback cb) {
        this(wrapper, cb, "Change wp");
    }

    public QueryAllFoldersTask(GoogleDriveWrapper wrapper, QueryCallback cb, String applicationNAme) {
        mCallback = new WeakReference<>(cb);
        mGoogleDriveWrapper = new WeakReference<>(wrapper);

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mDriveService = new com.google.api.services.drive.Drive.Builder(
                transport, jsonFactory, mGoogleDriveWrapper.get().getCredential())
                .setApplicationName(applicationNAme)
                .build();
    }

    public QueryAllFoldersTask queryTrash(boolean includeTrash) {
        mIncludeTrash = includeTrash;
        return this;
    }

    @Override
    protected FileList doInBackground(Void... voids) {
        try {
            List<String> conditions = new ArrayList<>();
            conditions.add("mimeType = 'application/vnd.google-apps.folder'");
            if (!mIncludeTrash) {
                conditions.add("trashed = false");
            }
            String finalCondition = QUtility.generateQCondition(conditions);
            return mDriveService.files().list()
                    .setOrderBy("folder")
                    .setQ(finalCondition)
                    .execute();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(FileList fileList) {
        super.onPostExecute(fileList);
        QueryCallback cb = mCallback.get();
        if (cb == null) return;
        cb.onQueryResult(fileList);
    }

    @Override
    protected void onCancelled() {
        QueryCallback cb = mCallback.get();
        if (cb == null) return;
        if (mLastError == null) return;
        cb.onQueryError(mLastError);
    }
}
