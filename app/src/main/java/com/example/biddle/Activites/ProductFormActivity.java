package com.example.biddle.Activites;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import Models.Products;
import Utils.SetDate;
import Utils.SetYourTime;

public class ProductFormActivity extends AppCompatActivity {

    private TextView newProduct_btn;
    private String productName;
    private String productDescription;
    private double productPrice;
    // will convert to date type
    private EditText productTTLDate;
    private EditText productTTLTime;
    private String productCategory;

    private ProgressBar progressb;

    private SetDate set_date;
    private SetYourTime set_time;

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
        productTTLTime = (EditText) findViewById(R.id.ProductTTLTime);


        productTTLDate = (EditText) findViewById(R.id.ProductTTLDate);
        newProduct_btn = (TextView)findViewById(R.id.newProduct_btn);

        set_date = new SetDate();
        set_time = new SetYourTime();

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

                progressb.setVisibility(View.VISIBLE);

                productName = ((EditText)findViewById(R.id.ProductName)).getText().toString().trim();
                productDescription = ((EditText) findViewById(R.id.ProductDescription)).getText().toString().trim();
                //get the user price input string then convert it to double
                productPrice = Double.parseDouble(((EditText) findViewById(R.id.ProductPrice)).getText().toString().trim());
                //productTTLDate = ((EditText) findViewById(R.id.ProductTTLDate)).getText().toString().trim();
                //productTTLTime = ((EditText) findViewById(R.id.ProductTTLTime)).getText().toString().trim();
                productCategory = ((EditText) findViewById(R.id.ProductCategory)).getText().toString().trim();


                // write new product to firebase

                String productID = UUID.randomUUID().toString(); // genreate unique product id

                Date dateTime = new Date(set_date.getYear(), set_date.getMonth(), set_date.getDay(), set_time.getHour(), set_time.getMinute());
                Products p = new Products(productID, productName, productPrice, productCategory, dateTime, productDescription);


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
    /*

    	Date dateTime = new Date(2021,12,3,1,2);
		System.out.println(dateTime);
		String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(dateTime);
        System.out.println(date);

     */
}