package com.genius.payhrms.activity.utility;

import android.util.Log;

import java.net.URL;

public class FindDocumentInformation {
    private static final String TAG = "FindDocumentName";
    public static String FileNameFromURL(String fileURL){
        String NAME = "";
        String arrayURL[] = fileURL.split("/");
        Log.e(TAG, "FileNameFromURL: Number of Split: "+arrayURL.length);
        NAME = arrayURL[(arrayURL.length)-1];
        return NAME;
    }

    public static String FindFileTypeFromDocumentName(String fileName){
        String TYPE = "";
        Log.e(TAG, "FindFileTypeFromDocumentName: "+fileName);
        String arrayType[] = fileName.split(".");
        int lastDotIndex = fileName.lastIndexOf('.');
        //Log.e(TAG, "FindFileTypeFromDocumentName: type: size: "+arrayType.length);
        //Log.e(TAG, "FindFileTypeFromDocumentName: type: "+arrayType[(arrayType.length)-1]);
        TYPE = fileName.substring(lastDotIndex+1);
        return TYPE;
    }

    public static String getFileExtension(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            return path.substring(path.lastIndexOf('.') + 1);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static String getFileType(String doc){
        try {
            //URL url = new URL(fileUrl);
            //String path = url.getPath();
            //return path.substring(path.lastIndexOf('.') + 1);
            String[] parts = doc.split(";");
            Log.e(TAG, "getFileType: "+parts[0]);
            String re = parts[0].replace("data:","");
            return re;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "";
        }

    }


    public static String getBase64Url(String doc){
        try {
            //URL url = new URL(fileUrl);
            //String path = url.getPath();
            //return path.substring(path.lastIndexOf('.') + 1);
            /*String[] parts = doc.split(";");
            Log.e(TAG, "getBase64Url: "+parts[1]);
            String parts1[] = parts[1].split(",");
            //String part2 = parts[0].replace("data:","");
            //String re = parts[0].replace("data:","");
            Log.e(TAG, "getBase64Url 1: "+parts1[0]);
            String parts2[] = parts1[1].split("\\$");
            Log.e(TAG, "getBase64Url: 2: "+parts2[0] );
*/
            String[] parts = doc.split(",");
            String part1 = parts[1];
            String[] partsB = part1.split("\\$");
            String doclink = partsB[0];
            Log.e(TAG, "getBase64Url: "+doclink);
            return doclink;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "";
        }

    }
}
