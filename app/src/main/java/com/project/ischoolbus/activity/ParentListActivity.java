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
import com.project.ischoolbus.adapter.ParentAdapter;
import com.project.ischoolbus.R;
import com.project.ischoolbus.model.Parents;
import com.project.ischoolbus.tools.URL;

import cz.msebera.android.httpclient.Header;

public class ParentListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    AsyncHttpClient client;
    Gson gson;
    RequestParams params;
    ParentAdapter parentAdapter;
    Parents parents;
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
        client.post(URL.LIST_PARENT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    gson = new Gson();
                    parents = gson.fromJson(new String(responseBody), Parents.class);

                    parentAdapter = new ParentAdapter(parents);
                    listView.setAdapter(parentAdapter);

                    if (parentAdapter.getCount()==0){
                        Toast.makeText(getApplicationContext(), "No parent hasn't added yet!", Toast.LENGTH_SHORT).show();
                    }
//                    Log.v("RESULT", parents.getParent().get(0).getParentFirstname());
//                    Log.v("RESULT", parents.getParent().get(0).getParentLastname());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String pId = parents.getParent().get(position).getParentId();
//        Toast.makeText(getApplicationContext(), "hello id: "+pId, Toast.LENGTH_SHORT).show();
        intent = new Intent(getApplicationContext(), ProfileParentActivity.class);
        intent.putExtra("PARENT_ID",pId);
        Log.wtf("before",pId);
        startActivity(intent);
        Log.wtf("after",pId);
    }

}
