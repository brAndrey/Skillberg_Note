package com.example.skillberg_note.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class CopyFile {


    public void copyFile(File sourceFile, File destFile) throws IOException {
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }

    // в этом классе мы получаем размер файла
    //public class FileSize {

        public  void main(String[] args) {
            File file = new File("/Users/prologistic/Desktop/TestVideo.mov");
            if(file.exists()){
                System.out.println(getFileSizeBytes(file));
                System.out.println(getFileSizeKiloBytes(file));
                System.out.println(getFileSizeMegaBytes(file));
            }else System.out.println("Файла нет!");

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
}
