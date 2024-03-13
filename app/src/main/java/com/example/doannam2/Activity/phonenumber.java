package com.example.doannam2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doannam2.R;
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

public class phonenumber extends AppCompatActivity {

    public static  final String TAG = phonenumber.class.getName();
    private EditText edtphonenumber;
    private Button btnverifyphone;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonenumber);

        control();
        settitletoolbar();
        mAuth = FirebaseAuth.getInstance();


        btnverifyphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strphonenumber = edtphonenumber.getText().toString().trim();
                onClickverifyphone(strphonenumber);
            }
        });
    }
    private void settitletoolbar(){
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Verify Phone Number");
        }
    }
    private  void control()
    {
        edtphonenumber = findViewById(R.id.edtphonenumber);
        btnverifyphone = findViewById(R.id.btnphone);
    }
    private void onClickverifyphone(String strphonenumber)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(strphonenumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(phonenumber.this, "verification faild", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                goToEnterOtpActivity(strphonenumber,verificationId);
                            }
                        })          
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

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
                                Toast.makeText(phonenumber.this,
                                        "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private  void goToMainActivity(String phoneNumber){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Phone_number",phoneNumber);
        startActivity(intent);
    }
    private void goToEnterOtpActivity(String strphonenumber, String verificationId) {
        Intent intent = new Intent(this, enterotp.class);
        intent.putExtra("phone_number",strphonenumber);
        intent.putExtra("verification_id",verificationId);
        startActivity(intent);
    }

}