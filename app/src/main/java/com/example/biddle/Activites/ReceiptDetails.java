package com.example.biddle.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.biddle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import Models.Products;
import Models.Receipt;
import Utils.AlgoLibrary;
import Utils.DBmethods;

public class ReceiptDetails extends AppCompatActivity {
    private EditText productName_et, productPrice_et, sellerPhone_et, sellerName_et, customerPhone_et, customerName_et, fullAddress_et, SaleDate_et;
    private FirebaseDatabase database;
    private DatabaseReference refReceiptDetails;
    private ProgressBar processbar;
    private Button mail_btn;

    private String user_type;
    private String receiptID;

    private String seller_mail, customer_mail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_details);

        database = FirebaseDatabase.getInstance();
        refReceiptDetails = database.getReference().child("ReceiptsDetails");

        // seller or customer
        user_type = getIntent().getStringExtra("user_type");
        receiptID = getIntent().getStringExtra("receiptID");

        productName_et = (EditText)findViewById(R.id.ProductName);
        productPrice_et = (EditText)findViewById(R.id.ProductPrice);
        sellerPhone_et = (EditText)findViewById(R.id.sellerPhone);
        sellerName_et = (EditText)findViewById(R.id.sellerName);
        customerPhone_et = (EditText)findViewById(R.id.customerPhone);
        customerName_et = (EditText)findViewById(R.id.customerName);
        fullAddress_et = (EditText)findViewById(R.id.sellerAddress);
        SaleDate_et = (EditText)findViewById(R.id.SaleDate);
        mail_btn = (Button)findViewById(R.id.emailBtn);

        processbar = (ProgressBar)findViewById(R.id.progressBar);
        processbar.setVisibility(View.GONE);

        ReadFromDB();

        if(user_type.equals("customer")) {
            mail_btn.setText("שליחת מייל לסוחר/ת");
            sendMailToSeller();
        }
        else {
            mail_btn.setText("שליחת מייל ללקוח/ה");
            sendMailToCustomer();
        }
    }

    private void sendMailToCustomer() {
        mail_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {seller_mail, customer_mail});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.customerMailTitle));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.customerMailText));
                startActivity(intent);
            }
        });
    }

    private void sendMailToSeller() {
        mail_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {customer_mail, seller_mail});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sellerMailTitle));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sellerMailText));
                startActivity(intent);
            }
        });
    }

    // fetch single receipt from firebase that equal receiptID
    private void ReadFromDB(){

        processbar.setVisibility(View.VISIBLE);

        refReceiptDetails.orderByKey().equalTo(receiptID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    setReceiptData(dataSnapshot);
                }
                else {
                    Toast.makeText(ReceiptDetails.this, R.string.noReceiptFound, Toast.LENGTH_LONG).show();
                    Log.d("FaildReadDB","didnt find receipt");
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


    private void setReceiptData(DataSnapshot ds){
        // will be only one product
        for(DataSnapshot receipt : ds.getChildren()){

            productName_et.setText(receipt.getValue(Receipt.class).getProductName());
            productPrice_et.setText(Integer.toString(receipt.getValue(Receipt.class).getPrice()));
            sellerPhone_et.setText(Integer.toString(receipt.getValue(Receipt.class).getSellerPhoneNumber()));
            sellerName_et.setText(receipt.getValue(Receipt.class).getSellerName());
            customerPhone_et.setText(Integer.toString(receipt.getValue(Receipt.class).getCustomerPhoneNumber()));
            customerName_et.setText(receipt.getValue(Receipt.class).getCustomerName());

            seller_mail = receipt.getValue(Receipt.class).getSeller_mail();
            customer_mail = receipt.getValue(Receipt.class).getCustomer_mail();

            String address = receipt.getValue(Receipt.class).getAddress();
            String city = receipt.getValue(Receipt.class).getCity();
            int zipcode = receipt.getValue(Receipt.class).getZipCode();
            String fullAddress = city + "," + address + "-" + zipcode;

            fullAddress_et.setText(fullAddress);
            SaleDate_et.setText(AlgoLibrary.DateFormating(receipt.getValue(Receipt.class).getSellDate()));

        }
        processbar.setVisibility(View.GONE);
    }

}