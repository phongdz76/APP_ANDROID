package com.example.doannam2.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doannam2.Adapter.MyAdapter;
import com.example.doannam2.Adapter.cartadapter;
import com.example.doannam2.R;
import com.example.doannam2.model.Cartdata;
import com.example.doannam2.model.dataclass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    // Khai báo các biến thành viên
    ImageView productImage;
    TextView productPrice,productName,totalAmountext,backCart;
    DatabaseReference databaseReference;
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
        setContentView(R.layout.activity_cart);  // Thiết lập giao diện
        addcontrol();
        mfunctions = FirebaseFunctions.getInstance();
        
        recyclerViewcart = findViewById(R.id.recyclerViewcart);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CartActivity.this,1);
        recyclerViewcart.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        cartdatalist = new ArrayList<>();
        Adapter = new cartadapter(CartActivity.this,cartdatalist,databaseReference);
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
                eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cartdatalist.clear();
                        Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dialog.dismiss();
                    }
                });
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
                Intent intent = new Intent(CartActivity.this,manhinhchinh.class);
                startActivity(intent);
            }
        });
    }


}