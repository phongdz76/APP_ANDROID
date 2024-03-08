package com.example.doannam2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class onClickforgotpass extends AppCompatActivity {

    Button btnReset, btnBack;
    EditText edtEmailadress;
    ProgressBar progressBar;
    FirebaseAuth auth;
    String strEmail;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_clickforgotpass);

        btnReset = findViewById(R.id.btnResetPassword);
        btnBack = findViewById(R.id.btnpasswordback);
        edtEmailadress = findViewById(R.id.emailAdress);
        auth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 strEmail = edtEmailadress.getText().toString().trim();
                if(!TextUtils.isEmpty(strEmail)){
                    RestPassword();
                }else {
                    edtEmailadress.setError("Email field can't be empty");
                }

            }
        });
    }
    private  void RestPassword(){
        btnReset.setVisibility(View.INVISIBLE);
        auth.sendPasswordResetEmail(strEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(onClickforgotpass.this,"Reset Password link has been sent to your registered email",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(onClickforgotpass.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(onClickforgotpass.this,"Error :-"+e.getMessage(),Toast.LENGTH_SHORT).show();
                btnReset.setVisibility(View.VISIBLE);
            }
        });

    }
}