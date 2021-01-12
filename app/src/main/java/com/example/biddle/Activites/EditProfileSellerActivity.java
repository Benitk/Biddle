package com.example.biddle.Activites;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.biddle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.Seller;

public class EditProfileSellerActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference refUser;

    private TextView City_tv,Adress_tv,zip_tv, Bank_tv, Branch_tv,Acount_tv,Update_btn,PhoneNumber_tv,personalID_tv,Name_tv;
    private String City,Adress,zip, Bank, Branch,Acount,Name,PhoneNumber,personalID;
    private  int ZipNumber,AcountNumber;
    private String userId;
    private ProgressBar progressb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_seller);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        refUser = database.getReference().child("Users").child(userId).child("sellerDetails");

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);

        City_tv  =(TextView)findViewById(R.id.cityname);
        Adress_tv  =(TextView)findViewById(R.id.adress);
        zip_tv  =(TextView)findViewById(R.id.zipcode);
        Bank_tv  =(TextView)findViewById(R.id.bankName);
        Branch_tv  =(TextView)findViewById(R.id.branch);
        Acount_tv  =(TextView)findViewById(R.id.bankAcount);
        Update_btn  =(TextView)findViewById(R.id.update_btn);
        PhoneNumber_tv = (TextView)findViewById(R.id.PhoneNumber);
        personalID_tv = (TextView)findViewById(R.id.personalID);
        Name_tv = (TextView)findViewById(R.id.Name);

        Update_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                City = City_tv.getText().toString().trim();
                Adress=  Adress_tv.getText().toString().trim();
                zip =zip_tv.getText().toString().trim();
                Bank = Bank_tv.getText().toString().trim();
                Branch = Branch_tv.getText().toString().trim();
                Acount =  Acount_tv.getText().toString().trim();
                Name = Name_tv.getText().toString().trim();
                personalID = personalID_tv.getText().toString().trim();
                PhoneNumber = PhoneNumber_tv.getText().toString().trim();

                // validtate input
                boolean flag = true;

                if(TextUtils.isEmpty(City)) {
                    ((EditText) findViewById(R.id.cityname)).setError(R.string.mustFill+"");
                    flag = false;

                }
                if(TextUtils.isEmpty(Adress)) {
                    ((EditText) findViewById(R.id.adress)).setError(R.string.mustFill+"");
                    flag = false;

                }
                if(TextUtils.isEmpty(zip)) {
                    ((EditText) findViewById(R.id.zipcode)).setError(R.string.mustFill+"");
                    flag = false;

                }
                else {
                    ZipNumber = Integer.parseInt(zip);
                }
                if(TextUtils.isEmpty(Bank)) {
                    ((EditText) findViewById(R.id.bankName)).setError(R.string.mustFill + "");
                    flag = false;
                }
                if(TextUtils.isEmpty(Branch)) {
                    ((EditText) findViewById(R.id.branch)).setError(R.string.mustFill+"");
                    flag = false;

                }
                if(TextUtils.isEmpty(PhoneNumber)) {
                    ((EditText) findViewById(R.id.PhoneNumber)).setError(R.string.mustFill+"");
                    flag = false;
                }
                if(TextUtils.isEmpty(personalID)) {
                    ((EditText) findViewById(R.id.personalID)).setError(R.string.mustFill+"");
                    flag = false;
                }
                if(TextUtils.isEmpty(Name)) {
                    ((EditText) findViewById(R.id.Name)).setError(R.string.mustFill+"");
                    flag = false;
                }
                if(TextUtils.isEmpty(Acount)) {
                    ((EditText) findViewById(R.id.bankAcount)).setError(R.string.mustFill+"");
                    flag = false;
                }
                else {
                    AcountNumber = Integer.parseInt(Acount);
                }

                if(flag) {
                    Seller seller = new Seller( userId,  City,  Adress, Bank, Branch, ZipNumber, AcountNumber,Name,
                            Integer.parseInt(PhoneNumber_tv.getText().toString().trim()),Integer.parseInt(personalID_tv.getText().toString().trim()));
                    WriteToDB(seller,refUser);
                }
                else{
                    Toast.makeText(EditProfileSellerActivity.this, "נכשל", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void WriteToDB(Seller seller,DatabaseReference ref) {

        progressb.setVisibility(View.VISIBLE);

        ref.setValue(seller,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(EditProfileSellerActivity.this, "העדכון נכשל", Toast.LENGTH_SHORT).show();
                }
                else {
                    finish();
                    Toast.makeText(EditProfileSellerActivity.this, "הפרטים עודכנו בהצלחה", Toast.LENGTH_SHORT).show();
                }
                progressb.setVisibility(View.GONE);
            }
        });
    }
}
