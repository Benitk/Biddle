package com.example.biddle.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biddle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Models.Cards;
import Models.Products;
import Utils.Adapter;


public class SellerActivity extends AppCompatActivity {


    private TextView AddProduct_tv;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private ArrayList<Cards> cards;
    private Adapter adapter;
    private TextView tv_noProductText;
    private ProgressBar progressb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Products");

        String userId = firebaseAuth.getCurrentUser().getUid();

        cards = new ArrayList<Cards>();

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);


        tv_noProductText = (TextView) findViewById(R.id.noProducts);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        AddProduct_tv = (TextView) findViewById(R.id.AddProduct_tv);

        AddProduct_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellerActivity.this, ProductFormActivity.class));
            }
        });

        progressb.setVisibility(View.VISIBLE);

        ref.child(userId).addValueEventListener(new ValueEventListener() {
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

    @Override
    protected void onResume(){
        super.onResume();
//
//        Log.d("ben", "here");
//        finish();
        // put your code here...

    }

    // print products of current user to list

    private void showData(DataSnapshot dataSnapshot) {

        cards.clear();

        for(DataSnapshot ds : dataSnapshot.getChildren()){

            Cards card = new Cards();

            card.setProductName(ds.getValue(Products.class).getName());
            card.setCurrentPrice(Double.toString(ds.getValue(Products.class).getPrice()));
            card.setEndingDate(ds.getValue(Products.class).getEndingDate().toString());
            card.setProductId(ds.getValue(Products.class).getId());

            cards.add(card);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String user_type = getIntent().getStringExtra("user_type");
        adapter = new Adapter(this,cards, user_type);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.seller_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homePage:
                finish();
                return true;
            case R.id.unofferedProducts:
                return true;
            case R.id.priceOfferedProducts:
                return true;
            case R.id.purchasedProducts:
                return true;
            case R.id.waitForPickUpProducts:
                return true;
            case R.id.editProfile:
                return true;
            case R.id.logOut:
                firebaseAuth.signOut();
                startActivity(new Intent(SellerActivity.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}