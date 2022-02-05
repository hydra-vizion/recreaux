package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.recreaux.EventRecords;
import com.example.recreaux.R;
import com.example.recreaux.ViewEventParticipantsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

public class ViewReportActivity extends AppCompatActivity {
    LinearLayout LL_ViewReportGallery;
    ImageView IV_ViewReportEventIcon,ViewReportShare;
    TextView TV_ViewReportEventName,TV_ViewReportEventDate,TV_ViewReportEventTime, TV_ViewReportEventDescription,
            TV_ViewReportEventTags,TV_ViewReportEventLocation,TV_ViewReportParticipants,TV_ViewReportPostEventReport;
    int eventid;
    EventRecords sentEvent;
    private DatabaseReference ref;
    ArrayList<byte[]> allimage = new ArrayList<byte[]>();
    String currentuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        ref = FirebaseDatabase.getInstance().getReference();
        //currentuserid ="OmnyApXKCAOdnll4Dc1RKoLLSKH3";
        currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        TV_ViewReportEventName = findViewById(R.id.TV_ViewReportEventName);
        TV_ViewReportEventDate = findViewById(R.id.TV_ViewReportEventDate);
        TV_ViewReportEventTime = findViewById(R.id.TV_ViewReportEventTime);
        TV_ViewReportEventDescription = findViewById(R.id.TV_ViewReportEventDescription);
        TV_ViewReportEventTags = findViewById(R.id.TV_ViewReportEventTags);
        TV_ViewReportEventLocation = findViewById(R.id.TV_ViewReportEventLocation);
        TV_ViewReportParticipants = findViewById(R.id.TV_ViewReportParticipants);
        TV_ViewReportPostEventReport = findViewById(R.id.TV_ViewReportPostEventReport);
        IV_ViewReportEventIcon = findViewById(R.id.IV_ViewReportEventIcon);
        LL_ViewReportGallery = findViewById(R.id.LL_ViewReportGallery);
        ViewReportShare = findViewById(R.id.ViewReportShare);

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
                        sentEvent.setEventPostReport(snapshot.child("Event Report").getValue().toString());
                        String creatorid=snapshot.child("Event Creator").getValue().toString().split("_")[1];
                        sentEvent.setEventCreatorID(creatorid);
                        TV_ViewReportEventName.setText("Event Name : "+sentEvent.getEventName());
                        TV_ViewReportEventDate.setText("Event Date : "+sentEvent.getEventDate());
                        TV_ViewReportEventTime.setText("Event Time : "+sentEvent.getEventTime());
                        TV_ViewReportEventDescription.setText("Event Description : "+sentEvent.getEventDescription());
                        TV_ViewReportEventTags.setText("Event Tags : "+sentEvent.getEventTags());
                        TV_ViewReportEventLocation.setText("Event Location : "+sentEvent.getEventLocation());
                        TV_ViewReportPostEventReport.setText("Event Report : "+sentEvent.getEventPostReport());
                        IV_ViewReportEventIcon.setImageBitmap(getImage(sentEvent.getIconID()));
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
                                                TV_ViewReportParticipants.setText("Event Participants : "+participant.toString().replace("[", "").replace("]", ""));
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });

            ref.child("Event").child(String.valueOf(eventid)).child("Event Gallery").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterator<DataSnapshot> iter = snapshot.getChildren().iterator();
                    if (snapshot.exists()){
                        while (iter.hasNext()){
                            DataSnapshot snap = iter.next();
                            String galleryid = snap.getKey();
                            ref.child("Event").child(String.valueOf(eventid)).child("Event Gallery").child(galleryid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    task.getResult().getValue().toString();
                                    Bitmap image =getImage(Base64.getDecoder().decode(String.valueOf(task.getResult().getValue())));
                                    Bitmap imageforgallery = scaleCenterCrop(image,300,300);
                                    ImageView imageview = new ImageView(ViewReportActivity.this);
                                    imageview.setImageBitmap(imageforgallery);
                                    imageview.setLayoutParams(new android.view.ViewGroup.LayoutParams(300,300));
                                    imageview.setMaxHeight(300);
                                    imageview.setMaxWidth(300);
                                    imageview.setPadding(4,0,4,0);
                                    LL_ViewReportGallery.addView(imageview);
                                }
                            });
                        }
                    }
                    else{

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            for (int i = 0; i < allimage.size(); i++) {
                Bitmap images = getImage(allimage.get(i));
                Bitmap imageforgallery = scaleCenterCrop(images,300,300);
                ImageView imageview = new ImageView(this);
                imageview.setImageBitmap(imageforgallery);
                imageview.setLayoutParams(new android.view.ViewGroup.LayoutParams(300,300));
                imageview.setMaxHeight(300);
                imageview.setMaxWidth(300);
                imageview.setPadding(4,0,4,0);
                LL_ViewReportGallery.addView(imageview);
            }
        }

        TV_ViewReportParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewReportActivity.this, ViewEventParticipantsActivity.class);
                intent.putExtra("eventid",eventid);
                intent.putExtra("shareable",false);
                startActivity(intent);
            }
        });
        ViewReportShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewReportActivity.this,ViewEventParticipantsActivity.class);
                intent.putExtra("eventid",eventid);
                intent.putExtra("shareable",true);
                startActivity(intent);
            }
        });
    }

    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
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