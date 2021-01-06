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
import Utils.AlgoLibrary;
import Utils.CardsAdapter;

public class PriceOfferedProductsCustomerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Cards> cards;
    private CardsAdapter cardsAdapter;
    private TextView tv_noProductText;
    private ProgressBar progressb;

    private DatabaseReference refOnBidProducts;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference refProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_offered_products_customer);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        String userId = firebaseAuth.getCurrentUser().getUid();

        refProduct = database.getReference().child("Products");

        refOnBidProducts = database.getReference().child("Users").child(userId).child("ProductOnBid");

        cards = new ArrayList<Cards>();

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);

        tv_noProductText = (TextView) findViewById(R.id.noProducts);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        initRecyclerAdapter();
        ReadKeysFromDB();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    // this method is getting all user onBid product keys from user root and retrive all of them from product root
    private void ReadKeysFromDB(){

        progressb.setVisibility(View.VISIBLE);


        refOnBidProducts.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                cards.clear();
                tv_noProductText.setText("");

                if (dataSnapshot.exists())
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        fetchProductFromKey(ds.getKey().toString());

                    }
                else {
                    tv_noProductText.setText(R.string.noProduct);
                    progressb.setVisibility(View.GONE);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // would change to toast
                Log.d("dbCanceled: ", databaseError.getMessage());
                progressb.setVisibility(View.GONE);

            }


        });
    }


    private void fetchProductFromKey(String ProductID){
        refProduct.orderByKey().equalTo(ProductID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    addNewCard(dataSnapshot);
                }
                else {
                    Log.d("FaildReadDB","didnt find product");
                    // back one page
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // would change to toast
                Log.d("FaildReadDB",databaseError.toString());
            }
        });
    }


    // create new card from given snapshot of seller products
    private void addNewCard(DataSnapshot ds){

        Cards card = null;

        for(DataSnapshot product : ds.getChildren()){
            card = new Cards();

            card.setProductName(product.getValue(Products.class).getName());

            card.setCurrentPrice(Integer.toString(product.getValue(Products.class).getPrice()));

            card.setEndingDate(AlgoLibrary.DateFormating(product.getValue(Products.class).getEndingDate()));

            card.setProductId(product.getValue(Products.class).getId());
            card.setImgPath(product.getValue(Products.class).getImgPath());

            cards.add(card);

        }
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