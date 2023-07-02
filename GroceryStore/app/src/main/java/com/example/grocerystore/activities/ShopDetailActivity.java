package com.example.grocerystore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.Constants;
import com.example.grocerystore.R;
import com.example.grocerystore.adapters.AdapterProductUser;
import com.example.grocerystore.models.ModelProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopDetailActivity extends AppCompatActivity {

    //declare ui views
    private ImageView ivShop;
    private TextView tvShopName, tvPhone, tvEmail, tvOpenClose, tvDeliveryFee, tvAddress, tvFilteredProduct;
    private ImageButton btnCall, btnMap, btnCart, btnBack, btnFilterProduct;
    private EditText edtSearchProduct;
    private RecyclerView rvProducts;
    private String shopUid, myLatitude, myLongitude, shopLatitude, shopLongitude, shopName, shopEmail, shopPhone, shopAddress;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelProduct> productList;
    private AdapterProductUser adapterProductUser;

    private void bindingView() {
        ivShop = findViewById(R.id.ivShop);
        tvShopName = findViewById(R.id.tvShopName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvOpenClose = findViewById(R.id.tvOpenClose);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
        tvAddress = findViewById(R.id.tvAddress);
        tvFilteredProduct = findViewById(R.id.tvFilteredProduct);
        btnCall = findViewById(R.id.btnCall);
        btnMap = findViewById(R.id.btnMap);
        btnCart = findViewById(R.id.btnCart);
        btnBack = findViewById(R.id.btnBack);
        btnFilterProduct = findViewById(R.id.btnFilterProduct);
        edtSearchProduct = findViewById(R.id.edtSearchProduct);
        rvProducts = findViewById(R.id.rvProducts);

        //search
        edtSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterProductUser.getFilter().filter(s);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //get user data
                            String name = "" + ds.child("name").getValue();
                            String email = "" + ds.child("email").getValue();
                            String phone = "" + ds.child("phone").getValue();
                            String profileImage = "" + ds.child("profileImage").getValue();
                            String accountType = "" + ds.child("accountType").getValue();
                            String city = "" + ds.child("city").getValue();
                            myLatitude = "" + ds.child("latitude").getValue();
                            myLongitude = "" + ds.child("longitude").getValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadShopDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get shop data
                String name = "" + snapshot.child("name").getValue();
                shopName = "" + snapshot.child("shopName").getValue();
                shopEmail = "" + snapshot.child("email").getValue();
                shopPhone = "" + snapshot.child("phone").getValue();
                shopAddress = "" + snapshot.child("address").getValue();
                shopLatitude = "" + snapshot.child("latitude").getValue();
                shopLongitude = "" + snapshot.child("longitude").getValue();
                String deliveryFee = "" + snapshot.child("deliveryFee").getValue();
                String profileImage = "" + snapshot.child("profileImage").getValue();
                String shopOpen = "" + snapshot.child("shopOpen").getValue();

                //set data
                tvShopName.setText(shopName);
                tvEmail.setText(shopEmail);
                tvDeliveryFee.setText("Delivery Fee: $" + deliveryFee);
                tvAddress.setText(shopAddress);
                tvPhone.setText(shopPhone);
                if (shopOpen.equals("true")) {
                    tvOpenClose.setText("Open");
                } else {
                    tvOpenClose.setText("Closed");
                }
                try {
                    Picasso.get().load(profileImage).into(ivShop);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadShopProduct() {
        //init list
        productList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid).child("Product")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //clear list before adding items
                        productList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                            productList.add(modelProduct);
                        }

                        //setup adapter
                        adapterProductUser = new AdapterProductUser(ShopDetailActivity.this, productList);

                        //set adapter
                        rvProducts.setAdapter(adapterProductUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void bindingAction() {
        btnBack.setOnClickListener(this::onBtnBackClick);
        btnCart.setOnClickListener(this::onBtnCartClick);
        btnCall.setOnClickListener(this::onBtnCallClick);
        btnMap.setOnClickListener(this::onBtnMapClick);
        btnFilterProduct.setOnClickListener(this::onBtnFilterProductClick);
    }

    private void onBtnFilterProductClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetailActivity.this);
        builder.setTitle("Chọn danh mục sản phẩm: ")
                .setItems(Constants.productCategory1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedCategory = Constants.productCategory1[which];
                        tvFilteredProduct.setText(selectedCategory);
                        if (selectedCategory.equals("All")) {
                            loadShopProduct();
                        } else {
                            adapterProductUser.getFilter().filter(selectedCategory);
                        }
                    }
                }).show();
    }

    private void onBtnMapClick(View view) {
        openMap();
    }

    private void openMap() {
        String address = "https://maps.google.com/maps?saddr=" + myLatitude + "," + myLongitude + "&daddr=" + shopLatitude + "," + shopLongitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
        startActivity(intent);
    }

    private void onBtnCallClick(View view) {
        dialPhone();
    }

    private void dialPhone() {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(shopPhone))));
        Toast.makeText(this, "" + shopPhone, Toast.LENGTH_LONG).show();
    }

    private void onBtnCartClick(View view) {

    }

    private void onBtnBackClick(View view) {
        //go previous activity
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        //init ui views
        bindingView();
        bindingAction();

        //get uid of the shop from intent
        shopUid = getIntent().getStringExtra("shopUid");
        firebaseAuth = FirebaseAuth.getInstance();
        loadMyInfo();
        loadShopDetails();
        loadShopProduct();
    }
}