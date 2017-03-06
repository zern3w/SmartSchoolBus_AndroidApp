package com.project.ischoolbus.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.ischoolbus.adapter.StudentAdapter;
import com.project.ischoolbus.R;
import com.project.ischoolbus.model.Students;
import com.project.ischoolbus.tools.URL;

import cz.msebera.android.httpclient.Header;

public class StudentListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    AsyncHttpClient client;
    Gson gson;
    RequestParams params;
    StudentAdapter studentAdapter;
    Students students;
    Intent intent = new Intent();
    String driverId;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String DRIVERId = "driver_id";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        driverId = sharedpreferences.getString(DRIVERId, "");
        Log.wtf("DriverID_ListView",driverId);

        getData();
    }

    private void getData() {
        params = new RequestParams();
        params.put("driver_id", driverId);
        client = new AsyncHttpClient();
        client.post(URL.LIST_STUDENT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    gson = new Gson();
                    students = gson.fromJson(new String(responseBody), Students.class);

                    studentAdapter = new StudentAdapter(students);
                    listView.setAdapter(studentAdapter);

                    if (studentAdapter.getCount()==0){
                        Toast.makeText(getApplicationContext(), "No student hasn't added yet!", Toast.LENGTH_SHORT).show();
                    }

//                    Log.v("RESULT", students.getStudent().get(0).getStudentNickname());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String sId = students.getStudent().get(position).getStudentId();
        Log.wtf("before",sId);
//        Toast.makeText(getApplicationContext(), "hello id: "+sId, Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), ProfileStudentActivity.class);
        intent.putExtra("STUDENT_ID", sId);
//
        startActivity(intent);
//        Log.wtf("after",sId);
    }
}
