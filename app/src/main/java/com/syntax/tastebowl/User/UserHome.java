package com.syntax.tastebowl.User;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.syntax.tastebowl.Admin.AdminHome;
import com.syntax.tastebowl.Common.GPSTracker;
import com.syntax.tastebowl.Common.Utility;
import com.syntax.tastebowl.Login;
import com.syntax.tastebowl.R;

import java.util.HashMap;
import java.util.Map;

public class UserHome extends AppCompatActivity {


    private GoogleMap mMap;
    GPSTracker gps;
    double latitude;
    double longitude;

    double sel_lati = 0.0;
    double sel_longi = 0.0;

    String Flag="0";


    Dialog dialog;

    Fragment fragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.user_navigation_home:
                    fragment=new Home();
                    loadFragment(fragment);
                    return true;
                case R.id.user_navigation_add_recipie:
                    fragment=new AddRecipie();
                    loadFragment(fragment);
                    return true;
                case R.id.user_navigation_save:
                    fragment=new Savedlist();
                    loadFragment(fragment);
                    return true;
                case R.id.user_navigation_search:
                    fragment=new Usersearch();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


//        .....
        gps = new GPSTracker(getApplicationContext());
        getLocatons();

//        .....
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragment=new Home();
        loadFragment(fragment);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    protected void showCustomDialog() {
        // TODO Auto-generated method stub
        // final Dialog dialog = new Dialog(User_View_Recipie_Details.this);
        dialog = new Dialog(UserHome.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_menu);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //final String pid=Pid;

        final LinearLayout tips=dialog.findViewById(R.id.cus_menu_health_tips);
        final LinearLayout addshop = dialog.findViewById(R.id.cus_menu_addShop);
        final LinearLayout helthupdate =dialog.findViewById(R.id.cus_menu_helth_update);
        final LinearLayout feedback =dialog.findViewById(R.id.cus_menu_helth_feedback);
        final LinearLayout nearshop =dialog.findViewById(R.id.cus_menu_viewshops);

        feedback.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragment=new UserfeedBack();
                loadFragment(fragment);
                dialog.cancel();
                return false;
            }
        });

        addshop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragment=new ShopLocation();
                loadFragment(fragment);
                dialog.cancel();
                return false;
            }
        });

        helthupdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragment=new HealthUpdate();
                loadFragment(fragment);
                dialog.cancel();
                return false;
            }
        });
        tips.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragment=new UserHealthTips();
                loadFragment(fragment);
                dialog.cancel();
                return false;
            }
        });
        nearshop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fragment=new NearestShops();
                loadFragment(fragment);
                dialog.cancel();
                return false;
            }
        });



        dialog.show();
    }

//    ......
//    ......

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
//    ......


    public void getLocatons() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
                if (!response.trim().equals("failed")) {

                    Flag="0";
                    String data[] = response.trim().split("#");
                    String lat[]=data[0].split(":");
                    String logg[]=data[1].split(":");
                    String name[]=data[2].split(":");
                    String details[]=data[3].split(":");

                    for(int i=0;i<lat.length-1;i++){
                        //Toast.makeText(getApplicationContext(), "lat="+lat[i]+"\nlogg="+logg[i], Toast.LENGTH_SHORT).show();
                        double dist;
                        sel_lati=Double.parseDouble(lat[i]);
                        sel_longi=Double.parseDouble(logg[i]);
                        dist=distance(latitude,longitude,sel_lati,sel_longi);

                        if(dist<0.500){
                            Flag="1";
                        }
                    }

                    if(Flag.equals("1")){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.logo);

        long[] v = {500, 1000};
        builder.setVibrate(v);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);

       // Intent intent = new Intent(getApplicationContext(), UserHome.class);

        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
       // PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        //PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
       // builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        builder.setContentTitle("TasteBowl");
        builder.setContentText("Your near Shop . purchase ingredients");
      //  builder.setSubText("Tap to view the details.");
        builder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());

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
                map.put("key", "getLocatons");

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
