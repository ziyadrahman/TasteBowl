package com.syntax.tastebowl.User;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syntax.tastebowl.Common.Base64;
import com.syntax.tastebowl.Common.Utility;
import com.syntax.tastebowl.Login;
import com.syntax.tastebowl.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class User_View_Recipie_Details extends AppCompatActivity {

    ImageView image,addcomment,comments,likeorsave;
    TextView uname,uphone,recipie_name,rating;
    EditText ingredients,description;
    String UID,UNAME,UPHONE,UEMAIL,RID,RNAME,TYPE,RATING,DESCRIPTION,INGREDIENTS,IMAGE;
    String COMMENT_DETAILS,ACTION;
     Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user__view__recipie__details);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();


        uname=findViewById(R.id.user_view_recipie_uname);
        uphone=findViewById(R.id.user_view_recipie_uphone);
        recipie_name=findViewById(R.id.user_view_recipie_rname);
        rating=findViewById(R.id.user_view_recipie_rating);
        ingredients=findViewById(R.id.user_view_recipie_ingredients);
        description=findViewById(R.id.user_view_recipie_description);
        image=findViewById(R.id.user_view_recipie_img);
        addcomment=findViewById(R.id.user_view_recipie_add_comments);
        comments=findViewById(R.id.user_view_recipie_view_comments);


        likeorsave=findViewById(R.id.user_view_recipie_like_or_save);


        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(500); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        likeorsave.startAnimation(animation); //to start animation

        Intent i=getIntent();
        UID=i.getStringExtra("uid");
        UNAME=i.getStringExtra("uname");
        UPHONE=i.getStringExtra("uphone");
        UEMAIL=i.getStringExtra("uemail").trim();
        RID=i.getStringExtra("rid");
        RNAME=i.getStringExtra("recipie_name");
        TYPE=i.getStringExtra("type");
        DESCRIPTION=i.getStringExtra("description");
        RATING=i.getStringExtra("rating").trim();
        INGREDIENTS=i.getStringExtra("ingredients");
        IMAGE=i.getStringExtra("image");

        uname.setText(UNAME);
        uphone.setText(UPHONE);
        recipie_name.setText(RNAME);
        rating.setText(RATING);
        ingredients.setText(INGREDIENTS);
        description.setText(DESCRIPTION);

        image.setScaleType(ImageView.ScaleType.FIT_XY);

        try {
            byte[] imageAsBytes = Base64.decode(IMAGE.getBytes());

            image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length) );
        } catch (IOException e) {

            e.printStackTrace();
        }

        likeorsave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showCustomDialog1();
                return false;
            }
        });

        addcomment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showCustomDialog();
                return false;
            }
        });
        comments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Intent i=new Intent(getApplicationContext(),Comments.class);
                i.putExtra("rid",RID);
                i.putExtra("uid",UID);
                startActivity(i);
                //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);

                return false;
            }
        });



    }

    protected void showCustomDialog1() {
        // TODO Auto-generated method stub
        // final Dialog dialog = new Dialog(User_View_Recipie_Details.this);
        dialog = new Dialog(User_View_Recipie_Details.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_like_svae);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //final String pid=Pid;

        final ImageView like=dialog.findViewById(R.id.custom_user_like);
        final ImageView save = dialog.findViewById(R.id.custom_user_save);


        like.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                ACTION="like";
                addLike_save();
                return false;
            }
        });

        save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ACTION="save";
                addLike_save();
                return false;
            }
        });

        dialog.show();
    }


    protected void showCustomDialog() {
        // TODO Auto-generated method stub
       // final Dialog dialog = new Dialog(User_View_Recipie_Details.this);
        dialog = new Dialog(User_View_Recipie_Details.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_add_comment_dialog);

        //final String pid=Pid;
        final Button btnadd = (Button) dialog.findViewById(R.id.cus_dlg_add_comment_btnadd);
        final EditText details = (EditText) dialog.findViewById(R.id.cus_dlg_add_comment_details);


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 COMMENT_DETAILS=details.getText().toString();

                if(COMMENT_DETAILS.isEmpty()){
                    details.requestFocus();
                    details.setError("Amount");
                }else{
                    AddComments();
                }



            }
        });


        dialog.show();
    }

    public void AddComments()
    {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******",response);
                if (!response.trim().equals("failed")) {

                    Toast.makeText(getApplicationContext(), "success..!", Toast.LENGTH_LONG).show();
                    dialog.cancel();

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
                SharedPreferences prefs = getSharedPreferences("SharedData", MODE_PRIVATE);
                final   String uid = prefs.getString("logid", "No logid");//"No name defined" is the default value.
                map.put("key","AddComments");
                map.put("rid",RID);
                map.put("uid",uid);
                map.put("comment",COMMENT_DETAILS);



                return map;
            }
        };
        queue.add(request);
    }

    public void addLike_save()
    {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******",response);
                if (!response.trim().equals("failed")) {

                    if(ACTION.equals("like")&&response.trim().equals("already")){
                        Toast.makeText(getApplicationContext(), "already Liked..!", Toast.LENGTH_LONG).show();
                    }else if(ACTION.equals("like")&&response.trim().equals("success")){
                        Toast.makeText(getApplicationContext(), "Liked..!", Toast.LENGTH_LONG).show();
                    }else if(ACTION.equals("save")&&response.trim().equals("already")){
                        Toast.makeText(getApplicationContext(), "already Saved..!", Toast.LENGTH_LONG).show();
                    }else if(ACTION.equals("save")&&response.trim().equals("success")){
                        Toast.makeText(getApplicationContext(), "Saved..!", Toast.LENGTH_LONG).show();
                    }

                    dialog.cancel();

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
                SharedPreferences prefs = getSharedPreferences("SharedData", MODE_PRIVATE);
                final   String uid = prefs.getString("logid", "No logid");//"No name defined" is the default value.
                map.put("key","addLike_save");
                map.put("rid",RID);
                map.put("uid",uid);
                map.put("action",ACTION);
                map.put("ingredients",INGREDIENTS);

                return map;
            }
        };
        queue.add(request);
    }



}
