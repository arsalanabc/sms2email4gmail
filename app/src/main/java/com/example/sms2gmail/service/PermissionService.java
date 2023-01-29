package com.example.sms2gmail.service;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.example.sms2gmail.R;

public class PermissionService {
    String[] permissions = new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};

    private static final int SMS_PERMISSION_CODE = 100;

    Activity activity;
    public PermissionService(Activity activity) {
        this.activity = activity;
    }

    public void onRequestPermissionsResult(int requestCode,
                                                  String[] permissions,
                                                  int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    this.activity.finish();
                }
                return;
            }

        }
    }
    /**
     * Request runtime SMS permission
     */
    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html
        }
        ActivityCompat.requestPermissions(activity, permissions, SMS_PERMISSION_CODE);
    }

    /**
     * Displays an AlertDialog explaining the user why the SMS permission is going to be requests
     *
     */
    public void showRequestPermissionsInfoAlertDialog() {
        showRequestPermissionsInfoAlertDialog(true);
    }

    public void showRequestPermissionsInfoAlertDialog(final boolean makeSystemRequest) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.permission_alert_dialog_title); // Your own title
        builder.setMessage(R.string.permission_dialog_message); // Your own message

        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Display system runtime permission request?
                if (makeSystemRequest) {
                    requestReadAndSendSmsPermission();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

}
