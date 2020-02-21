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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import androidx.loader.content.CursorLoader;
//import androidx.loader.app.LoaderManager;
//import androidx.loader.content.Loader;

import com.example.skillberg_note.db.NotesContract;
import com.example.skillberg_note.ui.NoteImagesAdapter;

//public class NoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

public class NoteActivity extends BaseNoteActivity {


    static String LOG_TAG = NoteActivity.class.getName();

    private TextView noteTv;

    public static final String EXTRA_NOTE_ID = "note_id";

    private long noteId;

    // для выбора URI изображения из базы
    private static final int LOADER_NOTE = 0; // берем заметки
    private static final int LOADER_IMAGES = 1;

    NoteImagesAdapter noteImagesAdapter;

    //для выбора изображения
    private static final int REQUEST_CODE_PICK_FROM_GALLARY = 1;
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTv = findViewById(R.id.text_tv);

        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);

        RecyclerView recyclerView = findViewById(R.id.images_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        noteImagesAdapter = new NoteImagesAdapter(null,null);
        recyclerView.setAdapter(noteImagesAdapter);

        if (noteId != -1) {
            initNoteLoader();

            initImagesLoader();
        } else {
            finish();
        }

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

//    //********************************************************************************************
//    @NonNull
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
//        Log.i(LOG_TAG,"Loader<Cursor>" + noteId);
//        Log.i(LOG_TAG,"Loader<Cursor>" + NotesContract.Notes.URI);
//        Log.i(LOG_TAG,"Loader<Cursor>" + ContentUris.withAppendedId(NotesContract.Notes.URI, noteId));
//        Log.i(LOG_TAG,"Loader<Cursor>" + NotesContract.Notes.SINGLE_PROJECTION);
//
//        if (id == LOADER_NOTE) {
//            Log.i("Loader<Cursor>", "id =" + id + " LOADER_NOTE");
//            return new CursorLoader(
//                    this,
//                    ContentUris.withAppendedId(NotesContract.Notes.URI, noteId),//URI
//                    //NotesContract.Notes.URI+"/"+noteId,
//                    NotesContract.Notes.SINGLE_PROJECTION,
//                    null,
//                    null,
//                    null);
//
//        } else {
//            Log.i("Loader<Cursor>", "id =" + id + " LOADER_IMAGES");
//
//            return new CursorLoader(
//                    this,
//                    NotesContract.Images.URI,
//                    NotesContract.Images.PROJECTION,
//                    NotesContract.Images.COLUMN_NOTE_ID + " =?",
//                    new String[]{String.valueOf(noteId)},
//                    null);
//        }
//
//
//    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {

            if (loader.getId() == LOADER_NOTE) {

                Log.i("onLoadFinished", "Load finished: " + cursor.getCount());

                cursor.setNotificationUri(getContentResolver(), NotesContract.Notes.URI);
                displayNote(cursor);
            } else {

                cursor.setNotificationUri(getContentResolver(), NotesContract.Images.URI);
                noteImagesAdapter.swapCurcor(cursor);
            }
        }

    }
/*
* @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor==null){finish();}

        if (loader.getId() == LOADER_NOTE) {
            try {
                cursor.setNotificationUri(getContentResolver(), NotesContract.Notes.URI);
                displayNote(cursor);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

                cursor.setNotificationUri(getContentResolver(), NotesContract.Images.URI);

                try {
                    noteImagesAdapter.swapCurcor(cursor);
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }*/
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    /**
     * Редактирование заметки
     */

    //@Override
    protected void displayNote(Cursor cursor) {
        if (!cursor.moveToFirst()) {
            // Если не получилось перейти к первой строке — завершаем Activity
            finish();
        }
        String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_TITLE));
        setTitle(title);
        String noteText = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_NOTE));
        noteTv.setText(noteText);
    }

    // инициализируем загрузчик заметки
    protected void initNoteLoader() {
        getLoaderManager().initLoader(
                LOADER_NOTE,
                null,
                this);
    }

    // инициализируем загрузчик изображения
    protected void initImagesLoader() {
        getLoaderManager().initLoader(
                LOADER_IMAGES,
                null,
                this);

    }

    private String ArreyToString(String[] arrey) {
        String rez = "";
        for (String num : arrey) {
            rez = rez + " " + num;
        }
        return rez;
    }
}
