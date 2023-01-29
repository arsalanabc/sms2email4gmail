package com.example.sms2gmail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sms2gmail.service.PermissionService;
import com.example.sms2gmail.service.PreferenceRepository;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    PermissionService permissionService ;
    PreferenceRepository preferenceRepository;

    TextView sendingEmailView;
    TextView passwordView;
    TextView forwardEmailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceRepository = new PreferenceRepository(this);

        sendingEmailView = findViewById(R.id.sender_email);
        passwordView = findViewById(R.id.password);
        forwardEmailView = findViewById(R.id.forward_email);

        sendingEmailView.setText( preferenceRepository.getSettings(getString(R.string.sending_gmail)));
        passwordView.setText( preferenceRepository.getSettings(getString(R.string.gmail_app_pass)));
        forwardEmailView.setText(preferenceRepository.getSettings(getString(R.string.forwarding_email)));

        Button button = findViewById(R.id.save);
        button.setOnClickListener(view -> saveButtonListener(view));

        this.permissionService = new PermissionService(this);
        if(!isSmsPermissionGranted()) permissionService.showRequestPermissionsInfoAlertDialog();

    }

    private Map<String, String> updateValues() {
        Map<String, String> values = new HashMap<>();

        String sendingEmail = this.sendingEmailView.getText().toString();
        if (!sendingEmail.isEmpty()) {
            values.put(getString(R.string.sending_gmail), sendingEmail);
        }

        String password = this.passwordView.getText().toString();
        if (!password.isEmpty()) {
            values.put(getString(R.string.gmail_app_pass), password);
        }

        String forwardEmail = this.forwardEmailView.getText().toString();
        if (!forwardEmail.isEmpty()) {
            values.put(getString(R.string.forwarding_email), forwardEmail);
        }

        return values;
    }
    private  void saveButtonListener (View view) {
        String error = "";

        if(!isEmailValid(sendingEmailView.getText().toString())){error = getString(R.string.invalid_email);}
        else if(!isEmailValid(forwardEmailView.getText().toString())){error = getString(R.string.invalid_email);}
        else if(!isPasswordValid(passwordView.getText().toString())){error = getString(R.string.invalid_password);}

        if(!error.isEmpty()){
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (true) return;
        final Map<String, String> values = updateValues();
        String toastMessage;

        if(values.size() == 3) {
            preferenceRepository.saveSettings(values);
            toastMessage = "Saved!";
        } else {
            toastMessage = "Failed to save: Missing fields!";
        }

        Toast.makeText(this,
                toastMessage,
                Toast.LENGTH_LONG).show();
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
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        return email.contains("@") && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() == 16;
    }
}
