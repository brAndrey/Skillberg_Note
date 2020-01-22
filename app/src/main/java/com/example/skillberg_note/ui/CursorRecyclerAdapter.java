package com.example.skillberg_note.ui;


import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillberg_note.db.NotesContract;

public abstract class CursorRecyclerAdapter<ViewHolder extends RecyclerView.ViewHolder>

    extends RecyclerView.Adapter<ViewHolder> {

    protected Cursor cursor;

    protected boolean isDataValid;

    protected int idColumnIndex;


    public CursorRecyclerAdapter(Cursor cursor) {
        super();

        this.cursor = cursor;

        // данные корректны если курсор не null
        isDataValid = cursor != null;

        // Пытаемся получить индекс столбца ID, если курсор не null, в ином случае -1
        idColumnIndex = cursor != null
                ? cursor.getColumnIndexOrThrow(NotesContract.Notes._ID)
                : -1;

        // каждый элемент имеет уникальный ID
        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        if (isDataValid && cursor != null){
            return cursor.getCount();
        }else {return 0;}
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        if (!isDataValid){throw new IllegalStateException("Cursor is not valid");}

        if (!cursor.moveToPosition(position)){
            throw  new IllegalStateException("Can not move to positin" + position);
        }

        onBindViewHolder(viewHolder, cursor);
    }

    public abstract void onBindViewHolder(ViewHolder viewHolder, Cursor cursor);

    @Override
    public long getItemId(int position) {
        //если с данными всё хорошо и есть курсор
        if (isDataValid && cursor != null){
            // Если смогли найти нужную строку в курсоре
            if (cursor.moveToPosition(position)){
                return cursor.getLong(idColumnIndex);
            }
        }
        return RecyclerView.NO_ID;
    }

    public Cursor swapCurcor (Cursor newCurcor){
        //Если курсор не изменился - ничего не заменяем
        if (newCurcor == this.cursor){return null;}

        Cursor oldCursor = this.cursor;
        this.cursor=newCurcor;

        if (newCurcor != null){
            idColumnIndex = newCurcor.getColumnIndexOrThrow(NotesContract.Notes._ID);
            isDataValid = true;
            notifyDataSetChanged();
        }else {
            idColumnIndex = -1;
            isDataValid = false;
            // Сообщаем что данных в адаптере больше нет
            notifyItemRangeChanged(0,getItemCount());
        }
        return  oldCursor;

    }



}
