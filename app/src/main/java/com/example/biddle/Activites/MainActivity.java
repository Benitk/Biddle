package com.example.biddle.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biddle.R;

import Utils.InputValidator;

public class MainActivity extends AppCompatActivity {
    private EditText et_email;
    private EditText et_password;
    private TextView tv_login_btn;
    private TextView tv_signup_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_signup_btn = (TextView)findViewById(R.id.signup_btn);
        tv_login_btn = (TextView)findViewById(R.id.login_btn);


        /* login button listener - on click getting user input
         * call validate function to confirm valid input
         * check if exist in firebase
         */

        tv_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_email = (EditText)findViewById(R.id.email);
                et_password = (EditText)findViewById(R.id.password);

                InputValidator validator = new InputValidator();

                if(!validator.isValidEmail(et_email.getText().toString()))
                    et_email.setError("מייל בפורמט לא תקין.");

                if(!validator.isValidPassword(et_password.getText().toString()))
                    et_password.setError("סיסמא שגויה, נסה שנית");


                // input is valid check if exist in firebase


//                Toast.makeText(MainActivity.this,
//                        "username: " + et_email.getText().toString(), Toast.LENGTH_SHORT).show();




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