package com.example.grocerystore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ForgotPasswordActivity extends AppCompatActivity {

        private ImageButton backBtn;
        private EditText emailEt;
        private Button recoverBtn;

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
    }
    private void bindingAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}