package com.example.doannam2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doannam2.Activity.CartActivity;
import com.example.doannam2.Activity.DetailActivity;
import com.example.doannam2.model.Cartdata;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class Detailphanquyen extends AppCompatActivity {


    StorageReference storageReference;
    Uri uri;
    DatabaseReference databaseReference;
    TextView detailDesc, detailTitle,detailLang;
    ImageView detailImage;
    Button addcart;
    String key ="";
    String imageUrl="";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailphanquyen);
        addcontrol();

        addcart = findViewById(R.id.addToCartButtonphanquyen);
        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDatecart();
                Intent intent = new Intent(Detailphanquyen.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }
    private void moveDatecart() {
        DatabaseReference sourceReference = FirebaseDatabase.getInstance().getReference("Android Products").child(key);

        sourceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String productName = dataSnapshot.child("dataTitle").getValue(String.class);
                    String productImage = dataSnapshot.child("dataimage").getValue(String.class);
                    int productPrice = dataSnapshot.child("datalang").getValue(Integer.class);

                    // Tạo đối tượng Cartdata
                    Cartdata cartdata = new Cartdata(productName, productPrice, productImage);

                    // Đưa dữ liệu từ nút "Android Products" lên nút "cart"
                    DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference("cart").child(key);
                    cartReference.setValue(cartdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Detailphanquyen.this, "Chuyển dữ liệu thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Detailphanquyen.this, "Chuyển dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Detailphanquyen.this, "Lỗi khi chuyển dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Detailphanquyen.this, "Không tìm thấy dữ liệu để chuyển", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Detailphanquyen.this, "Lỗi khi đọc dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addcontrol() {
        detailDesc = findViewById(R.id.detailDescphanquyen);
        detailImage = findViewById(R.id.detailImagephanquyen);
        detailTitle = findViewById(R.id.detailTitlephanquyen);
        detailLang = findViewById(R.id.detailLangphanquyen);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailLang.setText(bundle.getString("Language"));
            key = bundle.getString("key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
    }
}