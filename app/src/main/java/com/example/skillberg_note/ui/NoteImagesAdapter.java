package com.example.skillberg_note.ui;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillberg_note.CreateNoteActivity;
import com.example.skillberg_note.R;
import com.example.skillberg_note.db.NotesContract;
import com.example.skillberg_note.db.NotesDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteImagesAdapter extends CursorRecyclerAdapter<NoteImagesAdapter.ViewHolder> {

    static String LOG_TAG = NoteImagesAdapter.class.getName();

    /* создаём глобальную переменную класса которая заполнит свое значение в методе
    onCreateViewHolder а будет использована в методе deleteImage
    https://www.fandroid.info/context-kontekst-v-android-chto-eto-kak-poluchit-i-zachem-ispolzovat/

     */
    private Context context;

    @Nullable
    private final OnNoteImageLongClickListener onNoteImageLongClickListener;

    public NoteImagesAdapter(Cursor cursor,OnNoteImageLongClickListener onNoteImageLongClickListener) {
        super(cursor);
        this.onNoteImageLongClickListener = onNoteImageLongClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.i(LOG_TAG, "onCreateViewHolder Time " + currentDateandTime());

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.view_item_note_image, parent, false);

        // тут берем контекст
        context=parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        Log.i(LOG_TAG, "onBindViewHolder Time " + currentDateandTime());

        Bitmap bitmap=null;

        Boolean failBitmap=false;

        long imageId = cursor.getLong(cursor.getColumnIndexOrThrow(NotesContract.Images._ID));

        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Images.COLUMN_PATH));

        Log.i("NoteImagesAdapter", "imageId " + imageId);

        Log.i("NoteImagesAdapter", "imagePath " + imagePath);


        try {
            bitmap = BitmapFactory.decodeFile(imagePath);
        } catch (Exception e) {

            failBitmap=true;

            e.printStackTrace();
        }


        Log.i("NoteImagesAdapter", "bitmap " + bitmap);

        if (bitmap != null) {
            viewHolder.itemView.setTag(imageId);

            viewHolder.imageView.setImageBitmap(bitmap);
        } else
            // если файл не нашли то удаляем запись о нем
        if (!failBitmap){deleteImage(imageId);}
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;

        }
    }

    private String currentDateandTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public interface OnNoteImageLongClickListener {
        void onImageLongClick (long imageId);
    }


    private void deleteImage (long imageId) {
        /*
        * В этом методе удаляем из базы те записи которые указывают на файлы которых нет
        * */

        Log.i(LOG_TAG + " deleteImage ", "imageId =" + imageId);
        NotesDBHelper mNotesDBHelper = new NotesDBHelper(context);
        SQLiteDatabase db = mNotesDBHelper.getWritableDatabase();
        //Log.i(LOG_TAG + " deleteImage ", String.valueOf(ContentUris.withAppendedId(NotesContract.Images.URI, imageId)));
        try {
            db.delete(NotesContract.Images.TABLE_NAME,
                    "_ID = ?",
                    new String[] {String.valueOf(imageId)});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



