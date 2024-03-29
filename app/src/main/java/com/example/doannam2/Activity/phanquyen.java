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
import com.example.doannam2.Adapter.Adapterphanquyen;
import com.example.doannam2.CartPhanquyen;
import com.example.doannam2.MyProfile;
import com.example.doannam2.MyProfilephanquyen;
import com.example.doannam2.R;
import com.example.doannam2.model.dataclass;
import com.google.android.material.appbar.MaterialToolbar;
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

public class phanquyen extends AppCompatActivity {
    DrawerLayout drawerLayout;
    MaterialToolbar materialToolbar;
    NavigationView navigationView;
    private ImageView imgAvatar;
    private TextView tvName,tvEmail;
    RecyclerView recyclerView;
    List<dataclass> dataListphanquyen;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    SearchView searchView;
    Adapterphanquyen adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phanquyen);
        addcontrol();
        onsearch();
        initui();
        showUserInformation();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                phanquyen.this, drawerLayout, materialToolbar, R.string.drawer_close, R.string.drawer_open);
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
        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.cart){
                    Intent intent = new Intent(phanquyen.this, CartPhanquyen.class);
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
                    Toast.makeText(phanquyen.this, "home", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId()==R.id.Profile) {
                    Intent intent = new Intent(phanquyen.this, MyProfile.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (item.getItemId()==R.id.setting) {
                    Toast.makeText(phanquyen.this,"setting",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (item.getItemId()==R.id.about) {
                    Toast.makeText(phanquyen.this,"About",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId()==R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(phanquyen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
    }
    public void searchList(String text){
        ArrayList<dataclass> searchList = new ArrayList<>();
        for(dataclass dataclass: dataListphanquyen){
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
        navigationView = findViewById(R.id.navigation_View);
        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(phanquyen.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(phanquyen.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataListphanquyen = new ArrayList<>();

        adapter = new Adapterphanquyen(phanquyen.this,dataListphanquyen);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Android Products");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataListphanquyen.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    dataclass dataclass = itemSnapshot.getValue(dataclass.class);
                    dataclass.setKey(itemSnapshot.getKey());
                    dataListphanquyen.add(dataclass);
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
    private void initui(){
        navigationView = findViewById(R.id.navigation_View);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.image_avatar);
        tvName =navigationView.getHeaderView(0). findViewById(R.id.tv_name);
        tvEmail = navigationView.getHeaderView(0). findViewById(R.id.tv_email);
    }
    public void showUserInformation(){
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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }
}