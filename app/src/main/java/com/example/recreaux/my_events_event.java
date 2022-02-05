package com.example.recreaux;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class my_events_event extends AppCompatActivity {

    TabLayout L_Tab_Layout;
    ViewPager2 VP_pagers;
    FragmentAdapter_My_Events adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events_event);

        L_Tab_Layout = findViewById(R.id.L_Tab_Layout);
        VP_pagers = findViewById(R.id.VP_pagers);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter_My_Events(fm, getLifecycle());
        VP_pagers.setAdapter(adapter);

        L_Tab_Layout.addTab(L_Tab_Layout.newTab().setText("Events"));
        L_Tab_Layout.addTab(L_Tab_Layout.newTab().setText("History"));

        L_Tab_Layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                VP_pagers.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        VP_pagers.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                L_Tab_Layout.selectTab(L_Tab_Layout.getTabAt(position));
            }
        });

    }
}