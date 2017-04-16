package bj4.yhh.googledrivehelper.query;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

import java.lang.ref.WeakReference;

import bj4.yhh.googledrivehelper.GoogleDriveWrapper;

/**
 * Created by s011208 on 2017/4/4.
 */

public abstract class GoogleDriveTask extends AsyncTask<Void, Void, FileList> {
    public static final String DRIVE_FILE_ROOT = "root";

    private Drive mDriveService;
    private Exception mLastError = null;
    private String mDriveFolderId;
    private WeakReference<GoogleDriveWrapper> mGoogleDriveWrapper;
    private WeakReference<QueryCallback> mCallback;
    private boolean mIncludeTrash = false;

    public GoogleDriveTask(GoogleDriveWrapper wrapper, QueryCallback cb, String applicationNAme) {
        mCallback = new WeakReference<>(cb);
        mGoogleDriveWrapper = new WeakReference<>(wrapper);

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mDriveService = new Drive.Builder(
                transport, jsonFactory, mGoogleDriveWrapper.get().getCredential())
                .setApplicationName(applicationNAme)
                .build();
    }

    public void setLastException(Exception lastException) {
        mLastError = lastException;
    }

    public Exception getLastException() {
        return mLastError;
    }

    public WeakReference<QueryCallback> getCallback() {
        return mCallback;
    }

    public Drive getDriveService() {
        return mDriveService;
    }

    public GoogleDriveTask setQueryTrash(boolean includeTrash) {
        mIncludeTrash = includeTrash;
        return this;
    }

    public boolean getQueryTrash() {
        return mIncludeTrash;
    }

    public GoogleDriveTask setDriveFolderId(String id) {
        mDriveFolderId = id;
        return this;
    }

    public String getDriveFolderId() {
        return mDriveFolderId;
    }

    @Override
    protected abstract FileList doInBackground(Void... voids);

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
