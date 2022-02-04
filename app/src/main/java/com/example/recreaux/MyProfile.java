package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.Name;

                    FullNameTextView.setText(fullName);
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

    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MyProfile.this,Login.class));
    }


}