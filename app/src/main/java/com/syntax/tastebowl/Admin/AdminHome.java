package com.syntax.tastebowl.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.syntax.tastebowl.R;
import com.syntax.tastebowl.User.UserfeedBack;

public class AdminHome extends AppCompatActivity {

    ImageView approveshop,viewuser,feedbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        approveshop=findViewById(R.id.admin_approve_shop);
        viewuser=findViewById(R.id.admin_usres);
        feedbacks=findViewById(R.id.admin_userfeedback);
        approveshop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(getApplicationContext(),AdminApproveShop.class));
                return false;
            }
        });
        viewuser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(getApplicationContext(),AdminViewUser.class));
                return false;
            }
        });
        feedbacks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startActivity(new Intent(getApplicationContext(),Userfeedbacks.class));
                return false;
            }
        });

    }
}
