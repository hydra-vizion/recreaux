package com.example.recreaux;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {
EditText userNickname,userResidence,userInterests,userFullName,userBio,userPhoneNumber;
Button btnConfirm;
UserRecords userRecords;
DatabaseReference ref;
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
        userRecords=new UserRecords();
        ref= FirebaseDatabase.getInstance().getReference().child("User").child("4");

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                userRecords.setUserNickname(userNickname.getText().toString().trim());
                userRecords.setUserResidence(userResidence.getText().toString().trim());
                userRecords.setUserInterests(userInterests.getText().toString().trim());
                userRecords.setUserFullName(userFullName.getText().toString().trim());
                userRecords.setUserBio(userBio.getText().toString().trim());
                userRecords.setUserPhoneNumber(userPhoneNumber.getText().toString().trim());
                ref.setValue(userRecords);
                Toast.makeText(EditProfile.this,"Data inserted succesfully",Toast.LENGTH_LONG).show();
            }
        });
    }
}