package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
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
    DatabaseReference reffFriends,reffNotifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_up);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BTN_SignUp:
                registerUser();
                break;
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
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
                                    reffFriends= FirebaseDatabase.getInstance().getReference().child("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    reffFriends.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                                    reffNotifications= FirebaseDatabase.getInstance().getReference().child("Notifications").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    reffNotifications.child("Noti_1").child("Message").setValue("Welcome to Recreaux!");
                                    reffNotifications.child("Noti_1").child("Seen").setValue(false);
                                    reffNotifications.child("Noti_1").child("UserImage").setValue("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");
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

