package com.project.ischoolbus.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

/**
 * Created by Puttipong New on 14/1/2559.
 */

public class ForgotPwActivity extends Activity implements View.OnClickListener {
    EditText etEmail;
    Button btnResetPw;

    String email;
    AsyncHttpClient client;
    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);

        initInstance();
    }

    private void initInstance() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        btnResetPw = (Button) findViewById(R.id.btnOtpEmail);

        btnResetPw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnResetPw) {
            email = etEmail.getText().toString().trim().toLowerCase();

            if (!Validation.isFieldNotEmpty(email) || !Validation.isEmailValid(email)) {
                Snackbar.make(getCurrentFocus(), "An invalid email was entered", Snackbar.LENGTH_LONG).show();
            } else {
                client = new AsyncHttpClient();
                params = new RequestParams();
                params.put("email", email);

                EventManager eventManager = new EventManager();

                eventManager.postMethod(URL.FORGOTPASSWORD, params, new EventManager.PostListener() {
                    @Override
                    public void onInternetDown() {

                    }

                    @Override
                    public void onSuccess() {
                        Intent intent;
                        Toast.makeText(getApplicationContext(), "Good Job! Reset Code has sent successfully", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), OTPValidationActivity.class);
                        intent.putExtra("EMAIL", email);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(getApplicationContext(), "An invalid email was entered", Toast.LENGTH_SHORT).show();
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