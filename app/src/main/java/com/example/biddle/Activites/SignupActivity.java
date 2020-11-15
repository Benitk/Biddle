package com.example.biddle.Activites;

import android.content.Intent;
import android.os.Bundle;

import com.example.biddle.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import Utils.InputValidator;

public class SignupActivity extends AppCompatActivity {

    private TextView tv_login_btn;
    private TextView tv_signup_btn;
    private EditText et_email;
    private EditText et_password;
    private EditText et_confirmPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        tv_signup_btn = (TextView)findViewById(R.id.signup_btn);
        tv_login_btn = (TextView)findViewById(R.id.to_loginPage_Btn);


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

                InputValidator validator = new InputValidator();

                if(!validator.isValidEmail(et_email.getText().toString()))
                    et_email.setError("מייל בפורמט לא תקין.");

                if(!validator.isValidPassword(et_password.getText().toString()))
                    et_password.setError("סיסמה לא תקינה, נסה להצמד להוראות הבאות: " +
                                         "לפחות אות אחת קטנה [a-z] " +
                                         "לפחות אות אחת גדולה [A-Z] " +
                                         "מינימום אורך 8 תווים, מקסימום 20 תווים ");


                if(!validator.isEqual(et_password.getText().toString(), et_confirmPassword.getText().toString()))
                    et_confirmPassword.setError("הסיסמה לא תואמת לקודמת.");


                // input is valid send in to firebase

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