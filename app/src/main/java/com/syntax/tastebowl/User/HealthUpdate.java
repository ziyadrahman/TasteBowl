package com.syntax.tastebowl.User;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syntax.tastebowl.Common.MyBmi;
import com.syntax.tastebowl.Common.Mybmr;
import com.syntax.tastebowl.Common.Utility;
import com.syntax.tastebowl.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthUpdate extends Fragment {

    String SUGER,PRESURE,HEIGHT,WEIGHT,AGE,BMI,BMR;
    EditText suger,presure,height,weight,bmr,bmi,age;
    Button update;
    Float bweight,bheight,bage;
    String genderdata;

    public HealthUpdate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_helth_update, container, false);

        volly_call_getGender();
        suger=(EditText)view.findViewById(R.id.txt_suger);
        presure=(EditText)view.findViewById(R.id.txt_pressure);
        height=(EditText)view.findViewById(R.id.txt_height);
        weight=(EditText)view.findViewById(R.id.txt_weight);
        bmr=(EditText)view.findViewById(R.id.txt_bmr);
        bmi=(EditText)view.findViewById(R.id.txt_bmi);
        age=(EditText)view.findViewById(R.id.txt_age);
        update=(Button)view.findViewById(R.id.btn_update_helth);

        bmr.setEnabled(false);
        bmi.setEnabled(false);

        weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b)
                {
                    bweight=Float.parseFloat(weight.getText().toString().trim());
                    bheight=Float.parseFloat(height.getText().toString().trim());
                    bage=Float.parseFloat(age.getText().toString().trim());

                    MyBmi myBmi=new MyBmi(bweight, bheight);
                    float fresultbmi=myBmi.calculateBmi();
                    String resultbmi=String.valueOf(fresultbmi);
                    bmi.setText(resultbmi);

                    Mybmr mybmr=new Mybmr(bweight, bheight, bage,genderdata.trim());
                    String resultbmr=mybmr.calculateBmr();
                    bmr.setText(resultbmr);

                    //Toast.makeText(health_update.this, "BMI="+result, Toast.LENGTH_SHORT).show();
                }
            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SUGER=suger.getText().toString().trim();
                PRESURE=presure.getText().toString().trim();
                WEIGHT=weight.getText().toString().trim();
                HEIGHT=height.getText().toString().trim();
                AGE=age.getText().toString().trim();
                BMI=bmr.getText().toString().trim();
                BMR=bmi.getText().toString().trim();

                if (AGE.isEmpty()) {
                    age.requestFocus();
                    age.setError("ENTER VALID NUMBER)");
                } else if (HEIGHT.isEmpty()) {
                    height.requestFocus();
                    height.setError("ENTER VALID NUMBER)");
                } else if (WEIGHT.isEmpty()) {
                    weight.requestFocus();
                    weight.setError("ENTER VALID NUMBER)");
                } else if (SUGER.isEmpty()) {
                    suger.requestFocus();
                    suger.setError("ENTER VALID NUMBER)");
                } else if (PRESURE.isEmpty()) {
                    presure.requestFocus();
                    presure.setError("ENTER VALID NUMBER)");
                } else if (BMI.isEmpty()) {
                    bmi.requestFocus();
                    bmi.setError("ENTER VALID NUMBER)");
                }  else if (BMR.isEmpty()) {
                    bmr.requestFocus();
                    bmr.setError("ENTER VALID NUMBER)");
                } else {
                    volly_call_helth_update();

                }


            }
        });




        return  view;
    }



    public void volly_call_getGender()
    {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******",response);
                if(!response.trim().equals("failed"))
                {
                    genderdata=response;
                    //Toast.makeText(getApplicationContext(), "genderdata="+genderdata, Toast.LENGTH_LONG).show();


                }
                else
                {
                    Toast.makeText(getContext(), "ERROR UPDATION!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "my error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key","getGender");

                SharedPreferences prefs = getContext().getSharedPreferences("SharedData", MODE_PRIVATE);
                final String uid = prefs.getString("logid", "No logid");//"No name defined" is the default value.

                map.put("uid",uid);





                return map;
            }
        };
        queue.add(request);
    }

    public void volly_call_helth_update()
    {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******",response);
                if(!response.trim().equals("failed"))
                {
                    String id=response;
                    Toast.makeText(getContext(), "Update Successfull", Toast.LENGTH_LONG).show();
                    Intent i =new Intent(getContext(),UserHome.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getContext(), "ERROR UPDATION!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "my error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                SharedPreferences prefs = getContext().getSharedPreferences("SharedData", MODE_PRIVATE);
                final String uid = prefs.getString("logid", "No logid");//"No name defined" is the default value.

                map.put("key","updatehelth");
                map.put("uid",uid);
                map.put("suger",SUGER);
                map.put("presure",PRESURE);
                map.put("height",HEIGHT);
                map.put("weight",WEIGHT);
                map.put("age",AGE);
                map.put("bmr",BMR);
                map.put("bmi",BMI);

                return map;
            }
        };
        queue.add(request);
    }

}
