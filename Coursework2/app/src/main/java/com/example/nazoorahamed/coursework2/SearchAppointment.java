package com.example.nazoorahamed.coursework2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchAppointment extends AppCompatActivity {
    SearchView searchView;
    List<Appointment> List;
    List<Appointment> resycleList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_appointment);


        listView = findViewById(R.id.list_search);
        searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                getSearchResult(s);
                return false;
            }
        });
    }

    //Check the Onchange String in the Database by getting the data from the database and add them into Appointment class and getting them
    //into Adapter in and add those adapter into list view
    private void getSearchResult(String search) {
        DatabaseHelper db = new DatabaseHelper(this);
        resycleList = new ArrayList<Appointment>();
        List = db.getAllAppointments();

        for (Appointment app : List) {
            if (app.getDesc().contains(search) || app.getTitle().contains(search)) {
                resycleList.add(app);
            }
        }
        AppointmentAdapter adapter = new AppointmentAdapter(this,
                R.layout.list_layout_appointments, resycleList);
        listView.setAdapter(adapter);
    }
}
