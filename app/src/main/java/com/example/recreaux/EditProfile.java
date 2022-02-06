package com.example.recreaux;

import static com.example.recreaux.hamburger_nav.redirectActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class EditProfile extends AppCompatActivity {
    EditText userNickname,userResidence,userInterests,userFullName,userBio,userPhoneNumber;
    Button btnConfirm;
    String email;
    ImageView btnProfPic;
    User userProfile;
    DatabaseReference reff;
    Bitmap imageicon;
    private static final int RESULT_LOAD_IMAGE=1;
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
        btnProfPic=(ImageView)findViewById(R.id.V_EditProfile_ProfPic);
        userProfile=new User();

        imageicon = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        imageicon.eraseColor(Color.BLACK);

        reff =FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Bio").exists()){
                    userNickname.setText(dataSnapshot.child("Username").getValue().toString());
                    userFullName.setText(dataSnapshot.child("Name").getValue().toString());
                    email=dataSnapshot.child("email").getValue().toString();
                }
                else{
                    Bitmap icon= getImage(Base64.getDecoder().decode(dataSnapshot.child("UserImage").getValue().toString()));
                    btnProfPic.setImageBitmap(icon);
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

        btnProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view){

                 String s = Base64.getEncoder().encodeToString(getBytes(scaleCenterCrop(imageicon,btnProfPic.getHeight(),btnProfPic.getWidth())));

                 User user = new User(userFullName.getText().toString().trim(),
                         userNickname.getText().toString().trim(),
                         email,
                         s,
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!= null){

            btnProfPic.setBackground(null);
            Uri selectedImage = data.getData();
            try {
                imageicon = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap imageforicon = scaleCenterCrop(imageicon,btnProfPic.getHeight(),btnProfPic.getWidth());
            btnProfPic.setImageBitmap(imageforicon);
        }
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }
    public void ClickMenu(View view){redirectActivity(this,hamburger_nav.class);}
}