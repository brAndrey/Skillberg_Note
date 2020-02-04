package com.example.skillberg_note;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillberg_note.R;
import com.example.skillberg_note.db.DataBaseOperation;
import com.example.skillberg_note.db.NotesContract;
import com.example.skillberg_note.file.FileStream;
import com.example.skillberg_note.ui.NoteImagesAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CreateNoteActivity<intent> extends BaseNoteActivity {

//public class CreateNoteActivity<intent> extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    static String LOG_TAG = CreateNoteActivity.class.getName();

    private Context context;

    public static final String EXTRA_NOTE_ID = "note_id";

    private TextInputEditText titleEt;
    private TextInputEditText textEt;

    private TextInputLayout titleTil;
    private TextInputLayout textTil;

    //для выбора изображения
    private static final int REQUEST_CODE_PICK_FROM_GALLARY = 1;
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;

    private File currentImageFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleEt = findViewById(R.id.title_et);
        textEt = findViewById(R.id.text_et);

        titleTil = findViewById(R.id.title_til);
        textTil = findViewById(R.id.text_til);

        RecyclerView recyclerView = findViewById(R.id.images_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        noteImagesAdapter = new NoteImagesAdapter(null);
        recyclerView.setAdapter(noteImagesAdapter);

        // считываем входящий параметр ID корректируемой строки , если он пуст то -1
        noteId = getIntent().getLongExtra(EXTRA_NOTE_ID, -1);


        if (noteId != -1) {

            initNoteLoader();

            initImagesLoader();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.create_note, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveNote();
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_attache:
                showImageSelectionDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {

        String title = titleEt.getText().toString().trim();
        String text = textEt.getText().toString().trim();

        boolean isCorrect = true;

        if (TextUtils.isEmpty(title)) {
            isCorrect = false;

            titleTil.setError(getString(R.string.error_empty_field));
            titleTil.setErrorEnabled(true);
        } else {
            titleTil.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(text)) {
            isCorrect = false;

            textTil.setError(getString(R.string.error_empty_field));
            textTil.setErrorEnabled(true);
        } else {
            textTil.setErrorEnabled(false);
        }

        if (isCorrect) {
            long currentTime = System.currentTimeMillis();

            ContentValues contentValues = new ContentValues();
            contentValues.put(NotesContract.Notes.COLUMN_TITLE, title);
            contentValues.put(NotesContract.Notes.COLUMN_NOTE, text);

            if (noteId == -1) {
                contentValues.put(NotesContract.Notes.COLUMN_CREATED_TS, currentTime);
            }


            contentValues.put(NotesContract.Notes.COLUMN_UPDATED_TS, currentTime);

            if (noteId == -1) {
                getContentResolver().insert(NotesContract.Notes.URI, contentValues);
            } else {
                getContentResolver().update(ContentUris.withAppendedId(NotesContract.Notes.URI, noteId),
                        contentValues,
                        null,
                        null);
            }

            finish();
        }
    }

    //*******************************************************************
    private void showImageSelectionDialog() {
        //создаём билдер диалога
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                // Задаём название
                .setTitle(R.string.title_dialog_attachment_variants)
                // Задаём список и листнер списка
                .setItems(R.array.attachment_variants, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            pickImageFromGallery();
                        } else if (which == 1) {
                            takePhoto();
                        }
                    }
                }).create();

        if (!isFinishing()) {
            alertDialog.show();
        }

    }

    private void pickImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, REQUEST_CODE_PICK_FROM_GALLARY);


    }

    //lang=java
    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Создаём файл для изображения
        currentImageFile = createImageFile();

        if (currentImageFile != null) {
            //Если файл создался - получаем его URI
            Uri imageUri = FileProvider.getUriForFile(this,
                    "com.example.skillberg_note.fileprovider",
                    currentImageFile);

            //Передаём URI в камеру
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.i(LOG_TAG +" onActivityResult ", "requestCode "+String.valueOf(requestCode));
//        Log.i(LOG_TAG +" onActivityResult ", "resultCode "+String.valueOf(resultCode));
//        Log.i(LOG_TAG +" onActivityResult ", "data "+String.valueOf(data));

        if (requestCode == REQUEST_CODE_PICK_FROM_GALLARY
                && resultCode == RESULT_OK
                && data != null) {

            // ПОлучаем URI изображения
            Uri imageUri = data.getData();

            Log.i(LOG_TAG + " onActivityResult ", "imageUri " + String.valueOf(imageUri));

            if (imageUri != null) {
                try {
                    //ПОлучаем InputStream, из которого будем декодировать Bitmap
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);

                    Log.i(LOG_TAG + " onActivityResult", "inputStream " + inputStream);

                    // Создаём файл
                    File imageFile = createImageFile();

                    // уходим в отдельный класс работы с файлами
                    FileStream fileStream = new FileStream();
                    fileStream.writeInputStreamToFile(inputStream, imageFile, imageUri);

                    Log.i(LOG_TAG + " onActivityResult", "imageFile " + imageFile);

                    //DataBaseOperation dataBaseOperation = new DataBaseOperation(context);
                    addImageToDatabase(imageFile);

                    Log.i(LOG_TAG, "onActivityResult getFileSizeBytes " + getFileSizeBytes(imageFile));


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO && requestCode == RESULT_OK) {

            //Bitmap bitmap = BitmapFactory.decodeFile(currentImageFile.getAbsolutePath());
            //Log.i("Test", "Bitmap size: " + bitmap.getWidth() + "x" + bitmap.getHeight());

             // Сохраняем изображение
            addImageToDatabase(currentImageFile);

            // На всякий случай обнуляем файл
            currentImageFile = null;

        }
    }

    // метод записи в базу
    public void addImageToDatabase(File file) {
        if (noteId == -1) {
            // На данный момент мы добавляем аттачи только в режиме редактирования
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.Images.COLUMN_PATH, file.getAbsolutePath());
        contentValues.put(NotesContract.Images.COLUMN_NOTE_ID, noteId);

        getContentResolver().insert(NotesContract.Images.URI, contentValues);

        Log.i(LOG_TAG,"addImageToDatebase "+file+" "+noteId);
    }

    @Nullable
    private File createImageFile() {
        //Генерируем имя файла
        String filename = System.currentTimeMillis() + ".jpg";

        // Получаем приватную директорию на карте памяти для хранения изображений
        // Выглядит она примерно так: /sdcard/Android/data/com.skillberg.notes/files/Pictures
        // Директория будет создана автоматически, если ещё не существует
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Создаём файл
        File image = new File(storageDir, filename);

        try {
            if (image.createNewFile()) {
                Log.i(LOG_TAG, "image.createNewFile() = true");
                return image;
            }
        } catch (IOException e) {
            Log.i(LOG_TAG, "image.createNewFile() = false");
            e.printStackTrace();
        }
        return null;
    }


    //@Override
    protected void displayNote(Cursor cursor) {

        if (cursor != null) {
            if (!cursor.moveToFirst()) {
                // Если не получилось перейти к первой строке — завершаем Activity
                finish();
            }

            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_TITLE));
            String noteText = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.Notes.COLUMN_NOTE));

            titleEt.setText(title);
            textEt.setText(noteText);
        }
    }

    // просто вызываем метод length() и получаем размер файла в байтах
    public String getFileSizeBytes(File file) {
        return file.length() + " bytes";
    }
}
