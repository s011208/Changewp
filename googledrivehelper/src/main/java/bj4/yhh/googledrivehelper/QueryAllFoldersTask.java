package bj4.yhh.googledrivehelper;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by s011208 on 2017/4/4.
 */

public class QueryAllFoldersTask extends AsyncTask<Void, Void, FileList> {
    public static final String DRIVE_FILE_ROOT = "root";

    private String mDriveFolderId = DRIVE_FILE_ROOT;
    private Drive mDriveService;
    private Exception mLastError = null;
    private WeakReference<GoogleDriveWrapper> mGoogleDriveWrapper;
    private WeakReference<Callback> mCallback;
    private boolean mIncludeTrash = false;

    public QueryAllFoldersTask(GoogleDriveWrapper wrapper, Callback cb) {
        this(wrapper, cb, "Change wp");
    }

    public QueryAllFoldersTask(GoogleDriveWrapper wrapper, Callback cb, String applicationNAme) {
        mCallback = new WeakReference<>(cb);
        mGoogleDriveWrapper = new WeakReference<>(wrapper);

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mDriveService = new com.google.api.services.drive.Drive.Builder(
                transport, jsonFactory, mGoogleDriveWrapper.get().getCredential())
                .setApplicationName(applicationNAme)
                .build();
    }

    public QueryAllFoldersTask setDriveFolderId(String folderId) {
        mDriveFolderId = folderId;
        return this;
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
            conditions.add("'" + mDriveFolderId + "' in parents");
            if (!mIncludeTrash) {
                conditions.add("trashed = false");
            }
            String finalCondition = QUtility.generateQCondition(conditions);
            return mDriveService.files().list()
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
        Callback cb = mCallback.get();
        if (cb == null) return;
        cb.onResult(fileList);
    }

    @Override
    protected void onCancelled() {
        Callback cb = mCallback.get();
        if (cb == null) return;
        if (mLastError == null) return;
        cb.onError(mLastError);
    }

    public interface Callback {
        void onError(Exception e);

        @Nullable
        void onResult(FileList fileList);
    }
}
