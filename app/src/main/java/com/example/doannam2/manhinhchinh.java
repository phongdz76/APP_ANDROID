package com.example.doannam2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class manhinhchinh extends AppCompatActivity {

   DrawerLayout drawerLayout;
   MaterialToolbar materialToolbar;
   FrameLayout frameLayout;

   private ImageView imgAvatar;
   private TextView tvName,tvEmail;
   NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhchinh);

        addcontrol();
        initui();
        //showUserInformation();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                manhinhchinh.this, drawerLayout, materialToolbar, R.string.drawer_close, R.string.drawer_open);
        drawerLayout.addDrawerListener(toggle);

        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.share){
                    Toast.makeText(manhinhchinh.this, "share", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(manhinhchinh.this,"Profile",Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(manhinhchinh.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
    }

    private void addcontrol() {
        drawerLayout = findViewById(R.id.drawer_layout);
        materialToolbar = findViewById(R.id.materialToolBar);

        navigationView = findViewById(R.id.navigation_View);
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

        if(name == null)
        {
            tvName.setVisibility(View.GONE);
        }else {
            tvName.setVisibility(View.VISIBLE);
        }
        tvName.setText(name);
        tvEmail.setText(email);
        //Glide.with(this).load(photoUrl).error(R.drawable.maleuser).into(imgAvatar);
    }
}