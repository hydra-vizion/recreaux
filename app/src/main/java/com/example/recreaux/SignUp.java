package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class SignUp extends AppCompatActivity implements  View.OnClickListener {

    public TextView textView;
    private EditText editTextName, editTextUsername,editTextEmail,editTextPassword;
    private Button registerUser;
    private FirebaseAuth mAuth;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_up);
        reff= FirebaseDatabase.getInstance().getReference().child("Friends");

        textView = findViewById(R.id.TV_login_nav);

        String text = "Already have an Account? Login";

        SpannableString ss = new SpannableString(text);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,Login.class));

            }
        });

        ClickableSpan clickableLogin = new ClickableSpan() {
            @Override
            public void onClick(View Widget) {
                Intent intent = new Intent(SignUp.this,Login.class);

            }
        };

        ss.setSpan(clickableLogin,25,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

        textView.setText(ss);

        //SignUp module
        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.BTN_SignUp);
        registerUser.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.TI_Login_Email);
        editTextName  = (EditText) findViewById(R.id.TI_SignUp_name);
        editTextPassword = (EditText) findViewById(R.id.TI_Forgot_email);
        editTextUsername = (EditText) findViewById(R.id.TI_SignUp_profile_username);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BTN_SignUp:
                registerUser();
                break;
        }
    }



    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String Name = editTextName.getText().toString().trim();
        String Username = editTextUsername.getText().toString().trim();
        String UserQR = null;

        MultiFormatWriter writer = new MultiFormatWriter();


        try {
            BitMatrix matrix = writer.encode(email, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);

            UserQR = Base64.getEncoder().encodeToString(getBytes(bitmap));


        } catch (WriterException e) {
            e.printStackTrace();
        }



        if(Name.isEmpty()){
            editTextName.setError("Please enter your full name");
            editTextName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }
        /*
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter valid email");
            editTextEmail.requestFocus();
            return;
        } */
        if(password.isEmpty()){
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Please enter password atleast 6 characters long");
            editTextPassword.requestFocus();
            return;
        }
        if(Username.isEmpty()){
            editTextUsername.setError("Please enter your nickname");
            editTextUsername.requestFocus();
            return;
        }

        String finalUserQR = UserQR;
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        User user = new User(Name,Username,email, finalUserQR);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    reff= FirebaseDatabase.getInstance().getReference().child("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    reff.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                                    Toast.makeText(SignUp.this,"User has been registered successfully",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SignUp.this,Login.class));


                                }else{
                                    Toast.makeText(SignUp.this,"Failed to register user",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(SignUp.this,"Failed to register user",Toast.LENGTH_LONG).show();
                    }
                }
            });



    }

    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}

