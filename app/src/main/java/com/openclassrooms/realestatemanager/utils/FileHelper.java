package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    public static String saveToInternalStorage(Context context, Bitmap bitmap, String fileName) {
        File directory = getFileDirectory(context);

        //Create media path
        File mediaPath = new File(directory, fileName);

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(mediaPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fos != null){
                try {
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return directory.getAbsolutePath();
    }

    private static File getFileDirectory(Context context) {
        ContextWrapper cw = new ContextWrapper(context);

        // Path to /data/data/com.openclassrooms/realestatemanager/media
        return cw.getDir("media", Context.MODE_PRIVATE);
    }

    public static File getFile(Context context, String fileName) {
        if(fileName == null) return null;

        File directory = getFileDirectory(context);
        String path = directory.getAbsolutePath();
        return new File(path, fileName);
    }

    public static Bitmap loadImageFromStorage(Context context, String fileName) {
        if(fileName == null) return null;

        File directory = getFileDirectory(context);
        String path = directory.getAbsolutePath();
        FileInputStream fis = null;

        try {
            File file = new File(path, fileName);
            fis = new FileInputStream(file);

            return BitmapFactory.decodeStream(fis);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static boolean deleteFile(Context context, String fileName) {
        if(fileName == null) return false;

        File directory = getFileDirectory(context);
        String path = directory.getAbsolutePath();

        File file = new File(path, fileName);
        if(file.exists()) {
            return file.delete();
        }

        return false;
    }
}
