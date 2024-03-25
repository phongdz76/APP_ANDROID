package com.example.doannam2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doannam2.Activity.CartActivity;
import com.example.doannam2.Activity.manhinhchinh;
import com.example.doannam2.Activity.phanquyen;
import com.example.doannam2.Activity.phonenumber;
import com.example.doannam2.Adapter.cartadapter;
import com.example.doannam2.model.Cartdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.List;

public class CartPhanquyen extends AppCompatActivity {
    ImageView productImage;
    TextView productPrice,productName,totalAmountext,backCart;
    DatabaseReference databaseReference,databaseReference1;
    RecyclerView recyclerViewcart;
    int totalAmount;

    List<Cartdata> cartdatalist;
    ValueEventListener eventListener,eventListener1;

    Button checkout;

    FirebaseFunctions mfunctions;
    cartadapter Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_phanquyen);
        addcontrol();
        mfunctions = FirebaseFunctions.getInstance();

        recyclerViewcart = findViewById(R.id.recyclerViewcart);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CartPhanquyen.this,1);
        recyclerViewcart.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(CartPhanquyen.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        cartdatalist = new ArrayList<>();
        Adapter = new cartadapter(CartPhanquyen.this,cartdatalist,databaseReference);
        recyclerViewcart.setAdapter(Adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("cart");
        dialog.show();


        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartdatalist.clear();
                totalAmount = 0;
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Cartdata cartdata = itemSnapshot.getValue(Cartdata.class);
                    totalAmount += cartdata.getTotalPrice();
                    cartdata.setKey(itemSnapshot.getKey());
                    cartdatalist.add(cartdata);
                }
                totalAmountext.setText(String.valueOf(totalAmount));
                Adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartPhanquyen.this, hoadonphanquyen.class);
                startActivity(intent);
            }
        });
    }
    private void addcontrol() {
        totalAmountext= findViewById(R.id.totalprice);
        productImage=findViewById(R.id.productImage);
        productPrice=findViewById(R.id.productPrice);
        productName=findViewById(R.id.productName);
        checkout = findViewById(R.id.checkout);
        backCart = findViewById(R.id.backCart);
        backCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartPhanquyen.this, phanquyen.class);
                startActivity(intent);
            }
        });
    }
}