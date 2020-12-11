package com.example.biddle.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biddle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Utils.GMailSender;
import Utils.InputValidator;
import android.os.AsyncTask;

import android.os.Build;

import android.os.Bundle;

import android.os.StrictMode;

import android.annotation.SuppressLint;

import android.annotation.TargetApi;

import android.app.Activity;

import android.app.ProgressDialog;

import android.view.Menu;

import android.view.View;

import android.view.View.OnClickListener;

import android.widget.Button;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;
    private TextView tv_login_btn;
    private TextView tv_signup_btn;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressb;

    Button button;

    GMailSender sender;

    @SuppressLint("NewApi")

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sender = new GMailSender("Biddle2021@gmail.com", "2021bidd");
        progressb = (ProgressBar)findViewById(R.id.progressBar);
        tv_signup_btn = (TextView)findViewById(R.id.signup_btn);
        tv_login_btn = (TextView)findViewById(R.id.login_btn);

        progressb.setVisibility(View.GONE);

       // button = (Button) findViewById(R.id.mybtn);

        firebaseAuth = FirebaseAuth.getInstance();

        // if already logged in
        if(firebaseAuth.getCurrentUser() != null)
            startActivity(new Intent(MainActivity.this, LandingPageActivity.class));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    new MyAsyncClass().execute();
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* login button listener - on click getting user input
         * call validate function to confirm valid input
         * check if exist in firebase
         */

        tv_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_email = (EditText)findViewById(R.id.email);
                et_password = (EditText)findViewById(R.id.password);
                String user_email = et_email.getText().toString().trim();
                String user_password = et_password.getText().toString().trim();

                InputValidator validator = new InputValidator();
                boolean flag = true;

                if(!validator.isValidEmail(user_email)) {
                    et_email.setError("מייל בפורמט לא תקין.");
                    flag = false;
                }

                if(!validator.isValidPassword(user_password)) {
                    et_password.setError("סיסמא שגויה, נסה שנית");
                    flag = false;
                }

                if(flag)
                    LoginDB(user_email, user_password);

            }
        });

        /* signup button listener - on click return to login page
         */

        tv_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });

    }

    private void LoginDB(String user_email, String user_password){
        progressb.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this, "הכניסה הושלמה.", Toast.LENGTH_SHORT).show();
                    progressb.setVisibility(View.GONE);

                    startActivity(new Intent(MainActivity.this, LandingPageActivity.class));


                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MainActivity.this, "הכניסה נכשלה.", Toast.LENGTH_SHORT).show();
                    progressb.setVisibility(View.GONE);
                }
            }
        });
    }



    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it //     is present.
     //   getMenuInflater().inflate(R.menu.main, menu);
       return true;
    }

    class MyAsyncClass extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                sender.sendMail("Subject", " body", "from Mail", "to mail");
            }
            catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            Toast.makeText(getApplicationContext(), "Email send", Toast.LENGTH_SHORT).show();
        }
    }
}
