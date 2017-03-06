package com.project.ischoolbus.tools;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.ischoolbus.model.Driver;
import com.project.ischoolbus.model.Drivers;
import com.project.ischoolbus.model.Parent;
import com.project.ischoolbus.model.Parents;
import com.project.ischoolbus.model.Student;
import com.project.ischoolbus.model.Students;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Puttipong New on 25/3/2559.
 */
public class EventManager {
    AsyncHttpClient client;
    Context context;
    Gson gson;

    public EventManager() {
        this.client = new AsyncHttpClient();
    }
    public EventManager(Context context) {
        this.client = new AsyncHttpClient();
        this.context = context;
    }



    public interface PostListener {
        void onInternetDown();

        void onSuccess();

        void onFailed();

        void onFailedError(String errorMsg);
    }

    public interface CreateAccListener {
        void onInternetDown();

        void onSuccess();

        void onFailed();

        void onFailedEmail();

        void onFailedPlateNo();

        void onFailedError(String errorMsg);
    }

    public interface AddParentListener {
        void onInternetDown();

        void onSuccess();

        void onFailed();

        void onFailedEmail();

        void onFailedError(String errorMsg);
    }

    public interface DriverProfileListener {
        void onInternetDown();

        void onSuccess(Driver driver);

        void onFailed();

        void onFailedError(String errorMsg);
    }

    public interface ParentProfileListener {
        void onInternetDown();

        void onSuccess(Parent parent);

        void onFailed();

        void onFailedError(String errorMsg);
    }

    public interface StudentProfileListener {
        void onInternetDown();

        void onSuccess(Student student);

        void onFailed();

        void onFailedError(String errorMsg);
    }

    public interface LogInListener {
        void onInternetDown();

        void onSuccess(String driver_id);

        void onFailed();

        void onFailedError(String errorMsg);
    }

    public interface CheckAtdListener {
        void onInternetDown();

        void onSuccess(String atdStatus, String tel);

        void onFailed();

        void onFailedError(String errorMsg);
    }

    public void postMethod(String url, RequestParams params, final PostListener listener) {
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody, Charset.forName("UTF-8")));
                    String status = jsonObject.getString("status");

                    Log.wtf("status", "hold");
                    if (status == "1") {
                        Log.wtf("status", "1");
                        listener.onSuccess();
                    } else {
                        Log.wtf("status", "0");
                        listener.onFailed();
                        Log.wtf("ERROR ON FAILED", "ERROR ON FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.wtf("ERROR!", e);
                    listener.onFailedError(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFailed();
            }
        });
    }

    public void createAcc(String url, RequestParams params, final CreateAccListener listener) {
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody, Charset.forName("UTF-8")));
                    String status = jsonObject.getString("status");

                    Log.wtf("status", "hold");
                    if (status == "1") {
                        listener.onSuccess();
                    } else if (status == "2") {
                        listener.onFailedEmail();
                    } else if (status == "3") {
                        listener.onFailedPlateNo();
                    } else listener.onFailed();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.wtf("ERROR!", e);
                    listener.onFailedError(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFailed();
            }
        });
    }

    public void getDriverProfile(RequestParams params, final DriverProfileListener listener) {
        client.post(URL.DRIVER_DETAIL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    StringBuilder result = new StringBuilder();
                    gson = new Gson();
                    Drivers drivers = gson.fromJson(new String(responseBody), Drivers.class);
                    List<Driver> user = drivers.getDriver();

                    listener.onSuccess(user.get(0));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFailed();
            }
        });

    }

    public void addParent(String url, RequestParams params, final AddParentListener listener) {
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody, Charset.forName("UTF-8")));
                    String status = jsonObject.getString("status");

                    Log.wtf("status", "hold");
                    if (status == "1") {
                        listener.onSuccess();
                    } else if (status == "2") {
                        listener.onFailedEmail();
                    } else listener.onFailed();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.wtf("ERROR!", e);
                    listener.onFailedError(e.getMessage());
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFailed();
            }
        });
    }

    public void getParentProfile(RequestParams params, final ParentProfileListener listener) {
        client.post(URL.PARENT_DETAIL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    StringBuilder result = new StringBuilder();
                    gson = new Gson();
                    Parents parents = gson.fromJson(new String(responseBody), Parents.class);
                    List<Parent> user = parents.getParent();
                    listener.onSuccess(user.get(0));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFailed();
            }
        });
    }

    public void getStudentProfile(RequestParams params, final StudentProfileListener listener) {
        client.post(URL.STUDENT_DETAIL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    StringBuilder result = new StringBuilder();
                    gson = new Gson();
                    Students students = gson.fromJson(new String(responseBody), Students.class);
                    List<Student> users = students.getStudent();
                    listener.onSuccess(users.get(0));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFailed();
            }
        });
    }

    public void logIn(String url, RequestParams params, final LogInListener listener) {
        if (Validation.isNetworkConnected(context)) {
            Log.wtf("isNetworkConnected!", "up");
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(responseBody, Charset.forName("UTF-8")));
                        String status = jsonObject.getString("status");
                        if (status == "1") {
                            String driver_id = jsonObject.getString("driver_id");
                            listener.onSuccess(driver_id);
                        } else {
                            listener.onFailed();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailedError(e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    listener.onFailed();
                }
            });
        }
        if (!Validation.isNetworkConnected(context)) {
            Log.wtf("isNetworkConnected!", "down");
            listener.onInternetDown();
            return;
        }
    }

    public void checkAtd(String url, RequestParams params, final CheckAtdListener listener) {
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody, Charset.forName("UTF-8")));
                    String status = jsonObject.getString("status");
                    String atdstatus = jsonObject.getString("atdstatus"); //ATTENDANCE STATUS
                    String tel = jsonObject.getString("tel");   // SENDING SMS TO THEIR PARENT

                    Log.wtf("status", "hold");
                    if (status == "1") {
                        listener.onSuccess(atdstatus, tel);
                    } else {
                        Log.wtf("status", "0");
                        listener.onFailed();
                        Log.wtf("ERROR ON FAILED", "ERROR ON FAILED");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.wtf("ERROR!", e);
                    listener.onFailedError(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                listener.onFailed();
            }
        });
    }
}

