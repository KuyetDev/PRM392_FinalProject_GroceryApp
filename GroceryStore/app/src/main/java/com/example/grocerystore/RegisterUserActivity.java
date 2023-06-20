package com.example.grocerystore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterUserActivity extends AppCompatActivity {

    private ImageButton backBtn, gpsBtn;
    private ImageView profileIv;
    private EditText nameEt, phoneEt, countryEt, stateEt, cityEt, addressEt,
                    emailEt, passwordEt, cPasswordEt;
    private Button registerBtn;
    private TextView registerSellerTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        bindingView();
        bindingAction();
    }
    private void bindingView() {
        backBtn = (ImageButton) findViewById(R.id.backBtn);
        gpsBtn = (ImageButton) findViewById(R.id.gpsBtn);
        profileIv = (ImageView) findViewById(R.id.profileIv);
        nameEt = (EditText) findViewById(R.id.nameEt);
        phoneEt = (EditText) findViewById(R.id.phoneEt);
        countryEt = (EditText) findViewById(R.id.countryEt);
        stateEt = (EditText) findViewById(R.id.stateEt);
        cityEt = (EditText) findViewById(R.id.cityEt);
        addressEt = (EditText) findViewById(R.id.addressEt);
        emailEt = (EditText) findViewById(R.id.emailEt);
        passwordEt = (EditText) findViewById(R.id.passwordEt);
        cPasswordEt = (EditText) findViewById(R.id.cPasswordEt);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerSellerTv = (TextView) findViewById(R.id.registerSellerTv);

    }
    private void bindingAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //detect current location
            }
        });
        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick image
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //register user
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open register seller activity
                startActivity(new Intent(RegisterUserActivity.this, RegisterSellerActivity.class));
            }
        });

}