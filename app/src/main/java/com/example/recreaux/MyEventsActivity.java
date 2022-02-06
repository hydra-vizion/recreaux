package com.example.recreaux;

import static com.example.recreaux.hamburger_nav.redirectActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recreaux.MyEventListAdapter;
import com.example.recreaux.PostEventViewActivity;
import com.example.recreaux.R;
import com.example.recreaux.ViewEvent;
import com.example.recreaux.ViewReportActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyEventsActivity extends AppCompatActivity {
    String currentuserid;
    ListView LV_MyEvents;
    DatabaseReference ref;
    View LineEvents,LineHistory;
    TextView TV_MyEventsHistory,TV_MyEventsEvents;
    int nowview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        nowview=0;
        ref = FirebaseDatabase.getInstance().getReference();
        //currentuserid ="OmnyApXKCAOdnll4Dc1RKoLLSKH3";
        currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LV_MyEvents = findViewById(R.id.LV_MyEvents);
        LineEvents = findViewById(R.id.LineEvents);
        LineHistory = findViewById(R.id.LineHistory);
        TV_MyEventsHistory = findViewById(R.id.TV_MyEventsHistory);
        TV_MyEventsEvents = findViewById(R.id.TV_MyEventsEvents);

        TV_MyEventsHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowview=1;
                TV_MyEventsHistory.setClickable(false);
                TV_MyEventsEvents.setClickable(true);
                TV_MyEventsHistory.setTextColor(Color.BLACK);
                TV_MyEventsEvents.setTextColor(Color.GRAY);
                LineEvents.setBackgroundColor(Color.WHITE);
                LineHistory.setBackgroundColor(Color.BLACK);
                updatelistMyHistory();
            }
        });
        TV_MyEventsEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowview=0;
                TV_MyEventsEvents.setTextColor(Color.BLACK);
                TV_MyEventsHistory.setTextColor(Color.GRAY);
                LineHistory.setBackgroundColor(Color.WHITE);
                LineEvents.setBackgroundColor(Color.BLACK);
                TV_MyEventsEvents.setClickable(false);
                TV_MyEventsHistory.setClickable(true);
                updatelistMyEvents();
            }
        });
        LV_MyEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int item = (Integer) parent.getAdapter().getItem(position);
                //Toast.makeText(getApplicationContext(),"Position "+String.valueOf(item), Toast.LENGTH_SHORT).show();
                ref.child("Event").child(String.valueOf(item)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot snap= task.getResult();
                        boolean iseventavai;
                        iseventavai=Boolean.parseBoolean(snap.child("Event Status").getValue().toString());
                        if (iseventavai){
                            Intent intent = new Intent(MyEventsActivity.this, ViewEvent.class);
                            intent.putExtra("eventid",item);
                            startActivity(intent);
                        }
                        else {
                            boolean hasreport;
                            hasreport=snap.child("Event Report").exists();
                            if (hasreport){
                                Intent intent = new Intent(MyEventsActivity.this, ViewReportActivity.class);
                                intent.putExtra("eventid",item);
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(MyEventsActivity.this, PostEventViewActivity.class);
                                intent.putExtra("eventid",item);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
    }

    protected void onResume(){
        super.onResume();
        if(nowview==0){
            TV_MyEventsEvents.setTextColor(Color.BLACK);
            TV_MyEventsHistory.setTextColor(Color.GRAY);
            LineHistory.setBackgroundColor(Color.WHITE);
            LineEvents.setBackgroundColor(Color.BLACK);
            TV_MyEventsEvents.setClickable(false);
            TV_MyEventsHistory.setClickable(true);
            updatelistMyEvents();
        }
        else{
            TV_MyEventsHistory.setClickable(false);
            TV_MyEventsEvents.setClickable(true);
            LineEvents.setBackgroundColor(Color.WHITE);
            LineHistory.setBackgroundColor(Color.BLACK);
            TV_MyEventsHistory.setTextColor(Color.BLACK);
            TV_MyEventsEvents.setTextColor(Color.GRAY);
            updatelistMyHistory();
        }
    }

    private void updatelistMyEvents(){
        final List<Integer> values = new ArrayList<Integer>();
        ref.child("Event").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isComplete() && task.isSuccessful()){
                    DataSnapshot snap= task.getResult();
                    Iterator<DataSnapshot> iter = snap.getChildren().iterator();
                    if (snap.exists()){
                        while (iter.hasNext()){
                            DataSnapshot snapevent = iter.next();
                            boolean eventstatus;
                            eventstatus = Boolean.parseBoolean(snapevent.child("Event Status").getValue().toString());
                            if(eventstatus){
                                boolean inevent=false;
                                Iterator<DataSnapshot> part =snapevent.child("Event Participants").getChildren().iterator();
                                while(part.hasNext()){
                                    DataSnapshot snappart = part.next();
                                    if(currentuserid.equals(snappart.getValue().toString())){
                                        inevent=true;
                                        break;
                                    }
                                }
                                if(inevent){
                                    values.add(Integer.valueOf(snapevent.child("Event ID").getValue().toString()));
                                }
                            }
                        }
                    }
                    MyEventListAdapter adapter = new MyEventListAdapter(MyEventsActivity.this, R.layout.event_record,values);
                    LV_MyEvents.setAdapter(adapter);
                }
            }
        });

    }
    private void updatelistMyHistory(){
        final List<Integer> values = new ArrayList<Integer>();
        ref.child("Event").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isComplete() && task.isSuccessful()){
                    DataSnapshot snap= task.getResult();
                    Iterator<DataSnapshot> iter = snap.getChildren().iterator();
                    if (snap.exists()){
                        while (iter.hasNext()){
                            DataSnapshot snapevent = iter.next();
                            boolean eventstatus;
                            eventstatus = Boolean.parseBoolean(snapevent.child("Event Status").getValue().toString());
                            if(!eventstatus){
                                boolean inevent=false;
                                Iterator<DataSnapshot> part =snapevent.child("Event Participants").getChildren().iterator();
                                while(part.hasNext()){
                                    DataSnapshot snappart = part.next();
                                    if(currentuserid.equals(snappart.getValue().toString())){
                                        inevent=true;
                                        break;
                                    }
                                }
                                if(inevent){
                                    values.add(Integer.valueOf(snapevent.child("Event ID").getValue().toString()));
                                }
                            }
                        }
                    }
                    MyEventListAdapter adapter = new MyEventListAdapter(MyEventsActivity.this,R.layout.event_record,values);
                    LV_MyEvents.setAdapter(adapter);
                }
            }
        });
    }

    public void ClickMenu(View view){redirectActivity(this,hamburger_nav.class);}

}