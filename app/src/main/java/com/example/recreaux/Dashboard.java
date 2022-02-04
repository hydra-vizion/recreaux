package com.example.recreaux;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class Dashboard extends AppCompatActivity {
    //init var
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //assign var
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void ClickMenu(View view){
        //Open drawer
        hamburger_nav.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        hamburger_nav.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        //Redirect activity to home
        hamburger_nav.redirectActivity(this,MyProfile.class);
    }
    public void ClickDashboard(View view){
        recreate();
    }
    public void ClickAboutUs(View view){
        hamburger_nav.redirectActivity(this,About_us.class);
    }
    public void ClickLogout(View view){
        hamburger_nav.logout(this);
    }


    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        hamburger_nav.closeDrawer(drawerLayout);
    }

}