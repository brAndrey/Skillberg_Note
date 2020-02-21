package com.example.skillberg_note;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.skillberg_note.db.NotesContract;
import com.example.skillberg_note.db.NotesDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class InfoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String LOG_TAG = InfoActivity.class.getName();

    // для выбора URI изображения из базы
    private static final int LOADER_IMAGES = 1;

    protected long noteId = -1;

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

        StringBuilder tebles = new StringBuilder(String.valueOf(NotesContract.DB_VERSION));

        NotesDBHelper notesDBHelper = new NotesDBHelper(context);

        SQLiteDatabase db = notesDBHelper.getReadableDatabase();

        final int version = db.getVersion();

        tebles.append(" -> ").append(version).append("\n");

        ArrayList<Pair<String, String>> attachedDbs = new ArrayList<Pair<String, String>>();

        attachedDbs = (ArrayList<Pair<String, String>>) db.getAttachedDbs();//getAttachedDbs();

        tebles.append(String.valueOf(attachedDbs.size())).append("\n");

        for (Pair name : attachedDbs) {
            tebles.append(name).append("\n");
            tebles.append(name.first).append("\n");
            tebles.append(name.second).append("\n");

        }


        textView.setText(tebles.toString());

        initImagesLoader();
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


    // инициализируем загрузчик изображения
    protected void initImagesLoader() {
        getLoaderManager().initLoader(
                LOADER_IMAGES,
                null,
                this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                NotesContract.Images.URI,
                NotesContract.Images.PROJECTION,
                NotesContract.Images.COLUMN_NOTE_ID + " =?",
                null,
                null);
//new String[]{String.valueOf(noteId)},
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i(LOG_TAG,"  onLoadFinished "+cursor.getCount());
        //if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(NotesContract.Images._ID);
            int pathColumnIndex = cursor.getColumnIndex(NotesContract.Images.COLUMN_PATH);
            int noteColumnIndex = cursor.getColumnIndex(NotesContract.Images.COLUMN_NOTE_ID);


            //Проходим по рядам
            while (cursor.moveToNext()) {
                // используем индексы для получения значений как строк
                String ID_tabs = Integer.toString(cursor.getInt(idColumnIndex));
                String path_tabs = cursor.getString(pathColumnIndex);
                String note_tabs = cursor.getString(noteColumnIndex);
                Log.i(LOG_TAG, "Images: " + ID_tabs +"  "+path_tabs+ "  " + note_tabs);
            }
        //}
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}