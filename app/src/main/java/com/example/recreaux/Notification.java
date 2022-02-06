package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Base64;
import java.util.Iterator;

public class Notification extends AppCompatActivity {
    DatabaseReference refNotification,refUser;
    LinearLayout notifications;
    String image,name,UserId,FriendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notifications=(LinearLayout)findViewById(R.id.LinearNotifications);

        LayoutInflater inflater = LayoutInflater.from(this);

        UserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        refUser= FirebaseDatabase.getInstance().getReference("Users");
        refNotification= FirebaseDatabase.getInstance().getReference("Notifications").child(UserId);


        refNotification.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                if(dataSnapshot.exists()){
                    while (iter.hasNext()){
                        DataSnapshot singleNotification= iter.next();

                            View view = inflater.inflate(R.layout.notification, notifications, false);
                            RelativeLayout NotificationBody = view.findViewById(R.id.NotificationFrame);
                            if((Boolean)singleNotification.child("Seen").getValue()){
                                NotificationBody.setBackgroundColor(Color.LTGRAY);
                            }
                            else{
                                NotificationBody.setBackgroundColor(0xFFF8F8F8);
                                refNotification.child(singleNotification.getKey().toString()).child("Seen").setValue(true);
                            }

                            if(singleNotification.child("UserID").exists()){
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Notification.this,OtherProfile.class);
                                        intent.putExtra("id",singleNotification.child("UserID").getValue().toString());
                                        startActivity(intent);
                                    }
                                });
                            }
                            if(singleNotification.child("Event ID").exists()){
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Notification.this,ViewEvent.class);
                                    intent.putExtra("eventid",Integer.valueOf(singleNotification.child("Event ID").getValue().toString()));
                                    startActivity(intent);
                                }
                            });
                        }


                            ImageView imageView = view.findViewById(R.id.IM_Notification_ProfilePic);
                            String UserImage = singleNotification.child("UserImage").getValue().toString();
                            imageView.setImageBitmap(getImage(Base64.getDecoder().decode(UserImage)));

                            TextView textMessage = view.findViewById(R.id.TV_Notification_Name);
                            textMessage.setText(singleNotification.child("Message").getValue().toString());
                            notifications.addView(view,0);


                    }
                }
            }@Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Notification.this,"Some error occured ", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}