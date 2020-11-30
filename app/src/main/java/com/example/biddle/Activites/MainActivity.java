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

import Utils.InputValidator;

public class MainActivity extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;
    private TextView tv_login_btn;
    private TextView tv_signup_btn;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        tv_signup_btn = (TextView)findViewById(R.id.signup_btn);
        tv_login_btn = (TextView)findViewById(R.id.login_btn);

        progressb.setVisibility(View.GONE);


        firebaseAuth = FirebaseAuth.getInstance();

        // if already logged in
        if(firebaseAuth.getCurrentUser() != null)
            startActivity(new Intent(MainActivity.this, LandingPageActivity.class));



        /* login button listener - on click getting user input
         * call validate function to confirm valid input
         * check if exist in firebase
         */

        tv_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressb.setVisibility(View.VISIBLE);


                et_email = (EditText)findViewById(R.id.email);
                et_password = (EditText)findViewById(R.id.password);
                String user_email = et_email.getText().toString().trim();
                String user_password = et_password.getText().toString().trim();

                InputValidator validator = new InputValidator();

                if(!validator.isValidEmail(user_email))
                    et_email.setError("מייל בפורמט לא תקין.");

                if(!validator.isValidPassword(user_password))
                    et_password.setError("סיסמא שגויה, נסה שנית");

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



}