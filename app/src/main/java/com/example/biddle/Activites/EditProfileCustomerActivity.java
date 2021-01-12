package com.example.biddle.Activites;

import android.app.DatePickerDialog;
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

import java.util.Calendar;
import java.util.Date;

import Models.Customer;
import Utils.SetDate;

public class EditProfileCustomerActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference refUser;
    private ProgressBar progressb;
    private TextView card_tv, cvv_tv,edit_btn,PhoneNumber_tv,personalID_tv,Name_tv,email_tv;
    private EditText date_tv;
    private int CardNumber,cvvNumber;
    private String card,Name,PhoneNumber,personalID;
    private String userId;
    private SetDate setDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_customer);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        refUser = database.getReference().child("Users").child(userId).child("CustomerDetails");

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        progressb.setVisibility(View.GONE);

        card_tv  =(TextView)findViewById(R.id.cardnumber);
        cvv_tv  =(TextView)findViewById(R.id.cvv1);
        date_tv  =(EditText) findViewById(R.id.expireDate);
        edit_btn  =(TextView)findViewById(R.id.update_btn);
        setDate = new SetDate(date_tv);//need to check
        PhoneNumber_tv = (TextView)findViewById(R.id.PhoneNumberC);
        personalID_tv = (TextView)findViewById(R.id.personalIDC);
        Name_tv = (TextView)findViewById(R.id.NameC);
        email_tv = (TextView) findViewById(R.id.email);

        date_tv.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           if (view == date_tv) {
                                               Calendar systemCalender = Calendar.getInstance();
                                               int year = systemCalender.get(Calendar.YEAR);
                                               int month = systemCalender.get(Calendar.MONTH);
                                               int day = systemCalender.get(Calendar.DAY_OF_MONTH);
                                               DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileCustomerActivity.this, setDate, year, month, day);
                                               datePickerDialog.show();
                                           }
                                       }
                                   });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card = card_tv.getText().toString().trim();
                String cvV = cvv_tv.getText().toString().trim();
                Name = Name_tv.getText().toString().trim();
                personalID = personalID_tv.getText().toString().trim();
                PhoneNumber = PhoneNumber_tv.getText().toString().trim();

                //validation
                boolean flag = true;

                if (TextUtils.isEmpty(card)) {
                    ((EditText) findViewById(R.id.cardnumber)).setError(R.string.mustFill + "");
                    flag = false;
                    Toast.makeText(EditProfileCustomerActivity.this, "נכשל1", Toast.LENGTH_SHORT).show();
                } else {
                    CardNumber = Integer.parseInt(card);
                }

                if (TextUtils.isEmpty(cvV)) {
                    ((EditText) findViewById(R.id.cvv1)).setError(R.string.mustFill + "");
                    flag = false;
                    Toast.makeText(EditProfileCustomerActivity.this, "נכשל2", Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(PhoneNumber)) {
                    ((EditText) findViewById(R.id.PhoneNumberC)).setError("חובה למלא" + "");
                    flag = false;
                    Toast.makeText(EditProfileCustomerActivity.this, "נכשל3", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(personalID)) {
                    ((EditText) findViewById(R.id.personalIDC)).setError("חובה למלא" + "");
                    flag = false;
                    Toast.makeText(EditProfileCustomerActivity.this, "נכשל4", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(Name)) {
                    ((EditText) findViewById(R.id.NameC)).setError(R.string.mustFill + "");
                    flag = false;
                    Toast.makeText(EditProfileCustomerActivity.this, "נכשל5", Toast.LENGTH_SHORT).show();
                }
                if (cvV.length() != 3) {
                    ((EditText) findViewById(R.id.cvv1)).setError("הכנס שלוש ספרות");
                    flag = false;
                    Toast.makeText(EditProfileCustomerActivity.this, "נכשל6", Toast.LENGTH_SHORT).show();
                } else {
                    cvvNumber = Integer.parseInt(cvV);
                }

                if (TextUtils.isEmpty(date_tv.getText().toString().trim())) {
                    ((EditText) findViewById(R.id.cardnumber)).setError(R.string.mustFill + "");
                    flag = false;
                    Toast.makeText(EditProfileCustomerActivity.this, "נכשל7", Toast.LENGTH_SHORT).show();
                }

                if (flag) {
                    Date dateTime = new Date(setDate.getYear(), setDate.getMonth() - 1, setDate.getDay());
                    int year = setDate.getYear();// dateTime.getYear();
                    int month = setDate.getMonth();            //dateTime.getMonth();
                    ExpireDate ex = new ExpireDate(year, month);
                    EditText t = (EditText) findViewById(R.id.NameC);
                    String name = t.getText().toString().trim();
                    t = (EditText) findViewById(R.id.PhoneNumberC);
                    int phone = Integer.parseInt(t.getText().toString().trim());
                    t = (EditText) findViewById(R.id.personalIDC);
                    int id = Integer.parseInt(t.getText().toString().trim());
                    Customer costomer = new Customer(userId, CardNumber, cvvNumber, ex, Name, phone, id);
                    WriteToDB(costomer, refUser);
                } else {
                    Toast.makeText(EditProfileCustomerActivity.this, "נכשל7", Toast.LENGTH_SHORT).show();
                }
            }
            });
        }

    private void WriteToDB(Customer customer, DatabaseReference ref) {

        progressb.setVisibility(View.VISIBLE);

        ref.setValue(customer,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(EditProfileCustomerActivity.this, "עדכון נכשל", Toast.LENGTH_SHORT).show();
                }
                else {
                    finish();
                    Toast.makeText(EditProfileCustomerActivity.this, "פרטים עודכנו בהצלחה", Toast.LENGTH_SHORT).show();
                }
                progressb.setVisibility(View.GONE);

            }
        });
    }
}
