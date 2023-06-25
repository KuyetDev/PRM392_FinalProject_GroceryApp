package com.example.grocerystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainSellerActivity extends AppCompatActivity {


    private TextView tvName, tvEmail, tvShopName;
    private ImageButton btnLogout, btnEditProfile, btnAddProduct;
    private ImageView ivProfile;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private void bindingView() {
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvShopName = findViewById(R.id.tvShopName);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnLogout = findViewById(R.id.btnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        ivProfile = findViewById(R.id.ivProfile);
    }

    private void bindingAction() {

        btnLogout.setOnClickListener(this::onBtnLogoutClick);
        btnEditProfile.setOnClickListener(this::onBtnEditProfileClick);
        btnAddProduct.setOnClickListener(this::onBtnAddProductClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);
        bindingView();
        bindingAction();

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Vui lòng đợi");
        progressDialog.setCanceledOnTouchOutside(false);
        checkUser();
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainSellerActivity.this, LoginActivity.class));
            finish();
        } else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //get data from db
                            String name = "" + ds.child("name").getValue();
                            String accountType = "" + ds.child("accountType").getValue();
                            String email = "" + ds.child("email").getValue();
                            String shopName = "" + ds.child("shopName").getValue();
                            String profileImage = "" + ds.child("profileImage").getValue();

                            //set data ti ui
                            tvName.setText(name);
                            tvEmail.setText(email);
                            tvShopName.setText(shopName);
                            try {
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_store_grey).into(ivProfile);
                            } catch (Exception e) {
                                ivProfile.setImageResource(R.drawable.ic_store_grey);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void onBtnAddProductClick(View view) {
        startActivity(new Intent(MainSellerActivity.this, AddProductActivity.class));
    }

    private void onBtnEditProfileClick(View view) {
        //open edit profile activity
        startActivity(new Intent(MainSellerActivity.this, ProfileEditSellerActivity.class));

    }

    private void onBtnLogoutClick(View view) {
        // make offline
        //sign out
        //go to login activity
        makeMeOffline();

    }

    private void makeMeOffline() {
        //after logging in, make user online
        progressDialog.setTitle("Đang đăng xuất...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // update successfully
                        firebaseAuth.signOut();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // failed updating
                        progressDialog.dismiss();
                        Toast.makeText(MainSellerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}