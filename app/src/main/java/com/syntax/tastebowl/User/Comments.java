package com.syntax.tastebowl.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syntax.tastebowl.Common.Utility;
import com.syntax.tastebowl.R;

import java.util.HashMap;
import java.util.Map;

public class Comments extends AppCompatActivity {

    ListView commentlist;
    String RID,UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comments);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        commentlist = findViewById(R.id.user_view_comments_list);

        RID=getIntent().getStringExtra("rid");
        UID=getIntent().getStringExtra("uid");
        getComments();



    }
    public void getComments()
    {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******",response);
                if (!response.trim().equals("failed")) {


                    String data[]=response.trim().split("#");
                    ArrayAdapter ar=new ArrayAdapter(getApplicationContext(),R.layout.cust_list,data);
                    commentlist.setAdapter(ar);


                } else {
                    Toast.makeText(getApplicationContext(), "failed..!", Toast.LENGTH_LONG).show();
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
                map.put("key","getComments");
                map.put("rid",RID);


                return map;
            }
        };
        queue.add(request);
    }

}
