package com.example.recreaux;

import static com.example.recreaux.hamburger_nav.redirectActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recreaux.EventMapsActivity;
import com.example.recreaux.EventRecords;
import com.example.recreaux.PostEventViewActivity;
import com.example.recreaux.R;
import com.example.recreaux.ViewEventParticipantsActivity;
import com.example.recreaux.ViewReportActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

public class ViewEvent extends AppCompatActivity {
    Button btnJoinLeaveEvent,btnViewShareEvent;
    ImageView IV_ViewEventIcon,IV_ViewMapPreview;
    TextView TV_ViewEventName,TV_ViewEventDate,TV_ViewEventTime,TV_ViewEventDescription,TV_ViewEventTags,
            TV_ViewEventLocation,TV_ViewParticipants;
    int eventid;
    EventRecords sentEvent;
    private DatabaseReference ref;
    Bitmap mappreview;
    ArrayList<String> participantname= new ArrayList<>();
    ArrayList<String> participantid= new ArrayList<>();
    String currentusername;
    String currentuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        btnJoinLeaveEvent = findViewById(R.id.btnJoinLeaveEvent);
        btnViewShareEvent = findViewById(R.id.btnViewShareEvent);
        //currentuserid ="OmnyApXKCAOdnll4Dc1RKoLLSKH3";
        currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference();
        TV_ViewEventName = findViewById(R.id.TV_ViewEventName);
        TV_ViewEventDate = findViewById(R.id.TV_ViewEventDate);
        TV_ViewEventTime = findViewById(R.id.TV_ViewEventTime);
        TV_ViewEventDescription = findViewById(R.id.TV_ViewEventDescription);
        TV_ViewEventTags = findViewById(R.id.TV_ViewEventTags);
        TV_ViewEventLocation = findViewById(R.id.TV_ViewEventLocation);
        TV_ViewParticipants = findViewById(R.id.TV_ViewParticipants);
        IV_ViewEventIcon = findViewById(R.id.IV_ViewEventIcon);
        IV_ViewMapPreview = findViewById(R.id.IV_ViewMapPreview);

