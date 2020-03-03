package com.example.nazoorahamed.coursework2;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.nazoorahamed.coursework2.MainActivity.date;

public class SelectAppointmentDate extends AppCompatActivity {
    ListView appointlist;
    List<Appointment> viewlist;
    DatabaseHelper myDb;
    SQLiteDatabase sqdb;
    public static String txt_moveid;
    EditText txt_EditID;
    Button bt_Edit;
    String MoveID;
    Dialog dialog;
    String movedate;
    Button bt_movedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appointment_date);

        myDb = new DatabaseHelper(this);
        bt_Edit = findViewById(R.id.bt_slAP);
        appointlist = findViewById(R.id.list_select);
        dialog = new Dialog(this);


        viewAll();

        bt_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_moveid==null){
                    Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_SHORT).show();
                }else {
                    if(myDb.chechID(txt_moveid)){
                        Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_SHORT).show();

                    }else {
                        dialog.setContentView(R.layout.activity_move_appointment);
                        CalendarView datePicker = dialog.findViewById(R.id.cal_move_date);

                        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                                movedate = (i)+"/"+(i1+1)+"/"+(i2);
                                System.out.println(movedate);
                            }
                        });

                        bt_movedate = dialog.findViewById(R.id.bt_move_date);
                        bt_movedate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(movedate == null){
                                    Toast.makeText(getApplication(),"Select A date",Toast.LENGTH_SHORT).show();
                                }else {
                                    boolean isUpdate = myDb.Movedate(txt_moveid,movedate);
                                    if(isUpdate == true){
                                        Toast.makeText(SelectAppointmentDate.this,"Data Update",Toast.LENGTH_LONG).show();
                                    } else{
                                        Toast.makeText(SelectAppointmentDate.this,"Data not Updated", Toast.LENGTH_LONG).show();
                                    }
                                    dialog.dismiss();
                                    txt_moveid=null;
                                    viewAll();

                                }

                            }
                        });

                        dialog.show();

                    }
                }
            }
        });
    }

    public void viewAll() {

        viewlist = myDb.getAllDateData(date);
        sqdb = this.openOrCreateDatabase(
                DatabaseHelper.DATABASE_NAME, android.content.Context.MODE_PRIVATE, null);
        AppointmentAdapter adapter = new AppointmentAdapter(this,
                R.layout.list_layout_appointments, viewlist);
        appointlist.setAdapter(adapter);

        appointlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Appointment appointment = viewlist.get(i);
                txt_moveid = appointment.getId().toString();

            }
        });
    }
}
