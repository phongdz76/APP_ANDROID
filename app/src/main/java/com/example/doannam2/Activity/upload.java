package com.example.doannam2.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doannam2.R;
import com.example.doannam2.model.dataclass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class upload extends AppCompatActivity {

    ImageView uploadimage;
    Button saveButton;
    EditText uploadTopic, uploadDesc,uploadlang;
    String imageURL;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        addcontrol();
    }

    private void addcontrol() {
        uploadimage= findViewById(R.id.uploadimage);
        uploadDesc= findViewById(R.id.uploaddesc);
        uploadTopic= findViewById(R.id.uploadtopic);
        uploadlang= findViewById(R.id.uploadlang);
        saveButton= findViewById(R.id.savebutton);
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadimage.setImageURI(uri);
                        }
                        else {
                            Toast.makeText(upload.this,"No Image selected",Toast.LENGTH_SHORT);
                        }

                    }
                }
        );
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images");
    public void saveData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(upload.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
               while (!uriTask.isComplete());
               Uri urlImage = uriTask.getResult();
               imageURL = urlImage.toString();
               uploadData();
               dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public  void uploadData(){

        String title = uploadTopic.getText().toString();
        String desc = uploadDesc.getText().toString();
        int lang = Integer.parseInt(uploadlang.getText().toString());

        dataclass dataClass = new dataclass(title,desc,lang,imageURL);


        String productID = FirebaseDatabase.getInstance().getReference().child("Android Products").push().getKey();



        FirebaseDatabase.getInstance().getReference("Android Products").child(productID)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(upload.this,"saved",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(upload.this, manhinhchinh.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(upload.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });



    }
}