package com.example.doannam2;

import static android.app.PendingIntent.getActivity;

import static com.example.doannam2.Activity.manhinhchinh.MY_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doannam2.Activity.manhinhchinh;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class MyProfile extends AppCompatActivity {

    ImageView imgAvatar;
    EditText edtFullname,edtEmail;
    Button btnUpdateProfile;
    manhinhchinh parentActivity;
    Uri mUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initui();
        linner();
        setUserInformation();
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentActivity.openGallery();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            parentActivity = (manhinhchinh) extras.getSerializable("parentActivity");
        }
    }
    public void initui(){
        imgAvatar =findViewById(R.id.img_avatar);
        edtFullname = findViewById(R.id.edt_full_name);
        edtEmail = findViewById(R.id.edt_email);
        btnUpdateProfile = findViewById(R.id.btn_update_profile);
    }
    private void linner() {


        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }


        });
    }
    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
        {
            return;
        }
        String strFullName = edtFullname.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullName)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MyProfile.this, "update thành công", Toast.LENGTH_SHORT).show();
                            parentActivity.showUserInformation();
                        }
                    }
                });
    }
    private void onclickPermission() {
        if(parentActivity == null){
            return;
        }
        if(this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            parentActivity.openGallery();
        }else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            this.requestPermissions(permissions,MY_REQUEST_CODE);
        }
    }

       private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
        {
            return;
        }
        edtFullname.setText(user.getDisplayName());
        edtEmail.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.maleuser).into(imgAvatar);
    }


    public void setBitmapImageView(Bitmap bitmapImageView){
        imgAvatar.setImageBitmap(bitmapImageView);
    }

    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }
}