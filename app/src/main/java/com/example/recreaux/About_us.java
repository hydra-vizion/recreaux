package com.example.recreaux;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class About_us extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

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
        hamburger_nav.redirectActivity(this,hamburger_nav.class);
    }
    public void ClickDashboard(View view){
        hamburger_nav.redirectActivity(this,Dashboard.class);
    }
    public void CLickAboutUs(View view){
        recreate();
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