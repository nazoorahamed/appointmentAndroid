package com.example.nazoorahamed.coursework2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static com.example.nazoorahamed.coursework2.MainActivity.date;

/**
 * Created by nazoorahamed on 4/5/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "appointment.db";
    public static final String TABLE_NAME = "appointment_table";
    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String TIME = "TIME";
    public static final String DETAILS = "DETAILS";
    public static final String DATE = "DATE";
    String[] columns ={ID,TITLE,TIME,DETAILS,DATE};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,TIME TEXT,DETAILS TEXT,DATE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
    //Insert all the data to a particular appointment
    public boolean insertData(String title,String time,String details,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE,title);
        contentValues.put(TIME,time);
        contentValues.put(DETAILS,details);
        contentValues.put(DATE,date);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }




    //Delete the particular appointment by its ID
    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});

    }
    //Delete all the date of that particular date
    public Integer deleteAllData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "DATE = ?",new String[] {id});

    }

    //get the all data for a particular date
    public ArrayList<Appointment> getAllDateData(String date) {
        ArrayList<Appointment> array_list = new ArrayList<Appointment>();
        ArrayList<Appointment> allAppointments = getAllAppointments();

        for (Appointment appoint: allAppointments) {
            if(appoint.getDate().equals(date)) {
                array_list.add(appoint);
            }
        }
        return array_list;
    }


    //Check the ID for Move and Edit
    public boolean chechID(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.query(TABLE_NAME,columns,"ID =?" ,new String[]{id},null,null,null);
        if(res.getCount()==0){
            return true;
        }else {
            return false;
        }
    }


    // to check the title and the date of that title are not to repeat again
    public boolean CheckTitle(String title,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM appointment_table where TITLE ='"+title+"' and DATE ='"+date+"'", null );

        if(res.getCount()==0){
            return false;
        }else {
            return true;
        }
    }

    // to set the edited text values by sending the EDited id and return the Cursor
    public Cursor Addtext(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.query(TABLE_NAME,columns,"ID =?" ,new String[]{id},null,null,null);
        return  res;

    }

    //Editing an appointment using the ID of the appointment
    public boolean updateData(String id,String title,String time,String details,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID,id);
        contentValues.put(TITLE,title);
        contentValues.put(TIME,time);
        contentValues.put(DETAILS,details);
        contentValues.put(DATE,date);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    //Move the date of a particular appointment
    public boolean Movedate(String id,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DATE,date);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }


    //To view All the appointment for searching the appointment
    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> array_list = new ArrayList<Appointment>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM appointment_table ", null );

        res.moveToFirst();

        while(res.isAfterLast() == false){
            Integer id = res.getInt(res.getColumnIndex(ID));
            String title = res.getString(res.getColumnIndex(TITLE));
            String details = res.getString(res.getColumnIndex(DETAILS));
            String date = res.getString(res.getColumnIndex(DATE));
            String time = res.getString(res.getColumnIndex(TIME));
            Appointment appointment = new Appointment(id, title, details, date, time);
            array_list.add(appointment);
            res.moveToNext();
        }
        return array_list;
    }

}
