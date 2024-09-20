package com.genius.payhrms.activity.utility;


import static org.apache.commons.io.IOUtils.DEFAULT_BUFFER_SIZE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileToBase64Converter {
    private static final String TAG = "FileToBase64Converter";
    public static String imageToBase64(String imagePath) {
        Log.e(TAG, "FileToBase64: "+imagePath);
        File file = new File(imagePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(imageData);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        return null;
    }



    public static Bitmap base64ToImage(String base64Image){
        byte[] decodedString = new byte[0];
        Bitmap decodedByte = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            decodedString = Base64.getDecoder().decode(base64Image);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            //return decodedByte;
        }
        return decodedByte;
    }

    public static String convertToBase64(File file) throws IOException {
        // Read the PDF file into a byte array
        //File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] pdfBytes = new byte[(int) file.length()];
        fis.read(pdfBytes);
        fis.close();
        String base64Encoded = null;
        // Encode the byte array to Base64
       /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            base64Encoded = Base64.getEncoder().encodeToString(pdfBytes);
        }*/
        base64Encoded = android.util.Base64.encodeToString(pdfBytes, android.util.Base64.DEFAULT);
        //String base64Encoded =   Base64.decode(base64, Base64.)
        //String base64Encoded = Base64.getDecoder().decode(pdfBytes);
        return base64Encoded;
    }

    public static File convertInputStreamToFile(Context context,Uri uri, String fileNme) {
        InputStream inputStream;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file= new File(context.getExternalFilesDir("/").getAbsolutePath(), fileNme);

        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            Log.e(TAG, "convertInputStreamToFile: file: "+file.getPath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }


}
