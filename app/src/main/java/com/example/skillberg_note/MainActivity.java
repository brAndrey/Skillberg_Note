package com.example.skillberg_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.example.skillberg_note.db.InsertOneRecord;
import com.example.skillberg_note.db.NotesContract;
import com.example.skillberg_note.ui.NotesAdapter;

//import com.example.skillberg_note.db.InsertOneRecord;
//import com.example.skillberg_note.db.NotesContract;
//import com.example.skillberg_note.ui.NotesAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private NotesAdapter notesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.notes_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
//        InsertOneRecord insertOneRecord = new InsertOneRecord(this);

        notesAdapter = new NotesAdapter(null,onNoteClickListener );
        recyclerView.setAdapter(notesAdapter);

        getLoaderManager().initLoader(
                0, // Инентивикатор загрузчика
                null, // Аргументы
                this // Callback для событий загрузчика
        );

        //InsertOneRecord insertOneRecord = new InsertOneRecord(this);

        findViewById(R.id.create_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
                startActivity(intent);
            }
        });

        Log.i(MainActivity.class.getName()," DB_VERSION " + NotesContract.DB_VERSION);
 //       Log.i(MainActivity.class.getName()," DB_lenth " + insertOneRecord.lenthSQL());

        //findViewById(R.id.notes_rv).setOnClickListener();

    }
//*********************************************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_note, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //**************************************************************************************************
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                NotesContract.Notes.URI,
                NotesContract.Notes.LIST_PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i("Test", "Load finished: " + cursor.getCount());

        cursor.setNotificationUri(getContentResolver(), NotesContract.Notes.URI);
        notesAdapter.swapCurcor(cursor);
    }


    /**
     * Listener для клика по заметке
     */
    private final NotesAdapter.OnNoteClickListener onNoteClickListener = new NotesAdapter.OnNoteClickListener() {
        @Override
        public void onNoteClick(long noteId) {
            Log.i(" onNoteClick "," onNoteClick "+noteId);
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.putExtra(NoteActivity.EXTRA_NOTE_ID, noteId);

            startActivity(intent);
        }
    };

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

//    private void insert() {
//        ContentResolver contentResolver = getContentResolver();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(NotesContract.Notes.COLUMN_TITLE, "Заголовок заметки" );
//        contentValues.put(NotesContract.Notes.COLUMN_NOTE, "Текст заметки");
//        contentValues.put(NotesContract.Notes.COLUMN_CREATED_TS, System.currentTimeMillis());
//        contentValues.put(NotesContract.Notes.COLUMN_UPDATED_TS, System.currentTimeMillis());

//        Uri uri = contentResolver.insert(NotesContract.Notes.URI, contentValues);
//        Log.i("Test", "URI: " + uri);
//    }
//
//    private void select() {
//        ContentResolver contentResolver = getContentResolver();
//        Cursor cursor = contentResolver.query(
//                NotesContract.Notes.URI, // URI
//                NotesContract.Notes.LIST_PROJECTION, // Столбцы
//                null, // Параметры выборки
//                null, // Аргументы выборки
//                null // Сортировка по умолчанию
//        );
//        Log.i("Test", "Count: " + cursor.getCount());
//        cursor.close();
//    }



