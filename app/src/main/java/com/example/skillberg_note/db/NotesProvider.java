package com.example.skillberg_note.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.Arrays;

public class NotesProvider extends ContentProvider {

    static String LOG_TAG = NotesProvider.class.getName();

    private NotesDBHelper notesDBHelper;

    private static final int NOTES = 1;
    private static final int NOTE = 0;

    private static final int IMAGES = 3;
    private static final int IMAGE = 4;

    //
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(NotesContract.AUTHORITY, "notes", NOTES);
        URI_MATCHER.addURI(NotesContract.AUTHORITY, "notes/#", NOTE);

        URI_MATCHER.addURI(NotesContract.AUTHORITY, "images", IMAGES);
        URI_MATCHER.addURI(NotesContract.AUTHORITY, "images/#", IMAGE);
    }

    @Override
    public boolean onCreate() {
        notesDBHelper = new NotesDBHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        Log.i(LOG_TAG+" query "," "+uri);
        Log.i(LOG_TAG+" URI_MATCHER "," "+URI_MATCHER.match(uri));
        Log.i(LOG_TAG+" projection "," "+ArreyToString(projection));
        Log.i(LOG_TAG,"**************************************************");

        SQLiteDatabase db = notesDBHelper.getReadableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case NOTES:
                // много записей
                Log.i(LOG_TAG + " switch  ","NOTES s s");
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NotesContract.Notes.COLUMN_UPDATED_TS + " DESC";
                }
                try {
                    return db.query(NotesContract.Notes.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);

                } catch (Exception e) {
                    Log.i(LOG_TAG, "projection  " + Arrays.toString(projection));
                    Log.i(LOG_TAG, "selection  " + selection);
                    Log.i(LOG_TAG, "selectionArgs  " + selectionArgs);
                    Log.i(LOG_TAG, "sortOrder  " + sortOrder);


                    e.printStackTrace();
                }


            case NOTE:
                Log.i(LOG_TAG + " switch  ","NOTE");
                // 1-на запись
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    selection = NotesContract.Notes._ID + " =?";
                    selectionArgs = new String[]{id};
                } else {
                    selection = selection + " AND " + NotesContract.Notes._ID + " =?";

                    String[] newSelectionArgs = new String[selectionArgs.length + 1];

                    System.arraycopy(selectionArgs, 0, newSelectionArgs, 0, selectionArgs.length);

                    newSelectionArgs[newSelectionArgs.length - 1] = id;

                    selectionArgs = newSelectionArgs;
                }
                return db.query(NotesContract.Notes.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

            case IMAGES:
                Log.i(LOG_TAG + " switch  ","IMAGES s s");
                if (TextUtils.isEmpty(sortOrder)){
                    sortOrder = NotesContract.Images._ID + " ASC";}

                return db.query(NotesContract.Images.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case NOTES:
                return NotesContract.Notes.URI_TYPE_NOTE_DIR;
            case NOTE:
                return NotesContract.Notes.URI_TYPE_NOTE_ITEM;
            case IMAGES:
                    return NotesContract.Images.URI_TYPE_IMAGE_DIR;
            case IMAGE:
                    return NotesContract.Images.URI_TYPE_IMAGE_ITEM;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase db = notesDBHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case NOTES:
                long rowId = db.insert(NotesContract.Notes.TABLE_NAME,
                        null,
                        contentValues);

                if (rowId > 0) {
                    Uri noteUri = ContentUris.withAppendedId(NotesContract.Notes.URI, rowId);
                    getContext().getContentResolver().notifyChange(uri, null);//извещаем систему о том что данные изменились
                    return noteUri;

                }

                return null;
            case IMAGES:
                long imageRowId = db.insert(NotesContract.Images.TABLE_NAME,
                        null,
                        contentValues);
                if (imageRowId >0){
                    Uri imageUri = ContentUris.withAppendedId(NotesContract.Images.URI,imageRowId);
                    getContext().getContentResolver().notifyChange(uri,null);
                    return imageUri;
                }

                return null;
            default:
                return null;
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = notesDBHelper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)){
            case NOTE:
                String noreId = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)){
                    selection=NotesContract.Notes._ID + " =?";
                    selectionArgs=new String[]{noreId};
                }else {
                    selection = selection + " AND "+NotesContract.Notes._ID + " =?";
                    String[] newSelectionArgs = new String[selectionArgs.length + 1];
                    System.arraycopy(selectionArgs,0,newSelectionArgs,0 ,selectionArgs.length);
                    newSelectionArgs[newSelectionArgs.length-1]=noreId;
                    selectionArgs = newSelectionArgs;
                }

                int noreRowsUpdated = db.delete(NotesContract.Notes.TABLE_NAME,selection,selectionArgs);

                getContext().getContentResolver().notifyChange(uri,null);

                return noreRowsUpdated;

            case IMAGE:

                String imageId = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)){

                    selection=NotesContract.Images._ID + " =?";
                    selectionArgs=new String[]{imageId};

                }else {
                    selection = selection + " AND "+NotesContract.Images._ID + " =?";

                    String[] newSelectionArgs = new String[selectionArgs.length + 1];
                    System.arraycopy(selectionArgs,0,newSelectionArgs,0 ,selectionArgs.length);
                    newSelectionArgs[newSelectionArgs.length-1]=imageId;
                    selectionArgs = newSelectionArgs;
                }
                int imageRowsUpdated = db.delete(NotesContract.Images.TABLE_NAME,selection,selectionArgs);

                getContext().getContentResolver().notifyChange(uri,null);

                return imageRowsUpdated;

        }

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = notesDBHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)){

            case NOTE:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)){
                    selection = NotesContract.Notes._ID + " =?";
                    selectionArgs = new String[]{id};
                } else {
                    selection = selection + " AND " + NotesContract.Notes._ID + " = ?";
                    String[] newSelectionArgs = new String[selectionArgs.length + 1];
                    System.arraycopy(selectionArgs,0,newSelectionArgs,0,selectionArgs.length);
                    newSelectionArgs[newSelectionArgs.length -1 ] = id;
                    selectionArgs = newSelectionArgs;
                }

                int rowsUpdater = db.update(NotesContract.Notes.TABLE_NAME,contentValues,selection,selectionArgs);

                getContext().getContentResolver().notifyChange(uri,null);

                return rowsUpdater;
        }


        return 0;
    }

    private String ArreyToString(String[] arrey){
        String rez = "";
        for (String num : arrey) {
            rez = rez+" "+num;
        }
        return rez;
    }
}
