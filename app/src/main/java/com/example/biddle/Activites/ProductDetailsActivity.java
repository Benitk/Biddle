package com.example.biddle.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Products;
import Utils.AlgoLibrary;
import Utils.DBmethods;

import static com.example.biddle.R.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView productName_tv, productCategory_tv, productDescrption_tv, productPrice_tv,
            timer, ProductEndingDate_tv, star_tv;
    private Button typeBtn;
    private ImageView Productimg;

    private List<UplouadImg> uplouadImgs;
    private RecyclerView Productimgs;
   private ImagAdapter imagAdapter;

    private Products currentProduct;
    private ProgressBar processbar;
    private String user_type;
    private String ProductID;
    private Integer currentPrice;
    private String userId;
    private String ProductSellerId;
    private String productCategory;
    private Date ProductEndingDate;
    private Integer newBid;

    private long millisUntilFinished;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference refProduct;
    private DatabaseReference refCurrUser;


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
        productName_tv = (TextView)findViewById(id.productName);
        productCategory_tv = (TextView)findViewById(id.productCategory);
        productDescrption_tv = (TextView)findViewById(id.productDescription);
        productPrice_tv = (TextView)findViewById(id.productPrice);
        ProductEndingDate_tv = (TextView)findViewById(id.Product_endingDate);

        typeBtn = (Button) findViewById(id.typeBtn);

        star_tv = (TextView)findViewById(id.star);
        setStarColor();

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
            SetDeleteBtn();
        }
       // Productimg = (ImageView) findViewById(id.productpic);
        //Productimgs.setHasFixedSize(true);
        //Productimgs.setLayoutManager(new LinearLayoutManager(this));
        Productimg = (ImageView) findViewById(id.productpic);
        uplouadImgs = new ArrayList<>();
        processbar = (ProgressBar)findViewById(id.progressBar);
        processbar.setVisibility(View.GONE);





        starBtn();
        ReadFromDB();
    }

    private void setStarColor() {
        DatabaseReference ref = refCurrUser.child("favoriteProducts").child(ProductID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists())  // star pressed before
                    star_tv.setTextColor(Color.parseColor("#FFD600"));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDetailsActivity.this, string.tryAgain, Toast.LENGTH_LONG).show();
            }
        });
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
                    Toast.makeText(ProductDetailsActivity.this, R.string.productNotExist, Toast.LENGTH_LONG).show();
                    Log.d("FaildReadDB","didnt find product");
                    // back one page
                    processbar.setVisibility(View.GONE);
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                // would change to toast
                Log.d("FaildReadDB",databaseError.toString());
                processbar.setVisibility(View.GONE);
            }
        });
    }

    private void setProductData(DataSnapshot ds){
        // will be only one product
        for(DataSnapshot product : ds.getChildren()){

            String productName = product.getValue(Products.class).getName();
            productName_tv.setText(productName);
            productCategory = product.getValue(Products.class).getCategory();
            productCategory_tv.setText(productCategory);
            String productDescrption = product.getValue(Products.class).getDescription();
            productDescrption_tv.setText(productDescrption);
            currentPrice = product.getValue(Products.class).getPrice();
            productPrice_tv.setText(Integer.toString(currentPrice)+" ₪");
            ProductEndingDate_tv.setText(AlgoLibrary.DateFormating(product.getValue(Products.class).getEndingDate()));
            ProductEndingDate = product.getValue(Products.class).getEndingDate();
            millisUntilFinished = product.getValue(Products.class).millisUntilFinished();
            // used when customer bid on this product
            ProductSellerId = product.getValue(Products.class).getSellerID();

            String ProductCustomerId = product.getValue(Products.class).getCustomerID();


            String imgPath = product.getValue(Products.class).getImgPath();


            UploadPic(imgPath);

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

public void  UploadPic(String imagPath){
if (imagPath.isEmpty()){}
else {
    final StorageReference mImageRef =
            FirebaseStorage.getInstance().getReference(imagPath);
    final long FIVE_MEGABYTE = 1024 * 1024 * 5;

    mImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess(Uri uri) {

            mImageRef.getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {

                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);

                    Productimg.setMinimumHeight(dm.heightPixels);
                    Productimg.setMinimumWidth(dm.widthPixels);
                    Productimg.setImageBitmap(bm);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    Toast.makeText(ProductDetailsActivity.this, "Error with image", Toast.LENGTH_LONG).show();

                }
            });

        }
    });
}
}

