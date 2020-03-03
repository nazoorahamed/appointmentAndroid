package com.example.nazoorahamed.coursework2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.nazoorahamed.coursework2.MainActivity.date;

public class DeleteAppointment extends AppCompatActivity {
    Button delete,deleteall;
    ListView appointlist;
    List<Appointment> viewlist;
    DatabaseHelper myDb;
    SQLiteDatabase sqdb;
    String DelID;

    EditText delID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletes_appointment);
        myDb = new DatabaseHelper(this);
        appointlist = findViewById(R.id.list_viewDEl);

        DeleteData();
        viewAll();
    }

    public void DeleteData() {
        delete = findViewById(R.id.bt_delete);

        //delete the selected Appointment by using the ID
        delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DelID==null){
                            Toast.makeText(DeleteAppointment.this,"Invalid ID",Toast.LENGTH_LONG).show();

                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeleteAppointment.this);
                            builder.setTitle("Delete");
                            builder.setMessage("Are you sure you want delete this Appointment?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Integer deletedRows = myDb.deleteData(DelID);
                                    viewAll();
                                    if(deletedRows > 0)

                                        Toast.makeText(DeleteAppointment.this,"Data Deleted",Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(DeleteAppointment.this,"Invalid ID", Toast.LENGTH_LONG).show();

                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    }
                }
        );
        deleteall = findViewById(R.id.bt_alldelete);

        //delete all the data of that particular date
        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteAppointment.this);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want delete this Appointment?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Integer deletedRows = myDb.deleteAllData(date);
                        viewAll();
                        if(deletedRows > 0)

                            Toast.makeText(DeleteAppointment.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(DeleteAppointment.this,"Data not Deleted", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });


    }


    public void viewAll() {
        //view All the Appointment of that date
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
                DelID = appointment.getId().toString();

            }
        });

    }
}
