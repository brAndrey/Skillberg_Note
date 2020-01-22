package com.example.skillberg_note.ui;

import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillberg_note.R;
import com.example.skillberg_note.db.NotesContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotesAdapter extends CursorRecyclerAdapter<NotesAdapter.ViewHolder>{

    private final OnNoteClickListener onNoteClickListener;

    public  NotesAdapter(Cursor cursor, OnNoteClickListener onNoteClickListener ){
        super(cursor);
        this.onNoteClickListener = onNoteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_item_note, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, Cursor cursor) {

        //      int titleColumnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_TITLE);
        long id = cursor.getLong(idColumnIndex);

        viewHolder.itemView.setTag(id);

        int titleColumnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_TITLE);


        //***************** until 03.12.2019

        String title = cursor.getString(titleColumnIndex);

        viewHolder.titleTv.setText(title);


        int dateColumnIndex = cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_UPDATED_TS);
        long updateTs = cursor.getLong(dateColumnIndex);

        Date date = new Date(updateTs);

        viewHolder.dateTv.setText(viewHolder.SDF.format(date));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTv;
        private final TextView dateTv;

        final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.titleTv = itemView.findViewById(R.id.title_tv);
            this.dateTv  = itemView.findViewById(R.id.date_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.i(" ViewHolder ", "  " + view.getTag());

                        long noteId = (Long) view.getTag();

                        onNoteClickListener.onNoteClick(noteId);
                    } finally {
                        Log.i(" ViewHolder ", " try ");
                    }
                }
            });

        }
    }

    /**
     * Слушатель для обработки кликов
     */
    public interface OnNoteClickListener {

        void onNoteClick(long noteId);

    }

}
