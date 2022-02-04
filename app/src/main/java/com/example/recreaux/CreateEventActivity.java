package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.recreaux.EventRecords;
import com.example.recreaux.R;
import com.example.recreaux.ViewEventCreator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {
    EditText et_CreateEventDate;
    EditText et_CreateEventTime;
    EditText et_CreateEventName;
    EditText et_CreateEventDescription;
    EditText et_CreateEventTags;
    EditText et_CreateEventLocation;
    ImageView iv_CreateEventIcon;
    ImageView IV_CreateMapPreview;
    Button btn_CreateEvent;
    Bitmap imageicon,mappreview;
    double placelongitude,placelatitude;
    public static int currentuserid;
    public static int highestid;
    boolean iconselected,locationselected;


    private DatabaseReference ref;

    private static final int RESULT_LOAD_IMAGE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ref = FirebaseDatabase.getInstance().getReference();

        currentuserid=2;////////////////////////////////////////////////Change here later//////////////////////////////////////////////////////
        iconselected=false;
        locationselected=false;
        IV_CreateMapPreview = findViewById(R.id.IV_CreateMapPreview);
        btn_CreateEvent = findViewById(R.id.btnCreateEvent);
        et_CreateEventName = findViewById(R.id.ET_CreateEventName);
        et_CreateEventDate = findViewById(R.id.ET_CreateEventDate);
        et_CreateEventTime = findViewById(R.id.ET_CreateEventTime);
        et_CreateEventDescription = findViewById(R.id.ET_CreateEventDescription);
        et_CreateEventTags = findViewById(R.id.ET_CreateEventTags);
        et_CreateEventLocation = findViewById(R.id.ET_CreateEventLocation);
        iv_CreateEventIcon = findViewById(R.id.IV_CreateEventIcon);
        et_CreateEventDate.setInputType(InputType.TYPE_NULL);
        et_CreateEventTime.setInputType(InputType.TYPE_NULL);
        et_CreateEventLocation.setInputType(InputType.TYPE_NULL);

        ref.child("Event").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> iter = snapshot.getChildren().iterator();
                if (snapshot.exists()){
                    while (iter.hasNext()){
                        DataSnapshot snap = iter.next();
                        String nodId = snap.getKey();
                        if(Integer.valueOf(nodId)>=highestid){
                            CreateEventActivity.highestid=Integer.valueOf(nodId);
                        }
                        //System.out.println(snap.child("Event Name").getValue());
                    }
                }
                else{
                    highestid=-1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        et_CreateEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_CreateEventLocation.setError(null);
                /*Intent intent = new Intent(CreateEventActivity.this,EventMapsActivity.class);
                intent.putExtra("parent","create");
                intent.putExtra("height",IV_CreateMapPreview.getHeight());
                intent.putExtra("width",IV_CreateMapPreview.getWidth());
                startActivityForResult(intent,0);*/
            }
        });
        et_CreateEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_CreateEventLocation.setError(null);
                /*Intent intent = new Intent(CreateEventActivity.this,EventMapsActivity.class);
                intent.putExtra("parent","create");
                intent.putExtra("height",IV_CreateMapPreview.getHeight());
                intent.putExtra("width",IV_CreateMapPreview.getWidth());
                startActivityForResult(intent,0);*/
            }
        });

        et_CreateEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_CreateEventDate.setError(null);
                et_CreateEventTime.setError(null);
                showDateDialog(et_CreateEventDate);
            }
        });
        et_CreateEventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                et_CreateEventDate.setError(null);
                et_CreateEventTime.setError(null);
                if(hasFocus==true){
                    showDateDialog(et_CreateEventDate);
                }
            }
        });


        et_CreateEventTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus==true){
                    showTimeDialog(et_CreateEventTime);
                }
            }
        });
        et_CreateEventTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                showTimeDialog(et_CreateEventTime);
            }
        });

        btn_CreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveRecord(v);
                saveRecord2(v);
            }
        });
        iv_CreateEventIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
            }
        });


    }

    private void showTimeDialog(EditText timeinput) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                timeinput.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new TimePickerDialog(CreateEventActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
    }

    private void showDateDialog(EditText dateinput) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateinput.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(CreateEventActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!= null){
            iv_CreateEventIcon.setBackground(null);
            Uri selectedImage = data.getData();
            try {
                imageicon = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] test = getBytes(imageicon);
            Bitmap test2= getImage(test);
            Bitmap imageforicon = scaleCenterCrop(imageicon,iv_CreateEventIcon.getHeight(),iv_CreateEventIcon.getWidth());
            iv_CreateEventIcon.setImageBitmap(imageforicon);
            iconselected=true;
        }
        else if(requestCode==0 && resultCode == RESULT_OK && data!=null){
            et_CreateEventLocation.setText(data.getStringExtra("address"));
            placelatitude = data.getDoubleExtra("latitude",0);
            placelongitude = data.getDoubleExtra("longitude",0);
            byte[] mapprev = data.getByteArrayExtra("mappreview");
            mappreview = getImage(mapprev);
            mappreview=scaleCenterCrop(mappreview,IV_CreateMapPreview.getHeight(),IV_CreateMapPreview.getWidth());
            IV_CreateMapPreview.setImageBitmap(mappreview);
            locationselected=true;
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

    public void saveRecord2(View v) {
        et_CreateEventName = findViewById(R.id.ET_CreateEventName);
        et_CreateEventDate = findViewById(R.id.ET_CreateEventDate);
        et_CreateEventTime = findViewById(R.id.ET_CreateEventTime);
        et_CreateEventDescription = findViewById(R.id.ET_CreateEventDescription);
        et_CreateEventTags = findViewById(R.id.ET_CreateEventTags);
        et_CreateEventLocation = findViewById(R.id.ET_CreateEventLocation);
        iv_CreateEventIcon = findViewById(R.id.IV_CreateEventIcon);


        String name,date,time,description,tags,location;
        byte[] eventicon;
        if(iconselected==false){
            Toast.makeText(CreateEventActivity.this, "Please select an Icon for your event", Toast.LENGTH_SHORT).show();
            return;
        }
        name=et_CreateEventName.getText().toString();
        if(name.isEmpty()){
            et_CreateEventName.setError("Name cannot be empty!");
            return;
        }
        date=et_CreateEventDate.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String getCurrentDateTime = sdf.format(c.getTime());

        if(date.isEmpty()){
            et_CreateEventDate.setError("Date cannot be empty!");
            return;
        }
        time=et_CreateEventTime.getText().toString();
        if(time.isEmpty()){
            et_CreateEventTime.setError("Email cannot be empty!");
            return;
        }
        try {
            Date currentdate = sdf.parse(getCurrentDateTime);
            Date selecteddate = sdf.parse(date+" "+time);
            if(selecteddate.before(currentdate)){
                et_CreateEventDate.setError("Current date and time cannot be in the past!");
                et_CreateEventTime.setError("Current date and time cannot be in the past!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        description=et_CreateEventDescription.getText().toString();
        if(description.isEmpty()){
            et_CreateEventDescription.setError("Description cannot be empty!");
            return;
        }
        tags=et_CreateEventTags.getText().toString();
        if(tags.isEmpty()){
            et_CreateEventTags.setError("Tags cannot be empty!");
            return;
        }
        location=et_CreateEventLocation.getText().toString();
        if(location.isEmpty() || locationselected ==false){
            et_CreateEventLocation.setError("Invalid location.!");
            return;
        }
        BitmapDrawable drawable = (BitmapDrawable) iv_CreateEventIcon.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        eventicon=getBytes(bitmap);

        highestid++;

        ref.child("Event").child(String.valueOf(highestid)).child("Event Name").setValue(name);
        ref.child("Event").child(String.valueOf(highestid)).child("Event Date").setValue(date);
        ref.child("Event").child(String.valueOf(highestid)).child("Event Time").setValue(time);
        ref.child("Event").child(String.valueOf(highestid)).child("Event Description").setValue(description);
        ref.child("Event").child(String.valueOf(highestid)).child("Event Tags").setValue(tags);
        ref.child("Event").child(String.valueOf(highestid)).child("Event Location").setValue(location);
        ref.child("Event").child(String.valueOf(highestid)).child("Event Latitude").setValue(placelatitude);
        ref.child("Event").child(String.valueOf(highestid)).child("Event Longitude").setValue(placelongitude);
        //String s = Base64.getEncoder().encodeToString(eventicon);
        //ref.child("Event").child(String.valueOf(highestid)).child("Event Icon").setValue(s);
        ref.child("Event").child(String.valueOf(highestid)).child("Event ID").setValue(String.valueOf(highestid));
        ref.child("Event").child(String.valueOf(highestid)).child("Event Creator").setValue("user_"+String.valueOf(currentuserid));
        ref.child("Event").child(String.valueOf(highestid)).child("Event Participants").child("user_"+String.valueOf(currentuserid)).setValue(currentuserid);
        //String s2 = Base64.getEncoder().encodeToString(getBytes(mappreview));
        //ref.child("Event").child(String.valueOf(highestid)).child("Event MapPreview").setValue(s2);
        ref.child("Event").child(String.valueOf(highestid)).child("Event Status").setValue(true);
        Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_SHORT).show();

        EventRecords eventRecords = new EventRecords();
        eventRecords.setEventName(name);
        eventRecords.setEventDate(date);
        eventRecords.setEventTime(time);
        eventRecords.setEventDescription(description);
        eventRecords.setEventTags(tags);
        eventRecords.setEventLocation(location);
        eventRecords.setEventLocationLatitude(String.valueOf(placelatitude));
        eventRecords.setEventLocationLongitude(String.valueOf(placelongitude));
        //eventRecords.setIconID(eventicon);
        ArrayList<String> participant = new ArrayList<String>();
        participant.add(String.valueOf(currentuserid));
        eventRecords.setEventParticipants(participant);
        //eventRecords.setEventstatus(true);
        eventRecords.setEventID(highestid);
        eventRecords.setEventCreatorID(currentuserid);


        Intent intent = new Intent(CreateEventActivity.this, ViewEventCreator.class);
        intent.putExtra("eventrecord",eventRecords);
        this.finish();
        startActivity(intent);

        //iv_CreateEventIcon.setImageBitmap(getImage(Base64.getDecoder().decode(s)));

        /*
        ref.child("Event").child("1").child("Event Icon").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    //System.out.println(String.valueOf(task.getResult().getValue()));
                    iv_CreateEventIcon.setImageBitmap(getImage(Base64.getDecoder().decode(String.valueOf(task.getResult().getValue()))));
                }
            }
        });
        ref.child("Event").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    //System.out.println(String.valueOf(task.getResult().getValue()));
                }
            }
        });*/


    }
}