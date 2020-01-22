package com.example.skillberg_note;

import android.database.Cursor;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import androidx.loader.content.CursorLoader;
//import androidx.loader.app.LoaderManager;
//import androidx.loader.content.Loader;

import com.example.skillberg_note.db.NotesContract;

 public class NoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

     static String LOG_TAG = NoteActivity.class.getName();

    private TextView noteTv;

    public static final String EXTRA_NOTE_ID = "note_id";

    private long noteId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTv = findViewById(R.id.text_tv);

        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);

        if (noteId == -1) {
            finish();
        }

        // инициализируем лоадер
        getLoaderManager().initLoader(
                0,
                null,
                this);

Log.i(" Activity_log ",LOG_TAG);

    }


     @Override
     public boolean onCreateOptionsMenu(Menu menu) {

         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.view_note, menu);
         return true;

     }
     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case android.R.id.home:
                 finish();
                 return true;

             case R.id.action_edit:
                 editNote();
                 return true;

             default:
                 return super.onOptionsItemSelected(item);
         }
     }

     private void editNote() {

         Intent intent = new Intent(this, CreateNoteActivity.class);
         intent.putExtra(CreateNoteActivity.EXTRA_NOTE_ID, noteId);

         startActivity(intent);
     }
//********************************************************************************************
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i("Loader<Cursor>"," "+noteId);
        Log.i("Loader<Cursor>"," "+NotesContract.Notes.URI);
        Log.i("Loader<Cursor>"," "+ContentUris.withAppendedId(NotesContract.Notes.URI, noteId));
        Log.i("Loader<Cursor>"," "+NotesContract.Notes.SINGLE_PROJECTION);
        CursorLoader cursorLoader = new CursorLoader(
                this,
                ContentUris.withAppendedId(NotesContract.Notes.URI, noteId),//URI
                //NotesContract.Notes.URI+"/"+noteId,
                NotesContract.Notes.SINGLE_PROJECTION,
                null,
                null,
                null);

        return  cursorLoader;


    }

   @Override
     public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        Log.i("onLoadFinished", "Load finished: " + cursor.getCount());

        cursor.setNotificationUri(getContentResolver(), NotesContract.Notes.URI);
        displayNote(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

//     /**
//      * Редактирование заметки
//      */


    private void displayNote(Cursor cursor) {
        if (!cursor.moveToFirst()) {
// Если не получилось перейти к первой строке — завершаем Activity
            finish();
        }

        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_TITLE));
        String noteText = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_NOTE));

        setTitle(title);
        noteTv.setText(noteText);
    }





    private String ArreyToString(String[] arrey){
        String rez = "";
        for (String num : arrey) {
            rez = rez+" "+num;
        }
        return rez;
    }
}
