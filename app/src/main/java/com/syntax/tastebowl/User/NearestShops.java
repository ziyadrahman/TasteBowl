package com.syntax.tastebowl.User;


import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.syntax.tastebowl.Common.GPSTracker;
import com.syntax.tastebowl.Common.Utility;
import com.syntax.tastebowl.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearestShops extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    GPSTracker gps;
    Double latitude,longitude;
    public NearestShops() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_nearest_shops, container, false);

        gps = new GPSTracker(getContext());
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocatons();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        mMap.addMarker(new MarkerOptions().position( new LatLng(latitude, longitude)).title("Me").snippet(""));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(18).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });
    }

    public void getLocatons() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
                if (!response.trim().equals("failed")) {


                    String data[] = response.trim().split("#");
                    String lat[]=data[0].split(":");
                    String logg[]=data[1].split(":");
                    String name[]=data[2].split(":");
                    String details[]=data[3].split(":");

                    for(int i=0;i<lat.length-1;i++) {

                        Double  sel_lati=Double.parseDouble(lat[i]);
                        Double  sel_longi=Double.parseDouble(logg[i]);

                        mMap.addMarker(new MarkerOptions().position(new LatLng(sel_lati, sel_longi)).title(name[i]).snippet(details[i]).icon(BitmapDescriptorFactory.fromResource(R.drawable.shopicon)));

                    }



                } else {
                    Toast.makeText(getContext(), "Login failed..!", Toast.LENGTH_LONG).show();
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
                map.put("key", "getLocatons");

                return map;
            }
        };
        queue.add(request);
    }
}
