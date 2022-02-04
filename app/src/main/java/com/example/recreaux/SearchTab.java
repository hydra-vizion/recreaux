package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class SearchTab extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapterSearch adapter;

    //widgets
    private EditText mSearchParam;
    private ListView mListView;
    //vars
    private List<String> mUserList;
    private int currentTabPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tab);


        mListView = (ListView) findViewById(R.id.searchList);
        mListView.setOnItemClickListener(this);

        mSearchParam = (EditText) findViewById(R.id.searchBar);


        tabLayout = findViewById(R.id.tab_search);
        pager2  = findViewById(R.id.search_view_pager);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapterSearch(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Tags"));
        tabLayout.addTab(tabLayout.newTab().setText("People"));
        tabLayout.addTab(tabLayout.newTab().setText("Event"));
        tabLayout.addTab(tabLayout.newTab().setText("Place"));
        initTextListener();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
                currentTabPos = tab.getPosition();
                mUserList.clear();
                updateUserName();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));

            }
        });
    }
    private void searchForMatch(String keyword){
        mUserList.clear();
        updateUserName();
        if(keyword.length() <= 2 ){

        }else{
            DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("User");
            DatabaseReference referenceEvent = FirebaseDatabase.getInstance().getReference("Event");
            DatabaseReference referenceVenue = FirebaseDatabase.getInstance().getReference("Venue");

            if(currentTabPos == 0){

                referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String[] key;

                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            key = singleSnapshot.child("userInterests").getValue().toString().toLowerCase().split(",");
                            for (int index = 0; index < key.length; index++){
                                if(key[index].contains(keyword)){
                                    mUserList.add(singleSnapshot.child("userNickname").getValue().toString());
                                    break;
                                }
                            }
                        }
                        updateUserName();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                referenceEvent.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String[] key;

                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            key = singleSnapshot.child("Event Tags").getValue().toString().toLowerCase().split(",");
                            for (int index = 0; index < key.length; index++){
                                if(key[index].contains(keyword)){
                                    mUserList.add(singleSnapshot.child("Event Name").getValue().toString());
                                    break;
                                }
                            }
                        }
                        updateUserName();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                referenceVenue.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String[] key;

                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            key = singleSnapshot.child("VenueTag").getValue().toString().toLowerCase().split(",");
                            for (int index = 0; index < key.length; index++){
                                if(key[index].contains(keyword)){
                                    mUserList.add(singleSnapshot.child("VenueName").getValue().toString());
                                    break;
                                }
                            }
                        }
                        updateUserName();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            if(currentTabPos == 1){
                referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            if(singleSnapshot.child("userNickname").getValue().toString().toLowerCase().contains(keyword)){
                                mUserList.add(singleSnapshot.child("userNickname").getValue().toString());
                            }
                            else if(singleSnapshot.child("userFullName").getValue().toString().toLowerCase().contains(keyword)){
                                mUserList.add(singleSnapshot.child("userNickname").getValue().toString());
                            }
                        }
                        updateUserName();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            if(currentTabPos == 2){
                referenceEvent.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            if(singleSnapshot.child("Event Name").getValue().toString().toLowerCase().contains(keyword)){
                                mUserList.add(singleSnapshot.child("Event Name").getValue().toString());
                            }
                        }
                        updateUserName();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            if(currentTabPos == 3){
                referenceVenue.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            if(singleSnapshot.child("VenueName").getValue().toString().toLowerCase().contains(keyword)){
                                mUserList.add(singleSnapshot.child("VenueName").getValue().toString());
                            }
                        }
                        updateUserName();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    private void initTextListener(){
        mUserList =  new ArrayList<>();
        mSearchParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = mSearchParam.getText().toString().toLowerCase(Locale.getDefault());
                searchForMatch(text);
            }
        });
    }

    private void updateUserName(){
        LinkedHashSet<String> lhSetColors = new LinkedHashSet<String>(mUserList);
        String[] newArray = lhSetColors.toArray(new String[ lhSetColors.size() ]);
        List<String> list = Arrays.asList(newArray);
        CustomListAdapter listAdapter = new CustomListAdapter(this , R.layout.custom_list , list);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUserList);
        mListView.setAdapter(listAdapter);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {

        Toast.makeText(SearchTab.this,"pos"+position,Toast.LENGTH_LONG).show();

        // Then you start a new Activity via Intent
//        Intent intent = new Intent();
//        intent.setClass(this, ListItemDetail.class);
//        intent.putExtra("position", position);
//        // Or / And
//        intent.putExtra("id", id);
//        startActivity(intent);
    }
}