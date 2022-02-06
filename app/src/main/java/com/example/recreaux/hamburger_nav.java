package com.example.recreaux;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class hamburger_nav extends AppCompatActivity {
    //
    DrawerLayout drawerLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamburger_nav);

        //
        drawerLayout = findViewById(R.id.drawer_layout);

    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {

        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public void ClickSearch(View view){
        Intent intent = new Intent(hamburger_nav.this,SearchTab.class);
        startActivity(intent);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    public void ClickHome(View view){
        recreate();
    }
    public void ClickCreateEvent(View view){redirectActivity(this,CreateEventActivity.class);}
    public void ClickMyEvents(View view){redirectActivity(this,MyEventsActivity.class);}
    public void ClickNotifications(View view){redirectActivity(this, Notifications.class);}
    public void ClickFriendsList(View view){redirectActivity(this,FriendList.class);}
    public void ClickGenerateQR(View view){redirectActivity(this,MyQRCode.class);}
    public void ClickScanQR(View view){redirectActivity(this,ScanQRCode.class);}
    public void ClickMyProfile(View view){redirectActivity(this,MyProfile.class);}
    public void ClickLogout(View view){
        logout(this);
    }

    public static void logout(Activity activity) {
        //Alert dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set the title
        builder.setTitle("Log Out");
        //Set message
        builder.setMessage("Are you sure you want to log out?");
        //Yes builder
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Finish activity
                activity.finishAffinity();
                System.exit(0);
            }
        });

        //Negative builder
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss dialogue
                dialog.dismiss();
            }
        });
        //Show dialog
        builder.show();
    }

    public static void redirectActivity(Activity activity,Class aClass) {
        Intent intent = new Intent(activity,aClass);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }



}
