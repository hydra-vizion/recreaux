package com.example.recreaux;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Nav_to_Nav extends AppCompatActivity implements View.OnClickListener {

    private Button NavtoNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_to_nav);

        NavtoNav = (Button) findViewById(R.id.BTN_NAV_NAV);
        NavtoNav.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}