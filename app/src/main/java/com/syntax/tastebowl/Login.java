package com.syntax.tastebowl;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syntax.tastebowl.Admin.AdminHome;
import com.syntax.tastebowl.Common.Utility;
import com.syntax.tastebowl.User.UserHome;
import com.syntax.tastebowl.User.User_registration;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button btnlog;
    TextView reg;
    String UNAME, PASS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Toolbar toolbar = new Toolbar(this);
        toolbar.setVisibility(View.INVISIBLE);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,

        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        btnlog = findViewById(R.id.login_btnlog);
        reg = findViewById(R.id.login_signup);

        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UNAME = username.getText().toString();
                PASS = password.getText().toString();
                if (UNAME.isEmpty()) {
                    username.requestFocus();
                    username.setError("enter username");
                } else if (PASS.isEmpty()) {
                    password.requestFocus();
                    password.setError("enter password");
                } else {
                    login();
                }
            }
        });

        reg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(getApplicationContext(), User_registration.class));
                return false;
            }
        });

    }


    public void login() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
                if (!response.trim().equals("failed")) {

                    String data = response;
                    String arr[] = data.trim().split(":");

                    SharedPreferences.Editor editor = getSharedPreferences("SharedData", MODE_PRIVATE).edit();
                    editor.putString("logid", arr[0]);
                    editor.putString("type", arr[1]);
                    editor.commit();


                    if (arr[1].equals("admin")) {
                        startActivity(new Intent(Login.this, AdminHome.class));
                    } else if (arr[1].equals("user")) {
                        startActivity(new Intent(Login.this, UserHome.class));
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Login failed..!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "my error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "login");
                map.put("username", UNAME);
                map.put("password", PASS);


                return map;
            }
        };
        queue.add(request);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
