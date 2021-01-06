package com.example.biddle.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.biddle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import Models.Cards;
import Models.Products;
import Utils.CardsAdapter;
import Utils.AlgoLibrary;
import Utils.DBmethods;


public class SellerActivity extends AppCompatActivity {


    private Button AddProduct_btn;

    private RecyclerView recyclerView;
    private ArrayList<Cards> cards;
    private CardsAdapter cardsAdapter;
    private TextView tv_noProductText;
    private ProgressBar progressb;

    private DatabaseReference refProducts;
    private DatabaseReference refUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        String userId = firebaseAuth.getCurrentUser().getUid();

        // root to user
        refUser = database.getReference().child("Users").child(userId).child("sellerProducts");

        cards = new ArrayList<Cards>();

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);


        tv_noProductText = (TextView) findViewById(R.id.noProducts);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        AddProduct_btn = (Button) findViewById(R.id.AddProduct_tv);

        AddProduct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellerActivity.this, ProductFormActivity.class));
            }
        });

        initRecyclerAdapter();
        ReadFromDB();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    // this method is getting all sellers product keys from user root and retrive all of them from product root
    private void ReadFromDB(){

        progressb.setVisibility(View.VISIBLE);


        refUser.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
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

                Log.d("dbCanceled: ", databaseError.getMessage());
                progressb.setVisibility(View.GONE);

            }


        });
    }

    // create new card from given snapshot of seller products
    private void addNewCard(DataSnapshot ds){

        Cards card = null;

        for(DataSnapshot product : ds.getChildren()){

            String productSellerID = product.getValue(Products.class).getSellerID();
            String productCategory = product.getValue(Products.class).getCategory();
            String productId = product.getValue(Products.class).getId();
            Date currentDate = new Date(System.currentTimeMillis());
            Date productDate = product.getValue(Products.class).getEndingDate();

            // product timer is over
            if(productDate != null && productDate.compareTo(currentDate) < 0){
                DBmethods.DeleteProduct(productId, productCategory, productSellerID, database.getReference());
                continue;
            }


            card = new Cards();

            card.setProductName(product.getValue(Products.class).getName());

            card.setCurrentPrice(Integer.toString(product.getValue(Products.class).getPrice()));

            card.setEndingDate(AlgoLibrary.DateFormating(product.getValue(Products.class).getEndingDate()));

            card.setProductId(productId);
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
            case R.id.purchasedProducts:
                startActivity(new Intent(SellerActivity.this, PurchasedProductsSellerActivity.class));
                return true;
            case R.id.editProfile:
                startActivity(new Intent(SellerActivity.this, EditProfileSellerActivity.class));
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