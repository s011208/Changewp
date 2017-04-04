package bj4.yhh.googledrivehelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;

import java.util.Arrays;

/**
 * Created by s011208 on 2017/4/4.
 */

public class GoogleDriveWrapper {
    public static final String[] SCOPES = {DriveScopes.DRIVE};

    private GoogleAccountCredential mGoogleAccountCredential;

    public GoogleDriveWrapper(Context context) {
        mGoogleAccountCredential = GoogleAccountCredential.usingOAuth2(
                context.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    public GoogleAccountCredential getCredential() {
        return mGoogleAccountCredential;
    }

    public boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    public void acquireGooglePlayServices(Activity activity, final int requestCode) {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(activity);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(activity, requestCode, connectionStatusCode);
        }
    }

    public void showGooglePlayServicesAvailabilityErrorDialog(Activity activity, final int requestCode, final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                activity,
                connectionStatusCode,
                requestCode);
        dialog.show();
    }

    public boolean isDeviceOnline(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public Intent getChooseAccountIntent() {
        return mGoogleAccountCredential.newChooseAccountIntent();
    }

    public String getSelectedAccountName() {
        return mGoogleAccountCredential.getSelectedAccountName();
    }

    public void setSelectedAccountName(String accountName) {
        mGoogleAccountCredential.setSelectedAccountName(accountName);
    }
}
