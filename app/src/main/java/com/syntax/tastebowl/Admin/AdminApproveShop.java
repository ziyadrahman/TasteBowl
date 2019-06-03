package com.syntax.tastebowl.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class AdminApproveShop extends AppCompatActivity {

    ListView shoplist;
    String Action,sid;
    String id[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approve_shop);

        shoplist = findViewById(R.id.admin_view_shops_list);
        getShopList();

        shoplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                sid=id[i];

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminApproveShop.this);
                alertDialogBuilder.setMessage("User Shop Request ..?");
                alertDialogBuilder.setPositiveButton("Approve",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                Action="Approve";
                                AdminShopAction();

                            }
                        });

                alertDialogBuilder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        Action="Reject";
                        AdminShopAction();
                    }
                });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

    }

    public void getShopList() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
                if (!response.trim().equals("failed")) {


                    String data[] = response.trim().split("&");
                    id=data[0].split(":");
                    String listinfo[]=data[1].split("#");

                    ArrayAdapter ar = new ArrayAdapter(getApplicationContext(), R.layout.cust_list, listinfo);
                    shoplist.setAdapter(ar);


                } else {
                   // Toast.makeText(getApplicationContext(), "failed..!", Toast.LENGTH_LONG).show();
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
                map.put("key", "getShopList");
                return map;
            }
        };
        queue.add(request);
    }
    public void AdminShopAction() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
                if (!response.trim().equals("failed")) {

                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(getApplicationContext(),AdminApproveShop.class));
                } else {
                    Toast.makeText(getApplicationContext(), "failed..!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),AdminApproveShop.class));
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
                map.put("key", "AdminShopAction");
                map.put("Action", Action);
                map.put("sid", sid);
                return map;
            }
        };
        queue.add(request);
    }
}