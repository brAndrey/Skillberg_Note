package com.example.skillberg_note.file;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import android.provider.OpenableColumns;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.skillberg_note.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class FileStream {

    static String LOG_TAG = FileStream.class.getName();

    private static final String fileName = "hello.txt";
    private static final String text = "Hello World";

    private Context intContext;

    private Context intBaseContext;


    public FileStream (Context intContext,Context intBaseContext){
        this.intContext = intContext;
        this.intBaseContext = intBaseContext;
    }


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
        FileOutputStream fileOutputStream = new FileOutputStream(outFile);

//        Log.i(LOG_TAG, "outFile.getName()" + outFile.getName());
//        Log.i(LOG_TAG, "outFile.getAbsolutePath() " + outFile.getAbsolutePath());

        byte[] buffer = new byte[8192];
        int n = 0;

        //http://developer.alexanderklimov.ru/android/java/inputstream.php

        while ((n = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, n);
        }

        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();

        /*
        *  Вопрос о определении размера полученного файла так и не решон
        * */
    }

    @Nullable
    public File createImageFile() {
        //Генерируем имя файла
        String filename = System.currentTimeMillis() + ".jpg";
        File image=null;

        // Получаем приватную директорию на карте памяти для хранения изображений
        // Выглядит она примерно так: /sdcard/Android/data/com.skillberg.notes/files/Pictures
        // Директория будет создана автоматически, если ещё не существует
        try {

            File storageDir = intBaseContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            // Создаём файл
            image = new File(storageDir, filename);
           //image = new File(storageDir, tipeContext+".txt");
            Log.i(LOG_TAG," storageDir "+storageDir+"/"+filename);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LOG_TAG," NO create file");
        }

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



    // метод возвращает размер файла в мегабайтах
    // длину файла делим на 1 мегабайт (1024 * 1024 байт) и узнаем количество мегабайт
    public String getFileSizeMegaBytes(File file) {
        return (double) file.length()/(1024*1024)+" mb";
    }

    // метод возвращает размер файла в килобайтах
    // длину файла делим на 1 килобайт (1024 байт) и узнаем количество килобайт
    public String getFileSizeKiloBytes(File file) {
        return (double) file.length()/1024 + " kb";
    }

    // просто вызываем метод length() и получаем размер файла в байтах
    public String getFileSizeBytes(File file) {
        return file.length() + " bytes";
    }
    //}

    // просто вызываем метод length() и получаем размер файла в байтах
    public int getFileSizeBytesInt(File file) {
        return (int) file.length();
    }

    private String ArreyToString(String[] arrey) {
        String rez = "";
        for (String num : arrey) {
            rez = rez + " " + num;
        }
        return rez;
    }

    private String ArreyToString(List arrey) {
        String rez = "";
        for (Object num:  arrey) {
            rez = rez + " " + num;
        }
        return rez;
    }

}



//        try {
//            File[] roots = ContextCompat.getExternalFilesDirs(intContext, null);
//            if (roots != null) {
//                for (File root : roots) {
//                    if (root != null) {
//                        Log.i(LOG_TAG, " ContextCompat "+root);
//                    }
//                }
//            }
//        }
//
//        catch (Exception e)
//        {
//            Log.i(LOG_TAG, " ContextCompat не прокатил");
//            e.printStackTrace();
//        }