package com.example.biddle.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.biddle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Utils.SetDate;

public class PurchasedProductsCustomerActivity extends AppCompatActivity {

    private String userId;
    private ProgressBar progressb;

    private SetDate set_date;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageref;
    private DatabaseReference refUser;
    private DatabaseReference refProduct;
    private DatabaseReference refCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_customer);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();
        userId = firebaseAuth.getCurrentUser().getUid();
        refUser =database.getReference().child("Users").child(userId).child("PurchasedByCustomer");

        progressb = (ProgressBar) findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);


        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for every product in database ("Products")
                for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                    if(dataSnapshot.exists()){
                        //Strings to presents in Xml
                        String productName = imageSnapshot.child("name").getValue().toString();
                        String price = imageSnapshot.child("price").getValue().toString();
                        String productID = imageSnapshot.child("id").getValue().toString();
                        String sellerID = imageSnapshot.child("costumerID").getValue().toString();
                        String image = imageSnapshot.child("imgPath").getValue().toString();
                        String Day = imageSnapshot.child("endingDate").child("day").getValue().toString();
                        String Date = imageSnapshot.child("endingDate").child("Date").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FaildReadDB", error.toString());
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
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
                startActivity(new Intent(PurchasedProductsCustomerActivity.this, PurchasedProductsSellerActivity.class));
                return true;
            case R.id.editProfile:
                return true;
            case R.id.logOut:
                firebaseAuth.signOut();
                startActivity(new Intent(PurchasedProductsCustomerActivity.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}