package com.example.doannam2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    TextView btnforgerpass,signup;

    ProgressDialog progressDialog;

    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addcontrol();
        initlistener();
        settitletoolbar();


        btnforgerpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickforgotpass();
            }
        });

    }

    private void initlistener() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,signupActivity.class);
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
        String stremail = edtEmail.getText().toString().trim();
        String strpassword = edtPassword.getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressDialog.show();
        auth.signInWithEmailAndPassword(stremail, strpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent1= new Intent(MainActivity.this,manhinhchinh.class);
                            startActivity(intent1);
                            finishAffinity();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "tài khoản hoặc mật khẩu sai",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void getDataIntent(){
        String strphonenumber = getIntent().getStringExtra("phone_number");
        TextView tvUserInfor = findViewById(R.id.tv_user_infor);
        tvUserInfor.setText(strphonenumber);
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
    public void onClickforgotpass() {

    }
}