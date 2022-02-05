package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity implements View.OnClickListener {

    private Button forgotPassword;
    private EditText editTextForgotEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

    //Forgot password module
        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (Button) findViewById(R.id.BTN_forgor_password);
        forgotPassword.setOnClickListener(this);

        editTextForgotEmail = findViewById(R.id.TI_Forgot_email);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.BTN_forgor_password:
                password_reset();
                break;
        }
    }

    private void password_reset() {
        String email = editTextForgotEmail.getText().toString().trim();

        if(email.isEmpty()){
            editTextForgotEmail.setError("Please enter your email");
            editTextForgotEmail.requestFocus();
            return;
        }
        /*
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter valid email");
            editTextEmail.requestFocus();
            return;
        } */

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Forgot_Password.this,"Email sent",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(Forgot_Password.this,"Please check if its the correct email",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}