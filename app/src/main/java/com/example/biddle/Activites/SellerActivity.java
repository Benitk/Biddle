package com.example.biddle.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import Models.Products;


public class SellerActivity extends AppCompatActivity {

    private Button menu_seller_btn;
    private Button AddProduct_btn;
    private ListView lv_list;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("Products");

        String userId = firebaseAuth.getCurrentUser().getUid();

        lv_list = (ListView)findViewById(R.id.list_view);
        menu_seller_btn = (Button) findViewById(R.id.menu_seller_btn);
        AddProduct_btn = (Button) findViewById(R.id.AddProduct_btn);


        AddProduct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellerActivity.this, ProductFormActivity.class));
            }
        });

        menu_seller_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //want to open the menu from the left side
            }
        });

            ref.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    showData(dataSnapshot);
                }
                else{
                    ArrayList<String> array  = new ArrayList<>();
                    array.add("אין מוצרים");

                    ArrayAdapter adapter = new ArrayAdapter(SellerActivity.this, R.layout.simple_list ,array);
                    lv_list.setAdapter(adapter);
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
        for(DataSnapshot ds : dataSnapshot.getChildren()){

            Products p = new Products();

            p.setName(ds.getValue(Products.class).getName());
            p.setPrice(ds.getValue(Products.class).getPrice());
            p.setEndingDate(ds.getValue(Products.class).getEndingDate());

            ArrayList<String> array  = new ArrayList<>();
            array.add(p.getName());
            array.add(Double.toString(p.getPrice()));
            array.add(p.getEndingDate().toString());
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_list,array);
            lv_list.setAdapter(adapter);
        }
    }
}