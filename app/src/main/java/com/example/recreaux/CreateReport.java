package com.example.recreaux;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.recreaux.R;

public class CreateReport extends AppCompatActivity {
    Button btn_PostAddReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        btn_PostAddReport = findViewById(R.id.btnPostAddReport);

        btn_PostAddReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Report Added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}