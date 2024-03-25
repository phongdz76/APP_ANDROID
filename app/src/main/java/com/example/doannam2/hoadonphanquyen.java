package com.example.doannam2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannam2.Activity.manhinhchinh;
import com.example.doannam2.Activity.phanquyen;
import com.example.doannam2.Adapter.AdapterHoadon;
import com.example.doannam2.Adapter.AdapterHoadonphanquyen;
import com.example.doannam2.model.Cartdata;
import com.example.doannam2.model.hoadondata;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class hoadonphanquyen extends AppCompatActivity {
    TextView tenkhachhang,emailkhachhang,sdtkhachhang,tienhoadon,diachi,backshop,makhachhang;
    DatabaseReference databaseReference;
    Button xacnhan;
    RecyclerView hoadonsanpham;
    int totalAmount;
    ValueEventListener eventListener;
    List<Cartdata> cartdatalist;
    AdapterHoadonphanquyen Adapter;
    Task<Void> firebaseDatabase;
    String key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadonphanquyen);
        addcontrol();
        String UId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
        {
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        String uid = user.getUid();

        if(name == null){
            tenkhachhang.setVisibility(View.GONE);
        }else {
            tenkhachhang.setVisibility(View.VISIBLE);
            tenkhachhang.setText(name);
        }
        makhachhang.setText(uid);
        emailkhachhang.setText(email);
        backshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(hoadonphanquyen.this, phanquyen.class);
                startActivity(intent);
            }
        });
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            sdtkhachhang.setText(bundle.getString("Phone_number"));
            diachi.setText(bundle.getString("diachi"));
        }
        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailuser = emailkhachhang.getText().toString();
                String nameuser = tenkhachhang.getText().toString();

                hoadondata hoadon1 = new hoadondata(emailuser,nameuser);
                firebaseDatabase = FirebaseDatabase.getInstance().getReference("hoadon").child(uid)
                        .setValue(hoadon1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(hoadonphanquyen.this, phanquyen.class);
                                startActivity(intent);
                            }
                        });
            }
        });
    }


    private void addcontrol() {
        xacnhan = findViewById(R.id.xacnhanhoadon);
        makhachhang=findViewById(R.id.makhachhang);
        diachi=findViewById(R.id.diachi);
        tenkhachhang = findViewById(R.id.tenkhachhang);
        emailkhachhang = findViewById(R.id.emailkhachhang);
        sdtkhachhang = findViewById(R.id.sdtkhachhang);
        hoadonsanpham = findViewById(R.id.hoadonsanpham);
        tienhoadon=findViewById(R.id.tienhoadon);
        backshop=findViewById(R.id.backshop);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(hoadonphanquyen.this,1);
        hoadonsanpham.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(hoadonphanquyen.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        cartdatalist = new ArrayList<>();
        Adapter = new AdapterHoadonphanquyen(hoadonphanquyen.this,cartdatalist,databaseReference);
        hoadonsanpham.setAdapter(Adapter);

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
                tienhoadon.setText(String.valueOf(totalAmount));
                Adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });


    }
}