        ref.child("Users").child(currentuserid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isComplete()&&task.isSuccessful()){
                    currentusername=task.getResult().child("Username").getValue().toString();
                }
            }
        });

        if(getIntent().getExtras() != null) {
            sentEvent = new EventRecords();
            eventid=getIntent().getExtras().getInt("eventid");
            ref.child("Event").child(String.valueOf(eventid)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isComplete() && task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();

                        if(!Boolean.parseBoolean(snapshot.child("Event Status").getValue().toString())){
                            boolean hasreport;
                            hasreport=snapshot.child("Event Report").exists();
                            if (hasreport){
                                Intent intent = new Intent(ViewEvent.this, ViewReportActivity.class);
                                intent.putExtra("eventid",eventid);
                                finish();
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(ViewEvent.this, PostEventViewActivity.class);
                                intent.putExtra("eventid",eventid);
                                finish();
                                startActivity(intent);
                            }
                        }
                        sentEvent.setEventID(Integer.valueOf(snapshot.child("Event ID").getValue().toString()));
                        sentEvent.setEventName(snapshot.child("Event Name").getValue().toString());
                        sentEvent.setEventDate(snapshot.child("Event Date").getValue().toString());
                        sentEvent.setEventTime(snapshot.child("Event Time").getValue().toString());
                        sentEvent.setEventLocation(snapshot.child("Event Location").getValue().toString());
                        sentEvent.setEventLocationLatitude(snapshot.child("Event Latitude").getValue().toString());
                        sentEvent.setEventLocationLongitude(snapshot.child("Event Longitude").getValue().toString());
                        sentEvent.setIconID(Base64.getDecoder().decode(snapshot.child("Event Icon").getValue().toString()));
                        sentEvent.setEventDescription(snapshot.child("Event Description").getValue().toString());
                        sentEvent.setEventTags(snapshot.child("Event Tags").getValue().toString());
                        sentEvent.setEventstatus(Boolean.parseBoolean(snapshot.child("Event Status").getValue().toString()));
                        String creatorid=snapshot.child("Event Creator").getValue().toString().split("_")[1];
                        sentEvent.setEventCreatorID(creatorid);
                        TV_ViewEventName.setText("Event Name : "+sentEvent.getEventName());
                        TV_ViewEventDate.setText("Event Date : "+sentEvent.getEventDate());
                        TV_ViewEventTime.setText("Event Time : "+sentEvent.getEventTime());
                        TV_ViewEventDescription.setText("Event Description : "+sentEvent.getEventDescription());
                        TV_ViewEventTags.setText("Event Tags : "+sentEvent.getEventTags());
                        TV_ViewEventLocation.setText("Event Location : "+sentEvent.getEventLocation());
                        IV_ViewEventIcon.setImageBitmap(getImage(sentEvent.getIconID()));
                    }
                }
            });


            ref.child("Event").child(String.valueOf(eventid)).child("Event Participants").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()&&task.isComplete()){
                        DataSnapshot snapshot = task.getResult();
                        Iterator<DataSnapshot> iter = snapshot.getChildren().iterator();
                        if (snapshot.exists()){
                            while (iter.hasNext()){
                                DataSnapshot snap = iter.next();
                                String participantuserid = snap.getValue().toString();
                                participantid.add(participantuserid);
                                ref.child("Users").child(participantuserid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.isComplete() && task.isSuccessful()){
                                            DataSnapshot snap= task.getResult();
                                            if (snap.exists()){
                                                String nickname=snap.child("Username").getValue().toString();
                                                participantname.add(nickname);
                                                TV_ViewParticipants.setText("Event Participants : "+participantname.toString().replace("[", "").replace("]", ""));
                                                if(participantid.contains(currentuserid)){
                                                    btnJoinLeaveEvent.setText("Leave Event");
                                                    btnJoinLeaveEvent.setBackgroundColor(0xFFBA1010);
                                                }
                                                else{
                                                    btnJoinLeaveEvent.setText("Join Event");
                                                    btnJoinLeaveEvent.setBackgroundColor(0xFF67DE73);
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });



            ref.child("Event").child(String.valueOf(eventid)).child("Event MapPreview").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    String imej = task.getResult().getValue().toString();
                    mappreview =getImage(Base64.getDecoder().decode(imej));
                    IV_ViewMapPreview.setImageBitmap(mappreview);
                }
            });
        }

        IV_ViewMapPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEvent.this, EventMapsActivity.class);
                intent.putExtra("parent","viewonly");
                intent.putExtra("address",sentEvent.getEventLocation());
                intent.putExtra("latitude",Double.valueOf(sentEvent.getEventLocationLatitude()));
                intent.putExtra("longitude",Double.valueOf(sentEvent.getEventLocationLongitude()));
                startActivity(intent);
            }
        });

        TV_ViewParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEvent.this, ViewEventParticipantsActivity.class);
                intent.putExtra("eventid",eventid);
                intent.putExtra("shareable",false);
                startActivity(intent);
            }
        });
        btnViewShareEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Event Shared", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewEvent.this,ViewEventParticipantsActivity.class);
                intent.putExtra("eventid",eventid);
                intent.putExtra("shareable",true);
                startActivity(intent);
            }
        });



        btnJoinLeaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(participantid.contains(currentuserid)){
                    btnJoinLeaveEvent.setText("Join Event");
                    btnJoinLeaveEvent.setBackgroundColor(0xFF67DE73);
                    participantname.remove(currentusername);
                    participantid.remove(currentuserid);
                    TV_ViewParticipants.setText("Event Participants : "+participantname.toString().replace("[", "").replace("]", ""));
                    Toast.makeText(getApplicationContext(), "Event Left", Toast.LENGTH_SHORT).show();
                    ref.child("Event").child(String.valueOf(eventid)).child("Event Participants").child(String.valueOf("user_"+currentuserid)).removeValue();
                }
                else{
                    btnJoinLeaveEvent.setText("Leave Event");
                    btnJoinLeaveEvent.setBackgroundColor(0xFFBA1010);
                    Toast.makeText(getApplicationContext(), "Event Joined", Toast.LENGTH_SHORT).show();
                    participantname.add(currentusername);
                    participantid.add(currentuserid);
                    TV_ViewParticipants.setText("Event Participants : "+participantname.toString().replace("[", "").replace("]", ""));
                    ref.child("Event").child(String.valueOf(eventid)).child("Event Participants").child(String.valueOf("user_"+currentuserid)).setValue(currentuserid);
                }
            }
        });

    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public void ClickMenu(View view){redirectActivity(this,hamburger_nav.class);}
}
