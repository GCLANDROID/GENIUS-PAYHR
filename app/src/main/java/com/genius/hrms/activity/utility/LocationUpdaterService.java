package com.genius.hrms.activity.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.genius.hrms.R;


import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;

import com.genius.hrms.activity.helper.FencingDatabaseHelper;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationUpdaterService extends Service {
    public LocationManager mLocationManager;
    public LocationUpdaterListener mLocationListener;
    public Location previousBestLocation = null;
    public static final int TWO_MINUTES = 1000; // 120 seconds
    public static Boolean isRunning = false;
    Pref pref;
    Context context;
    String lat;
    String longitude;
    String location;
    String address;

    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    FencingDatabaseHelper db;
    String date;
    //float radiusValue;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final String TAG = LocationUpdaterService.class.getSimpleName();
    private final static String FOREGROUND_CHANNEL_ID = "foreground_channel_id";
    private NotificationManager mNotificationManager;
    private Handler handler;
    private int count = 0;
    private static int stateService = Constants.STATE_SERVICE.NOT_CONNECTED;

    private int counter = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationUpdaterListener();
        pref = new Pref(this);

        db=new FencingDatabaseHelper(this);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        stateService = Constants.STATE_SERVICE.NOT_CONNECTED;

        /*int endPoint= Integer.parseInt(pref.getEndPoint());
        float c=endPoint/10;
        float de=c/10;
        radiusValue=de/10;*/





        // Toast.makeText(this,"start",Toast.LENGTH_LONG).show();
        super.onCreate();

    }

    Handler mHandler = new Handler();
    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) {
                startListening();



            }
            mHandler.postDelayed(mHandlerTask, TWO_MINUTES);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent != null) {
            mHandlerTask.run();

            return START_NOT_STICKY;
        }

        // if user starts the service


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        stateService = Constants.STATE_SERVICE.NOT_CONNECTED;
        super.onDestroy();
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) mLocationListener);

            if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }
        isRunning = true;
    }

    private void stopListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(mLocationListener);
        }
        isRunning = false;
    }

    public class LocationUpdaterListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (isBetterLocation(location, previousBestLocation)) {
                previousBestLocation = location;
                try {
                    lat = String.valueOf(previousBestLocation.getLatitude());
                    longitude = String.valueOf(previousBestLocation.getLongitude());
                    pref.saveEndLat(lat);
                    pref.saveEndLong(longitude);
                    Log.d("latiet", lat);
                    Log.d("longitude", longitude);
                    address = getCompleteAddressString(previousBestLocation.getLatitude(), previousBestLocation.getLongitude());

                   // CalculationByDistance();
                    double clat = Double.parseDouble(pref.getOwnLat());
                    Log.d("clat", String.valueOf(clat));
                    double clong = Double.parseDouble(pref.getOwnLong());
                    LatLng latLng = new LatLng(clat, clong);
                    LatLng elatLng = new LatLng(previousBestLocation.getLatitude(), previousBestLocation.getLongitude());
                    CalculationByDistance(latLng,elatLng);
                    double dlat=clat-0.0000500;
                    Log.d("dlat", String.valueOf(dlat));





                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopListening();
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            stopListening();
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private void locationUpdate() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authentication..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Log.d("riku", "run");


    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current ", strReturnedAddress.toString());
            } else {
                Log.w("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current", "Canont get Address!");
        }
        return strAdd;
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
        Log.d("RadiusValue", valueResult + " KM " + kmInDec
                + " Meter " + meterInDec);
        String distance = String.format("%.3f", valueResult);
        double ddis = Double.parseDouble(distance);
        Log.d("chota", String.valueOf(ddis));
        Date dof = Calendar.getInstance().getTime();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateTimeString = sdf.format(d);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(dof);
        date = formattedDate+"="+currentDateTimeString;
        if (ddis < 0.00 || ddis == 0.00) {
            //Toast.makeText(getApplicationContext(), "fencing zone", Toast.LENGTH_LONG).show();
       /*     String surl = "https://www.cloud.geniusconsultant.com/GHRMSApi/api/get_EmployeewiseGeofence?EmployeeId="+pref.getEmpId()+"&Longitude="+longitude+"&Latitude="+lat+"&Address="+address.replaceAll("\\s+","")+"&FenceType=IN&Createdon="+date+"&Operation=3&SecurityCode="+pref.getSecurityCode();
            Log.d("inputatt", surl);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("responseLeave", response);

                            try {
                                JSONObject job1 = new JSONObject(response);
                                Log.e("response12", "@@@@@@" + job1);
                                String responseText = job1.optString("responseText");
                                boolean responseStatus = job1.optBoolean("responseStatus");
                                if (responseStatus) {
                                    addNotification();
                                }


                                // boolean _status = job1.getBoolean("status");


                            } catch (JSONException e) {
                                e.printStackTrace();
                              //  Toast.makeText(context, "Volly Error", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                   // Toast.makeText(context, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                    Log.e("ert", error.toString());
                    saveNameToLocalStorage(address.replaceAll("\\s+", ""),date,lat,longitude,"IN",NAME_NOT_SYNCED_WITH_SERVER);


                }
            }) {

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
            Toast.makeText(getApplicationContext(), "not fencing zone", Toast.LENGTH_LONG).show();*/




        } else {
           /* String surl = "https://www.cloud.geniusconsultant.com/GHRMSApi/api/get_EmployeewiseGeofence?EmployeeId="+pref.getEmpId()+"&Longitude="+longitude+"&Latitude="+lat+"&Address="+address.replaceAll("\\s+","")+"&FenceType=OUT&Createdon="+date+"&Operation=3&SecurityCode="+pref.getSecurityCode();
            Log.d("inputatt333", surl);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("responseLeave", response);

                            try {
                                JSONObject job1 = new JSONObject(response);
                                Log.e("response12", "@@@@@@" + job1);
                                String responseText = job1.optString("responseText");
                                boolean responseStatus = job1.optBoolean("responseStatus");
                                if (responseStatus) {
                                    outNotification();

                                }


                                // boolean _status = job1.getBoolean("status");


                            } catch (JSONException e) {
                                e.printStackTrace();
                               // Toast.makeText(context, "Volly Error", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("ert", error.toString());
                    saveNameToLocalStorage(address.replaceAll("\\s+", ""),date,lat,longitude,"OUT",NAME_NOT_SYNCED_WITH_SERVER);
                }
            }) {

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


            Toast.makeText(getApplicationContext(), "not fencing zone", Toast.LENGTH_LONG).show();
*/
        }


        return Radius * c;
    }


    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.geniuslogo)
                        .setContentTitle("Notifications Example")
                        .setContentText("fence zone");


        /*Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);*/

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    private void outNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.geniuslogo)
                        .setContentTitle("Notifications Example")
                        .setContentText("not fence zone");


        /*Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);*/

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }


    private void saveNameToLocalStorage(String address ,String date, String lat, String longt,String remarks, int status) {
        db.addName(address,date,  lat, longt,remarks,  status);
        Log.d("local","arppan");



    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void print(){
        Log.d("print","1");
    }

    private void connect() {
        // after 10 seconds its connected
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Log.d(TAG, "Bluetooth Low Energy device is connected!!");

                        Toast.makeText(getApplicationContext(),"Connected!",Toast.LENGTH_SHORT).show();
                        stateService = Constants.STATE_SERVICE.CONNECTED;
                        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, prepareNotification());

                    }
                }, 10000);

    }


    @SuppressLint("WrongConstant")
    private Notification prepareNotification() {
        // handle build version above android oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                mNotificationManager.getNotificationChannel(FOREGROUND_CHANNEL_ID) == null) {
            CharSequence name = "tetst";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(FOREGROUND_CHANNEL_ID, name, importance);
            channel.enableVibration(false);
            mNotificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(this, EmployeeDashBoardActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // if min sdk goes below honeycomb
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }*/

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // make a stop intent
        Intent stopIntent = new Intent(this, LocationUpdaterService.class);
        stopIntent.setAction(Constants.ACTION.STOP_ACTION);
        PendingIntent pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification);
        remoteViews.setOnClickPendingIntent(R.id.btn_stop, pendingStopIntent);

        // if it is connected
        switch(stateService) {
            case Constants.STATE_SERVICE.NOT_CONNECTED:
                remoteViews.setTextViewText(R.id.tv_state, "DISCONNECTED");
                break;
            case Constants.STATE_SERVICE.CONNECTED:
                remoteViews.setTextViewText(R.id.tv_state, "CONNECTED");
                break;
        }

        // notification builder
        NotificationCompat.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(this, FOREGROUND_CHANNEL_ID);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }
        notificationBuilder
                .setContent(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        return notificationBuilder.build();
    }










}
