package com.project.ischoolbus.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.project.ischoolbus.R;
import com.project.ischoolbus.tools.EventManager;
import com.project.ischoolbus.tools.URL;
import com.project.ischoolbus.tools.Validation;
import com.vstechlab.easyfonts.EasyFonts;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    EditText etEmail, etPw;
//    TextView tvForgotPw, tvCreateAcc;
    TextView tvAppName;
    String email, password;
    AsyncHttpClient client;
    RequestParams params;
    Intent intent;
    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String DRIVERId = "driver_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstance();
    }

    @Override
    public void onClick(View v) {
        email = etEmail.getText().toString().trim().toLowerCase();
        password = etPw.getText().toString();

        if (v == btnLogin) {
            if (!Validation.isFieldNotEmpty(email) || !Validation.isFieldNotEmpty(password)
                    || !Validation.isEmailValid(email) || !Validation.isMinimumLength(password, 6)) {
                Snackbar.make(getCurrentFocus(), "An invalid email or password was entered", Snackbar.LENGTH_LONG).show();
            } else {
                client = new AsyncHttpClient();
                params = new RequestParams();
                params.put("email", email);
                params.put("password", password);

                EventManager eventManager = new EventManager(getApplicationContext());

                eventManager.logIn(URL.LOGIN, params, new EventManager.LogInListener() {

                    @Override
                    public void onInternetDown() {
                        Toast.makeText(getApplicationContext(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String driver_id) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(DRIVERId, driver_id);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Awesome!:) Login successfully", Toast.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed() {
                        Snackbar.make(getCurrentFocus(), "An invalid email or password was entered <DB>", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailedError(String errorMsg) {
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
//        else if (v == tvForgotPw) {
//            intent = new Intent(getApplicationContext(), ForgotPwActivity.class);
//            startActivity(intent);
//        } else if (v == tvCreateAcc) {
//            intent = new Intent(getApplicationContext(), CreateAccActivity.class);
//            startActivity(intent);
//        }
    }

    private void initInstance() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPw = (EditText) findViewById(R.id.etPw);
        btnLogin = (Button) findViewById(R.id.btnLogin);
//        tvForgotPw = (TextView) findViewById(R.id.tvForgotPw);
//        tvCreateAcc = (TextView) findViewById(R.id.tvCreateAcc);
        tvAppName = (TextView) findViewById(R.id.tvAppName);
        btnLogin.setOnClickListener(this);
//        tvForgotPw.setOnClickListener(this);
//        tvCreateAcc.setOnClickListener(this);

        tvAppName.setTypeface(EasyFonts.tangerineBold(this));
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

}