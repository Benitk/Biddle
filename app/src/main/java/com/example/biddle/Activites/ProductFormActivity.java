package com.example.biddle.Activites;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.biddle.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import Models.Products;
import Utils.SetDate;
import Utils.SetYourTime;

public class ProductFormActivity extends AppCompatActivity {

    private TextView newProduct_btn;
    private String productName, productDescription, productCategory;
    private double productPrice;
    // will convert to date type
    private EditText productTTLDate ,productTTLTime;
    private ImageView productImg;
    private String userId;

    private Uri imageUri;
    private String imgPath;

    private ProgressBar progressb;

    private SetDate set_date;
    private SetYourTime set_time;
    private String productID;

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
        productTTLTime = (EditText) findViewById(R.id.ProductTTLTime);

        productImg = (ImageView)findViewById(R.id.productImg);
        productTTLDate = (EditText)findViewById(R.id.ProductTTLDate);
        newProduct_btn = (TextView)findViewById(R.id.newProduct_btn);

        set_date = new SetDate(productTTLDate);
        set_time = new SetYourTime(productTTLTime);



        productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePic();
            }
        });

        productTTLDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == productTTLDate) {
                    Calendar systemCalender = Calendar.getInstance();
                    int year = systemCalender.get(Calendar.YEAR);
                    int month = systemCalender.get(Calendar.MONTH);
                    int day = systemCalender.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ProductFormActivity.this,set_date,year,month,day);
                    datePickerDialog.show();
                }
            }
        });

        productTTLTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == productTTLTime) {
                    Calendar systemCalendar = Calendar.getInstance();
                    int hour = systemCalendar.get(Calendar.HOUR_OF_DAY);
                    int minute = systemCalendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ProductFormActivity.this,set_time,hour,minute,true);
                    timePickerDialog.show();
                }
            }
        });

        newProduct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                productName = ((EditText)findViewById(R.id.ProductName)).getText().toString().trim();
                productDescription = ((EditText) findViewById(R.id.ProductDescription)).getText().toString().trim();
                //get the user price input string then convert it to double
                productCategory = ((EditText) findViewById(R.id.ProductCategory)).getText().toString().trim();

                String prodPrice = ((EditText) findViewById(R.id.ProductPrice)).getText().toString().trim();


                // validtate input
                boolean flag = true;


                if(TextUtils.isEmpty(prodPrice)) {
                    ((EditText) findViewById(R.id.ProductPrice)).setError("חובה למלא");
                    flag = false;
                }
                else
                    productPrice = Double.parseDouble(prodPrice);

                if(TextUtils.isEmpty(productName)) {
                    ((EditText) findViewById(R.id.ProductName)).setError("חובה למלא");
                    flag = false;
                }

                if(TextUtils.isEmpty(productDescription)) {
                    ((EditText) findViewById(R.id.ProductDescription)).setError("חובה למלא");
                    flag = false;
                }

                if(TextUtils.isEmpty(productCategory)){
                    ((EditText) findViewById(R.id.ProductCategory)).setError("חובה למלא");
                    flag = false;
                }

                if(TextUtils.isEmpty(productTTLTime.getText().toString().trim())) {
                    ((EditText) findViewById(R.id.ProductTTLTime)).setError("חובה למלא");
                    flag = false;
                }

                if(TextUtils.isEmpty(productTTLDate.getText().toString().trim())) {
                    ((EditText) findViewById(R.id.ProductTTLDate)).setError("חובה למלא");
                    flag = false;
                }


                // should check for img too

                if(flag){

                    // jave date class  is adding 1900 for year, month range(0,11)
                    Date dateTime = new Date(set_date.getYear()-1900, set_date.getMonth()-1, set_date.getDay(), set_time.getHour(), set_time.getMinute());

                    // there is no bidder so customerID set to null at the start
                    Products p = new Products(productID,userId,null, productName, productPrice, productCategory, dateTime, productDescription, imgPath);

                    // insert new product to product root
                    WriteToDB(p, refProduct.child(productID));

                    //insert new Product as Category prime key
                    WriteToDB(p, refCategory.child(productCategory).child(productID));

                    // insert new product id refrence to user root
                    WriteToDB(p, refUser.child(productID));
                }
            }
        });
    }

    private void WriteToDB(Object value, DatabaseReference ref){
        progressb.setVisibility(View.VISIBLE);

        ref.setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    progressb.setVisibility(View.GONE);
                    Toast.makeText(ProductFormActivity.this, "המוצר לא התווסף", Toast.LENGTH_SHORT).show();
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                    //finish();
                } else {
                    progressb.setVisibility(View.GONE);
                    Toast.makeText(ProductFormActivity.this, "המוצר התווסף בהצלחה", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
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
            uploadPicToDB();
        }
    }

    private void uploadPicToDB() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("העלאה מתבצעת...");
        pd.show();

        imgPath = userId + "/" + productID + "/" + UUID.randomUUID().toString();

        StorageReference Ref = storageref.child(imgPath);

        Ref.putFile(imageUri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(ProductFormActivity.this, "העלאת התמונה הצליחה", Toast.LENGTH_LONG).show();
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