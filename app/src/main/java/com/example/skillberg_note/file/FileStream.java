package com.example.skillberg_note.file;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import com.example.skillberg_note.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileStream {

    static String LOG_TAG = FileStream.class.getName();

    private static final String fileName = "hello.txt";
    private static final String text = "Hello World";

    public void writeFile() {
        try {
            /*
             * Создается объект файла, при этом путь к файлу находиться методом класcа Environment
             * Обращение идёт, как и было сказано выше к внешнему накопителю
             */
            File myFile = new File(Environment.getExternalStorageDirectory().toString() + "/" + fileName);
            myFile.createNewFile();                                         // Создается файл, если он не был создан
            FileOutputStream outputStream = new FileOutputStream(myFile);   // После чего создаем поток для записи
            outputStream.write(text.getBytes());                            // и производим непосредственно запись
            outputStream.close();
            /*
             * Вызов сообщения Toast не относится к теме.
             * Просто для удобства визуального контроля исполнения метода в приложении
             */
            //Toast.makeText(this,"asb, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        /*
         * Аналогично создается объект файла
         */
        File myFile = new File(Environment.getExternalStorageDirectory().toString() + "/" + fileName);
        try {
            FileInputStream inputStream = new FileInputStream(myFile);
            /*
             * Буфферезируем данные из выходного потока файла
             */
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            /*
             * Класс для создания строк из последовательностей символов
             */
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                /*
                 * Производим построчное считывание данных из файла в конструктор строки,
                 * Псоле того, как данные закончились, производим вывод текста в TextView
                 */
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                //textView.setText(stringBuilder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeInputStreamToFile(InputStream inputStream, File outFile, Uri imageUri) throws IOException {
        /*
         *  нам нужно скопировать изображение к себе. Для этого нам понадобится следующий метод:
         */
        String text = "Hello World";

        FileOutputStream fileOutputStream = new FileOutputStream(outFile);

        Log.i(LOG_TAG, "outFile.getName()" + outFile.getName());
        Log.i(LOG_TAG, "outFile.getAbsolutePath() " + outFile.getAbsolutePath());

        byte[] buffer = new byte[8192];
        int n = 0;

        Log.i(LOG_TAG, "inputStream " + inputStream);
        Log.i(LOG_TAG, "inputStream.available() " + inputStream.available());

        //http://developer.alexanderklimov.ru/android/java/inputstream.php
//
        while ((n = inputStream.read(buffer)) > 0) {
            Log.i(LOG_TAG,"NN = "+n);
//fileOutputStream.write(buffer, 0, n);
        }

        fileOutputStream.write(text.getBytes());

//        ByteArrayOutputStream result = new ByteArrayOutputStream();
//        int length;
//        while ((length = inputStream.read(buffer)) != -1) {
//            result.write(buffer, 0, length);
//        }
//
//        Log.i(LOG_TAG, "result.toString "+ result.toString().length());
//
//        //
//
//        Log.i(LOG_TAG,"n "+n);


        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();

//        /*
//         * Get the file's content URI from the incoming Intent,
//         * then query the server app to get the file's display name
//         * and size.
//         */
//        //Uri returnUri = returnIntent.getData();
//        Cursor returnCursor =   getContentResolver().query(imageUri, null, null, null, null);
//        /*
//         * Get the column indexes of the data in the Cursor,
//         * move to the first row in the Cursor, get the data,
//         * and display it.
//         */
//
//        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
//        returnCursor.moveToFirst();
//
//        Log.i(LOG_TAG,"nameIndex "+nameIndex);
//        Log.i(LOG_TAG,"sizeIndex "+sizeIndex);

//        TextView nameView = (TextView) findViewById(R.id.filename_text);
//        TextView sizeView = (TextView) findViewById(R.id.filesize_text);
//        nameView.setText(returnCursor.getString(nameIndex));
//        sizeView.setText(Long.toString(returnCursor.getLong(sizeIndex)));

    }
}
