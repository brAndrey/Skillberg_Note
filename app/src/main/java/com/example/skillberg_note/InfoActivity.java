package com.example.skillberg_note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.skillberg_note.db.NotesContract;
import com.example.skillberg_note.db.NotesDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class InfoActivity extends AppCompatActivity {

    String LOG_TAG = InfoActivity.class.getName();

    private Context context;

//    public InfoActivity(Context context) {
//        this.context = context;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = findViewById(R.id.textViewInfo);

        String tebles = String.valueOf(NotesContract.DB_VERSION);

        NotesDBHelper  notesDBHelper = new NotesDBHelper(getBaseContext());

        SQLiteDatabase db = notesDBHelper.getReadableDatabase();

        final int version = db.getVersion();

        tebles = tebles+" -> "+version+"\n";

        ArrayList<Pair<String, String>> attachedDbs = new ArrayList<Pair<String, String>>();

        attachedDbs = (ArrayList<Pair<String, String>>) db.getAttachedDbs();//getAttachedDbs();

        tebles=tebles+String.valueOf(attachedDbs.size()) + "\n";

        for (Pair name: attachedDbs) {
            tebles=tebles+String.valueOf(name)+"\n";
            tebles=tebles+String.valueOf(name.first)+"\n";
            tebles=tebles+String.valueOf(name.second)+"\n";

        }


        textView.setText(tebles);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


}
