package com.example.biddle.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.biddle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import Models.Products;
import Utils.AlgoLibrary;

import static com.example.biddle.R.*;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView productName, productCategory, productDescrption, productPrice,
            timer, ProductEndingDate, star_tv;
    private Button typeBtn;
    private ImageView Productimg;
    private ProgressBar processbar;
    private String user_type;
    private String ProductID;
    private Integer currentPrice;
    private String userId;
    private Integer newBid;

    private long millisUntilFinished;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference refProduct;
    private DatabaseReference refCurrUser;

    private boolean isfavorite;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_product_details);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        refProduct = database.getReference().child("Products");

        userId = firebaseAuth.getCurrentUser().getUid();
        refCurrUser = database.getReference().child("Users").child(userId);

        // seller or customer
        user_type = getIntent().getStringExtra("user_type");
        ProductID = getIntent().getStringExtra("productId");
        productName = (TextView)findViewById(id.productName);
        productCategory = (TextView)findViewById(id.productCategory);
        productDescrption = (TextView)findViewById(id.productDescription);
        productPrice = (TextView)findViewById(id.productPrice);
        ProductEndingDate = (TextView)findViewById(id.Product_endingDate);
        typeBtn = (Button) findViewById(id.typeBtn);

        star_tv = (TextView)findViewById(id.star);
        timer = (TextView)findViewById(id.timer);

        if(user_type.equals("customer")) {
            typeBtn.setText(string.bidProduct);
            typeBtn.setBackgroundTintList(ProductDetailsActivity.this.getResources().getColorStateList(color.green));
            SetbidBtn();
        }
        else {
            typeBtn.setText(string.deleteProduct);
            typeBtn.setBackgroundTintList(ProductDetailsActivity.this.getResources().getColorStateList(color.red));
            star_tv.setVisibility(View.INVISIBLE);
        }

        Productimg = (ImageView) findViewById(id.productpic);
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
            productPrice.setText(Integer.toString(currentPrice)+" ₪");
            ProductEndingDate.setText(AlgoLibrary.DateFormating(product.getValue(Products.class).getEndingDate()));
            millisUntilFinished = product.getValue(Products.class).millisUntilFinished();
        }
        // each second has 1000 millisecond
        // countdown Interveal is 1sec = 1000 I have used
        new CountDownTimer(this.millisUntilFinished, 1000) {
            public void onTick(long millis) {
                NumberFormat f = new DecimalFormat("00");
                long day = (millis / 86400000);
                long hour = (millis / 3600000) % 24;
                long min = (millis / 60000) % 60;
                long sec = (millis / 1000) % 60;
                timer.setText(f.format(day) + "d:" + f.format(hour) + "h:" + f.format(min) + "m:" + f.format(sec) + "s");
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                timer.setText("00d:00h:00m:00s");
            }
        }.start();
        processbar.setVisibility(View.GONE);
    }

    private void SetDeleteBtn() {}

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
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).setPositiveButton(string.accpet, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog ,int id) {
                                String userBid = userInput.getText().toString().trim();
                                Log.d("dialog",userBid);
                                newBid = userBid.length() > 0 ? Integer.parseInt(userBid) : -1;
                                if(newBid > -1)
                                    TransactionDB();
                                else
                                    Toast.makeText(ProductDetailsActivity.this, string.bidFailed, Toast.LENGTH_LONG).show();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });

        star_tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatabaseReference ref = refCurrUser.child("favoriteProducts").child(ProductID);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) {  // the child doesn't exist, should add it to DB
                            refCurrUser.child("favoriteProducts").child(ProductID);
                            isfavorite = true;
                        } else {  // star pressed already, remove product from the favorite products of this customer
                            refCurrUser.child("favoriteProducts").child(ProductID).removeValue();
                            isfavorite = false;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void TransactionDB(){
        refProduct.child(ProductID).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Products p = mutableData.getValue(Products.class);
                if (p == null) {
                    // change nothing
                    ProductDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ProductDetailsActivity.this, R.string.bidNoExist, Toast.LENGTH_LONG).show();
                        }
                    });
                    return Transaction.success(mutableData);
                }
                else {
                    // time of bid
                    Date currentDate = new Date(System.currentTimeMillis());

                    // bid is too low, will not set
                    if(newBid <= p.getPrice()) {
                        // update current price from DB
                        ProductDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(ProductDetailsActivity.this, R.string.bidFailed, Toast.LENGTH_LONG).show();
                            }
                        });
                        productPrice.setText(Integer.toString(p.getPrice())+" ₪");
                        return Transaction.abort();
                    }

                    // product time already ended
                   else if(p.getEndingDate().compareTo(currentDate) < 0){
                        ProductDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(ProductDetailsActivity.this, string.bidTimeEnded, Toast.LENGTH_LONG).show();
                            }
                        });
                        return Transaction.abort();
                    }
                    else {
                        p.setCustomerID(userId);
                        p.setPrice(newBid);
                        mutableData.setValue(p);
                        return Transaction.success(mutableData);
                    }
                }
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean comitted, DataSnapshot dataSnapshot) {
                if(databaseError == null && comitted){
                    // update new Bid Price
                    productPrice.setText(Integer.toString(newBid)+" ₪");
                    ProductDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ProductDetailsActivity.this, string.bidSucsses, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public void onToggleStar(View view){
        star_tv = (TextView) view;
        if(!isfavorite) {  // star didn't selected before
            isfavorite = true;
            star_tv.setTextColor(Color.parseColor("#FFD600"));
        } else {  // wants to cancel the choice
            isfavorite = false;
            star_tv.setTextColor(Color.parseColor("#9E9E9E"));
        }
    }

}