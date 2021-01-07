package com.example.biddle.Activites;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.squareup.picasso.Picasso;
import com.example.biddle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import Models.Products;
import Utils.SetDate;
import Utils.SetYourTime;

public class ProductFormActivity extends AppCompatActivity {

    private TextView newProduct_btn;
    private String productName, productDescription, productCategory;
    private int productPrice;
    // will convert to date type
    private EditText et_productTTLDate ,et_productTTLTime, et_productCategory;
    private ImageView productImg;
    private String userId;

    private Uri imageUri;
    private String imgPath = "";
    public Task<Uri> downloadUrl ;
    private ProgressBar progressb;

    private SetDate set_date;
    private SetYourTime set_time;
    private String productID;
public String imgPath2 = "";
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageref;
    private DatabaseReference refUser;
    private DatabaseReference refProduct;
    private DatabaseReference refgallery;

    private DatabaseReference refCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();

        userId = firebaseAuth.getCurrentUser().getUid();
        productID = UUID.randomUUID().toString(); // genreate unique product id

        refUser = database.getReference().child("Users").child(userId).child("sellerProducts");
        refProduct = database.getReference().child("Products");

        refCategory = database.getReference().child("Categories");


        progressb = (ProgressBar)findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);
        et_productTTLTime = (EditText) findViewById(R.id.ProductTTLTime);

        productImg = (ImageView)findViewById(R.id.productImg);
        et_productTTLDate = (EditText)findViewById(R.id.ProductTTLDate);
        et_productCategory =(EditText)findViewById(R.id.ProductCategory);
        newProduct_btn = (TextView)findViewById(R.id.newProduct_btn);

        set_date = new SetDate(et_productTTLDate);
        set_time = new SetYourTime(et_productTTLTime);



        productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePic();
            }
        });

        et_productTTLDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == et_productTTLDate) {
                    Calendar systemCalender = Calendar.getInstance();
                    int year = systemCalender.get(Calendar.YEAR);
                    int month = systemCalender.get(Calendar.MONTH);
                    int day = systemCalender.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ProductFormActivity.this,set_date,year,month,day);
                    datePickerDialog.show();
                }
            }
        });

        et_productTTLTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == et_productTTLTime) {
                    Calendar systemCalendar = Calendar.getInstance();
                    int hour = systemCalendar.get(Calendar.HOUR_OF_DAY);
                    int minute = systemCalendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ProductFormActivity.this,set_time,hour,minute,true);
                    timePickerDialog.show();
                }
            }
        });

        et_productCategory.setOnClickListener(new View.OnClickListener() {
            String selectedItem = "";
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProductFormActivity.this);
                builder.setTitle(R.string.pick_category);
                final String[] options = getResources().getStringArray(R.array.CategoriesArray);
                builder.setSingleChoiceItems(options, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectedItem = Arrays.asList(options).get(i);
                            }
                        });

                builder.setPositiveButton(R.string.accpet, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        et_productCategory.setText(selectedItem);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        newProduct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                productName = ((EditText)findViewById(R.id.ProductName)).getText().toString().trim();
                productDescription = ((EditText) findViewById(R.id.ProductDescription)).getText().toString().trim();
                //get the user price input string then convert it to int
                productCategory = et_productCategory.getText().toString().trim();

                String prodPrice = ((EditText) findViewById(R.id.ProductPrice)).getText().toString().trim();

                // validtate input
                boolean flag = true;

                if(TextUtils.isEmpty(prodPrice)) {
                    ((EditText) findViewById(R.id.ProductPrice)).setError(R.string.mustFill+"");
                    flag = false;
                }
                else
                    productPrice = Integer.parseInt(prodPrice);

                if(TextUtils.isEmpty(productName)) {
                    ((EditText) findViewById(R.id.ProductName)).setError(R.string.mustFill+"");
                    flag = false;
                }

                if(TextUtils.isEmpty(productDescription)) {
                    ((EditText) findViewById(R.id.ProductDescription)).setError(R.string.mustFill+"");
                    flag = false;
                }

                if(TextUtils.isEmpty(productCategory)){
                    ((EditText) findViewById(R.id.ProductCategory)).setError(R.string.mustFill+"");
                    flag = false;
                }

                if(TextUtils.isEmpty(et_productTTLTime.getText().toString().trim())) {
                    ((EditText) findViewById(R.id.ProductTTLTime)).setError(R.string.mustFill+"");
                    flag = false;
                }

                if(TextUtils.isEmpty(et_productTTLDate.getText().toString().trim())) {
                    ((EditText) findViewById(R.id.ProductTTLDate)).setError(R.string.mustFill+"");
                    flag = false;
                }


                if(TextUtils.isEmpty(imgPath)) {
                    flag = false;
                    Toast.makeText(ProductFormActivity.this, R.string.MustUploadPic, Toast.LENGTH_SHORT).show();
                }

                if(flag){

                    // jave date class  is adding 1900 for year, month range(0,11)
                    Date dateTime = new Date(set_date.getYear()-1900, set_date.getMonth()-1, set_date.getDay(), set_time.getHour(), set_time.getMinute());

                    Date currentDate = new Date(System.currentTimeMillis());

                    if(dateTime != null && dateTime.compareTo(currentDate) < 0) {
                        ((EditText) findViewById(R.id.ProductTTLTime)).setError("");
                        ((EditText) findViewById(R.id.ProductTTLDate)).setError("");
                        Toast.makeText(ProductFormActivity.this, R.string.DatePast, Toast.LENGTH_SHORT).show();
                        return;
                    }


                    // there is no bidder so customerID set to userId from start at the start
                    Products p = new Products(productID,userId,userId, productName, productPrice, productCategory, dateTime, productDescription, imgPath);

                    // insert new product to product root
                    WriteToDB(p, refProduct.child(productID));

                    //insert new Product as Category prime key
                    WriteToDB(p, refCategory.child(productCategory).child(productID));

                    // insert new product id refrence to user root
                    WriteToDB(p, refUser.child(productID));

                  //  database.getReference().child("Products").child(productID).child("imgPath").setValue("old link");

                    uploadPicToDB();


                }
            }
        });
    }

    private void WriteToDB(Products product, DatabaseReference ref){
        progressb.setVisibility(View.VISIBLE);

        ref.setValue(product, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    progressb.setVisibility(View.GONE);
                    Toast.makeText(ProductFormActivity.this, R.string.productFail, Toast.LENGTH_SHORT).show();
                } else {
                    progressb.setVisibility(View.GONE);

                    Toast.makeText(ProductFormActivity.this, R.string.productSucsess, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    // fetch single product from firebase that equal productID
    private void ReadProductFromDB(){

        progressb.setVisibility(View.VISIBLE);

        refProduct.orderByKey().equalTo(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // should be only once child
                if (dataSnapshot.exists()){
                    sendMail(dataSnapshot);
                }

                else {
                    Toast.makeText(ProductFormActivity.this, "המוצר לא קיים", Toast.LENGTH_LONG).show();
                    Log.d("FaildReadDB","didnt find product");
                    // back one page
                    progressb.setVisibility(View.GONE);
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                // would change to toast
                Log.d("FaildReadDB","databaseError.getCode()");
                progressb.setVisibility(View.GONE);
            }
        });
    }

    private void sendMail(DataSnapshot dataSnapshot){
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            String sellerID_To_Mail = ds.getValue(Products.class).getSellerID();
            String customerID_To_Mail = ds.getValue(Products.class).getCustomerID();

            // send only to seller because no one bid on this product
            if(sellerID_To_Mail.equals(customerID_To_Mail)){

            }
            // send both of them
            else{


            }

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void ChoosePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(productImg);
            imgPath = userId + "/" + productID + "/" +"gallery/"+ UUID.randomUUID().toString()+".jpg"; //getFileExtension(imageUri);

        }
    }


    private void uploadPicToDB() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("העלאה מתבצעת...");
        pd.show();
        StorageReference Ref = storageref.child(imgPath) ;
                //getFileExtension(imageUri));
        Ref.putFile(imageUri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText( ProductFormActivity.this, "העלאת התמונה הצליחה", Toast.LENGTH_LONG).show();
                        finish();



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    pd.dismiss();
                    Toast.makeText(ProductFormActivity.this , "העלאת התמונה נכשלה", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPrecent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage((int) progressPrecent + "%" );
            }
        });




    }
}