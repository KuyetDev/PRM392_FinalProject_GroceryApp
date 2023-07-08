package com.example.grocerystore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grocerystore.R;
import com.example.grocerystore.adapters.AdapterOrderedItem;
import com.example.grocerystore.models.ModelOrderedItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OrderDetailsActivity extends AppCompatActivity {

    private ImageButton backBtn, editBtn;
    private TextView orderIdTv, emailTv, phoneTv, totalItemsTv, amountTv, addressTv,dateTv,orderStatusTv;
    private RecyclerView itemsRv;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelOrderedItem> orderedItemArrayList;
    private AdapterOrderedItem adapterOrderedItem;
    String orderId;
    String orderBy;

    private void bindingView(){
        backBtn = findViewById(R.id.backBtn);
        editBtn = findViewById(R.id.editBtn);
        orderIdTv = findViewById(R.id.orderIdTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        totalItemsTv = findViewById(R.id.totalItemsTv);
        amountTv = findViewById(R.id.amountTv);
        addressTv = findViewById(R.id.addressTv);
        dateTv = findViewById(R.id.dateTv);
        itemsRv = findViewById(R.id.itemsRv);
        orderStatusTv = findViewById(R.id.orderStatusTv);
    }
    
    private void bindingAction(){
        backBtn.setOnClickListener(this::onBackBtnClicked);
        editBtn.setOnClickListener(this::onEditBtnClicked);
    }

    private void onEditBtnClicked(View view) {
        editOrderStatusDialog();
    }

    private void editOrderStatusDialog() {
        String[] options = {"In Progress", "Completed","Cancelled"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Order Status").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String seleted = options[which];
                editOrderStatus(seleted);
            }
        }).show();
    }

    private void editOrderStatus(String seleted) {
        //setup data to put in firebase
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderStatus",""+seleted);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Orders").child(orderId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(OrderDetailsActivity.this, "Order is now "+seleted,Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderDetailsActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onBackBtnClicked(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        firebaseAuth = FirebaseAuth.getInstance();
        receivingIntent();
        bindingView();
        bindingAction();
        loadMyInfo();
        loadBuyerInfo();
        loadOrderDetails();
        loadOrderedItems();
    }

    private void loadOrderedItems() {
        orderedItemArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Orders").child("orderId").child("Items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderedItemArrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ModelOrderedItem modelOrderedItem = ds.getValue(ModelOrderedItem.class);
                            orderedItemArrayList.add(modelOrderedItem);
                        }
                        adapterOrderedItem = new AdapterOrderedItem(OrderDetailsActivity.this, orderedItemArrayList);
                        itemsRv.setAdapter(adapterOrderedItem);
                        //totla number of items
                        totalItemsTv.setText(""+snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadOrderDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Orders").child(orderId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String orderBy = "" +snapshot.child("orderBy").getValue();
                String orderCost = "" +snapshot.child("orderCost").getValue();
                String orderId = "" +snapshot.child("orderId").getValue();
                String orderStatus = "" +snapshot.child("orderStatus").getValue();
                String orderTime = "" +snapshot.child("orderTime").getValue();
                String orderTo = "" +snapshot.child("orderTo").getValue();
                String deliveryFee = "" +snapshot.child("deliveryFee").getValue();
              /*  String orderStatus = "" +snapshot.child("orderStatus").getValue();
                String orderStatus = "" +snapshot.child("orderStatus").getValue();*/

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(orderTime));
                String dataFormated = DateFormat.format( "dd/MM/yyyy", calendar).toString();

                if(orderStatus.equals("In Progress")){
                    orderStatusTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                }else if (orderStatus.equals("Completed")){
                    orderStatusTv.setTextColor(getResources().getColor(R.color.colorGreen));
                }else if (orderStatus.equals("Cancelled")){
                    orderStatusTv.setTextColor(getResources().getColor(R.color.colorRed));
                }

                orderIdTv.setText(orderId);
                orderStatusTv.setText(orderStatus);
                amountTv.setText("$"+ orderCost+"[Including delivery fee $"+ deliveryFee +"]");
                dateTv.setText(dataFormated);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadBuyerInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(orderBy).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = ""+ snapshot.child("email").getValue();
                String phone = ""+ snapshot.child("phone").getValue();
                emailTv.setText(email);
                phoneTv.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    
    private void receivingIntent(){
        Intent i = getIntent();
        orderId = i.getStringExtra("orderId");
        orderBy = i.getStringExtra("orderBy");
    }
}