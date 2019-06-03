package com.syntax.tastebowl.User;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.syntax.tastebowl.Common.GPSTracker;
import com.syntax.tastebowl.Common.Utility;
import com.syntax.tastebowl.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopLocation extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker gps;
    double latitude;
    double longitude;

    Button btn;
    TextView lati, longi;
    double sel_lati = 0.0;
    double sel_longi = 0.0;

    public ShopLocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shop_location, container, false);

        lati = view.findViewById(R.id.latval);
        longi = view.findViewById(R.id.longval);
        btn = (Button) view.findViewById(R.id.continueRegbtn);

        gps = new GPSTracker(getContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.lat=""+sel_lati;
                Utility.logg=""+sel_longi;

                UserAddShop nextFrag= new UserAddShop();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.user_fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return  view;
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.user_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //code to get current location
        if (!gps.canGetLocation()) {
            gps.showSettingsAlert();
        } else {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


        }


        LatLng myLocation = new LatLng(latitude, longitude);


        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(gps.getLatitude(), gps.getLongitude());

        mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

        //Move the camera to the user's location and zoom in!
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 16.5f));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(gps.getLatitude(), gps.getLongitude()))      // Sets the center of the map to location user
                .zoom(16.5f)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to north
                .tilt(30)                   // Sets the tilt of the camera to 70 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("New Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng.latitude, latLng.longitude)));
                    sel_lati = latLng.latitude;
                    sel_longi = latLng.longitude;

                    lati.setText(sel_lati + "");
                    longi.setText(sel_longi + "");

                    //  Toast.makeText(getApplicationContext(),"One marker alloweded",Toast.LENGTH_SHORT).show();


            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng.latitude, latLng.longitude)));
            }
        });



    }


}
