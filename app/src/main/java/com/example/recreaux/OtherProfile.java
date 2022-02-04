package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OtherProfile extends AppCompatActivity {
    Button btnAdd;
    DatabaseReference reff;
    String otherid;
    private Button Logout,gotoEdit;
    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        btnAdd=(Button)findViewById(R.id.Btn_OtherProfile_Add);

        otherid=getIntent().getExtras().getString("id");

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        userID = user.getUid();

        final TextView FullNameTextView= (TextView) findViewById(R.id.TV_OtherProfile_FullName);
        final TextView NickNameTextView= (TextView) findViewById(R.id.TV_OtherProfile_Nickname);
        final TextView BioTextView= (TextView) findViewById(R.id.TV_OtherProfile_Bio);
        final TextView PhoneNumberTextView= (TextView) findViewById(R.id.TV_OtherProfile_PhoneNumber);
        final TextView ResidenceTextView= (TextView) findViewById(R.id.TV_OtherProfile_Residence);
        final TextView InterestsTextView= (TextView) findViewById(R.id.TV_OtherProfile_Interests);

        reference.child(otherid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);

                if(userProfile != null){
                    if(!dataSnapshot.child("Bio").exists()){
                        String fullName = userProfile.Name;
                        FullNameTextView.setText(fullName);
                        String nickName = userProfile.Username;
                        NickNameTextView.setText(nickName);
                        BioTextView.setText("");
                        PhoneNumberTextView.setText("");
                        ResidenceTextView.setText("");
                        InterestsTextView.setText("");
                    }
                    else{
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
                Toast.makeText(OtherProfile.this,"Some error occured ", Toast.LENGTH_LONG).show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                reff= FirebaseDatabase.getInstance().getReference().child("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reff.child("friend_0").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                btnAdd.setClickable(false);
                btnAdd.setText("Pending");
                btnAdd.setBackgroundColor(btnAdd.getContext().getResources().getColor(R.color.white));
            }
        });
    }
}