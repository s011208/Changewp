package bj4.yhh.changewp.googledrive;

import android.Manifest;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bj4.yhh.albumview.AlbumView;
import bj4.yhh.albumview.ImageData;
import bj4.yhh.changewp.R;
import bj4.yhh.changewp.utilities.Utility;
import bj4.yhh.googledrivehelper.GoogleDriveWrapper;
import bj4.yhh.googledrivehelper.query.GoogleDriveAllFoldersTask;
import bj4.yhh.googledrivehelper.query.QueryCallback;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GoogleDriveAlbumActivity extends AppCompatActivity implements AlbumView.Callback, QueryCallback {

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String TAG = "GoogleDriveAlbum";
    private static final boolean DEBUG = true;
    private static final String PREF_ACCOUNT_NAME = "pref_accountName";
    private AlbumView mAlbumView;
    private Map<String, List<ImageData>> mGoogleDriveImageDataMap = new HashMap<>();

    private GoogleDriveWrapper mGoogleDriveWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleDriveImageDataMap.clear();
        mGoogleDriveImageDataMap.putAll(Utility.groupImageDataByFolder(Utility.getAllExternalStorageImageData(GoogleDriveAlbumActivity.this)));

        mAlbumView = (AlbumView) findViewById(R.id.album_view);
        mAlbumView.setEnableSpan(true)
                .setEnableGridMargin(true)
                .setSpanSize(2)
                .buildAlbumView();

        mAlbumView.setCallback(this);

        initGoogleAccountCredential();
    }

    private void initGoogleAccountCredential() {
        mGoogleDriveWrapper = new GoogleDriveWrapper(this);
        getResultsFromApi();
    }

    private void getResultsFromApi() {
        if (!mGoogleDriveWrapper.isGooglePlayServicesAvailable(this)) {
            mGoogleDriveWrapper.acquireGooglePlayServices(this, REQUEST_GOOGLE_PLAY_SERVICES);
        } else if (mGoogleDriveWrapper.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!mGoogleDriveWrapper.isDeviceOnline(this)) {
            Log.w(TAG, "No network connection available.");
        } else {
            new GoogleDriveAllFoldersTask(mGoogleDriveWrapper, this, "change wp")
                    .setQueryTrash(false)
                    .execute();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mGoogleDriveWrapper.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mGoogleDriveWrapper.getChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(this, "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Log.d(TAG,
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mGoogleDriveWrapper.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        if (DEBUG) {
            Log.d(TAG, "onItemClick, position: " + position);
        }
    }

    @Override
    public void onItemLongClick(int position) {
        if (DEBUG) {
            Log.d(TAG, "onItemLongClick, position: " + position);
        }
    }

    @Override
    public void onQueryError(Exception error) {
        if (DEBUG) {
            Log.v(TAG, "onQueryError");
        }
        if (error != null) {
            if (error instanceof GooglePlayServicesAvailabilityIOException) {
                mGoogleDriveWrapper.showGooglePlayServicesAvailabilityErrorDialog(
                        GoogleDriveAlbumActivity.this, REQUEST_GOOGLE_PLAY_SERVICES, ((GooglePlayServicesAvailabilityIOException) error)
                                .getConnectionStatusCode());
            } else if (error instanceof UserRecoverableAuthIOException) {
                startActivityForResult(
                        ((UserRecoverableAuthIOException) error).getIntent(),
                        GoogleDriveAlbumActivity.REQUEST_AUTHORIZATION);
            } else {
                Log.d(TAG, "The following error occurred:\n"
                        + error.getMessage());
            }
        } else {
            Log.d(TAG, "Request cancelled.");
        }
    }

    @Nullable
    @Override
    public void onQueryResult(FileList fileList) {
        if (DEBUG) {
            Log.v(TAG, "onQueryResult");
        }
        List<File> files = fileList.getFiles();
        if (files != null) {
            for (File file : files) {
                Log.d(TAG, "name: " + file.getName() + ", id: " + file.getId() + ", mime: " + file.getMimeType()
                        + ", thumbnail: " + file.getThumbnailLink());
            }
        }
    }
}
