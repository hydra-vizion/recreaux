package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Search extends AppCompatActivity {
    View searchView;

    private BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        LinearLayout searchResult = findViewById(R.id.searchScroll);
        LayoutInflater inflater2 = LayoutInflater.from(this);

        for(int i = 0; i < 10; i++){
            View view2 = inflater2.inflate(R.layout.search_item_place, searchResult,false);

            TextView textView2 = view2.findViewById(R.id.searchItemtextView);
            textView2.setText("namae "+i);

            ImageView imageView = view2.findViewById(R.id.searchViewImage);
            imageView.setImageResource(R.drawable.ic_launcher_foreground);
            searchResult.addView(view2);
        }
//        searchView =
//        int searchFrameId = searchView.getContext().getResources().getIdentifier("android:id/search_edit_frame", null, null);
//        View searchFrame = searchView.findViewById(searchFrameId);
//        searchFrame.setBackgroundResource(R.drawable.bg_white_rounded);
//
//        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
//        View searchPlate = findViewById(searchPlateId);
//        searchPlate.setBackgroundResource(R.drawable.bg_white_rounded);
//
//        int searchBarId = searchView.getContext().getResources().getIdentifier("android:id/search_bar", null, null);
//        View searchBar = findViewById(searchBarId);
//        searchBar.setBackgroundResource(R.drawable.bg_white_rounded);

        //navigation
//        bottom_navigation = findViewById(R.id.bottom_navigation);
//        //set Home selected
//        bottom_navigation.setSelectedItemId(R.id.Nav_Assignment);
//        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.Nav_Assignment:
//                        return true;
//                    case R.id.Nav_Calendar:
//                        startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                    case R.id.Nav_Profile:
//                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                }
//                return false;
//            }
    }
}