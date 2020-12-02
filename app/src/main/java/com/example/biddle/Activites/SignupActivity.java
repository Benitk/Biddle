package com.example.biddle.Activites;

import android.content.Intent;
import android.os.Bundle;

import com.example.biddle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import Utils.InputValidator;

public class SignupActivity extends AppCompatActivity {


    private ProgressBar progressb;
    private TextView tv_login_btn;
    private TextView tv_signup_btn;
    private EditText et_email;
    private EditText et_password;
    private EditText et_confirmPassword;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();

        // if already logged in
        if(firebaseAuth.getCurrentUser() != null)
            finish();

        progressb = (ProgressBar)findViewById(R.id.progressBar);
        tv_signup_btn = (TextView)findViewById(R.id.signup_btn);
        tv_login_btn = (TextView)findViewById(R.id.to_loginPage_Btn);

        progressb.setVisibility(View.GONE);

        /* sign up button listener - on click getting user input
         * call validate function to confirm valid input
         *send it to fire base
         */
        tv_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_email = (EditText)findViewById(R.id.email);
                et_password = (EditText)findViewById(R.id.password);
                et_confirmPassword = (EditText)findViewById(R.id.confirm_password);
                String user_email = et_email.getText().toString().trim();
                String user_password = et_password.getText().toString().trim();
                String user_confirmPassword = et_confirmPassword.getText().toString().trim();
                InputValidator validator = new InputValidator();

                if(!validator.isValidPassword(user_password))
                    et_password.setError("סיסמה לא תקינה, נסה להצמד להוראות הבאות: " +
                            "לפחות אות אחת קטנה [a-z] " +
                            "לפחות אות אחת גדולה [A-Z] " +
                            "מינימום אורך 8 תווים, מקסימום 20 תווים ");


                if(!validator.isEqual(user_password, user_confirmPassword))
                    et_confirmPassword.setError("הסיסמה לא תואמת לקודמת.");

                if(!validator.isValidEmail(user_email))
                    et_email.setError("מייל בפורמט לא תקין.");

                else {

                    progressb.setVisibility(View.VISIBLE);
                    // input is valid send in to firebase
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "ההרשמה בוצעה בהצלחה!", Toast.LENGTH_SHORT).show();
                                progressb.setVisibility(View.GONE);

                                // move to login
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "ההרשמה נכשלה", Toast.LENGTH_SHORT).show();
                                progressb.setVisibility(View.GONE);

                            }

                        }
                    });
                }
            }
        });

        /* login button listener - on click return to login page
         */

        tv_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}