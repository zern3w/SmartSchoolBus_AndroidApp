package com.project.ischoolbus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

public class OTPValidationActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnValidate;
    EditText etOtp;

    String otp, email;
    AsyncHttpClient client;
    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpvalidation);

        initInstance();
    }

    private void initInstance() {
        btnValidate = (Button) findViewById(R.id.btnValidate);
        etOtp = (EditText) findViewById(R.id.etOtp);

        btnValidate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnValidate){
            otp = etOtp.getText().toString();

            if (!Validation.isFieldNotEmpty(otp) || !Validation.isMinimumLength(otp, 8)) {
                Snackbar.make(getCurrentFocus(), "An invalid OTP was entered", Snackbar.LENGTH_LONG).show();
            } else {
                client = new AsyncHttpClient();
                params = new RequestParams();

                email = getIntent().getExtras().getString("EMAIL");
                params.put("email", email);
                params.put("oneTimePw", otp);

//                Log.wtf("EMAIL : ", email);
//                Log.wtf("OTP : ", otp);

                EventManager eventManager = new EventManager();

                eventManager.postMethod(URL.VALIDATE_OTP, params, new EventManager.PostListener() {
                    @Override
                    public void onInternetDown() {

                    }

                    @Override
                    public void onSuccess() {
                        Intent intent;
                        Toast.makeText(getApplicationContext(), "Nice! Let's changing the password", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), ChangePwActivity.class);
                        intent.putExtra("EMAIL", email);
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