package com.example.biddle.Activites;

import android.content.Intent;
import android.os.Bundle;

import com.example.biddle.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import Models.Products;

public class ProductFormActivity extends AppCompatActivity {

    private TextView newProduct_btn;
    private String productName;
    private String productDescription;
    private double productPrice;
    // will convert to date type
    private String productTTL;
    private String productCategory;

    private ProgressBar progressb;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        String userId = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference ref = database.getReference().child("Products").child(userId);

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);

        newProduct_btn = (TextView)findViewById(R.id.newProduct_btn);



        newProduct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressb.setVisibility(View.VISIBLE);

                productName = ((EditText)findViewById(R.id.ProductName)).getText().toString().trim();
                productDescription = ((EditText) findViewById(R.id.ProductDescription)).getText().toString().trim();
                //get the user price input string then convert it to double
                productPrice = Double.parseDouble(((EditText) findViewById(R.id.ProductPrice)).getText().toString().trim());
                productTTL = ((EditText) findViewById(R.id.ProductTTL)).getText().toString().trim();
                productCategory = ((EditText) findViewById(R.id.ProductCategory)).getText().toString().trim();


                // write new product to firebase

                String productID = UUID.randomUUID().toString(); // genreate unique product id

                Products p = new Products(productID, productName, productPrice, productCategory, productTTL, productDescription);


                // insert new product to firebase

                ref.child(productID).setValue(p, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            progressb.setVisibility(View.GONE);
                            Toast.makeText(ProductFormActivity.this, "המוצר לא התווסף", Toast.LENGTH_SHORT).show();
                            System.out.println("Data could not be saved " + databaseError.getMessage());
                            finish();
                        } else {
                            progressb.setVisibility(View.GONE);
                            Toast.makeText(ProductFormActivity.this, "המוצר התווסף בהצלחה", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }
        });

    }
}