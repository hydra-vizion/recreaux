package com.example.recreaux;

import static com.example.recreaux.hamburger_nav.redirectActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recreaux.CreateReport;
import com.example.recreaux.EventMapsActivity;
import com.example.recreaux.EventRecords;
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

public class PostEventViewActivity extends AppCompatActivity {
    Button btn_PostAddReport;
    ImageView IV_PostEventIcon,IV_PostMapPreview;
    TextView TV_PostEventName,TV_PostEventDate,TV_PostEventTime,TV_PostEventDescription,
            TV_PostEventTags,TV_PostEventLocation,TV_PostParticipants;
    int eventid;
    Bitmap mappreview;
    EventRecords sentEvent;
    private DatabaseReference ref;
    public static Activity posteventviewactivity;
    String currentuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event_view);
        btn_PostAddReport = findViewById(R.id.btnPostAddReport);
        //currentuserid ="OmnyApXKCAOdnll4Dc1RKoLLSKH3";
        currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        posteventviewactivity = this;
        ref = FirebaseDatabase.getInstance().getReference();
        btn_PostAddReport = findViewById(R.id.btnPostAddReport);
        TV_PostEventName = findViewById(R.id.TV_PostEventName);
        TV_PostEventDate = findViewById(R.id.TV_PostEventDate);
        TV_PostEventTime = findViewById(R.id.TV_PostEventTime);
        TV_PostEventDescription = findViewById(R.id.TV_PostEventDescription);
        TV_PostEventTags = findViewById(R.id.TV_PostEventTags);
        TV_PostEventLocation = findViewById(R.id.TV_PostEventLocation);
        TV_PostParticipants = findViewById(R.id.TV_PostParticipants);
        IV_PostEventIcon = findViewById(R.id.IV_PostEventIcon);
        IV_PostMapPreview = findViewById(R.id.IV_PostMapPreview);

        if(getIntent().getExtras() != null) {
            sentEvent = new EventRecords();
            eventid=getIntent().getExtras().getInt("eventid");
            ref.child("Event").child(String.valueOf(eventid)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                        TV_PostEventName.setText("Event Name : "+sentEvent.getEventName());
                        TV_PostEventDate.setText("Event Date : "+sentEvent.getEventDate());
                        TV_PostEventTime.setText("Event Time : "+sentEvent.getEventTime());
                        TV_PostEventDescription.setText("Event Description : "+sentEvent.getEventDescription());
                        TV_PostEventTags.setText("Event Tags : "+sentEvent.getEventTags());
                        TV_PostEventLocation.setText("Event Location : "+sentEvent.getEventLocation());
                        IV_PostEventIcon.setImageBitmap(getImage(sentEvent.getIconID()));

                        if(!currentuserid.equals(sentEvent.getEventCreatorID())){
                            btn_PostAddReport.setVisibility(View.GONE);
                        }
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
                                                TV_PostParticipants.setText("Event Participants : "+participant.toString().replace("[", "").replace("]", ""));
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
                    IV_PostMapPreview.setImageBitmap(mappreview);
                }
            });
        }



        IV_PostMapPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostEventViewActivity.this, EventMapsActivity.class);
                intent.putExtra("parent","viewonly");
                intent.putExtra("address",sentEvent.getEventLocation());
                intent.putExtra("latitude",Double.valueOf(sentEvent.getEventLocationLatitude()));
                intent.putExtra("longitude",Double.valueOf(sentEvent.getEventLocationLongitude()));
                startActivity(intent);
            }
        });

        TV_PostParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostEventViewActivity.this, ViewEventParticipantsActivity.class);
                intent.putExtra("eventid",eventid);
                intent.putExtra("shareable",false);
                startActivity(intent);
            }
        });

        btn_PostAddReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Report Added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PostEventViewActivity.this, CreateReport.class);
                intent.putExtra("eventid",eventid);
                //finish();
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

    public void ClickMenu(View view){redirectActivity(this,hamburger_nav.class);}
}