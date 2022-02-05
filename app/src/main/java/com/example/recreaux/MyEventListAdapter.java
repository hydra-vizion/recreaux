package com.example.recreaux;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recreaux.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

public class MyEventListAdapter extends ArrayAdapter<Integer> {
    private DatabaseReference ref;
    public MyEventListAdapter(Activity context, int resource, List<Integer> list) {
        super(context, resource, list);
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ref = FirebaseDatabase.getInstance().getReference();
        int index = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_record, parent, false);
        }

        ImageView iv_EventRecordImage = (ImageView) convertView.findViewById(R.id.iv_EventRecordImage);
        TextView tv_EventRecordName, tv_EventRecordDate,tv_EventRecordTime,tv_EventRecordParticipant,tv_EventRecordLocation;
        tv_EventRecordName = (TextView) convertView.findViewById(R.id.tv_EventRecordName);
        tv_EventRecordDate = (TextView) convertView.findViewById(R.id.tv_EventRecordDate);
        tv_EventRecordTime = (TextView) convertView.findViewById(R.id.tv_EventRecordTime);
        tv_EventRecordParticipant = (TextView) convertView.findViewById(R.id.tv_EventRecordParticipant);
        tv_EventRecordLocation = (TextView) convertView.findViewById(R.id.tv_EventRecordLocation);

        ref.child("Event").child(String.valueOf(index)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isComplete() && task.isSuccessful()){
                    DataSnapshot snap= task.getResult();
                    if (snap.exists()){
                        try{
                            String s = snap.child("Event Icon").getValue().toString();
                            iv_EventRecordImage.setImageBitmap(getImage(Base64.getDecoder().decode(s)));
                        } catch (Exception e) {
                            iv_EventRecordImage.setBackgroundColor(Color.BLUE);
                            e.printStackTrace();
                        }
                        String eventname=snap.child("Event Name").getValue().toString();
                        String eventdate=snap.child("Event Date").getValue().toString();
                        String eventtime=snap.child("Event Time").getValue().toString();
                        String eventlocation= snap.child("Event Location").getValue().toString();
                        int eventparticipantcount = (int)snap.child("Event Participants").getChildrenCount();
                        tv_EventRecordName.setText(eventname);
                        tv_EventRecordDate.setText(eventdate);
                        tv_EventRecordTime.setText(eventtime);
                        tv_EventRecordLocation.setText(eventlocation);
                        tv_EventRecordParticipant.setText(String.valueOf(eventparticipantcount)+" participants responded");
                    }
                }
            }
        });



        return convertView;
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
