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
import com.syntax.tastebowl.Common.Utility;
import com.syntax.tastebowl.R;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAddShop extends Fragment {


    EditText name,ownername,place,details;
    Button add;
    String NAME,OWNERNAME,PLACE,DETAILS;
    public UserAddShop() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_add_shop, container, false);

        name=view.findViewById(R.id.user_add_shop_name);
        ownername=view.findViewById(R.id.user_add_shop_owner_nmae);
        place=view.findViewById(R.id.user_add_shop_place);
        details=view.findViewById(R.id.user_add_shop_details);
        add=view.findViewById(R.id.user_add_shop_add);

        //Toast.makeText(getContext(), "latt="+ Utility.lat+"\n long="+Utility.logg, Toast.LENGTH_SHORT).show();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NAME=name.getText().toString().trim();
                OWNERNAME=ownername.getText().toString().trim();
                PLACE=place.getText().toString().trim();
                DETAILS=details.getText().toString().trim();

                if(NAME.isEmpty()){
                    name.requestFocus();
                    name.setError("Enter Name");
                }else if(OWNERNAME.isEmpty()){
                    ownername.requestFocus();
                    ownername.setError("Enter owner name");
                }else if(PLACE.isEmpty()){
                    place.requestFocus();
                    place.setError("Enter place");
                }else if(DETAILS.isEmpty()){
                    details.requestFocus();
                    details.setError("Enter details");
                }else {
                    user_AddShop();
                }


                }

        });

        return  view;
    }

    public void user_AddShop()
    {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******",response);
                if(!response.trim().equals("failed"))
                {
                    String id=response;
                    Toast.makeText(getContext(), "Added Successfull", Toast.LENGTH_LONG).show();
                    Intent i =new Intent(getContext(),UserHome.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getContext(), "ERROR  !", Toast.LENGTH_LONG).show();
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
                final   String uid = prefs.getString("logid", "No logid");//"No name defined" is the default value.

                map.put("key","user_AddShop");
                map.put("uid",uid);
                map.put("name",NAME);
                map.put("ownername",OWNERNAME);
                map.put("place",PLACE);
                map.put("details",DETAILS);
                map.put("latt",Utility.lat);
                map.put("longg",Utility.logg);
                map.put("uid",uid);
                return map;
            }
        };
        queue.add(request);
    }

}
