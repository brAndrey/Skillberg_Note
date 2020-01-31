package com.example.skillberg_note.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class InsertOneRecord {

    private final String LOG_TAG=InsertOneRecord.class.getName();

    private Context intContext;

    public InsertOneRecord(Context intContext) {

        this.intContext = intContext;

        int i=lenthSQL()+1;

        ContentResolver contentResolver = intContext.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.Notes.COLUMN_TITLE, "Заголовок заметки "+ i );
        contentValues.put(NotesContract.Notes.COLUMN_NOTE, "Текст заметки"+ i );
        contentValues.put(NotesContract.Notes.COLUMN_CREATED_TS, System.currentTimeMillis());
        contentValues.put(NotesContract.Notes.COLUMN_UPDATED_TS, System.currentTimeMillis());
        Uri uri = contentResolver.insert(NotesContract.Notes.URI, contentValues);
        Log.i(LOG_TAG, "URI: " + uri);


    }
    public int lenthSQL(){
        ContentResolver contentResolver = intContext.getContentResolver();
        Cursor cursor = contentResolver.query(
                NotesContract.Notes.URI, // URI
                NotesContract.Notes.LIST_PROJECTION, // Столбцы
                null, // Параметры выборки
                null, // Аргументы выборки
                null // Сортировка по умолчанию
        );

        Log.i(LOG_TAG, "Count: " + cursor.getCount());

        return cursor.getCount();



    }


    private void select() {
        ContentResolver contentResolver = intContext.getContentResolver();
        Cursor cursor = contentResolver.query(
                NotesContract.Notes.URI, // URI
                NotesContract.Notes.LIST_PROJECTION, // Столбцы
                null, // Параметры выборки
                null, // Аргументы выборки
                null // Сортировка по умолчанию
        );




        cursor.close();
    }

}
