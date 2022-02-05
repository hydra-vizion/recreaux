package com.example.recreaux;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements  View.OnClickListener{

    public TextView textView,textView2;
    public EditText editTextPassword, editTextEmail;
    private Button signIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.TV_login_nav);

        String text = "Don't have an account? Signup";

        SpannableString ss = new SpannableString(text);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,SignUp.class);

                startActivity(intent);
            }
        });

        ClickableSpan clickableLogin = new ClickableSpan() {
            @Override
            public void onClick(View Widget) {
                Intent intent = new Intent(Login.this,SignUp.class);

            }
        };

        ss.setSpan(clickableLogin,23,29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

        textView.setText(ss);

        textView2 = findViewById(R.id.TV_login_forget_password);

        String text2 = "Forgot password?";

        SpannableString ss1 = new SpannableString(text2);

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Forgot_Password.class);

                startActivity(intent);
            }
        });

        ClickableSpan clickableForgotPassword = new ClickableSpan() {
            @Override
            public void onClick(View Widget) {
                Intent intent = new Intent(Login.this,Forgot_Password.class);

            }
        };

        ss1.setSpan(clickableLogin,1,14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

        textView.setText(ss);

        //Login module
        mAuth = FirebaseAuth.getInstance();

        signIn = (Button) findViewById(R.id.BTN_login);
        signIn.setOnClickListener(this);

        editTextEmail = findViewById(R.id.TI_Login_Email);
        editTextPassword = findViewById(R.id.TI_Forgot_email);





    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.BTN_login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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
            editTextPassword.setError("Please enter password at least 6 characters long");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        startActivity(new Intent(Login.this,hamburger_nav.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(Login.this,"Check your email to verify your account",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Login.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}