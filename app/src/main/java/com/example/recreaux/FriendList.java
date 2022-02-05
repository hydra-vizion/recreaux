package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class FriendList extends AppCompatActivity {
    DatabaseReference refFriend,refUser;
    LinearLayout friends;
    String image,name,UserId,FriendId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        friends=(LinearLayout)findViewById(R.id.LinearFriends);

        LayoutInflater inflater = LayoutInflater.from(this);

        UserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        refUser= FirebaseDatabase.getInstance().getReference("Users");
        refFriend= FirebaseDatabase.getInstance().getReference("Friends").child(UserId);


        refFriend.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                if(dataSnapshot.exists()){
                    while (iter.hasNext()){
                        DataSnapshot singleFriend= iter.next();
                        FriendId=singleFriend.getKey().toString();
                        if(!FriendId.equals(UserId)){
                            refUser.child(FriendId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful() && task.isComplete()){
                                        DataSnapshot snapshot=task.getResult();
                                        View view = inflater.inflate(R.layout.friend, friends, false);
                                        ImageView imageView = view.findViewById(R.id.IM_Friend_ProfilePic);
                                        imageView.setImageBitmap(getImage(Base64.getDecoder().decode(snapshot.child("UserImage").getValue().toString())));
                                        TextView textName = view.findViewById(R.id.TV_Friend_Name);
                                        textName.setText(snapshot.child("Username").getValue().toString());
                                        friends.addView(view);
                                    }
                                }
                        });}

                    }
                }
    }@Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendList.this,"Some error occured ", Toast.LENGTH_LONG).show();
            }
    });

}
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}