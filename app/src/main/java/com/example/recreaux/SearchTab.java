package com.example.recreaux;

import static com.example.recreaux.hamburger_nav.redirectActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
    private String userimage;
    //vars
    //private List<String> mUserList;

    private List<SearchContent> mContentList;
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
        hideSoftKeyboard();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
                currentTabPos = tab.getPosition();
               //mUserList.clear();
                mContentList.clear();
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
        //mUserList.clear();
        mContentList.clear();
        updateUserName();

        if(keyword.length() <= 2 ){

        }else{
            DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("Users");
            DatabaseReference referenceEvent = FirebaseDatabase.getInstance().getReference("Event");
            DatabaseReference referenceVenue = FirebaseDatabase.getInstance().getReference("Venue");

            if(currentTabPos == 0){

                referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String[] key;

                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            try{
                                singleSnapshot.child("Interests").getValue().toString().toLowerCase().split(",");
                            }catch (Exception e){
                                break;
                            }
                            key = singleSnapshot.child("Interests").getValue().toString().toLowerCase().split(",");
                            for (int index = 0; index < key.length; index++){
                                if(key[index].contains(keyword)){
                                    mContentList.add(new SearchContent(
                                            singleSnapshot.child("Name").getValue().toString(),
                                            singleSnapshot.child("Username").getValue().toString(),
                                            singleSnapshot.child("UserImage").getValue().toString(),
                                            "user",
                                            singleSnapshot.getKey()));
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
                            try{
                                singleSnapshot.child("Event Tags").getValue().toString().toLowerCase().split(",");
                            }catch (Exception e){
                                break;
                            }
                            key = singleSnapshot.child("Event Tags").getValue().toString().toLowerCase().split(",");
                            for (int index = 0; index < key.length; index++){
                                if(key[index].contains(keyword)){
                                    mContentList.add(new SearchContent(
                                            singleSnapshot.child("Event Name").getValue().toString(),
                                            singleSnapshot.child("Event Description").getValue().toString(),
                                            singleSnapshot.child("Event Icon").getValue().toString(),
                                            "event",
                                            singleSnapshot.getKey()));
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
                            try{
                                singleSnapshot.child("VenueTag").getValue().toString().toLowerCase().split(",");
                            }catch (Exception e){
                                break;
                            }
                            key = singleSnapshot.child("VenueTag").getValue().toString().toLowerCase().split(",");
                            for (int index = 0; index < key.length; index++){
                                if(key[index].contains(keyword)){
                                    mContentList.add(new SearchContent(
                                            singleSnapshot.child("VenueName").getValue().toString(),
                                            singleSnapshot.child("VenueAddress").getValue().toString(),
                                            singleSnapshot.child("VenueImage").getValue().toString(),
                                            "venue",
                                            singleSnapshot.getKey()));
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
            else if(currentTabPos == 1){
                referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            if(singleSnapshot.child("Name").getValue().toString().toLowerCase().contains(keyword)){

                                if(!singleSnapshot.child("UserImage").exists()){
                                    Bitmap image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                                    image.eraseColor(Color.BLACK);
                                    userimage=Base64.getEncoder().encodeToString(getBytes(image));
                                }
                                else{
                                    userimage=singleSnapshot.child("UserImage").getValue().toString();
                                }

                                mContentList.add(new SearchContent(
                                        singleSnapshot.child("Name").getValue().toString(),
                                        singleSnapshot.child("Username").getValue().toString(),
                                        userimage,
                                        "user",
                                        singleSnapshot.getKey()));
                            }
                            else if(singleSnapshot.child("Username").getValue().toString().toLowerCase().contains(keyword)){

                                if(!singleSnapshot.child("UserImage").exists()){
                                    Bitmap image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                                    image.eraseColor(Color.BLACK);
                                    userimage=Base64.getEncoder().encodeToString(getBytes(image));
                                }
                                else{
                                    userimage=singleSnapshot.child("UserImage").getValue().toString();
                                }

                                mContentList.add(new SearchContent(
                                        singleSnapshot.child("Name").getValue().toString(),
                                        singleSnapshot.child("Username").getValue().toString(),
                                        userimage,
                                        "user",
                                        singleSnapshot.getKey()));
                            }
                        }
                        updateUserName();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else if(currentTabPos == 2){
                referenceEvent.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                            if(singleSnapshot.child("Event Name").getValue().toString().toLowerCase().contains(keyword)){
                                mContentList.add(new SearchContent(
                                            singleSnapshot.child("Event Name").getValue().toString(),
                                            singleSnapshot.child("Event Description").getValue().toString(),
                                            singleSnapshot.child("Event Icon").getValue().toString(),
                                            "event",
                                            singleSnapshot.getKey()));
                            }
                        }
                        updateUserName();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else if(currentTabPos == 3){
                referenceVenue.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            if(singleSnapshot.child("VenueName").getValue().toString().toLowerCase().contains(keyword)){
                                mContentList.add(new SearchContent(
                                        singleSnapshot.child("VenueName").getValue().toString(),
                                        singleSnapshot.child("VenueAddress").getValue().toString(),
                                        singleSnapshot.child("VenueImage").getValue().toString(),
                                        "venue",
                                        singleSnapshot.getKey()));
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
        //mUserList =  new ArrayList<>();

        mContentList =  new ArrayList<>();
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
//        LinkedHashSet<SearchContent> lhSet = new LinkedHashSet<SearchContent>(mContentList);
//        SearchContent[] newArray = lhSet.toArray(new SearchContent[ lhSet.size() ]);
//        List<SearchContent> list = Arrays.asList(newArray);
        CustomListAdapter listAdapter = new CustomListAdapter(this , R.layout.custom_list , mContentList);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUserList);
        mListView.setAdapter(listAdapter);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {

        //Toast.makeText(SearchTab.this,mContentList.get(position).id+""+mContentList.get(position).type,Toast.LENGTH_LONG).show();
        String ActivityType = mContentList.get(position).type;

        if(ActivityType.equals("venue")){
            Intent intent = new Intent(SearchTab.this,Venue.class);
            intent.putExtra("venueid",mContentList.get(position).id);
            startActivity(intent);
        }
        else if(ActivityType.equals("user")){
            Intent intent = new Intent(SearchTab.this,OtherProfile.class);
            intent.putExtra("id",mContentList.get(position).id);
            startActivity(intent);
        }
        else if(ActivityType.equals("event")){
            Intent intent = new Intent(SearchTab.this,ViewEvent.class);
            int eventID = Integer.parseInt(mContentList.get(position).id);
            intent.putExtra("eventid",eventID);
            startActivity(intent);
        }


//        Intent intent = new Intent(SearchTab.this,OtherProfile.class);
//        intent.putExtra("id",otherid);
//        startActivity(intent);
        //
    }

    public void hideSoftKeyboard(){
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

}