package com.project.ischoolbus.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.project.ischoolbus.R;
import com.project.ischoolbus.model.Driver;
import com.project.ischoolbus.tools.EventManager;
import com.project.ischoolbus.tools.Tools;
import com.project.ischoolbus.tools.URL;
import com.project.ischoolbus.tools.Validation;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etFirstName, etLastName, etEmail, etPw, etPhoneNum, etPlateNum;
    Button btnConfirm, btnChangePhoto;
    ImageView imgPhoto;
    Intent intent;
    String driverId;
    RequestParams params;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String DRIVERId = "driver_id";
    SharedPreferences sharedpreferences;
    AsyncHttpClient client;

    String stringImg = "";
    private Bitmap bitmap;
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        client = new AsyncHttpClient();
        params = new RequestParams();
        initInstance();
        showData();
    }

    private void showData() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        driverId = sharedpreferences.getString(DRIVERId, "0");
        Log.wtf("DriverID", driverId);

        params = new RequestParams();
        params.put("driver_id", driverId);

        EventManager eventManager = new EventManager();
        eventManager.getDriverProfile(params, new EventManager.DriverProfileListener() {

            @Override
            public void onInternetDown() {

            }

            @Override
            public void onSuccess(Driver driver) {
                Log.wtf("photo", driver.getPhoto());
                etFirstName.setText(driver.getDriver_firstname());
                etLastName.setText(driver.getDriver_lastname());
                etPhoneNum.setText(driver.getMobile_tel());
                etPw.setText(driver.getPassword());

                Bitmap photo = Tools.decodeBase64(driver.getPhoto());
                stringImg = driver.getPhoto();
                imgPhoto.setImageBitmap(photo);
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onFailedError(String errorMsg) {

            }
        });
    }

    private void initInstance() {
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etPw = (EditText) findViewById(R.id.etPw);
        etPhoneNum = (EditText) findViewById(R.id.etPhoneNum);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnChangePhoto = (Button) findViewById(R.id.btnChangePhoto);

        btnChangePhoto.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            final String firstName = etFirstName.getText().toString();
            final String lastName = etLastName.getText().toString();
            final String password = etPw.getText().toString();
            final String phoneNum = etPhoneNum.getText().toString();

            if (!Validation.isFieldNotEmpty(firstName) || !Validation.isFieldNotEmpty(lastName)
                    || !Validation.isFieldNotEmpty(password) || !Validation.isFieldNotEmpty(phoneNum))
            { Snackbar.make(getCurrentFocus(), "Every field is required", Snackbar.LENGTH_SHORT).show();
            } else if (!Validation.isMinimumLength(password, 8)) {
                etPw.setError("be at least 8 characters");
            }else {


            params = new RequestParams();
            params.put("driver_id", driverId);
            params.put("firstName", firstName);
            params.put("lastName", lastName);
            params.put("password", password);
            params.put("phoneNum", phoneNum);
            params.put("photo", stringImg);

            EventManager eventManager = new EventManager();

            eventManager.postMethod(URL.EDITPROFILE, params, new EventManager.PostListener() {
                @Override
                public void onInternetDown() {

                }

                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Edit Profile Successfully", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailed() {
                    Toast.makeText(getApplicationContext(), "Edit Profile Failed."
                            , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailedError(String errorMsg) {
                    Toast.makeText(getApplicationContext(), "Edit Profile Failed."
                            , Toast.LENGTH_SHORT).show();
                }
            });
        }} else if (v == btnChangePhoto) {
            showImage();
        }
    }

    private void showImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imgPhoto.setImageBitmap(bitmap);
                stringImg = Tools.getStringImage(bitmap);
                imgPhoto.setImageBitmap(Tools.decodeBase64(stringImg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void logOut() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Log.d("LogOut", "Now log out and start the activity login");
        Intent intent = new Intent(EditProfileActivity.this,
                LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        Toast.makeText(getApplicationContext(), "You have successfully logged out!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logOut();
                return true;
            default:
                return false;
        }
    }
}
