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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Models.Seller;

public class EditProfileSellerActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    //    private FirebaseStorage storage;
//    private StorageReference storageref;
//private DatabaseReference refProduct;
    private DatabaseReference refUser;

    private TextView City_tv,Adress_tv,zip_tv, Bank_tv, Branch_tv,Acount_tv,Update_btn;
    private String City,Adress,zip, Bank, Branch,Acount;
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

        Update_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                City = City_tv.getText().toString().trim();
                Adress=  Adress_tv.getText().toString().trim();
                zip =zip_tv.getText().toString().trim();
                Bank = Bank_tv.getText().toString().trim();
                Branch = Branch_tv.getText().toString().trim();
                Acount =  Acount_tv.getText().toString().trim();

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
                    ((EditText) findViewById(R.id.bankName)).setError(R.string.mustFill+"");
                    flag = false;
                }
                if(TextUtils.isEmpty(Branch)) {
                    ((EditText) findViewById(R.id.branch)).setError(R.string.mustFill+"");
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
                    Seller seller = new Seller( userId,  City,  Adress, Bank, Branch, ZipNumber, AcountNumber);
                    WriteToDB(seller,refUser);
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
                    progressb.setVisibility(View.GONE);
                    Toast.makeText(EditProfileSellerActivity.this, "עדכון נכשל", Toast.LENGTH_SHORT).show();
                }
                else finish();
            }
        });
    }


}
