package com.genius.payhrms.activity.utility;

import android.content.Context;
import android.net.NetworkInfo;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Util implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static NetworkInfo networkInfo;
    private static int countryCode;
    private static Context c = null;
    public static String globalDateFormate = "yyyy-MM-dd'T'HH:mm:ss";

    private static String INIT_VECTOR="6832054171691981";
    public static String SECRET_KEY="74074750353890398886017484399862";


    public static String changeAnyDateFormat(String reqdate, String dateformat, String reqformat) {
        //String	date1=reqdate;

        if (reqdate.equalsIgnoreCase("") ||reqdate.equalsIgnoreCase("null") || dateformat.equalsIgnoreCase("") || reqformat.equalsIgnoreCase(""))
            return "";
        SimpleDateFormat format = new SimpleDateFormat(dateformat);
        String changedate = "";
        Date dt = null;
        if (!reqdate.equals("") && !reqdate.equals("null")) {
            try {
                dt = format.parse(reqdate);
                //SimpleDateFormat your_format = new SimpleDateFormat("dd-MMM-yyyy");
                SimpleDateFormat your_format = new SimpleDateFormat(reqformat);
                changedate = your_format.format(dt);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return reqdate;
            }


        }
        return changedate;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    public static String encrypt(String value,String KEY) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
