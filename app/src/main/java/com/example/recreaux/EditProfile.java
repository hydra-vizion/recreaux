package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
    EditText userNickname,userResidence,userInterests,userFullName,userBio,userPhoneNumber;
    Button btnConfirm;
    String email;
    User userProfile;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        userNickname=(EditText)findViewById(R.id.ET_EditProfile_Nickname);
        userResidence=(EditText)findViewById(R.id.ET_EditProfile_Residence);
        userInterests=(EditText)findViewById(R.id.ET_EditProfile_Interests);
        userFullName=(EditText)findViewById(R.id.ET_EditProfile_FullName);
        userBio=(EditText)findViewById(R.id.ET_EditProfile_Bio);
        userPhoneNumber=(EditText)findViewById(R.id.ET_EditProfile_PhoneNumber);
        btnConfirm=(Button)findViewById(R.id.Btn_EditProfile_Confirm);
        userProfile=new User();

        reff =FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Bio").exists()){
                    userNickname.setText(dataSnapshot.child("Username").getValue().toString());
                    userFullName.setText(dataSnapshot.child("Name").getValue().toString());
                    email=dataSnapshot.child("email").getValue().toString();
                }
                else{
                    userNickname.setText(dataSnapshot.child("Username").getValue().toString());
                    userResidence.setText(dataSnapshot.child("Residence").getValue().toString());
                    userInterests.setText(dataSnapshot.child("Interests").getValue().toString());
                    userFullName.setText(dataSnapshot.child("Name").getValue().toString());
                    userBio.setText(dataSnapshot.child("Bio").getValue().toString());
                    userPhoneNumber.setText(dataSnapshot.child("PhoneNumber").getValue().toString());
                    email=dataSnapshot.child("email").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditProfile.this,"Some error occured ", Toast.LENGTH_LONG).show();
            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                 User user = new User(userFullName.getText().toString().trim(),
                         userNickname.getText().toString().trim(),
                         email,
                         userResidence.getText().toString().trim(),
                         userInterests.getText().toString().trim(),
                         userBio.getText().toString().trim(),
                         userPhoneNumber.getText().toString().trim());

                 reff.setValue(user);
                 Toast.makeText(EditProfile.this,"Data inserted successfully", Toast.LENGTH_LONG).show();
                 startActivity(new Intent(EditProfile.this,MyProfile.class));

            }
        });
    }
}