package com.example.recreaux;

import static com.example.recreaux.hamburger_nav.redirectActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.recreaux.EventMapsActivity;
import com.example.recreaux.R;
import com.example.recreaux.ViewEventCreator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class EditEventActivity extends AppCompatActivity {
    EditText et_EditEventTime;
    EditText et_EditEventName;
    EditText et_EditEventDate;
    EditText et_EditEventDescription;
    EditText et_EditEventTags;
    EditText et_EditEventLocation;
    Button btn_EditEventConfirm;
    Button btn_EditEventDelete;
    Button btn_EditEventCancel;
    ImageView iv_EditEventIcon,IV_EditMapPreview;
    Bitmap imageicon,mappreview;;
    double placelongitude,placelatitude;
    int eventid;
    String currentuserid;

    com.example.recreaux.EventRecords sentEvent;
    private DatabaseReference ref;
    private static final int RESULT_LOAD_IMAGE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ref = FirebaseDatabase.getInstance().getReference();
        currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //currentuserid ="OmnyApXKCAOdnll4Dc1RKoLLSKH3";
        et_EditEventDate = findViewById(R.id.ET_EditEventDate);
        et_EditEventTime = findViewById(R.id.ET_EditEventTime);
        btn_EditEventConfirm = findViewById(R.id.btnEditEventConfirm);
        btn_EditEventDelete = findViewById(R.id.btnEditEventDelete);
        btn_EditEventCancel = findViewById(R.id.btn_EditEventCancel);
        iv_EditEventIcon = findViewById(R.id.IV_EditEventIcon);
        et_EditEventName = findViewById(R.id.ET_EditEventName);
        et_EditEventDate.setInputType(InputType.TYPE_NULL);
        et_EditEventTime.setInputType(InputType.TYPE_NULL);
        et_EditEventDescription = findViewById(R.id.ET_EditEventDescription);
        et_EditEventTags = findViewById(R.id.ET_EditEventTags);
        et_EditEventLocation = findViewById(R.id.ET_EditEventLocation);
        IV_EditMapPreview = findViewById(R.id.IV_EditMapPreview);

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
                        et_EditEventName.setText(sentEvent.getEventName());
                        et_EditEventDate.setText(sentEvent.getEventDate());
                        et_EditEventTime.setText(sentEvent.getEventTime());
                        et_EditEventDescription.setText(sentEvent.getEventDescription());
                        et_EditEventTags.setText(sentEvent.getEventTags());
                        et_EditEventLocation.setText(sentEvent.getEventLocation());
                        iv_EditEventIcon.setImageBitmap(getImage(sentEvent.getIconID()));
                    }
                }
            });

            ref.child("Event").child(String.valueOf(eventid)).child("Event MapPreview").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    String imej = task.getResult().getValue().toString();
                    mappreview =getImage(Base64.getDecoder().decode(imej));
                    IV_EditMapPreview.setImageBitmap(mappreview);
                }
            });
        }

        et_EditEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_EditEventLocation.setError(null);
                Intent intent = new Intent(EditEventActivity.this, EventMapsActivity.class);
                intent.putExtra("parent","edit");
                intent.putExtra("height",IV_EditMapPreview.getHeight());
                intent.putExtra("width",IV_EditMapPreview.getWidth());
                startActivityForResult(intent,0);
            }
        });
        et_EditEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_EditEventLocation.setError(null);
                Intent intent = new Intent(EditEventActivity.this,EventMapsActivity.class);
                intent.putExtra("parent","edit");
                intent.putExtra("height",IV_EditMapPreview.getHeight());
                intent.putExtra("width",IV_EditMapPreview.getWidth());
                startActivityForResult(intent,0);
            }
        });

        et_EditEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_EditEventDate.setError(null);
                et_EditEventTime.setError(null);
                showDateDialog(et_EditEventDate);
            }
        });
        et_EditEventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                et_EditEventDate.setError(null);
                et_EditEventTime.setError(null);
                if(hasFocus==true){
                    showDateDialog(et_EditEventDate);
                }
            }
        });
        et_EditEventTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){

                    showTimeDialog(et_EditEventTime);
                }
            }
        });
        et_EditEventTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                showTimeDialog(et_EditEventTime);
            }
        });

        btn_EditEventConfirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //updateRecord(v);
                updateRecord2(v);
                Toast.makeText(getApplicationContext(), "Confirm Edit Event", Toast.LENGTH_SHORT).show();
            }
        });

        btn_EditEventDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteRecord(v,currentEventRecord);

                AlertDialog.Builder builder = new AlertDialog.Builder(EditEventActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm Delete Event");
                builder.setMessage("Are you sure you want to delete the event?");
                builder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteRecord2(v);
                                Toast.makeText(getApplicationContext(), "Event Deleted", Toast.LENGTH_SHORT).show();
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
        btn_EditEventCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelUpdate(v);
            }
        });
        iv_EditEventIcon.setOnClickListener(new View.OnClickListener() {
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
        new TimePickerDialog(EditEventActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
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
        new DatePickerDialog(EditEventActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!= null){
            iv_EditEventIcon.setBackground(null);
            Uri selectedImage = data.getData();
            try {
                imageicon = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] test = getBytes(imageicon);
            Bitmap test2= getImage(test);
            Bitmap imageforicon = scaleCenterCrop(imageicon,iv_EditEventIcon.getHeight(),iv_EditEventIcon.getHeight());
            iv_EditEventIcon.setImageBitmap(imageforicon);
        }
        else if(requestCode==0 && resultCode == RESULT_OK && data!=null){
            et_EditEventLocation.setText(data.getStringExtra("address"));
            placelatitude = data.getDoubleExtra("latitude",0);
            placelongitude = data.getDoubleExtra("longitude",0);
            byte[] mapprev = data.getByteArrayExtra("mappreview");
            mappreview = getImage(mapprev);
            mappreview=scaleCenterCrop(mappreview,IV_EditMapPreview.getHeight(),IV_EditMapPreview.getWidth());
            IV_EditMapPreview.setImageBitmap(mappreview);

        }
    }

    public void CancelUpdate(View v){
        this.finish();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateRecord2(View v){

        et_EditEventName = findViewById(R.id.ET_EditEventName);
        et_EditEventDate = findViewById(R.id.ET_EditEventDate);
        et_EditEventTime = findViewById(R.id.ET_EditEventTime);
        et_EditEventDescription = findViewById(R.id.ET_EditEventDescription);
        et_EditEventTags = findViewById(R.id.ET_EditEventTags);
        et_EditEventLocation = findViewById(R.id.ET_EditEventLocation);
        iv_EditEventIcon = findViewById(R.id.IV_EditEventIcon);


        String name,date,time,description,tags,location;
        byte[] eventicon;
        name=et_EditEventName.getText().toString();
        if(name.isEmpty()){
            et_EditEventName.setError("Name cannot be empty!");
            return;
        }
        date=et_EditEventDate.getText().toString();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String getCurrentDateTime = sdf.format(c.getTime());

        if(date.isEmpty()){
            et_EditEventDate.setError("Date cannot be empty!");
            return;
        }
        time=et_EditEventTime.getText().toString();
        if(time.isEmpty()){
            et_EditEventTime.setError("Email cannot be empty!");
            return;
        }
        try {
            Date currentdate = sdf.parse(getCurrentDateTime);
            Date selecteddate = sdf.parse(date+" "+time);
            if(selecteddate.before(currentdate)){
                et_EditEventDate.setError("Current date and time cannot be in the past!");
                et_EditEventTime.setError("Current date and time cannot be in the past!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        description=et_EditEventDescription.getText().toString();
        if(description.isEmpty()){
            et_EditEventDescription.setError("Description cannot be empty!");
            return;
        }
        tags=et_EditEventTags.getText().toString();
        if(tags.isEmpty()){
            et_EditEventTags.setError("Tags cannot be empty!");
            return;
        }
        location=et_EditEventLocation.getText().toString();
        if(location.isEmpty()){
            et_EditEventLocation.setError("Location cannot be empty!");
            return;
        }
        BitmapDrawable drawable = (BitmapDrawable) iv_EditEventIcon.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        eventicon=getBytes(bitmap);

        ref.child("Event").child(String.valueOf(eventid)).child("Event Name").setValue(name);
        ref.child("Event").child(String.valueOf(eventid)).child("Event Date").setValue(date);
        ref.child("Event").child(String.valueOf(eventid)).child("Event Time").setValue(time);
        ref.child("Event").child(String.valueOf(eventid)).child("Event Description").setValue(description);
        ref.child("Event").child(String.valueOf(eventid)).child("Event Tags").setValue(tags);
        ref.child("Event").child(String.valueOf(eventid)).child("Event Location").setValue(location);
        ref.child("Event").child(String.valueOf(eventid)).child("Event Latitude").setValue(placelatitude);
        ref.child("Event").child(String.valueOf(eventid)).child("Event Longitude").setValue(placelongitude);
        String s = Base64.getEncoder().encodeToString(eventicon);
        ref.child("Event").child(String.valueOf(eventid)).child("Event Icon").setValue(s);
        String s2 = Base64.getEncoder().encodeToString(getBytes(mappreview));
        ref.child("Event").child(String.valueOf(eventid)).child("Event MapPreview").setValue(s2);

        ViewEventCreator.vieweventcreator.finish();
        this.finish();
        Intent intent = new Intent(EditEventActivity.this,ViewEventCreator.class);
        intent.putExtra("eventid",eventid);
        startActivity(intent);
    }
    public void deleteRecord2(View v){
        ref.child("Event").child(String.valueOf(eventid)).removeValue();
        ViewEventCreator.vieweventcreator.finish();
        this.finish();
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