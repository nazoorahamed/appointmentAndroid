package com.example.nazoorahamed.coursework2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
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

public class ViewEditAppointment extends AppCompatActivity {
    ListView appointlist;
    List<Appointment> viewlist;
    DatabaseHelper myDb;
    SQLiteDatabase sqdb;
    public static String EdidID;
    EditText txt_EditID;
    Button bt_Edit;
    Dialog dialog;
    public static EditText details,title,time;
    Button edit_save;
    public static String matchTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_appointment);
        myDb = new DatabaseHelper(this);
        //txt_EditID = findViewById(R.id.txt_EditID);
        bt_Edit = findViewById(R.id.bt_Edit);
        appointlist = findViewById(R.id.list_view);
        dialog = new Dialog(this);

        viewAll();

            bt_Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (EdidID == null){
                        Toast.makeText(getApplicationContext(),"Select the ID to Edit",Toast.LENGTH_SHORT).show();
                    }else {

                        if(myDb.chechID(EdidID)){
                            Toast.makeText(getApplicationContext(),"Invalid ID",Toast.LENGTH_SHORT).show();

                        }else {
                            dialog.setContentView(R.layout.activity_edit_appointment);

                            details = dialog.findViewById(R.id.edited_details);
                            title = dialog.findViewById(R.id.edited_title);
                            time = dialog.findViewById(R.id.edited_time);
                            details.setMovementMethod(new ScrollingMovementMethod());

                            ArrayList<String> listarray = new ArrayList<>();
                            Cursor res = myDb.Addtext(EdidID);
                            if (res.getCount() == 0) {
                                // show message
                                Toast.makeText(getApplicationContext(),"Databse is emphty",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            while (res.moveToNext()) {
                                title.setText(res.getString(1));
                                time.setText(res.getString(2));
                                details.setText(res.getString(3));
                                matchTitle = res.getString(1);

                            }
                            UpdateData();

                            dialog.show();

//                            Intent intent = new Intent(getApplication(),EditAppointment.class);
//                            startActivity(intent);
                        }
                    }
                }
            });
    }

    public void UpdateData() {
        edit_save = dialog.findViewById(R.id.bt_edit_appointment);
        edit_save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(title.getText().toString().equals("")||time.getText().toString().equals("")||details.getText().toString().equals("")){
                            Toast.makeText(ViewEditAppointment.this, "Add Title,Time and Details", Toast.LENGTH_LONG).show();
                        }else {
                            if(myDb.CheckTitle(title.getText().toString(),date)&& !matchTitle.equals(title.getText().toString())){
                                Toast.makeText(getApplication(), "Title is not valid", Toast.LENGTH_LONG).show();
                            }else {
                                boolean isUpdate = myDb.updateData(EdidID,
                                        title.getText().toString(),
                                        time.getText().toString(),details.getText().toString(),date);
                                if(isUpdate == true) {
                                    Toast.makeText(ViewEditAppointment.this, "Data Update", Toast.LENGTH_LONG).show();

                                }
                                else{
                                    Toast.makeText(ViewEditAppointment.this,"Data not Updated", Toast.LENGTH_LONG).show();

                                }
                                dialog.dismiss();
                                EdidID=null;
                                viewAll();
                            }
                        }
                    }
                }
        );
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
                EdidID = appointment.getId().toString();
                Toast.makeText(getApplication(),EdidID, Toast.LENGTH_LONG).show();

            }
        });

    }


}
