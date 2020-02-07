package com.example.skillberg_note.ui;

import android.database.Cursor;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillberg_note.R;
import com.example.skillberg_note.db.NotesContract;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteImagesAdapter extends CursorRecyclerAdapter<NoteImagesAdapter.ViewHolder> {

    static String LOG_TAG = NoteImagesAdapter.class.getName();

    public NoteImagesAdapter(Cursor cursor) {
        super(cursor);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.i(LOG_TAG,"onCreateViewHolder Time "+currentDateandTime());

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.view_item_note_image, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        Log.i(LOG_TAG, "onBindViewHolder Time "+currentDateandTime());


        long imageId = cursor.getLong(cursor.getColumnIndexOrThrow(NotesContract.Images._ID));

        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Images.COLUMN_PATH));

        Log.i("NoteImagesAdapter", "imageId " + imageId);

        Log.i("NoteImagesAdapter", "imagePath " + imagePath);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        Log.i("NoteImagesAdapter", "bitmap " + bitmap);
        if (bitmap != null) {
            viewHolder.itemView.setTag(imageId);

            viewHolder.imageView.setImageBitmap(bitmap);
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;

        }
    }
private String currentDateandTime(){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss");
    String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
}



}


