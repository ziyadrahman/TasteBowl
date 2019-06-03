package com.syntax.tastebowl.User;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.syntax.tastebowl.Common.Home_Recipie_Adapter;
import com.syntax.tastebowl.Common.Utility;
import com.syntax.tastebowl.Login;
import com.syntax.tastebowl.PojoClass.RecipieBean;
import com.syntax.tastebowl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Home extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager a;
    JsonArrayRequest RequestOfJSonArray;
    RequestQueue requestQueue;
    RecyclerView.LayoutManager layoutManagerOfrecyclerView;

    List<RecipieBean> ListOfdataAdapter= new ArrayList<>();

    //Adapter Object
    Home_Recipie_Adapter recyclerViewadapter;

    //to get the clicked views value
    ArrayList<String> click_val_id;
    ArrayList<String> click_val_image;
    ArrayList<String> click_val_price;
    View view1;
    int RecyclerViewItemPosition;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.home_recipie_list);



        click_val_id = new ArrayList<>();
        click_val_image = new ArrayList<>();
        click_val_price = new ArrayList<>();

        a=new GridLayoutManager(getContext(),2);
        recyclerView = (RecyclerView)view.findViewById(R.id.home_recipie_list);
        recyclerView.setLayoutManager(a);
        recyclerView.getLayoutManager().setMeasurementCacheEnabled(false);
        recyclerView.setHasFixedSize(true);

//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.setAdapter(adapter);

        get_home_recipieslist();

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                view1 = rv.findChildViewUnder(e.getX(), e.getY());

                if (view1 != null && gestureDetector.onTouchEvent(e)) {

                    //Getting RecyclerView Clicked Item value.
                    RecyclerViewItemPosition = rv.getChildAdapterPosition(view1);

                    Intent i= new Intent(getContext(),User_View_Recipie_Details.class);

                    i.putExtra("uid",ListOfdataAdapter.get(RecyclerViewItemPosition).getUid());
                    i.putExtra("uname",ListOfdataAdapter.get(RecyclerViewItemPosition).getUname());
                    i.putExtra("uphone",ListOfdataAdapter.get(RecyclerViewItemPosition).getUphone());
                    i.putExtra("uemail",ListOfdataAdapter.get(RecyclerViewItemPosition).getUemail());
                    i.putExtra("rid",ListOfdataAdapter.get(RecyclerViewItemPosition).getRid());
                    i.putExtra("recipie_name",ListOfdataAdapter.get(RecyclerViewItemPosition).getRecipie_name());
                    i.putExtra("type",ListOfdataAdapter.get(RecyclerViewItemPosition).getType());
                    i.putExtra("description",ListOfdataAdapter.get(RecyclerViewItemPosition).getDescription());
                    i.putExtra("ingredients",ListOfdataAdapter.get(RecyclerViewItemPosition).getIngredients());
                    i.putExtra("rating",ListOfdataAdapter.get(RecyclerViewItemPosition).getRating());
                    i.putExtra("image",ListOfdataAdapter.get(RecyclerViewItemPosition).getImage());

                    startActivity(i);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        return view;
    }



    public void get_home_recipieslist()
    {
        RequestOfJSonArray = new JsonArrayRequest(Utility.url+"?key=get_home_recipieslist",

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Log.d("@@@@@",response+"");
//                        Toast.makeText(view_recipes.this, response+"", Toast.LENGTH_SHORT).show();
                        ParseJSonResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(RequestOfJSonArray);
    }

    public void ParseJSonResponse(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {

            RecipieBean obj = new RecipieBean();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                obj.setUid(json.getString("uid"));
                obj.setUname(json.getString("uname"));
                obj.setUphone(json.getString("uphone"));
                obj.setUemail(json.getString("uemail"));
                obj.setRid(json.getString("rid"));
                obj.setRecipie_name(json.getString("recipie_name"));
                obj.setType(json.getString("type"));
                obj.setRating(json.getString("rating"));
                obj.setIngredients(json.getString("ingredients"));
                obj.setDescription(json.getString("description"));
                obj.setImage(json.getString("image"));
                ListOfdataAdapter.add(obj);

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
        recyclerViewadapter = new Home_Recipie_Adapter(ListOfdataAdapter, getContext());

        recyclerView.setAdapter(recyclerViewadapter);
    }


//    /**
//     * Converting dp to pixel
//     */
//    private int dpToPx(int dp) {
//        Resources r = getResources();
//        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
//    }
//
//
//    /**
//     * RecyclerView item decoration - give equal margin around grid item
//     */
//    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
//
//        private int spanCount;
//        private int spacing;
//        private boolean includeEdge;
//
//        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
//            this.spanCount = spanCount;
//            this.spacing = spacing;
//            this.includeEdge = includeEdge;
//        }
//    }

}

