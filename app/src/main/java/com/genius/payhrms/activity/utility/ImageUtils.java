package com.genius.payhrms.activity.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageUtils {

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap fileToBitmap(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static String fileToBase64(File file) throws IOException {
        Bitmap bitmap = fileToBitmap(file);
        return bitmapToBase64(bitmap);
    }

    public static byte[] fileToByteArray(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        return outputStream.toByteArray();
    }

    public static String byteArrayToBase64(byte[] byteArray) {
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
