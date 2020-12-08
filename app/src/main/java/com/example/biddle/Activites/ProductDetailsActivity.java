package com.example.biddle.Activites;

import androidx.appcompat.app.AppCompatActivity;
import com.example.biddle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView productName, productCategory, productDescrption, productPrice, ProductEndingDate;
    private Button typeBtn, HomeBtn;
    private ImageView Productimg;
    private ProgressBar processbar;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //DatabaseReference ref = database.getReference().child("Products");



    }
}