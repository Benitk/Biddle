package com.example.biddle.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.biddle.R;
import com.google.firebase.auth.FirebaseAuth;

public class SellerActivity extends AppCompatActivity {

    private Button menu_seller_btn;
    private Button product_publish_btn;
    private Button show_my_products_btn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        firebaseAuth = FirebaseAuth.getInstance();

        menu_seller_btn = (Button) findViewById(R.id.menu_seller_btn);
        product_publish_btn = (Button) findViewById(R.id.product_publication_btn);
        show_my_products_btn = (Button) findViewById(R.id.show_my_products_btn);

        product_publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to publish product activity
            }
        });

        show_my_products_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //should take all seller's products fro DB
            }
        });

        menu_seller_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //want to open the menu from the left side
            }
        });




    }
}