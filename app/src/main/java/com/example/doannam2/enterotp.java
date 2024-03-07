package com.example.doannam2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class enterotp extends AppCompatActivity {
    public  static  final  String TAG = enterotp.class.getName();
    private FirebaseAuth mAuth;
    private TextView tvsentotp;
    private EditText edtotp;
    private Button btnsentotp;
    private String mPhoneNumber;
    private String mVerificationId;
    public PhoneAuthProvider.ForceResendingToken mforceResendingToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterotp);

        control();
        settitletoolbar();
        getDataIntent();
        mAuth = FirebaseAuth.getInstance();

        btnsentotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strotp = edtotp.getText().toString().trim();
               onclicksentotp(strotp);
            }


        });
        tvsentotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicksentotpAgain();
            }


        });
    }
    private  void getDataIntent(){
        mPhoneNumber = getIntent().getStringExtra("phone_number");
        mVerificationId = getIntent().getStringExtra("verification_id");
    }
    private void settitletoolbar(){
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Enter OTp code");
        }
    }
    private void control(){
        edtotp = findViewById(R.id.edtotp);
        btnsentotp = findViewById(R.id.btnsentopt);
        tvsentotp = findViewById(R.id.btn_sent_otp_code);
    }
    private void onClicksentotpAgain() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setForceResendingToken(mforceResendingToken)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(enterotp.this, "verification faild", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                mVerificationId = verificationId;
                                mforceResendingToken = forceResendingToken;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void onclicksentotp(String strotp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, strotp);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            goToMainActivity(user.getPhoneNumber());
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(enterotp.this,
                                        "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private  void goToMainActivity(String phoneNumber){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("Phone_number",phoneNumber);
        startActivity(intent);
    }
}