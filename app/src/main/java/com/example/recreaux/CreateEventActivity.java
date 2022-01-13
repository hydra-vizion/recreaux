package com.example.recreaux;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.recreaux.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {
    EditText et_CreateEventDate;
    EditText et_CreateEventTime;
    Button btn_CreateEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        et_CreateEventDate = findViewById(R.id.ET_CreateEventDate);
        et_CreateEventTime = findViewById(R.id.ET_CreateEventTime);
        btn_CreateEvent = findViewById(R.id.btnCreateEvent);

        et_CreateEventDate.setInputType(InputType.TYPE_NULL);
        et_CreateEventTime.setInputType(InputType.TYPE_NULL);

        et_CreateEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(et_CreateEventDate);
            }
        });
        et_CreateEventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    showDateDialog(et_CreateEventDate);
                }
            }
        });
        et_CreateEventTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    showTimeDialog(et_CreateEventTime);
                }
            }
        });
        et_CreateEventTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showTimeDialog(et_CreateEventTime);
            }
        });
        
        btn_CreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Event Created", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showTimeDialog(EditText timeinput) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                timeinput.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new TimePickerDialog(CreateEventActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
    }

    private void showDateDialog(EditText dateinput) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateinput.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(CreateEventActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}