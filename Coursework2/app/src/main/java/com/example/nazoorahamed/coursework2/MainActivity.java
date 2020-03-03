package com.example.nazoorahamed.coursework2;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String date;
    DatabaseHelper db;
    Button createAppoint,viewAppoint,deleteAppoint,Moveappoint,SearchAppoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createAppoint = findViewById(R.id.create_appointment);
        viewAppoint = findViewById(R.id.bt_view_appoint);
        deleteAppoint = findViewById(R.id.bt_delete);
        Moveappoint = findViewById(R.id.bt_move);
        SearchAppoint = findViewById(R.id.bt_search);

        StartAppointment();

        db = new DatabaseHelper(this);

    }

    public void StartAppointment(){
        createAppoint.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if(date ==null){
                    Toast.makeText(getApplicationContext(),"Select a Date ",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(),AddAppointment.class);
                    startActivity(intent);
                }



                System.out.println(date);

            }
        });

        viewAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(date ==null){
                    Toast.makeText(getApplicationContext(),"Select a Date ",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(),ViewEditAppointment.class);
                    startActivity(intent);
                }
            }
        });

        deleteAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(date ==null){
                    Toast.makeText(getApplicationContext(),"Select a Date ",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplicationContext(),DeleteAppointment.class);
                    startActivity(intent);
                }

            }
        });

        Moveappoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date ==null){
                    Toast.makeText(getApplicationContext(),"Select a Date ",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getApplication(), SelectAppointmentDate.class);
                    startActivity(intent);
                }
            }
        });

        SearchAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), SearchAppointment.class);
                startActivity(intent);

            }
        });


        CalendarView datePicker = findViewById(R.id.cal_date);

        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = (i)+"/"+(i1+1)+"/"+(i2);
                System.out.println(date);
            }
        });

    }

}
