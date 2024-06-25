package com.genius.payhrms.activity.dailylog;

import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.helper.DatabaseHelper;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.GPSTracker;
import com.genius.payhrms.activity.utility.Pref;
import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QRCodeScannerActivity extends AppCompatActivity {
    private static final String TAG = "QRCodeScannerActivity";
    private CodeScanner mCodeScanner;
    double qrlat,qrlong;
    GPSTracker gps;
    double currentlaat,currentlong;
    AlertDialog alerDialog1;
    Pref pref;
    private DatabaseHelper db;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static String DATA_SAVED_BROADCAST = "";
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);
        Log.e(TAG, "onCreate: QR Attendance");
        initView();
    }

    private void initView(){
        db = new DatabaseHelper(this);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again

            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
        pref=new Pref(QRCodeScannerActivity.this);
        gps = new GPSTracker(QRCodeScannerActivity.this);
        if (gps.canGetLocation()) {
            currentlaat = gps.getLatitude();
            currentlong = gps.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
        }
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String coordinates=result.getText();
                        String[] separated = coordinates.split(",");
                        qrlat= Double.parseDouble(separated[0]);
                        qrlong= Double.parseDouble(separated[1]);

                        LatLng p = new LatLng(qrlat, qrlong);
                        LatLng q=new LatLng(currentlaat,currentlong);
                        Double distance = CalculationByDistance(p,q)*1000;
                        Double radius=distance/100;
                        if (distance<100||distance==100){
                            JSONObject object=new JSONObject();
                            try {
                                object.put("EmployeeId",pref.getEmpId());
                                object.put("Remarks","QR");
                                object.put("Longitude","");
                                object.put("Latitude","");
                                object.put("Address","");
                                object.put("ApprovalStatus","1");
                                object.put("Year","0");
                                object.put("Month","0");
                                object.put("Operation","3");
                                object.put("SecurityCode",pref.getSecurityCode());
                                postAttendance2(object);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //postAttendance();
                        }else {
                            Toast.makeText(QRCodeScannerActivity.this, "Sorry! You are out of Location Range", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    private void postAttendance() {

        /*object.put("EmployeeID",pref.getEmpId());
        object.put("RemarksIN","QR");
        object.put("LongitudeIN","");
        object.put("LatitudeIN","");
        object.put("AddressIN","");
        object.put("ApprovalStatus","1");
        object.put("Year","0");
        object.put("Month","0");
        object.put("Operation","3");
        object.put("SecurityCode",pref.getSecurityCode());*/

        final ProgressDialog pd=new ProgressDialog(QRCodeScannerActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(Api.sPostQRAttendance)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("RemarksIN", "QR")
                .addMultipartParameter("LongitudeIN", "")
                .addMultipartParameter("LatitudeIN", "")
                .addMultipartParameter("AddressIN", "")
                .addMultipartParameter("ApprovalStatus", "1")
                .addMultipartParameter("Year", "0")
                .addMultipartParameter("Month", "0")
                .addMultipartParameter("Operation", "3")
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("QR", "QR_ATTENDANCE: "+response.toString());
                        pd.dismiss();
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            successAlert();
                        } else {
                            Toast.makeText(QRCodeScannerActivity.this, Response_Message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "onError: "+anError);
                        pd.dismiss();
                        if (anError.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Date d = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            String currentDateTimeString = sdf.format(d);

                            Date dof = Calendar.getInstance().getTime();


                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(dof);

                            String date = formattedDate + "  " + currentDateTimeString;
                            saveNameToLocalStorage( date, NAME_NOT_SYNCED_WITH_SERVER);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.d("RadiusValue", " KM " + kmInDec
                + " Meter " + meterInDec);
        String distance = String.format("%.3f", valueResult);
        final double ddis = Double.parseDouble(distance);
        Log.d("distance", String.valueOf(ddis));
        final Handler handler = new Handler();

        // Toast.makeText(getApplicationContext(),distance+"KM",Toast.LENGTH_LONG).show();
        return ddis;
    }

    private void postAttendance2(JSONObject object) {
        Log.e(TAG, "postAttendance2: "+object.toString());
        final ProgressDialog pd=new ProgressDialog(QRCodeScannerActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sPostQRAttendance)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "QR_ATTENDANCE: "+response.toString());
                        pd.dismiss();
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message = job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            successAlert();
                        } else {
                            Toast.makeText(QRCodeScannerActivity.this, Response_Message, Toast.LENGTH_SHORT).show();
                        }

                        /*  JSONObject job = response;
                        boolean responseStatus = job.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert();
                        } else {

                        }*/


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        pd.dismiss();
                        if (error.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Date d = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            String currentDateTimeString = sdf.format(d);

                            Date dof = Calendar.getInstance().getTime();


                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(dof);

                            String date = formattedDate + "  " + currentDateTimeString;
                            saveNameToLocalStorage( date, NAME_NOT_SYNCED_WITH_SERVER);
                        }
                    }
                });
    }

    private void login(JSONObject jsonObject) {
        final ProgressDialog pd = new ProgressDialog((Context) QRCodeScannerActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sLoginapi)
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String Genius_Access_Token=obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);
                                // boolean _status = job1.getBoolean("status");
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                    }
                });
    }

    private void successAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(QRCodeScannerActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
                onBackPressed();

            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText("Your Attendance saved successfully");

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }

    private void saveNameToLocalStorage( String date ,int status) {
        db.addName( date,  status);
        successAlert();
    }
}