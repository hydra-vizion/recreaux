package com.example.recreaux;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Base64;

public class MyQRCode extends AppCompatActivity {

    TextView TV_UserNickname, TV_UserFullname;
    ImageView IV_QRCode;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode);

        TV_UserNickname = findViewById(R.id.TV_UserNickname);
        TV_UserFullname = findViewById(R.id.TV_UserFullname);
        IV_QRCode = findViewById(R.id.IV_QRCode);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        userID = user.getUid();

        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

                String fullname = userProfile.Name;
                TV_UserFullname.setText(fullname);

                String nickname = userProfile.Username;
                TV_UserNickname.setText(nickname);

                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix = writer.encode(userID, BarcodeFormat.QR_CODE, 350, 350);
                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap(matrix);
                    IV_QRCode.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

                /*
                String QR = userProfile.UserQR;
                Bitmap QRcode = getImage(Base64.getDecoder().decode(QR));

                IV_QRCode.setImageBitmap(QRcode);
                */




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}