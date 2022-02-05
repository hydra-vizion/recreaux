package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Base64;

public class MyProfile extends AppCompatActivity implements View.OnClickListener{

    private Button Logout,gotoEdit;
    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        userID = user.getUid();

        final TextView FullNameTextView= (TextView) findViewById(R.id.TV_MyProfile_FullName);
        final TextView NickNameTextView= (TextView) findViewById(R.id.TV_MyProfile_Nickname);
        final TextView BioTextView= (TextView) findViewById(R.id.TV_MyProfile_Bio);
        final TextView PhoneNumberTextView= (TextView) findViewById(R.id.TV_MyProfile_PhoneNumber);
        final TextView ResidenceTextView= (TextView) findViewById(R.id.TV_MyProfile_Residence);
        final TextView InterestsTextView= (TextView) findViewById(R.id.TV_MyProfile_Interests);
        ImageView ProfileImageView= (ImageView) findViewById(R.id.IV_MyProfile_ProfilePic);
        ImageButton SearchButton=(ImageButton)findViewById(R.id.Btn_OtherProfile_Search);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);

                if(userProfile != null){
                    if(!dataSnapshot.child("Bio").exists()){
                        String fullName = userProfile.Name;
                        FullNameTextView.setText(fullName);
                        String nickName = userProfile.Username;
                        NickNameTextView.setText(nickName);}
                    else{
                        Bitmap icon= getImage(Base64.getDecoder().decode(userProfile.UserImage));
                        ProfileImageView.setImageBitmap(icon);
                        String fullName = userProfile.Name;
                        FullNameTextView.setText(fullName);
                        String nickName = userProfile.Username;
                        NickNameTextView.setText(nickName);
                        String bio = userProfile.Bio;
                        BioTextView.setText(bio);
                        String phoneNumber = userProfile.PhoneNumber;
                        PhoneNumberTextView.setText(phoneNumber);
                        String residence = userProfile.Residence;
                        ResidenceTextView.setText(residence);
                        String interests = userProfile.Interests;
                        InterestsTextView.setText(interests);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyProfile.this,"Some error occured ", Toast.LENGTH_LONG).show();
            }
        });

        Logout = (Button) findViewById(R.id.Btn_MyProfile_LogOut);
        Logout.setOnClickListener(this);

        gotoEdit = (Button) findViewById(R.id.Btn_MyProfile_Edit);
        gotoEdit.setOnClickListener(this);

        SearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MyProfile.this,FriendList.class);
                //intent.putExtra("id","Ub7oIFwv88h6INPAWO04s4F9asi2");
                startActivity(intent);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.Btn_MyProfile_LogOut:
                userLogout();
                break;
            case R.id.Btn_MyProfile_Edit:
                startActivity(new Intent(MyProfile.this,EditProfile.class));
                break;
        }
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MyProfile.this,Login.class));
    }


}