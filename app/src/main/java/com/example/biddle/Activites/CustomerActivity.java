package com.example.biddle.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.biddle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Models.Cards;
import Models.Products;
import Utils.Adapter;
import Utils.AlgoLibrary;


public class CustomerActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private ArrayList<Cards> cards;
    private Adapter adapter;
    private TextView tv_noProductText;
    private String userId;
    private ProgressBar progressb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Products");

         userId = firebaseAuth.getCurrentUser().getUid();


        cards = new ArrayList<Cards>();

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);


        tv_noProductText = (TextView) findViewById(R.id.noProducts);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);



        progressb.setVisibility(View.VISIBLE);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    showData(dataSnapshot);
                    progressb.setVisibility(View.GONE);
                }
                else{
                    tv_noProductText.setText(R.string.noProduct);
                    progressb.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }




        // print products of current user to list

    private void showData(DataSnapshot dataSnapshot) {

        cards.clear();

        // get each user that created products
        for(DataSnapshot Userds : dataSnapshot.getChildren()){

            // user cant bid on his own product
            if(!userId.equals(Userds.getKey())) {
                // get each product in each user
                for (DataSnapshot ds : Userds.getChildren()) {

                    Cards card = new Cards();
                    card.setProductName(ds.getValue(Products.class).getName());
                    card.setCurrentPrice(Double.toString(ds.getValue(Products.class).getPrice()));
                    card.setEndingDate(AlgoLibrary.DateFormating(ds.getValue(Products.class).getEndingDate()));
                    card.setProductId(ds.getValue(Products.class).getId());

                    cards.add(card);
                }
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String user_type = getIntent().getStringExtra("user_type");
        adapter = new Adapter(this,cards, user_type);
        recyclerView.setAdapter(adapter);
    }
}