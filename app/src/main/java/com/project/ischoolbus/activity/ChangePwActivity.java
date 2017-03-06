package com.project.ischoolbus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.project.ischoolbus.R;
import com.project.ischoolbus.tools.EventManager;
import com.project.ischoolbus.tools.URL;
import com.project.ischoolbus.tools.Validation;

public class ChangePwActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnConfirm;
    EditText etEmail, etNewPw, etReNewPw;

    String newPassword, reNewPassword, email;
    AsyncHttpClient client;
    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        initInstance();
    }

    private void initInstance() {
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etNewPw = (EditText) findViewById(R.id.etNewPw);
        etReNewPw = (EditText) findViewById(R.id.etReNewPw);

        btnConfirm.setOnClickListener(this);

        email = getIntent().getExtras().getString("EMAIL");
        etEmail.setText(email);
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            newPassword = etNewPw.getText().toString();
            reNewPassword = etReNewPw.getText().toString();

            if (!Validation.isFieldNotEmpty(newPassword) || !Validation.isMinimumLength(newPassword, 8)) {
                Snackbar.make(getCurrentFocus(), "An invalid password was entered", Snackbar.LENGTH_LONG).show();
            }else if (!Validation.isStringEqual(newPassword, reNewPassword)) {
                    etNewPw.setError("Password is not match");
                    etReNewPw.setError("Password is not match");
                } else if (!Validation.isMinimumLength(newPassword, 8)) {
                etNewPw.setError("be at least 8 characters");
            }else {
                client = new AsyncHttpClient();
                params = new RequestParams();

                email = getIntent().getExtras().getString("EMAIL");

                params.put("email", email);
                params.put("newPassword", newPassword);

                Log.wtf("EMAIL : ", email);
                Log.wtf("newPassword : ", newPassword);

                EventManager eventManager = new EventManager();

                eventManager.postMethod(URL.CHANGEPASSWORD, params, new EventManager.PostListener() {
                    @Override
                    public void onInternetDown() {

                    }

                    @Override
                    public void onSuccess() {
                        Intent intent;
                        Toast.makeText(getApplicationContext(), "Your Password has changed", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(getApplicationContext(), "Failed! ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailedError(String errorMsg) {
                        Toast.makeText(getApplicationContext(), "Failed!" +
                                errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}