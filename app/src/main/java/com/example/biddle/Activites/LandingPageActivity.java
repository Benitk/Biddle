package com.example.biddle.Activites;

import android.os.Bundle;

import com.example.biddle.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class LandingPageActivity extends AppCompatActivity {

    private Button customer_btn;
    private Button seller_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        customer_btn = (Button) findViewById(R.id.customer_btn);
        seller_btn = (Button) findViewById(R.id.seller_btn);

        customer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(LandingActivity.this, CustomerActivity.class));
            }
        });

        seller_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(LandingActivity.this, SellerActivity.class));
            }
        });
    }
}