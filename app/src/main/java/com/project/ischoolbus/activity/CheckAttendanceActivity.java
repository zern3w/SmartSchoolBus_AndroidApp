package com.project.ischoolbus.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.project.ischoolbus.R;
import com.project.ischoolbus.tools.EventManager;
import com.project.ischoolbus.tools.RoundImage;
import com.project.ischoolbus.tools.Tools;
import com.project.ischoolbus.tools.URL;
import com.project.ischoolbus.tools.Validation;

public class CheckAttendanceActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnScan, btnConfirm;
    TextView tvQrDetail, tvNickname;
    ImageView imgPhoto;
    RoundImage roundImage;
    String contents, id, nickName, status, schoolName, txt, uri;
    String atdStat, msg, txtAtd = "";
    Intent intent;
    String driverId;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String DRIVERId = "driver_id";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkattendance);

        initInstances();
        intentScanner();
    }


    private void initInstances() {
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        tvQrDetail = (TextView) findViewById(R.id.tvQrDetail);
        tvNickname = (TextView) findViewById(R.id.tvNickName);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        driverId = sharedpreferences.getString(DRIVERId, "");

        btnConfirm.setOnClickListener(this);
    }

    private void intentScanner() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = data.getStringExtra("SCAN_RESULT");

                if (Validation.isQrValid(contents)) {
                    String[] qrCode = contents.split("_");
                    status = "valid";
                    id = qrCode[1];
                    nickName = qrCode[2];
//                    schoolName = qrCode[3];
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sid_1);
                    roundImage = new RoundImage(bitmap);
                    imgPhoto.setImageDrawable(roundImage);
                    tvNickname.setText(nickName);
                } else {
                    status = "****";
//                    tvNickname.setText(status);
//                    txt = "Status: An invalid QR code was scanned";
                    Toast.makeText(getApplicationContext(), "An invalid QR was scanned, please try again", Toast.LENGTH_SHORT).show();
                    intentScanner();
                }
//                tvQrDetail.setText(schoolName);

            }
            if (resultCode == RESULT_CANCELED) {//handle cancel
                tvQrDetail.setText(null);
                btnConfirm.setSelected(false);
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
      if (v == btnConfirm) {
            if (status == "****") {
                Toast.makeText(getApplicationContext(), "An invalid QR was scanned, please try again", Toast.LENGTH_SHORT).show();
                intentScanner();
            } else {
                RequestParams params = new RequestParams();
                params.put("student_id", id);
                params.put("driver_id", driverId);
                EventManager eventManager = new EventManager();
                eventManager.checkAtd(URL.STUDENT_CHKATTENDANCE, params, new EventManager.CheckAtdListener() {

                    @Override
                    public void onInternetDown() {

                    }

                    @Override
                    public void onSuccess(String atdStatus, String tel) {
                        atdStat = atdStatus;
                        if (status == "valid") {
                            if (atdStat == "1") {
                                txtAtd = "\n<GET ON THE BUS>";
                                msg = nickName + " GOT ON the school bus already. SmartSchoolBus Application:)";
                            } else if (atdStat == "0") {
                                txtAtd = "\n<GET OFF THE BUS>";
                                msg = nickName + " GOT OFF the school bus already. SmartSchoolBus Application:)";
                            }
                            Tools.sendSMSMessage(getApplicationContext(), tel, msg);
                            Toast.makeText(getApplicationContext(), "          Well done!" + txtAtd, Toast.LENGTH_LONG).show();
                            intentScanner();
                        }
                    }

                    @Override
                    public void onFailed() {
                        Toast.makeText(getApplicationContext(), "Got some problem!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailedError(String errorMsg) {

                    }
                });
            }
        }
    }
}
