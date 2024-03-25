package com.example.doannam2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;


import com.example.doannam2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {



    EditText edtEmail, edtPassword;
    TextView btnforgerpass,signup;
    String stremail;
    String strpassword;


    ProgressDialog progressDialog;

    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){
            if (isAdminUser(currentUser.getUid())) {
                // Nếu là admin, chuyển đến phần quyền layout
                Intent intent1 = new Intent(MainActivity.this, manhinhchinh.class);
                startActivity(intent1);
                finishAffinity();
            } else {
            Intent intent = new Intent(MainActivity.this, phanquyen.class);
            startActivity(intent);}
        }
        setContentView(R.layout.activity_main);
        addcontrol();
        initlistener();
        settitletoolbar();

        btnforgerpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, onClickforgotpass.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initlistener() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, signupActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignin();
            }
        });
    }

    private void onClickSignin() {


        stremail = edtEmail.getText().toString().trim();
        strpassword = edtPassword.getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressDialog.show();
        auth.signInWithEmailAndPassword(stremail, strpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            String userID = FirebaseAuth.getInstance().getUid();
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null){
                                if (isAdminUser(currentUser.getUid())) {
                                    // Nếu là admin, chuyển đến phần quyền layout
                                    Intent intent1 = new Intent(MainActivity.this, manhinhchinh.class);
                                    intent1.putExtra("UidAdmin",userID);
                                    startActivity(intent1);
                                    finishAffinity();
                                } else {
                                    Intent intent = new Intent(MainActivity.this, phanquyen.class);
                                    intent.putExtra("Uiduser",userID);
                                    startActivity(intent);}
                            }

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "tài khoản hoặc mật khẩu sai",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
    private boolean isAdminUser(String uId) {

        if (uId.equals("Mr25iKbCEUbrh8aKyXRrO7JRhxk2")){
            return true;
        } else {
            return  false;
        }
    }
    private void getDataIntent(){
        //String strphonenumber = getIntent().getStringExtra("phone_number");
        //TextView tvUserInfor = findViewById(R.id.tv_user_infor);
        //tvUserInfor.setText(strphonenumber);
    }
    private void settitletoolbar(){
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Main Activity");
        }
    }

    public  void addcontrol(){
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        signup = findViewById(R.id.signup);
        btnforgerpass = findViewById(R.id.btnforgotpass);
        progressDialog = new ProgressDialog(this);
    }

}