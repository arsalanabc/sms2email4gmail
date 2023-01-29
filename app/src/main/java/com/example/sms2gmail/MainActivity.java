package com.example.sms2gmail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.sms2gmail.service.PermissionService;

public class MainActivity extends AppCompatActivity {

    PermissionService permissionService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.permissionService = new PermissionService(this);
        if(!isSmsPermissionGranted()) permissionService.showRequestPermissionsInfoAlertDialog();

    }

    private boolean isSmsPermissionGranted() {
        return ContextCompat
                .checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat
                        .checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.permissionService.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }}