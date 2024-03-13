package com.example.doannam2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doannam2.Adapter.MyAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class phanquyen extends AppCompatActivity {

    DrawerLayout drawerLayout;
    MaterialToolbar materialToolbar;
    NavigationView navigationView;
    private ImageView imgAvatar;
    private TextView tvName,tvEmail;
    RecyclerView recyclerView;
    List<dataclass> dataList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    SearchView searchView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phanquyen);
        addcontrol();
        onsearch();
        initui();
        //showUserInformation();
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


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.home){
                    Toast.makeText(phanquyen.this, "home", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (item.getItemId()==R.id.Profile) {
                    Toast.makeText(phanquyen.this,"Profile",Toast.LENGTH_SHORT).show();
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
        navigationView = findViewById(R.id.navigation_View);
        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(phanquyen.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(phanquyen.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        adapter = new MyAdapter(phanquyen.this,dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Android Products");
        dialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
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
    private void initui(){
        imgAvatar = findViewById(R.id.image_avatar);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
    }
    private  void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
        {
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        //Uri photoUrl = user.getPhotoUrl();

        if(name != null)
        {
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(name);
        }else {
            tvName.setVisibility(View.GONE);
        }
        tvEmail.setText(email);
        //Glide.with(this).load(photoUrl).error(R.drawable.maleuser).into(imgAvatar);
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }
}