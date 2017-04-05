package bj4.yhh.changewp.externalstorage;

import android.Manifest;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bj4.yhh.albumview.AlbumView;
import bj4.yhh.albumview.ImageData;
import bj4.yhh.changewp.R;
import bj4.yhh.changewp.utilities.Utility;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ExternalStorageAlbumActivity extends AppCompatActivity implements AlbumView.Callback {

    private static final String TAG = "ESAlbumActivity";
    private static final boolean DEBUG = true;

    private static final int REQUEST_PERMISSION_GET_READ_EXTERNAL_STORAGE = 10001;
    private static final int REQUEST_PERMISSION_ACTIVITY = 10002;

    private final Handler mHandler = new Handler();

    private TextView mRequestPermissionTextView;

    private AlbumView mAlbumView;
    private Map<String, List<ImageData>> mExternalStorageImageDataMap = new HashMap<>();

    private final ContentObserver mImageProviderObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (selfChange) return;
            updateAlbumViewDataList();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRequestPermissionTextView = (TextView) findViewById(R.id.permission_request_text);
        mRequestPermissionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_ACTIVITY);
            }
        });

        mAlbumView = (AlbumView) findViewById(R.id.album_view);
        mAlbumView.setEnableSpan(true)
                .setEnableGridMargin(true)
                .setSpanSize(2)
                .buildAlbumView();

        mAlbumView.setCallback(this);

        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, mImageProviderObserver);
        updateAlbumViewDataList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_ACTIVITY) {
            if (EasyPermissions.hasPermissions(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                updateAlbumViewDataList();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mImageProviderObserver);
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_READ_EXTERNAL_STORAGE)
    private void updateAlbumViewDataList() {
        Log.d(TAG, "updateAlbumViewDataList");
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            mRequestPermissionTextView.setVisibility(View.INVISIBLE);
            mAlbumView.setVisibility(View.VISIBLE);

            mExternalStorageImageDataMap.clear();
            mExternalStorageImageDataMap.putAll(Utility.groupImageDataByFolder(Utility.getAllExternalStorageImageData(ExternalStorageAlbumActivity.this)));
            mAlbumView.setImageDataList(Utility.getFirstImageDataFromGroup(mExternalStorageImageDataMap));
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.external_storage_activity_read_local_file_permission),
                    REQUEST_PERMISSION_GET_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
            mRequestPermissionTextView.setVisibility(View.VISIBLE);
            mAlbumView.setVisibility(View.INVISIBLE);
        }
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
        } else if (id == android.R.id.home) {
            finish();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
