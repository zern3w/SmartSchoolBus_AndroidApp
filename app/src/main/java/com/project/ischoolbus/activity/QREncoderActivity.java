package com.project.ischoolbus.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.project.ischoolbus.R;
import com.project.ischoolbus.tools.QRCodeEncoder;
import com.project.ischoolbus.tools.Tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class QREncoderActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgQr;
    Button btnSaveImg;
    String sId, nickname, school,txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrtest);

        initInstance();
    }

    private void initInstance() {
        imgQr = (ImageView) findViewById(R.id.imgQr);
        btnSaveImg = (Button) findViewById(R.id.btnSaveImg);

        btnSaveImg.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sId = bundle.getString("STUDENT_ID");
            nickname = bundle.getString("STUDENT_NICKNAME");
            school = bundle.getString("SCHOOL");
        }
        Log.wtf("Student_ID", sId);

        txt = "SchoolBusApp_" + sId + "_" + nickname + "_" + school;
        generateQr();
    }

    private void generateQr() {
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder();
        Bitmap myLogo = BitmapFactory.decodeResource(getResources(), R.drawable.qr_icon);
        try {
            Bitmap merge = Tools.mergeBitmaps(qrCodeEncoder.encodeAsBitmap(txt), myLogo);
            imgQr.setImageBitmap(merge);
//                imgQr.setImageBitmap(qrCodeEncoder.encodeAsBitmap(txt));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnSaveImg){
            Bitmap bitmap = ((BitmapDrawable)imgQr.getDrawable()).getBitmap();

            ContentResolver cr = getContentResolver();
            String title = nickname+sId;
            String description = "1234";
            String savedURL = MediaStore.Images.Media
                    .insertImage(cr, bitmap, title, description);

            Toast.makeText(QREncoderActivity.this,
                    savedURL,
                    Toast.LENGTH_LONG).show();
        }
    }
}
