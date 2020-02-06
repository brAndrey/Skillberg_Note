package com.example.skillberg_note;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skillberg_note.db.NotesContract;
import com.example.skillberg_note.ui.NoteImagesAdapter;


public abstract class BaseNoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String LOG_TAG=BaseNoteActivity.class.getName();

    // для выбора URI изображения из базы
    private static final int LOADER_NOTE = 0; // берем заметки
    private static final int LOADER_IMAGES = 1;

    public static final String EXTRA_NOTE_ID = "note_id";

    protected long noteId = -1;

    protected NoteImagesAdapter noteImagesAdapter;


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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);
        Log.i("Loader<Cursor>","noteId "+noteId);
        Log.i("Loader<Cursor>"," "+NotesContract.Notes.URI);
        Log.i("Loader<Cursor>"," "+ContentUris.withAppendedId(NotesContract.Notes.URI, noteId));
        Log.i("Loader<Cursor>"," "+NotesContract.Notes.SINGLE_PROJECTION);
        String[]ar={String.valueOf(noteId)};
        Log.i("Loader<Cursor>"," "+ArreyToString(ar));
        if (id == LOADER_NOTE) {
            return new CursorLoader(
                    this,
                    ContentUris.withAppendedId(NotesContract.Notes.URI, noteId),//URI
                    //NotesContract.Notes.URI+"/"+noteId,
                    NotesContract.Notes.SINGLE_PROJECTION,
                    null,
                    null,
                    null);


        } else {
            return new CursorLoader(
                    this,
                    NotesContract.Images.URI,
                    NotesContract.Images.PROJECTION,
                    NotesContract.Images.COLUMN_NOTE_ID + " =?",
                    new String[]{String.valueOf(noteId)},
                    null);
        }

    }

    @Override
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // Отображаем заметку. Этот метод должен быть реализован в Activity
    protected abstract void displayNote(Cursor cursor);

    private String ArreyToString(String[] arrey) {
        String rez = "";
        for (String num : arrey) {
            rez = rez + " " + num;
        }
        return rez;
    }

}


