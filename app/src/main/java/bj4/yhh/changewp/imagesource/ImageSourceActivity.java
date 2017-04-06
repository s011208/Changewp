package bj4.yhh.changewp.imagesource;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import bj4.yhh.changewp.BaseAppCompatActivity;
import bj4.yhh.changewp.R;
import bj4.yhh.changewp.externalstorage.ExternalStorageAlbumActivity;
import bj4.yhh.changewp.settings.main.MainPreferenceActivity;
import bj4.yhh.changewp.settings.main.WallpaperTimeInterval;
import bj4.yhh.changewp.utilities.PreferenceHelper;

public class ImageSourceActivity extends BaseAppCompatActivity implements ImageSourceAdapter.Callback {

    private static final String TAG = "ImageSourceActivity";
    private static final boolean DEBUG = true;

    private static final int REQUEST_EXTERNAL_STORAGE_ALBUM = 10000;

    private RecyclerView mSourceRecyclerView;
    private ImageSourceAdapter mImageSourceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.image_source_activity_title);

        mSourceRecyclerView = (RecyclerView) findViewById(R.id.source_recycler_view);
        mSourceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mImageSourceAdapter = new ImageSourceAdapter(this, this);
        mSourceRecyclerView.setAdapter(mImageSourceAdapter);
    }

    private void showIntervalDialogIfNecessary() {
        if (PreferenceHelper.getSharedPreference(this).getInt(PreferenceHelper.KEY_CHANGE_WALLPAPER_INTERVAL, -1) != -1) {
            return;
        }
        WallpaperTimeInterval.DialogFragmentImp dialogFragmentImp = new WallpaperTimeInterval.DialogFragmentImp();
        dialogFragmentImp.show(getFragmentManager(), WallpaperTimeInterval.DialogFragmentImp.class.getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        showIntervalDialogIfNecessary();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public String getTag() {
        return TAG;
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
            Intent preferenceIntent = new Intent(this, MainPreferenceActivity.class);
            startActivity(preferenceIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void imageSourceFrom(int from) {
        if (DEBUG) {
            Log.d(TAG, "imageSourceFrom: " + from);
        }
        if (ImageSourceAdapter.SOURCE_FROM_EXTERNAL_STORAGE == from) {
            Intent startIntent = new Intent(this, ExternalStorageAlbumActivity.class);
            startActivityForResult(startIntent, REQUEST_EXTERNAL_STORAGE_ALBUM);
        } else if (ImageSourceAdapter.SOURCE_FROM_GOOGLE_DRIVE == from) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXTERNAL_STORAGE_ALBUM) {
            if (resultCode == RESULT_OK) {
                // TODO: 2017/4/6 handle result
            }
        }
    }
}
