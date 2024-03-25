package com.example.doannam2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doannam2.R;
import com.example.doannam2.model.Cartdata;
import com.example.doannam2.model.dataclass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DetailActivity extends AppCompatActivity {

    ImageView productImage;
    String productName;
    int productPrice;


    StorageReference storageReference;
    Uri uri;
    DatabaseReference databaseReference;
    TextView detailDesc, detailTitle,detailLang;
    ImageView detailImage;
    Button addcart;
    com.google.android.material.floatingactionbutton.FloatingActionButton deleteButton,editButton;
    String key ="";
    String imageUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        addcontrol();

        addcart = findViewById(R.id.addToCartButton);
        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDatecart();
                Intent intent = new Intent(DetailActivity.this,CartActivity.class);
                intent.putExtra("Title",detailTitle.getText().toString());
                intent.putExtra("Description",detailDesc.getText().toString());
                intent.putExtra("Language",detailLang.getText().toString());
                intent.putExtra("Image",imageUrl);
                intent.putExtra("key",key);
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
                                Toast.makeText(DetailActivity.this, "Chuyển dữ liệu thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailActivity.this, "Chuyển dữ liệu thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailActivity.this, "Lỗi khi chuyển dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(DetailActivity.this, "Không tìm thấy dữ liệu để chuyển", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailActivity.this, "Lỗi khi đọc dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addcontrol() {
        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        detailLang = findViewById(R.id.detailLang);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailLang.setText(bundle.getString("Language"));
            key = bundle.getString("key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Android Products").child(key);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Products");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(DetailActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), manhinhchinh.class));
                        finish();
                    }
                });
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, UpdataActivity.class)
                        .putExtra("Title",detailTitle.getText().toString())
                        .putExtra("Description",detailDesc.getText().toString())
                        .putExtra("Language",detailLang.getText().toString())
                        .putExtra("Image",imageUrl)
                        .putExtra("key",key);
                startActivity(intent);
            }
        });
    }
}