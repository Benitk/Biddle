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
import Utils.CardsAdapter;
import Utils.AlgoLibrary;


public class CustomerActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private ArrayList<Cards> cards;
    private CardsAdapter cardsAdapter;
    private TextView tv_noProductText;
    private String userId;
    private ProgressBar progressb;

    private DatabaseReference refProducts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
         refProducts = database.getReference().child("Products");

         userId = firebaseAuth.getCurrentUser().getUid();


        cards = new ArrayList<Cards>();

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);


        tv_noProductText = (TextView) findViewById(R.id.noProducts);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        initRecyclerAdapter();
        ReadFromDB();
    }



    // read all product from
    private void ReadFromDB(){
        progressb.setVisibility(View.VISIBLE);

        refProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cards.clear();
                tv_noProductText.setText("");

                if (dataSnapshot.exists())
                    addNewCard(dataSnapshot);

                else {
                    tv_noProductText.setText(R.string.noProduct);
                    progressb.setVisibility(View.GONE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                // would change to toast
                System.out.println("The read failed: " + databaseError.getCode());
                progressb.setVisibility(View.GONE);
            }
        });

    }



    // create new card from given snapshot
    private void addNewCard(DataSnapshot ds){

        Cards card = null;

        for(DataSnapshot product : ds.getChildren()){

            Log.d("hiss",product.toString());

            // user cant bid on his own product
            if(!userId.equals(product.getValue(Products.class).getSellerID())) {
                card = new Cards();

                card.setProductName(product.getValue(Products.class).getName());
                card.setCurrentPrice(Integer.toString(product.getValue(Products.class).getPrice()));
                card.setEndingDate(AlgoLibrary.DateFormating(product.getValue(Products.class).getEndingDate()));
                card.setProductId(product.getValue(Products.class).getId());

                cards.add(card);
            }
        }
        if(cards.isEmpty())
            tv_noProductText.setText(R.string.noProduct);

        cardsAdapter.notifyDataSetChanged();
        progressb.setVisibility(View.GONE);
    }


    // print the cards arrays on the current activity recyclerView
    private void initRecyclerAdapter() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String user_type = getIntent().getStringExtra("user_type");
        cardsAdapter = new CardsAdapter(this,cards, user_type);
        recyclerView.setAdapter(cardsAdapter);

    }


}