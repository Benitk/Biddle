package com.example.biddle;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private TextView tv_login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_login_btn = (TextView)findViewById(R.id.login_btn);

        tv_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_username = (EditText)findViewById(R.id.username);
                et_password = (EditText)findViewById(R.id.password);

                Toast.makeText(LoginActivity.this,
                        "username: " + et_username.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}