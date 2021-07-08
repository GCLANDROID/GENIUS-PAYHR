package com.genius.hrms.activity.utility;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;

import android.util.Log;


import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationUpdatingService extends Service {
    private static final int ALARM_REQUEST_CODE =1000 ;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    private LocationRequest locationRequest;
    Pref pref;
    float radiusValue;
    String addrrd;
    String lattitude,longitude;





    @Override
    public void onCreate() {
        super.onCreate();
        pref=new Pref(this);
        getValue();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationCallback();

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
            startLocaionUpdate();
            showNotification();

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification() {
        Log.d("rtrt","o");
        Intent notificationIntent = new Intent(this, EmployeeDashBoardActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        String channelId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel(this,"channel-01", "Channel Name");
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)

        }
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Updating your location")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();
        startForeground(1,
                notification);



// schedule the task to run starting now and then every 1 min


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createNotificationChannel(Context context, String channelId, String channelName){
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);;
        service.createNotificationChannel(chan);
        return channelId;
    }

    protected LocationRequest createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000*14*60);
        locationRequest.setFastestInterval(1000*14*60);
        //  locationRequest.setSmallestDisplacement(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d("Farman-Accuray", String.valueOf(location.getAccuracy()));
                    // if (location.getAccuracy() < 18 && location.getAccuracy() > 0) {


                    Intent intent = new Intent();
                    intent.setAction("com.example.foody.LOCATION_UPDATE_INTENT");
                    intent.putExtra(Constants.ACTION.LATITUDE, location.getLatitude());
                    intent.putExtra(Constants.ACTION.LONGITUDE, location.getLongitude());
                    sendBroadcast(intent);

                    //  }
                }
                Location location = locationResult.getLastLocation();
                Intent intent = new Intent();
                intent.setAction("com.rogagocorp.yatnow.LOCATION_UPDATE_INTENT");
                intent.putExtra(Constants.ACTION.LATITUDE, location.getLatitude());
                intent.putExtra(Constants.ACTION.LONGITUDE, location.getLongitude());
                sendBroadcast(intent);
                lattitude= String.valueOf(location.getLatitude());
                longitude=String.valueOf(location.getLongitude());
                logout(Double.parseDouble(lattitude),Double.parseDouble(longitude));

            }
        };
    }

    private void getValue() {

       String surl = pref.getIpAddress()+"GHRMSApi/api/get_EmployeeGeofenceConfigure?EmployeeId=" + pref.getEmpId() + "&GeoFenceId=000&Operation=1&SecurityCode=" + pref.getSecurityCode();
        Log.d("valuefetechurl", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseconfig", response);


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("responseconfig", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");
                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {

                                JSONArray responseData = job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++) {
                                    JSONObject obj = responseData.getJSONObject(i);
                                    String SLongitude = obj.optString("SLongitude");
                                    String SLatitude = obj.optString("SLatitude");
                                    String EndPoint = obj.optString("EndPoint");
                                    int endPoint = Integer.parseInt(EndPoint);
                                    radiusValue = endPoint;


                                }




                            } else {

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(EmployeeDashBoardActivity.this, "volly 2" + error.toString(), Toast.LENGTH_LONG).show();

                Log.e("ert", error.toString());

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }



    private void startLocaionUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);


    }

    private void logout(double lat,double lon){
        addrrd=getCompleteAddressString(lat,lon).replaceAll("\\s+", "");;
        Log.d("address",addrrd);
        LatLng strtlatlng=new LatLng(Double.parseDouble(lattitude),Double.parseDouble(longitude));
        LatLng endlatlong=new LatLng(Double.parseDouble(pref.getOwnLat()),Double.parseDouble(pref.getOwnLong()));
        CalculationByDistanceforservice(strtlatlng,endlatlong);


    }

    public double CalculationByDistanceforservice(LatLng StartP, LatLng EndP) {
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


        if (ddis < radiusValue || ddis == radiusValue) {
            //Toast.makeText(getApplicationContext(), "fencing zone", Toast.LENGTH_LONG).show();


            fenceinforservice();


        } else {
            //Toast.makeText(getApplicationContext(), "not fencing zone", Toast.LENGTH_LONG).show();

            fenceoutforservice();



        }



        // Toast.makeText(getApplicationContext(),distance+"KM",Toast.LENGTH_LONG).show();
        return Radius * c;
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
                String country = returnedAddress.getCountryName();
                String postal_code = returnedAddress.getPostalCode();
                String state = returnedAddress.getAdminArea();
                String city = returnedAddress.getLocality();


              /* pref.saveString(Constants.PreferenceKey.CURRENT_ADDRESS, strAdd);
               pref.saveString(Constants.PreferenceKey.CURRENT_COUNTRY, country);
               pref.saveString(Constants.PreferenceKey.CURRENT_POSTAL_CODE, postal_code);
               pref.saveString(Constants.PreferenceKey.CURRENT_STATE, state);
               pref.saveString(Constants.PreferenceKey.CURRENT_CITY, city);*/

                //  pref.saveCurrentAddress(strAdd);
                Log.w("My Current address", strReturnedAddress.toString());
            } else {
                Log.w("My Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current address", "Canont get Address!");
        }
        return strAdd;
    }

    private void fenceinforservice() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateTimeString = sdf.format(d);

        Date dof = Calendar.getInstance().getTime();


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(dof);


        String date = formattedDate + "=" + currentDateTimeString;
        String surl = pref.getIpAddress()+"GHRMSApi/api/get_EmployeewiseGeofence?EmployeeId=" + pref.getEmpId() + "&Longitude=" + longitude + "&Latitude=" + lattitude + "&Address=" + addrrd + "&FenceType=IN&Createdon=" + date + "&Operation=3&SecurityCode=" + pref.getSecurityCode();

        Log.d("inurl", surl);
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

                                Log.d("epr","fence"+responseText);

                                setNotification();




                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void fenceoutforservice() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateTimeString = sdf.format(d);

        Date dof = Calendar.getInstance().getTime();


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(dof);


        String date = formattedDate + "=" + currentDateTimeString;
        String surl = pref.getIpAddress()+"GHRMSApi/api/get_EmployeewiseGeofence?EmployeeId=" + pref.getEmpId() + "&Longitude=" + longitude + "&Latitude=" + lattitude + "&Address=" + addrrd + "&FenceType=OUT&Createdon=" + date + "&Operation=3&SecurityCode=" + pref.getSecurityCode();
        Log.d("outyrl",surl);
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


                                 setNotification();
                                Log.d("epr","not fence"+responseText);
                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void setNotification(){


        String surl = pref.getIpAddress()+"GHRMSApi/api/pushNotification";
        Log.d("outyrl",surl);
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



                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }






}
