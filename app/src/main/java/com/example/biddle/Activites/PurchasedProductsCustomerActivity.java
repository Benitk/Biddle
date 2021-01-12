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
import Models.Receipt;
import Utils.AlgoLibrary;
import Utils.CardsAdapter;
import Utils.ReciptsAdapter;

public class PurchasedProductsCustomerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Receipt> receipts;
    private ReciptsAdapter receiptsAdapter;
    private TextView tv_noProductText;
    private ProgressBar progressb;

    private DatabaseReference refOnUserReceipts;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_products_customer);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        String userId = firebaseAuth.getCurrentUser().getUid();

        refOnUserReceipts = database.getReference().child("Users").child(userId).child("ReceiptsAsCustomer");

        receipts = new ArrayList<Receipt>();

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);

        tv_noProductText = (TextView) findViewById(R.id.noProducts);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

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


    // this method is getting all user onBid product keys from user root and retrive all of them from product root
    private void ReadFromDB(){

        progressb.setVisibility(View.VISIBLE);


        refOnUserReceipts.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                receipts.clear();
                tv_noProductText.setText("");

                if (dataSnapshot.exists()){
                    Log.d("benben", dataSnapshot.toString());
                    addNewReceipt(dataSnapshot);
                }
                else {
                    tv_noProductText.setText(R.string.noReceipts);
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


    // create new receipt from given snapshot of seller products
    private void addNewReceipt(DataSnapshot dataSnapshot){

        Receipt receipt = null;

        for(DataSnapshot ds : dataSnapshot.getChildren()){
            receipt = new Receipt();

            receipt.setProductName(ds.getValue().toString());

            receipt.setReceiptID(ds.getKey().toString());


            receipts.add(receipt);

        }
        receiptsAdapter.notifyDataSetChanged();
        progressb.setVisibility(View.GONE);
    }


    // print the cards arrays on the current activity recyclerView
    private void initRecyclerAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String user_type = getIntent().getStringExtra("user_type");
        receiptsAdapter = new ReciptsAdapter(this,receipts, user_type);
        recyclerView.setAdapter(receiptsAdapter);

    }
}