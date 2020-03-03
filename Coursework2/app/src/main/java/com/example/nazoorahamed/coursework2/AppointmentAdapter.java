package com.example.nazoorahamed.coursework2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {
    List<Appointment> list;
    Context context;
    int resource;


    public AppointmentAdapter(@NonNull Context context, int resource, List<Appointment> list) {
        super(context, resource,list);

        this.context  = context;
        this.resource = resource;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resource, null);

        TextView txt_title =  view.findViewById(R.id.txtTitle);
        TextView txt_desc =   view.findViewById(R.id.txtDesc);
        TextView txt_time =   view.findViewById(R.id.txtTime);
        TextView txt_date =   view.findViewById(R.id.txtDate);
        TextView txt_id =     view.findViewById(R.id.txtid);

        Appointment appointment = list.get(position);
        txt_title.setText(appointment.getTitle());
        txt_desc.setText(appointment.getDesc());
        txt_date.setText(appointment.getTime());
        txt_time.setText(appointment.getDate());
        txt_id.setText("Appointment ID : "+appointment.getId().toString());

        return view;
    }
}
