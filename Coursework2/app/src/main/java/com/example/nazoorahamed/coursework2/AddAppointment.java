package com.example.nazoorahamed.coursework2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.nazoorahamed.coursework2.MainActivity.*;

/**
 * Created by nazoorahamed on 3/13/18.
 */

public class AddAppointment extends AppCompatActivity{
    private String str_details,str_time,str_title;
    private DatabaseHelper myDb;
    private Button thes_show,save,bt_syno;
    private EditText details,title,time;
    private ListView thes_list;
    private TextView view_thes;
    private EditText txt_thes;
    private static final String TAG = "SAVE";
    static final String KEY_LIST = "list";
    static final String KEY_CATAGORY = "category";
    static final String KEY_SYNONYMS = "synonyms";
    static String Selcted_txt;
    private boolean isreplace;

    public AddAppointment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_appointment);



        myDb = new DatabaseHelper(this);
        details = findViewById(R.id.appoint_details);
        title = findViewById(R.id.appoint_title);
        time = findViewById(R.id.appoint_time);
        //details.setMovementMethod(new ScrollingMovementMethod());
        //getActivity().setTitle("Profile");
        save = findViewById(R.id.bt_save_appointment);

        thes_list = findViewById(R.id.these_listView);
        view_thes = findViewById(R.id.view_syno);
        txt_thes = findViewById(R.id.txt_thesa);
        thes_show = findViewById(R.id.bt_thesa);


        //Button which checks the Anonymous word and replace with the text (Using thesaurus web service)
        bt_syno = findViewById(R.id.bt_synonim);
        bt_syno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String full_txt = details.getText().toString();
                int startselect = details.getSelectionStart();
                int endselection =details.getSelectionEnd();

                Selcted_txt = full_txt.substring(startselect,endselection);

                if (!Selcted_txt.isEmpty()) {
                    isreplace=true;
                    ThesConnection task = new ThesConnection();
                    task.execute(Selcted_txt);

                }

            }
        });

        //thesaurus button ! check the given text for anonymous by sending a request through web service
        thes_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = txt_thes.getText().toString().trim();
                if (!word.isEmpty()) {
                    ThesConnection thes = new ThesConnection();
                    thes.execute(word);
                }
            }
        });

            // Button, Save the details to the database  and create an appointment!
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_title = title.getText().toString();
                str_time = time.getText().toString();
                str_details = details.getText().toString();

                //make sure all the text fields are not empty
                if(str_title.equals("") || str_time.equals("")|| str_details.equals("")){
                    Toast.makeText(getApplication(), "Add Title,Time and Details ", Toast.LENGTH_LONG).show();

                }else {
                    //check for the title and date to make sure same title is not appearing again in a particular date
                    if(myDb.CheckTitle(str_title,date)){
                        Toast.makeText(getApplication(), "Title is not valid", Toast.LENGTH_LONG).show();
                    }else {

                        //Insert values to the database by Calling the database Helper Class and adding the value to the Inserting Method
                        boolean isInserted = myDb.insertData(str_title, str_time, str_details, date);

                        if(isInserted == true) {
                            Toast.makeText(getApplication(), "Appointment Created", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(getApplication(),"Appointment not Created", Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();


                    }

                }
            }
        });
    }
        //get the all Synonyms from the web service and display in a dialog window !
    public void getSynonymous(final String text_syn,List<String> synonymous) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, synonymous);
        builder.setTitle("Synonyms for "+text_syn);

        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String matched_synonym = arrayAdapter.getItem(which);
                AlertDialog.Builder innerAnnonym = new AlertDialog.Builder(builder.getContext());
                innerAnnonym.setMessage(matched_synonym);
                innerAnnonym.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //replace the Synonyms word with the word which selected in the details text
                        if(isreplace){
                            Editable text = details.getText();
                            Editable phrase = new SpannableStringBuilder(text.toString().replace(text_syn,matched_synonym));
                            details.setText(phrase);
                            isreplace = false;
                        }
                        dialog.dismiss();
                    }
                });
                innerAnnonym.show();
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    //Establishing the connection with the web service by parsing the XML via XML parser class
    public class ThesConnection extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {

            final String given_txt = strings[0];
            Log.d("GIVEN_TEXT", "doInBackground: " + given_txt);

            final ArrayList<HashMap<String, String>> menulist = new ArrayList<HashMap<String, String>>();

            final List<String> synonymslist = new ArrayList<>();

            XMLWebParser parser = new XMLWebParser();
            String xml = parser.getXmlFromUrl(given_txt);
            Document doc = parser.getDomElement(xml);
            if (doc != null) {
                NodeList n = doc.getElementsByTagName(KEY_LIST);

                for (int i = 0; i < n.getLength(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    Element e = (Element) n.item(i);

                    map.put(KEY_CATAGORY, parser.getValue(e, KEY_CATAGORY));
                    map.put(KEY_SYNONYMS, parser.getValue(e, KEY_SYNONYMS));

                    String result = parser.getValue(e, KEY_SYNONYMS);
                    String[] words = result.split("\\|");
                    Log.d("RESULT", "doInBackground: "+ result);
                    for(String s : words) {
                        synonymslist.add(s);
                    }

                    menulist.add(map);
                }
            }
            
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    getSynonymous(given_txt,synonymslist);
                }
            });
            return null;
        }


    }

}
