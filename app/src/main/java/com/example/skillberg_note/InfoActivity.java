package com.example.skillberg_note;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.skillberg_note.db.NotesContract;
import com.example.skillberg_note.db.NotesDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class InfoActivity extends AppCompatActivity {

    private NotesDBHelper notesDBHelper;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = findViewById(R.id.textViewInfo);

        String tebles = String.valueOf(NotesContract.DB_VERSION);

        notesDBHelper = new NotesDBHelper(context);

        //SQLiteDatabase
      //  SupportSQLiteDatabase db =

                //getBaseContext().openOrCreateDatabase(NotesContract.DB_NAME, MODE_PRIVATE, null);
        //db.execSQL("CREATE TABLE IF NOT EXISTS users (name TEXT, age INTEGER)");
        //Cursor query = db.rawQuery("SELECT * FROM users;", null);
//        Cursor query = db.rawQuery("tables", null);
//        String name = "";
//
//
//        if(query.moveToFirst()){
//
//            name = query.getString(0);
////            int age = query.getInt(1);
//        }

        //SupportSQLiteDatabase

//        public List<Pair<String, String>> getAttachedDbs() {
//            return getAttachedDbs(this);
//        }


//        SQLiteDatabase db = notesDBHelper.getReadableDatabase();
//
        tebles = tebles ; //NotesContract.Informs.tables;

        textView.setText(tebles);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}
