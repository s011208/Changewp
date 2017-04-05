package bj4.yhh.changewp.imagesource;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import bj4.yhh.changewp.R;
import bj4.yhh.changewp.settings.main.MainPreferenceActivity;
import bj4.yhh.changewp.settings.main.WallpaperTimeInterval;
import bj4.yhh.changewp.utilities.PreferenceHelper;

public class ImageSourceActivity extends AppCompatActivity implements ImageSourceAdapter.Callback {

    private static final String TAG = "ImageSourceActivity";
    private static final boolean DEBUG = true;

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
        } else if (ImageSourceAdapter.SOURCE_FROM_GOOGLE_DRIVE == from) {
        }
    }
}
