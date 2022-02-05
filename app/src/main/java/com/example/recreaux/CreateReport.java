package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recreaux.PostEventViewActivity;
import com.example.recreaux.R;
import com.example.recreaux.ViewEventCreator;
import com.example.recreaux.ViewReportActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;

public class CreateReport extends AppCompatActivity {
    Button btn_PostAddReport,btn_CreateReportAddImage,btn_CreateReportDeleteImage;
    ImageView IV_CreateReportEventIcon;
    TextView TV_CreateReportEventName,TV_CreateReportEventDate,TV_CreateReportEventTime,TV_CreateReportEventDescription,TV_CreateReportEventTags,
            TV_CreateReportEventLocation,TV_CreateReportParticipants;
    EditText et_CreateReportPostEventReport;
    LinearLayout LL_CreateReportGallery;
    int eventid;
    com.example.recreaux.EventRecords sentEvent;
    private DatabaseReference ref;
    private static final int RESULT_LOAD_IMAGE=1;
    ArrayList<byte[]> allimage = new ArrayList<byte[]>();
    Bitmap imagegallery;
    String currentuserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);
        ref = FirebaseDatabase.getInstance().getReference();
        currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //currentuserid =currentuserid="OmnyApXKCAOdnll4Dc1RKoLLSKH3";
        btn_PostAddReport = findViewById(R.id.btnPostAddReport);
        et_CreateReportPostEventReport = findViewById(R.id.ET_CreateReportPostEventReport);
        TV_CreateReportEventName = findViewById(R.id.TV_CreateReportEventName);
        TV_CreateReportEventDate = findViewById(R.id.TV_CreateReportEventDate);
        TV_CreateReportEventTime = findViewById(R.id.TV_CreateReportEventTime);
        TV_CreateReportEventDescription = findViewById(R.id.TV_CreateReportEventDescription);
        TV_CreateReportEventTags = findViewById(R.id.TV_CreateReportEventTags);
        TV_CreateReportEventLocation = findViewById(R.id.TV_CreateReportEventLocation);
        TV_CreateReportParticipants = findViewById(R.id.TV_CreateReportParticipants);
        IV_CreateReportEventIcon = findViewById(R.id.IV_CreateReportEventIcon);
        btn_CreateReportAddImage = findViewById(R.id.btn_CreateReportAddImage);
        LL_CreateReportGallery = findViewById(R.id.LL_CreateReportGallery);
        btn_CreateReportDeleteImage = findViewById(R.id.btn_CreateReportDeleteImage);

        if(getIntent().getExtras() != null) {
            sentEvent = new com.example.recreaux.EventRecords();
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
                        TV_CreateReportEventName.setText("Event Name : "+sentEvent.getEventName());
                        TV_CreateReportEventDate.setText("Event Date : "+sentEvent.getEventDate());
                        TV_CreateReportEventTime.setText("Event Time : "+sentEvent.getEventTime());
                        TV_CreateReportEventDescription.setText("Event Description : "+sentEvent.getEventDescription());
                        TV_CreateReportEventTags.setText("Event Tags : "+sentEvent.getEventTags());
                        TV_CreateReportEventLocation.setText("Event Location : "+sentEvent.getEventLocation());
                        IV_CreateReportEventIcon.setImageBitmap(getImage(sentEvent.getIconID()));
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
                                                TV_CreateReportParticipants.setText("Event Participants : "+participant.toString().replace("[", "").replace("]", ""));
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }

        btn_PostAddReport.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Report Added", Toast.LENGTH_SHORT).show();
                String report=et_CreateReportPostEventReport.getText().toString();
                if(report.isEmpty()){
                    et_CreateReportPostEventReport.setError("Report cannot be empty!");
                    return;
                }
                ref.child("Event").child(String.valueOf(eventid)).child("Event Report").setValue(report);
                for (int i = 0; i < allimage.size(); i++) {
                    String s = Base64.getEncoder().encodeToString(allimage.get(i));
                    ref.child("Event").child(String.valueOf(eventid)).child("Event Gallery").child("image_"+String.valueOf(i)).setValue(s);
                }
                Intent intent = new Intent(CreateReport.this, ViewReportActivity.class);
                intent.putExtra("eventid",eventid);
                PostEventViewActivity.posteventviewactivity.finish();
                ViewEventCreator.vieweventcreator.finish();
                finish();
                startActivity(intent);
            }
        });
        btn_CreateReportAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
            }
        });

        btn_CreateReportDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateReport.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm Delete Images");
                builder.setMessage("Are you sure you want to delete your Images?");
                builder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LL_CreateReportGallery.removeAllViews();
                                allimage.clear();
                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!= null){
            Uri selectedImage = data.getData();
            try {
                imagegallery = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] imagebyte = getBytes(imagegallery);
            Bitmap imageforgallery = scaleCenterCrop(imagegallery,300,300);
            ImageView imageview = new ImageView(this);
            imageview.setImageBitmap(imageforgallery);
            imageview.setLayoutParams(new android.view.ViewGroup.LayoutParams(300,300));
            imageview.setMaxHeight(300);
            imageview.setMaxWidth(300);
            imageview.setPadding(4,0,4,0);
            allimage.add(imagebyte);
            LL_CreateReportGallery.addView(imageview);
        }
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