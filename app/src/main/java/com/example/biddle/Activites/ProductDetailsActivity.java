package com.example.biddle.Activites;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.biddle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import Models.Cards;
import Models.Products;
import Utils.AlgoLibrary;

import static com.example.biddle.R.*;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView productName, productCategory, productDescrption, productPrice, ProductEndingDate;
    private Button typeBtn;
    private ImageView Productimg, retrunBtn;
    private ProgressBar processbar;
    private String user_type;
    private String ProductID;
    private Double currentPrice;

    private FirebaseDatabase database;
    private DatabaseReference refProduct;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_product_details);

        database = FirebaseDatabase.getInstance();
        refProduct = database.getReference().child("Products");

        // seller or customer
        user_type = getIntent().getStringExtra("user_type");

        ProductID = getIntent().getStringExtra("productId");

        productName = (TextView)findViewById(id.productName);
        productCategory = (TextView)findViewById(id.productCategory);
        productDescrption = (TextView)findViewById(id.productDescription);
        productPrice = (TextView)findViewById(id.productPrice);
        ProductEndingDate = (TextView)findViewById(id.productDate);

        typeBtn = (Button) findViewById(id.typeBtn);


        if(user_type.equals("customer")){
            typeBtn.setText(string.bidProduct);
            typeBtn.setBackgroundTintList(ProductDetailsActivity.this.getResources().getColorStateList(color.green));
            SetbidBtn();
        }
        else{
            typeBtn.setText(string.deleteProduct);
            typeBtn.setBackgroundTintList(ProductDetailsActivity.this.getResources().getColorStateList(color.red));
        }


        Productimg = (ImageView) findViewById(id.productImg);
        retrunBtn = (ImageView) findViewById(id.returnBtn);

        processbar = (ProgressBar)findViewById(id.progressBar);
        processbar.setVisibility(View.GONE);

        ReadFromDB();

    }

    // fetch single product from firebase that equal productID
    private void ReadFromDB(){

        processbar.setVisibility(View.VISIBLE);

        refProduct.orderByKey().equalTo(ProductID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    setProductData(dataSnapshot);
                }
                else {
                    Toast.makeText(ProductDetailsActivity.this, "המוצר לא קיים", Toast.LENGTH_LONG).show();
                    Log.d("FaildReadDB","didnt find product");
                    // back one page
                    processbar.setVisibility(View.GONE);
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                // would change to toast
                Log.d("FaildReadDB","databaseError.getCode()");
                processbar.setVisibility(View.GONE);
            }
        });

    }

    private void setProductData(DataSnapshot ds){
        // will be only one product
        for(DataSnapshot product : ds.getChildren()){
            productName.setText(product.getValue(Products.class).getName());
            productCategory.setText(product.getValue(Products.class).getCategory());
            productDescrption.setText(product.getValue(Products.class).getDescription());
            currentPrice = product.getValue(Products.class).getPrice();
            productPrice.setText("₪ " + Double.toString(currentPrice));
            ProductEndingDate.setText(AlgoLibrary.DateFormating(product.getValue(Products.class).getEndingDate()));;
        }

        processbar.setVisibility(View.GONE);
    }

    private void SetDeleteBtn(){


    }

    private void SetbidBtn() {
        typeBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(ProductDetailsActivity.this);
                View promptsView = li.inflate(layout.bid_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProductDetailsActivity.this);

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.bid_DialogUserInput);

                // set dialog message
                alertDialogBuilder.setCancelable(false)
                        .setNegativeButton(string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        }).setPositiveButton(string.accpet, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog ,int id) {

                                String userBid = userInput.getText().toString().trim();
                                Log.d("dialog",userBid);
                                double newBid = userBid.length() > 0 ? Double.parseDouble(userBid) : -1;

                                 if(newBid <= currentPrice) {
                                    Toast.makeText(ProductDetailsActivity.this, string.bidTooLow, Toast.LENGTH_LONG).show();
                                     Log.d("dialog",""+(newBid <= currentPrice));

                                 }
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }
}