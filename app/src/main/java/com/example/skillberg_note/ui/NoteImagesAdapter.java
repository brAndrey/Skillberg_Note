package com.example.skillberg_note.ui;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillberg_note.R;
import com.example.skillberg_note.db.NotesContract;

public class NoteImagesAdapter extends CursorRecyclerAdapter<NoteImagesAdapter.ViewHolder>  {

    public NoteImagesAdapter(Cursor cursor) {
        super(cursor);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.view_item_note_image,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {


        long imageId = cursor.getLong(cursor.getColumnIndexOrThrow(NotesContract.Images._ID));
        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Images.COLUMN_PATH));
        Log.i("NoteImagesAdapter","imageId "+imageId);

        Log.i("NoteImagesAdapter","imagePath "+imagePath);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        Log.i("NoteImagesAdapter","bitmap "+bitmap);
        if (bitmap != null) {
            try {
                viewHolder.imageView.setImageBitmap(bitmap);
                viewHolder.imageView.setTag(imageId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView = (ImageView) imageView;
        }
    }
}
