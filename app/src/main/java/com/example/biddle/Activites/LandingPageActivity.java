package com.example.biddle.Activites;

import android.content.Intent;
import android.os.Bundle;

import com.example.biddle.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class LandingPageActivity extends AppCompatActivity {

    private Button b_customer_btn;
    private Button b_seller_btn;
    private Button b_logout_btn;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null)
            finish();


        b_logout_btn = (Button) findViewById(R.id.logout_btn);
        b_customer_btn = (Button) findViewById(R.id.customer_btn);
        b_seller_btn = (Button) findViewById(R.id.seller_btn);
        b_customer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPageActivity.this, CustomerActivity.class);
                intent.putExtra("user_type", "customer");
                startActivity(intent);
            }
        });

        b_seller_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPageActivity.this, SellerActivity.class);
                intent.putExtra("user_type", "seller");
                startActivity(intent);
            }
        });

        b_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
            }
        });

    }
}