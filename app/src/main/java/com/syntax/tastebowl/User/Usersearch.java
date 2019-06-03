package com.syntax.tastebowl.User;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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
public class Usersearch extends Fragment {

    ListView Englishlist,Recipielist;
    SearchView search;
    String Searchval,Enlishval;
    String arEnglish[];
    String recipietdata[];
    public Usersearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_usersearch, container, false);
        Englishlist = view.findViewById(R.id.user_search_enlish_list);
        search = view.findViewById(R.id.user_search_search);
        Recipielist = view.findViewById(R.id.user_search_recipie_list);

        search.requestFocus();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Searchval=s;
                getEnglishlist();
                return false;
            }
        });

        Englishlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Enlishval=arEnglish[i];
                getsearch_recipielist();
            }
        });

        Recipielist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String data[]=recipietdata[i].split(":");

                Intent i1= new Intent(getContext(),User_View_Recipie_Details.class);

                i1.putExtra("uid",data[0]);
                i1.putExtra("uname",data[1]);
                i1.putExtra("uphone",data[2]);
                i1.putExtra("uemail",data[3]);
                i1.putExtra("rid",data[4]);
                i1.putExtra("recipie_name",data[5]);
                i1.putExtra("type",data[6]);
                i1.putExtra("description",data[7]);
                i1.putExtra("rating",data[8]);
                i1.putExtra("ingredients",data[9]);
                i1.putExtra("image",data[10]);

                startActivity(i1);

            }
        });




        return view;
    }
    public void getEnglishlist() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
                if (!response.trim().equals("failed")) {
                    arEnglish= response.trim().split("#");

                    Recipielist.setVisibility(View.GONE);
                    Englishlist.setVisibility(View.VISIBLE);

                    Englishlist.setAdapter(null);
                    ArrayAdapter ar = new ArrayAdapter(getContext(), R.layout.cust_list, arEnglish);
                    Englishlist.setAdapter(ar);


                } else {
                    Englishlist.setAdapter(null);
                    // Toast.makeText(getApplicationContext(), "failed..!", Toast.LENGTH_LONG).show();
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
                map.put("key", "getEnglishlist");
                map.put("val",Searchval);
                return map;
            }
        };
        queue.add(request);
    }
    public void getsearch_recipielist() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
                if (!response.trim().equals("failed")) {
                    if(!response.trim().equals("&")) {
                        String arr[] = response.trim().split("&");
                        String recipietlist[] = arr[0].split("#");
                        recipietdata = arr[1].split("#");


                        Englishlist.setVisibility(View.GONE);
                        Recipielist.setVisibility(View.VISIBLE);
                        Recipielist.setAdapter(null);
                        ArrayAdapter ar = new ArrayAdapter(getContext(), R.layout.cust_list, recipietlist);
                        Recipielist.setAdapter(ar);


                    }
                } else {

                    // Toast.makeText(getApplicationContext(), "failed..!", Toast.LENGTH_LONG).show();
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
                map.put("key", "getsearch_recipielist");
                map.put("Enlishval",Enlishval);
                return map;
            }
        };
        queue.add(request);
    }


}
