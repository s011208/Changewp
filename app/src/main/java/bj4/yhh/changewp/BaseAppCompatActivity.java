package bj4.yhh.changewp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by s011208 on 2017/4/5.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrash.log("[" + getTag() + "] onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseCrash.log("[" + getTag() + "] onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseCrash.log("[" + getTag() + "] onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseCrash.log("[" + getTag() + "] onDestroy");
    }

    public abstract String getTag();
}
