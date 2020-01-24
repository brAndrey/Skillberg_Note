package com.example.skillberg_note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDBHelper extends SQLiteOpenHelper {

    public NotesDBHelper (Context context){
        super(context,NotesContract.DB_NAME,null,NotesContract.DB_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (String query : NotesContract.CREATE_DATABASE_QUERIES){
            sqLiteDatabase.execSQL(query);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {

            //sqLiteDatabase.execSQL(NotesContract.Images.CREATE_TABLE);
            try {
                sqLiteDatabase.execSQL(NotesContract.Images.CREATE_TABLE);
            } finally {

            }
//            ;


        }
    }

//    public String getDatabaseName() {
//        return null;
//    }
}
