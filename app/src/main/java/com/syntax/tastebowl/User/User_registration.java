package com.syntax.tastebowl.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syntax.tastebowl.Common.Utility;
import com.syntax.tastebowl.Login;
import com.syntax.tastebowl.R;

import java.util.HashMap;
import java.util.Map;

public class User_registration extends AppCompatActivity {

    EditText name,address,phone,email,password;
    TextView login;
    Button btnreg;
    RadioGroup rggender;
    String NAME,GENDER,ADDRESS,PHONE,EMAIL,PASS;
    RadioButton rdtemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        name=findViewById(R.id.user_reg_name);
        address=findViewById(R.id.user_reg_address);
        phone=findViewById(R.id.user_reg_phone);
        email=findViewById(R.id.user_reg_email);
        password=findViewById(R.id.user_reg_password);
        btnreg=findViewById(R.id.user_reg_btnreg);
        login=findViewById(R.id.user_reg_login);
        rggender=findViewById(R.id.user_reg_rdgroup);


        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NAME=name.getText().toString();
                int id=rggender.getCheckedRadioButtonId();
                rdtemp=findViewById(id);
                GENDER=rdtemp.getText().toString();
                ADDRESS=address.getText().toString();
                PHONE=phone.getText().toString();
                EMAIL=email.getText().toString();
                PASS=password.getText().toString();



                if(NAME.isEmpty()){
                    name.requestFocus();
                    name.setError("Enter Name");
                }else if(GENDER.isEmpty()){
                    Toast.makeText(User_registration.this, "Select Gender", Toast.LENGTH_SHORT).show();
                }else if(ADDRESS.isEmpty()){
                    address.requestFocus();
                    address.setError("Enter Address");
                }else if(PHONE.isEmpty()){
                    phone.requestFocus();
                    phone.setError("Enter Phone");
                }if(EMAIL.isEmpty()){
                    email.requestFocus();
                    email.setError("Enter Email");
                } if(PASS.isEmpty()){
                    password.requestFocus();
                    password.setError("Enter Password");
                }else{
                    user_Registration();
                }
            }
        });

        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                return false;
            }
        });

    }




    public void user_Registration()
    {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******",response);
                if(!response.trim().equals("failed"))
                {
                    String id=response;
                    Toast.makeText(getApplicationContext(), "Registration Successfull", Toast.LENGTH_LONG).show();
                    Intent i =new Intent(User_registration.this,Login.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "ERROR REGISTRATION !", Toast.LENGTH_LONG).show();
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
                map.put("key","reg_user");
                map.put("name",NAME);
                map.put("gender",GENDER);
                map.put("address",ADDRESS);
                map.put("phone",PHONE);
                map.put("email",EMAIL);
                map.put("password",PASS);

                return map;
            }
        };
        queue.add(request);
    }



}
