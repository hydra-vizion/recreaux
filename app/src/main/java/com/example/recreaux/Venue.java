package com.example.recreaux;

import static com.example.recreaux.hamburger_nav.redirectActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Base64;

public class Venue extends AppCompatActivity {

    String venueId;
    TextView venName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        venName = (TextView) findViewById(R.id.venueName);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Venue");
        LinearLayout gallery = findViewById(R.id.gallery);
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout venueDetails = findViewById(R.id.venueScroll);
        LayoutInflater inflater2 = LayoutInflater.from(this);

       if(getIntent().getExtras() != null) {
            venueId=getIntent().getExtras().getString("venueid");
            //venueId = 2;

            reference.child(venueId).addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    venName.setText(dataSnapshot.child("VenueName").getValue().toString());

                    for (int i = 0; i<dataSnapshot.child("VenueImages").getChildrenCount(); i++){

                        View view = inflater.inflate(R.layout.itemhorizontal, gallery,false);
                        ImageView imageView = view.findViewById(R.id.imageView);
                        imageView.setImageBitmap(getImage(Base64.getDecoder().decode(dataSnapshot.child("VenueImages").child(String.valueOf(i+1)).getValue().toString())));
                        gallery.addView(view);
                    }
                    String[] contentDetails = {
                            dataSnapshot.child("VenueAddress").getValue().toString(),
                            dataSnapshot.child("VenueHours").getValue().toString(),
                            dataSnapshot.child("VenueWebsite").getValue().toString(),
                            dataSnapshot.child("VenueTag").getValue().toString(),
                            dataSnapshot.child("VenueRating").getValue().toString()
                    };
                    int[] drawble = {
                            R.drawable.location,
                            R.drawable.baseline_schedule_black_48,
                            R.drawable.globe,
                            R.drawable.tag,
                            android.R.drawable.star_off
                    };

                    for(int i = 0; i < 5; i++){
                        View view2 = inflater2.inflate(R.layout.venuedetailsverticals, venueDetails,false);

                        TextView textView2 = view2.findViewById(R.id.textDetails);
                        textView2.setText(contentDetails[i]);

                        ImageView imageView = view2.findViewById(R.id.iconDetails);
                        imageView.setImageResource(drawble[i]);
                        venueDetails.addView(view2);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
       }




//        for(int i = 0; i < 6; i++){
//            View view = inflater.inflate(R.layout.itemhorizontal, gallery,false);
//
//            ImageView imageView = view.findViewById(R.id.imageView);
//            imageView.setImageResource(R.drawable.ic_launcher_background);
//            gallery.addView(view);
//        }


    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public void ClickMenu(View view){redirectActivity(this,hamburger_nav.class);}
}