// delete product from db on each relevant node
    private void SetDeleteBtn() {
        typeBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                DBmethods.DeleteProduct(ProductID, productCategory, ProductSellerId, database.getReference());
                finish();
            }
        });
    }


    private void starBtn(){
        star_tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                processbar.setVisibility(View.VISIBLE);

                DatabaseReference ref = refCurrUser.child("favoriteProducts").child(ProductID);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {  // star pressed already, remove product from the favorite products of this customer
                            refCurrUser.child("favoriteProducts").child(ProductID).removeValue();
                            star_tv.setTextColor(Color.parseColor("#9E9E9E"));
                        } else {  // the child doesn't exist, should add it to DB
                            refCurrUser.child("favoriteProducts").child(ProductID).setValue(true);
                            star_tv.setTextColor(Color.parseColor("#FFD600"));
                        }
                        processbar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProductDetailsActivity.this, string.tryAgain, Toast.LENGTH_LONG).show();
                        processbar.setVisibility(View.GONE);
                    }
                });
            }
        });

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
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).setPositiveButton(string.accpet, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog ,int id) {
                                String userBid = userInput.getText().toString().trim();
                                Log.d("dialog",userBid);
                                newBid = userBid.length() > 0 ? Integer.parseInt(userBid) : -1;

                                // set new bid
                                voidFetchProduct(newBid);

                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }

    // fetch product to check if exist before set new bid
    private void voidFetchProduct(int newBid){
        processbar.setVisibility(View.VISIBLE);

        refProduct.orderByKey().equalTo(ProductID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    UpdatePrice(newBid);
                }
                else {
                    Toast.makeText(ProductDetailsActivity.this, R.string.bidNoExist, Toast.LENGTH_LONG).show();
                    Log.d("FaildReadDB","didnt find product");
                    // back one page
                }
                processbar.setVisibility(View.GONE);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                // would change to toast
                Log.d("FaildReadDB","databaseError.getCode()");
                processbar.setVisibility(View.GONE);
            }
        });
    }

    // update the price in all db roots
    private void UpdatePrice(int newBid){
        boolean flag = true;
        // input validation
        // bid too low
        if(newBid <= currentPrice) {
            Toast.makeText(ProductDetailsActivity.this, string.bidFailed, Toast.LENGTH_LONG).show();
            flag = false;
        }
        Date currentDate = new Date(System.currentTimeMillis());
        // product timer is over
        if(ProductEndingDate != null && ProductEndingDate.compareTo(currentDate) < 0){
            Toast.makeText(ProductDetailsActivity.this, string.bidTimeEnded, Toast.LENGTH_LONG).show();
            flag = false;
        }


        if(flag) {
            processbar.setVisibility(View.VISIBLE);


            Map<String, Object> childUpdates = new HashMap<>();

            String Category = productCategory_tv.getText().toString().trim();
            childUpdates.put("/Products/" + ProductID + "/price", newBid);
            childUpdates.put("/Products/" + ProductID + "/customerID", userId);
            childUpdates.put("/Categories/" + Category + "/" + ProductID + "/price", newBid);
            childUpdates.put("/Categories/" + Category + "/" + ProductID + "/customerID", userId);
            childUpdates.put("/Users/" + ProductSellerId + "/sellerProducts/" + ProductID + "/price", newBid);
            childUpdates.put("/Users/" + ProductSellerId + "/sellerProducts/" + ProductID + "/customerID", userId);


            database.getReference().updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    currentPrice = newBid;
                    productPrice_tv.setText(Integer.toString(newBid) + " ₪");
                    Toast.makeText(ProductDetailsActivity.this, string.bidSucsses, Toast.LENGTH_LONG).show();
                    processbar.setVisibility(View.GONE);

                    // this method adding new product to db in Users/userID/productsOnBid
                    refCurrUser.child("ProductOnBid").child(ProductID).setValue(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProductDetailsActivity.this, R.string.bidNoExist, Toast.LENGTH_LONG).show();
                    Log.d("UpdatePrice", "onFailure: " + e.toString());
                    processbar.setVisibility(View.GONE);
                }
            });
        }
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
                        productPrice_tv.setText(Integer.toString(p.getPrice())+" ₪");
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
                    productPrice_tv.setText(Integer.toString(newBid)+" ₪");
                    ProductDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ProductDetailsActivity.this, string.bidSucsses, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

}