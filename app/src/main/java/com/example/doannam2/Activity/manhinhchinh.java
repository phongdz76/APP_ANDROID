package com.example.doannam2.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doannam2.Adapter.MyAdapter;
import com.example.doannam2.MyProfile;
import com.example.doannam2.R;
import com.example.doannam2.model.dataclass;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class manhinhchinh extends AppCompatActivity {
    public  static final int MY_REQUEST_CODE =10;
   DrawerLayout drawerLayout;
   MaterialToolbar materialToolbar;
   ImageView imgAvatar;
   TextView tvName,tvEmail;
   NavigationView navigationView;
   FloatingActionButton fab;
   RecyclerView recyclerView;
   List<dataclass> dataList;
   DatabaseReference databaseReference;
   ValueEventListener eventListener;
   SearchView searchView;
   MyAdapter adapter;
   MyProfile parentActivity ;

   final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
           new ActivityResultContracts.StartActivityForResult(),
           new ActivityResultCallback<ActivityResult>() {
       @Override
       public void onActivityResult(ActivityResult result) {
           if(result.getResultCode() == RESULT_OK){
               Intent intent = result.getData();
               if(intent == null){
                   return;
               }
                Uri uri = intent.getData();
               parentActivity.setUri(uri);
               try {
                   Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                   parentActivity.setBitmapImageView(bitmap);
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
   });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhchinh);
        addcontrol();
        String UidAdmin = getIntent().getStringExtra("UidAdmin");
        navigationView = findViewById(R.id.navigation_View);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.image_avatar);
        tvName =navigationView.getHeaderView(0). findViewById(R.id.tv_name);
        tvEmail = navigationView.getHeaderView(0). findViewById(R.id.tv_email);
        onsearch();
        showUserInformation();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                manhinhchinh.this, drawerLayout, materialToolbar, R.string.drawer_close, R.string.drawer_open);
        drawerLayout.addDrawerListener(toggle);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(manhinhchinh.this, upload.class);
                startActivity(intent);
            }
        });

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.cart){
                    Intent intent = new Intent(manhinhchinh.this, CartActivity.class);
                    intent.putExtra("UidAdmin",UidAdmin);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.home){
                    Toast.makeText(manhinhchinh.this, "home", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId()==R.id.Profile) {
                    Intent intent = new Intent(manhinhchinh.this, MyProfile.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (item.getItemId()==R.id.setting) {
                   Toast.makeText(manhinhchinh.this,"setting",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (item.getItemId()==R.id.about) {
                    Toast.makeText(manhinhchinh.this,"About",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId()==R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(manhinhchinh.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

    }
    public void searchList(String text){
        ArrayList<dataclass> searchList = new ArrayList<>();
        for(dataclass dataclass: dataList){
            if(dataclass.getDataTitle().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataclass);
            }
        }
        adapter.searchDataList(searchList);
    }
    private void onsearch() {
        searchView = findViewById(R.id.search);
        searchView.clearFocus();
    }

    private void addcontrol() {
        drawerLayout = findViewById(R.id.drawer_layout);
        materialToolbar = findViewById(R.id.materialToolBar);
        fab = findViewById(R.id.fab1);

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(manhinhchinh.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(manhinhchinh.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter = new MyAdapter(manhinhchinh.this,dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Android Products");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    dataclass dataclass = itemSnapshot.getValue(dataclass.class);
                    dataclass.setKey(itemSnapshot.getKey());
                    dataList.add(dataclass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
    }
   public   void showUserInformation(){
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
        {
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if(name == null){
            tvName.setVisibility(View.GONE);
        }else {
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(name);
        }
        tvEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.maleuser).into(imgAvatar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"select images"));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }
}