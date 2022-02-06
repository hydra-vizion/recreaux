package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recreaux.EventUserListAdapter;
import com.example.recreaux.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewEventParticipantsActivity extends AppCompatActivity {
    ListView LV_ViewEventParticipants;
    ImageView iv_backbuttonimage;
    private DatabaseReference ref;
    int eventid;
    boolean shareable;
    boolean friendshare;
    String currentuserid;

    TextView TV_ViewEventParticipantsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_participants);
        ref = FirebaseDatabase.getInstance().getReference();
        //currentuserid ="OmnyApXKCAOdnll4Dc1RKoLLSKH3";
        currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        TV_ViewEventParticipantsTitle = findViewById(R.id.TV_ViewEventParticipantsTitle);
        LV_ViewEventParticipants = findViewById(R.id.LV_ViewEventParticipants);
        iv_backbuttonimage = findViewById(R.id.iv_backbuttonimage);
        if(getIntent().getExtras() != null) {
            eventid=getIntent().getExtras().getInt("eventid");
            shareable = getIntent().getExtras().getBoolean("shareable");

        }
        if(shareable){
            TV_ViewEventParticipantsTitle.setText("Share to Friends");

        }
        else{
            TV_ViewEventParticipantsTitle.setText("Participant List");
        }
        LV_ViewEventParticipants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                //Toast.makeText(getApplicationContext(),"Position "+item, Toast.LENGTH_SHORT).show();
                if(item.equals(currentuserid)){
                    //Intent intent = new Intent(ViewEventParticipantsActivity.this,MyProfile.class);
                    //intent.putExtra("id",item);
                    //startActivity(intent);
                    //Toast.makeText(getApplicationContext(),"My Profile "+item, Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getApplicationContext(),"Other Profile "+item, Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(ViewEventParticipantsActivity.this,OtherProfile.class);
                    //intent.putExtra("id",item);
                    //startActivity(intent);
                }
            }
        });
        iv_backbuttonimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        if(shareable){
            updateListFriend();

        }
        else{
            updateListViewOnly();
        }
    }

    private void updateListViewOnly(){
        final List<String> values = new ArrayList<String>();
        ref.child("Event").child(String.valueOf(eventid)).child("Event Participants").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isComplete() && task.isSuccessful()){
                    DataSnapshot snap= task.getResult();
                    Iterator<DataSnapshot> iter = snap.getChildren().iterator();
                    if (snap.exists()){
                        while (iter.hasNext()){
                            DataSnapshot snapkey = iter.next();
                            String nodId = snapkey.getKey();
                            String idonly = nodId.split("_")[1];
                            values.add(idonly);

                        }
                    }
                    EventUserListAdapter adapter = new EventUserListAdapter(ViewEventParticipantsActivity.this, R.layout.user_record_view_event,values,false,eventid);
                    LV_ViewEventParticipants.setAdapter(adapter);
                }
            }
        });
    }

    private void updateListFriend(){
        final List<String> values = new ArrayList<String>();

        ref.child("Friends").child(currentuserid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isComplete() && task.isSuccessful()){
                    DataSnapshot friendsnap = task.getResult();
                    Iterator<DataSnapshot> frienditer = friendsnap.getChildren().iterator();
                    if (friendsnap.exists()){
                        while(frienditer.hasNext()){
                            DataSnapshot snapid = frienditer.next();
                            String friendid = snapid.getKey().toString();
                            if(!friendid.equals(currentuserid)){
                                values.add(friendid);
                            }
                        }
                    }
                    EventUserListAdapter adapter = new EventUserListAdapter(ViewEventParticipantsActivity.this,R.layout.user_record_view_event,values,true,eventid);
                    LV_ViewEventParticipants.setAdapter(adapter);
                }
            }
        });
    }

}