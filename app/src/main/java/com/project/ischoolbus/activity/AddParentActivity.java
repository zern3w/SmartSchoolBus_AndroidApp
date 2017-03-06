package com.project.ischoolbus.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.project.ischoolbus.R;
import com.project.ischoolbus.tools.EventManager;
import com.project.ischoolbus.tools.Tools;
import com.project.ischoolbus.tools.URL;
import com.project.ischoolbus.tools.Validation;

import java.io.IOException;

public class AddParentActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    EditText etFirstName, etLastName, etEmail, etReEmail, etPhoneNum;
    Button btnSignUp, btnUploadPhoto;
    RadioButton rdbMale, rdbFemale;
    RadioGroup rgGender;
    ImageView imgPhoto;
    String gender = "male";
    Intent intent;

    String stringImg = "";
    private Bitmap bitmap;
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;

    String driverId;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String DRIVERId = "driver_id";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parent);

        initInstance();
    }

    private void initInstance() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        driverId = sharedpreferences.getString(DRIVERId, "");

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etReEmail = (EditText) findViewById(R.id.etReEmail);
        etPhoneNum = (EditText) findViewById(R.id.etPhoneNum);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rdbMale = (RadioButton) findViewById(R.id.rdbMale);
        rdbFemale = (RadioButton) findViewById(R.id.rdbMale);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnUploadPhoto = (Button) findViewById(R.id.btnUploadPhoto);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);

        rgGender.setOnCheckedChangeListener(this);
        btnSignUp.setOnClickListener(this);
        btnUploadPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSignUp) {
            final String firstName = etFirstName.getText().toString();
            final String lastName = etLastName.getText().toString();
            final String email = etEmail.getText().toString().trim().toLowerCase();
            final String reEmail = etReEmail.getText().toString().trim().toLowerCase();
            final String phoneNum = etPhoneNum.getText().toString();

            if (!Validation.isFieldNotEmpty(firstName) || !Validation.isFieldNotEmpty(lastName)
                    || !Validation.isFieldNotEmpty(email) || !Validation.isFieldNotEmpty(phoneNum))
                Snackbar.make(getCurrentFocus(), "Every field is required", Snackbar.LENGTH_SHORT).show();
            else if (!Validation.isStringEqual(email, reEmail)) {
                etEmail.setError("Email is not match");
                etReEmail.setError("Email is not match");
            } else if (!Validation.isFieldNotEmpty(stringImg))
                btnUploadPhoto.setError("Upload the photo!");
            else {
                RequestParams params = new RequestParams();
                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("email", email);
                params.put("phoneNum", phoneNum);
                params.put("gender", gender);
                params.put("photo", stringImg);
                params.put("driver_id", driverId);

                EventManager eventManager = new EventManager();

                eventManager.addParent(URL.ADD_PARENT, params, new EventManager.AddParentListener() {
                    @Override
                    public void onInternetDown() {
//                        Toast.makeText(getApplicationContext(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Adding parent successfully", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(getCurrentFocus(), "Signup Successfully", Snackbar.LENGTH_SHORT).show();
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(getApplicationContext(), "Adding parent failed."
                                , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailedEmail() {
                        Toast.makeText(getApplicationContext(), "The email already exist in the system."
                                , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailedError(String errorMsg) {
                        Toast.makeText(getApplicationContext(), "Signup Failed." +
                                errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else if (v == btnUploadPhoto) {
            showImage();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rdbMale:
                gender = "male";
                break;
            case R.id.rdbFemale:
                gender = "female";
                break;
            default:
                break;
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
                imgPhoto.setImageBitmap(bitmap);
                stringImg = Tools.getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}