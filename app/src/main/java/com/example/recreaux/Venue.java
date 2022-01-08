package com.example.recreaux;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Venue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);



        LinearLayout gallery = findViewById(R.id.gallery);

        LayoutInflater inflater = LayoutInflater.from(this);

        for(int i = 0; i < 6; i++){
            View view = inflater.inflate(R.layout.itemhorizontal, gallery,false);

            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_launcher_background);
            gallery.addView(view);
        }


        LinearLayout venueDetails = findViewById(R.id.venueDetails);
        LayoutInflater inflater2 = LayoutInflater.from(this);

        for(int i = 0; i < 6; i++){
            View view2 = inflater2.inflate(R.layout.venuedetailsverticals, venueDetails,false);

            TextView textView2 = view2.findViewById(R.id.textDetails);
            textView2.setText("Item "+i);

            ImageView imageView = view2.findViewById(R.id.iconDetails);
            imageView.setImageResource(R.drawable.ic_launcher_foreground);
            venueDetails.addView(view2);
        }
    }
}