package com.example.skillberg_note.db;

import android.content.ContentValues;
import android.content.Context;

import java.io.File;

public class DataBaseOperation {

    Context intContext;

    // для выбора URI изображения из базы
    private static final int LOADER_NOTE = 0;
    private static final int LOADER_IMAGES = 1;



    public DataBaseOperation(Context intContext){
        this.intContext=intContext;
    }


    // метод записи в базу
    public void addImageToDatebase(File file,long noteId){
        if (noteId == -1){
            // На данный момент мы добавляем аттачи только в режиме редактирования
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.Images.COLUMN_PATH,file.getAbsolutePath());
        contentValues.put(NotesContract.Images.COLUMN_NOTE_ID,noteId);

        intContext.getContentResolver().insert(NotesContract.Images.URI,contentValues);
    }

    public void OutImageTabl(){


    }
}
