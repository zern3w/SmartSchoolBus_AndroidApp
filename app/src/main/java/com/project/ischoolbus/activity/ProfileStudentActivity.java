package com.project.ischoolbus.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.project.ischoolbus.R;
import com.project.ischoolbus.model.Student;
import com.project.ischoolbus.tools.EventManager;
import com.project.ischoolbus.tools.Tools;
import com.project.ischoolbus.tools.URL;
import com.squareup.picasso.Picasso;

public class ProfileStudentActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton ibCall, ibParentCall, ibSosCall;
    Intent intent = new Intent();
    TextView tvProfile, tvNickName;
    ImageView imgPhoto;
    AsyncHttpClient client;

    String sId = "123";
    String txt, school, nickname, studentTel, parentTel, sosTel;
    RequestParams params;
    Intent callIntent = new Intent(Intent.ACTION_CALL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);

        initInstance();

        params = new RequestParams();
        client = new AsyncHttpClient();
    }

    private void initInstance() {
        tvNickName = (TextView) findViewById(R.id.tvNickName);
        tvProfile = (TextView) findViewById(R.id.tvProfile);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        ibCall = (ImageButton) findViewById(R.id.ibCall);
        ibParentCall = (ImageButton) findViewById(R.id.ibParentCall);
        ibSosCall = (ImageButton) findViewById(R.id.ibSosCall);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sId = bundle.getString("STUDENT_ID");
        }
        Log.wtf("Student_ID", sId);

        showData(sId);
    }

    private void showData(String student_id) {
        params = new RequestParams();
        params.put("student_id", student_id);

        EventManager eventManager = new EventManager();
        eventManager.getStudentProfile(params, new EventManager.StudentProfileListener() {

            @Override
            public void onInternetDown() {

            }

            @Override
            public void onSuccess(Student student) {
                txt =   "Name: "+student.getStudentFirstname()+" "+student.getStudentLastname()
                        +"\nParent name: "+student.getParentFirstname()+" "+student.getParentLastname()
                        +"\nSchool name: "+student.getSchoolName();
                tvProfile.setText(txt);
                tvNickName.setText(student.getStudentNickname());

                studentTel = student.getMobileTel();
                parentTel = student.getParentPhone();
                sosTel = student.getEmergencyTel();

                Picasso.with(getApplicationContext()).load(R.drawable.sid_1).into(imgPhoto);
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onFailedError(String errorMsg) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == ibCall) {
//            Toast.makeText(getApplicationContext(), "Call Student", Toast.LENGTH_SHORT).show();
            intentCall(studentTel);

        }
        else if (v == ibParentCall){
//            Toast.makeText(getApplicationContext(), "Call Parent", Toast.LENGTH_SHORT).show();
            intentCall(parentTel);
        }
        else if (v == ibSosCall){
//            Toast.makeText(getApplicationContext(), "Call SOS", Toast.LENGTH_SHORT).show();
            intentCall(sosTel);
        }
    }

    public void intentCall(String tel){
        callIntent.setData(Uri.parse("tel:"+tel));
        if (ActivityCompat.checkSelfPermission(ProfileStudentActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
}
