package com.example.recreaux;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateQRCode extends AppCompatActivity {
    EditText ET_Tulis;
    Button btn_Generate;
    ImageView IV_GenerateQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);

        ET_Tulis = findViewById(R.id.ET_Tulis);
        btn_Generate = findViewById(R.id.btn_Generate);
        IV_GenerateQR = findViewById(R.id.IV_GenerateQR);

        btn_Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = ET_Tulis.getText().toString().trim();
                MultiFormatWriter writer = new MultiFormatWriter();

                try {
                    //matrix
                    BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 350, 350);

                    //barcode encoder
                    BarcodeEncoder encoder = new BarcodeEncoder();

                    //Initialize BitMap

                    Bitmap bitmap = encoder.createBitmap(matrix);

                    IV_GenerateQR.setImageBitmap(bitmap);

                    //InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    //manager.hideSoftInputFromWindow(ET_Tulis.getApplicationWindowToken(),0);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}