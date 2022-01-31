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
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.recreaux.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity {
    EditText et_EditEventTime;
    EditText et_EditEventDate;
    Button btn_EditEventConfirm;
    Button btn_EditEventDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        et_EditEventDate = findViewById(R.id.ET_EditEventDate);
        et_EditEventTime = findViewById(R.id.ET_EditEventTime);
        btn_EditEventConfirm = findViewById(R.id.btnEditEventConfirm);
        btn_EditEventDelete = findViewById(R.id.btnEditEventDelete);

        et_EditEventDate.setInputType(InputType.TYPE_NULL);
        et_EditEventTime.setInputType(InputType.TYPE_NULL);

        et_EditEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(et_EditEventDate);
            }
        });
        et_EditEventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    showDateDialog(et_EditEventDate);
                }
            }
        });
        et_EditEventTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    showTimeDialog(et_EditEventTime);
                }
            }
        });
        et_EditEventTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showTimeDialog(et_EditEventTime);
            }
        });

        btn_EditEventConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Confirm Edit Event", Toast.LENGTH_SHORT).show();
            }
        });

        btn_EditEventDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Delete Event", Toast.LENGTH_SHORT).show();
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
        new TimePickerDialog(EditEventActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
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
        new DatePickerDialog(EditEventActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}