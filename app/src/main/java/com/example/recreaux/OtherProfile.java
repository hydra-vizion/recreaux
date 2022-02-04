package com.example.recreaux;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OtherProfile extends AppCompatActivity {
Button btnAdd;
DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        btnAdd=(Button)findViewById(R.id.Btn_OtherProfile_Add);
        ref= FirebaseDatabase.getInstance().getReference();

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ref.child("User").child("4").child("addPending").child("user_1").setValue(1);
            }
        });
    }
}