package com.example.grocerystore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    //UI views
    private EditText emailEt, passwordEt;
    private TextView forgotTv, noAccountTv;
    private Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindingView();
        bindingAction();
    }

    private void bindingView(){
        //init UI views
        emailEt = (EditText) findViewById(R.id.emailEt);
        passwordEt = (EditText) findViewById(R.id.passwordEt);
        forgotTv = (TextView) findViewById(R.id.forgotTv);
        noAccountTv = (TextView) findViewById(R.id.noAccountTv);
        loginBtn = (Button) findViewById(R.id.loginBtn);
    }

    private void bindingAction() {
        noAccountTv.setOnClickListener(this:: onNoAccountTvClick);
        forgotTv.setOnClickListener(this:: onForgotTvClick);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (user.equals(login.getText().toString())  && pass.equals(passW.getText().toString()) )
               //     Toast.makeText(MainActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
               // else Toast.makeText(MainActivity.this, "Login Fail!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onForgotTvClick(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

    }

    private void onNoAccountTvClick(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));

    }
}