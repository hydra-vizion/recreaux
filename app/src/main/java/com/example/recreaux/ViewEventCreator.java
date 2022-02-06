package com.example.recreaux;

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

import com.example.recreaux.EditEventActivity;
import com.example.recreaux.EventMapsActivity;
import com.example.recreaux.EventRecords;
import com.example.recreaux.PostEventViewActivity;
import com.example.recreaux.R;
import com.example.recreaux.ViewEventParticipantsActivity;
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

public class ViewEventCreator extends AppCompatActivity {
    Button btnCreatorEditEvent,btnCreatorShareEvent,btnCreatorEndEvent;
    ImageView IV_CreatorEventIcon,IV_CreatorMapPreview;
    TextView TV_CreatorEventName,TV_CreatorEventDate,TV_CreatorEventTime,TV_CreatorEventDescription,
            TV_CreatorEventTags,TV_CreatorEventLocation,TV_CreatorParticipants;
    int eventid;
    EventRecords sentEvent;
    private DatabaseReference ref;
    public static Activity vieweventcreator;
    Bitmap mappreview;
    String currentuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_creator);

        vieweventcreator = this;
        btnCreatorEditEvent=findViewById(R.id.btnCreatorEditEvent);
        btnCreatorShareEvent=findViewById(R.id.btnCreatorShareEvent);
        btnCreatorEndEvent=findViewById(R.id.btnCreatorEndEvent);

        ref = FirebaseDatabase.getInstance().getReference();
        currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //currentuserid="OmnyApXKCAOdnll4Dc1RKoLLSKH3";
        TV_CreatorEventName = findViewById(R.id.TV_CreatorEventName);
        TV_CreatorEventDate = findViewById(R.id.TV_CreatorEventDate);
        TV_CreatorEventTime = findViewById(R.id.TV_CreatorEventTime);
        TV_CreatorEventDescription = findViewById(R.id.TV_CreatorEventDescription);
        TV_CreatorEventTags = findViewById(R.id.TV_CreatorEventTags);
        TV_CreatorEventLocation = findViewById(R.id.TV_CreatorEventLocation);
        IV_CreatorEventIcon = findViewById(R.id.IV_CreatorEventIcon);
        TV_CreatorParticipants = findViewById(R.id.TV_CreatorParticipants);
        IV_CreatorMapPreview = findViewById(R.id.IV_CreatorMapPreview);

        if(getIntent().getExtras() != null) {
            sentEvent = new EventRecords();
            eventid=getIntent().getExtras().getInt("eventid");
            ref.child("Event").child(String.valueOf(eventid)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isComplete() && task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
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
                        TV_CreatorEventName.setText("Event Name : "+sentEvent.getEventName());
                        TV_CreatorEventDate.setText("Event Date : "+sentEvent.getEventDate());
                        TV_CreatorEventTime.setText("Event Time : "+sentEvent.getEventTime());
                        TV_CreatorEventDescription.setText("Event Description : "+sentEvent.getEventDescription());
                        TV_CreatorEventTags.setText("Event Tags : "+sentEvent.getEventTags());
                        TV_CreatorEventLocation.setText("Event Location : "+sentEvent.getEventLocation());
                        IV_CreatorEventIcon.setImageBitmap(getImage(sentEvent.getIconID()));
                    }
                }
            });

            ArrayList<String> participant= new ArrayList<>();
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
                                ref.child("Users").child(participantuserid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.isComplete() && task.isSuccessful()){
                                            DataSnapshot snap= task.getResult();
                                            if (snap.exists()){
                                                String nickname=snap.child("Username").getValue().toString();
                                                participant.add(nickname);
                                                TV_CreatorParticipants.setText("Event Participants : "+participant.toString().replace("[", "").replace("]", ""));
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
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    String imej = task.getResult().getValue().toString();
                    mappreview =getImage(Base64.getDecoder().decode(imej));
                    IV_CreatorMapPreview.setImageBitmap(mappreview);
                }
            });
        }

        IV_CreatorMapPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEventCreator.this, EventMapsActivity.class);
                intent.putExtra("parent","viewonly");
                intent.putExtra("address",sentEvent.getEventLocation());
                intent.putExtra("latitude",Double.valueOf(sentEvent.getEventLocationLatitude()));
                intent.putExtra("longitude",Double.valueOf(sentEvent.getEventLocationLongitude()));
                startActivity(intent);
            }
        });

        TV_CreatorParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEventCreator.this,ViewEventParticipantsActivity.class);
                intent.putExtra("eventid",eventid);
                intent.putExtra("shareable",false);
                startActivity(intent);
            }
        });


        btnCreatorEndEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Event Ended", Toast.LENGTH_SHORT).show();
                ref.child("Event").child(String.valueOf(eventid)).child("Event Status").setValue(false);
                Intent intent = new Intent(ViewEventCreator.this, PostEventViewActivity.class);
                intent.putExtra("eventid",eventid);
                finish();
                startActivity(intent);
            }
        });

        btnCreatorEditEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Event Edited", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewEventCreator.this, EditEventActivity.class);
                intent.putExtra("eventid",eventid);
                //finish();
                startActivity(intent);
            }
        });

        btnCreatorShareEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Event Shared", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewEventCreator.this, ViewEventParticipantsActivity.class);
                intent.putExtra("eventid",eventid);
                intent.putExtra("shareable",true);
                startActivity(intent);
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
}