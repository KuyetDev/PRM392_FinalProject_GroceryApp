package com.example.grocerystore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.Constants;
import com.example.grocerystore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat fcmSwtich;
    private TextView noticationStatusTv;
    private ImageButton backBtn;
    private static final String enabledMessage = "Notifications are enabled";
    private static final String disabledMessage = "Notifications are disabled";
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private boolean isChecked =false;
    private void bindingView(){
        fcmSwtich = findViewById(R.id.fcmSwitch);
        noticationStatusTv = findViewById(R.id.notificationStatusTv);
        backBtn = findViewById(R.id.backBtn);
    }

    private void bindingAction(){
        backBtn.setOnClickListener(this::onBackBtnClick);
        fcmSwtich.setOnCheckedChangeListener(this::onFcmSwitchChanged);
    }

    private void onFcmSwitchChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked){
            //checked - enable notification
            subscribeToTopic();
        }else {
            unSubscribeToTopic();
        }
    }

    private void onBackBtnClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        bindingView();
        bindingAction();
        checkedSelected();
        }

    private void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        spEditor = sharedPreferences.edit();
                        spEditor.putBoolean("FCM_ENABLE",true);
                        spEditor.apply();
                        Toast.makeText(SettingsActivity.this,""+ enabledMessage,Toast.LENGTH_SHORT).show();
                        noticationStatusTv.setText(enabledMessage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void unSubscribeToTopic(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FCM_TOPIC)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        spEditor = sharedPreferences.edit();
                        spEditor.putBoolean("FCM_ENABLE",false);
                        spEditor.apply();
                        Toast.makeText(SettingsActivity.this,""+ disabledMessage,Toast.LENGTH_SHORT).show();
                        noticationStatusTv.setText(disabledMessage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void checkedSelected(){
        sharedPreferences= getSharedPreferences("SETTING_SP",MODE_PRIVATE);
        isChecked = sharedPreferences.getBoolean("FCM_ENABLED",false);
        fcmSwtich.setChecked(isChecked);
        if (isChecked){
            noticationStatusTv.setText(enabledMessage);
        }else noticationStatusTv.setText(disabledMessage);
    }
}
