package com.project.ischoolbus.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.project.ischoolbus.R;
import com.project.ischoolbus.model.Parent;
import com.project.ischoolbus.tools.EventManager;
import com.project.ischoolbus.tools.Tools;
import com.project.ischoolbus.tools.URL;

public class ProfileParentActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnDelete, btnAddStudent;
    Intent intent = new Intent();
    TextView tvProfile;
    ImageView imgPhoto;
    AsyncHttpClient client;

    String pId = "123";
    String txt = "";
    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_parent);

        initInstance();

        params = new RequestParams();
        client = new AsyncHttpClient();
    }

    private void initInstance() {
        tvProfile = (TextView) findViewById(R.id.tvName);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        btnDelete.setOnClickListener(this);
        btnAddStudent = (Button) findViewById(R.id.btnAddStudent);
        btnAddStudent.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pId = bundle.getString("PARENT_ID");
        }
        Log.wtf("Parent_ID", pId);
        showData(pId);
    }

    private void showData(String parent_id) {
        params = new RequestParams();
        params.put("parent_id", parent_id);

        EventManager eventManager = new EventManager();
        eventManager.getParentProfile(params, new EventManager.ParentProfileListener() {

            @Override
            public void onInternetDown() {

            }

            @Override
            public void onSuccess(Parent parent) {
                txt = "ID: "+parent.getParentId()+
                        "\nName: "+parent.getParentFirstname()+" "+parent.getParentLastname()
                +"\nMobile phone: "+parent.getMobileTel();
                tvProfile.setText(txt);
                Log.wtf("photo", parent.getPhoto());

                Bitmap photo = Tools.decodeBase64(parent.getPhoto());
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



    @Override
    public void onClick(View v) {
        if (v == btnDelete) {
//            Toast.makeText(getApplicationContext(), pId, Toast.LENGTH_SHORT).show();
            params.put("parent_id", pId);
            EventManager eventManager = new EventManager();

            eventManager.postMethod(URL.DELETE_PARENT, params, new EventManager.PostListener() {
                @Override
                public void onInternetDown() {

                }

                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Deleted Successful", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailed() {
                    Toast.makeText(getApplicationContext(), "Delete Failed."
                            , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailedError(String errorMsg) {
                    Toast.makeText(getApplicationContext(), "Delete Failed."
                            , Toast.LENGTH_SHORT).show();
                }
            });
        } else if (v == btnAddStudent) {
            intent = new Intent(getApplicationContext(), AddStudentActivity.class);
            intent.putExtra("PARENT_ID", pId);
            startActivity(intent);
        }
    }
}
