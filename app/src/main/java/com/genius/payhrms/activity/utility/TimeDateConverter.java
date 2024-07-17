package com.genius.payhrms.activity.utility;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDateConverter {
    private static final String TAG = "TimeDateConverter";
    public static String loginTimeConverter(String inputDateString){
        String outputTimeString="";
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");

        try {
            Date date = inputFormat.parse(inputDateString);
            outputTimeString = outputFormat.format(date);
            Log.e(TAG, "loginTimeConverter: "+ outputTimeString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputTimeString;
    }

    public static String convert_Date_MM_DD_YYYY_To_dd_MMM_yyyy(String inputDateString){
        String outputTimeString="";
        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date date = inputFormat.parse(inputDateString);
            String outputDate = outputFormat.format(date);
            outputTimeString = outputDate;
            //System.out.println(outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputTimeString;
    }


    public static String convert_Date_DD_MM_YYYY_To_dd_MMM_yyyy(String inputDateString){
        String outputTimeString="";
        DateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date date = inputFormat.parse(inputDateString);
            String outputDate = outputFormat.format(date);
            outputTimeString = outputDate;
            //System.out.println(outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputTimeString;
    }


    public static String convert_Date_YYYY_MM_DD_To_dd_MMM_yyyy(String inputDateString){
        String outputTimeString="";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

        try {
            Date date = inputFormat.parse(inputDateString);
            String outputDate = outputFormat.format(date);
            outputTimeString = outputDate;
            //System.out.println(outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputTimeString;
    }
}
