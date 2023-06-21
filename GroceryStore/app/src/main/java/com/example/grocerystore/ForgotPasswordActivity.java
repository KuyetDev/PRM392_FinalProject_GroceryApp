package com.example.grocerystore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ForgotPasswordActivity extends AppCompatActivity {

        private ImageButton backBtn;
        private EditText emailEt;
        private Button recoverBtn;
        private TextView noAccountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        bindingView();
        bindingAction();
    }

    private void bindingView() {
        backBtn =(ImageButton) findViewById(R.id.backBtn);
        emailEt =(EditText) findViewById(R.id.emailEt);
        recoverBtn =(Button) findViewById(R.id.recoverBtn);
        noAccountTv = (TextView) findViewById(R.id.noAccountTv);
    }
    private void bindingAction() {
        backBtn.setOnClickListener(this:: onBackBtnClick);
        noAccountTv.setOnClickListener(this:: onNoAccountTvClick);
    }
    private void onBackBtnClick(View view) {
        onBackPressed();
    }
    private void onNoAccountTvClick(View view) {
        startActivity(new Intent(ForgotPasswordActivity.this, RegisterUserActivity.class));

    }
}