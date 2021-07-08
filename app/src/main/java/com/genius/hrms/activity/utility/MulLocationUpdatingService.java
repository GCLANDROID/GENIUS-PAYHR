package com.genius.hrms.activity.utility;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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

import com.genius.hrms.activity.model.MulFenceModel;
import com.genius.hrms.activity.reciver.MulAlarm;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

public class MulLocationUpdatingService extends Service {
    private static final int ALARM_REQUEST_CODE = 1000;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    private LocationRequest locationRequest;
    Pref pref;
    float radiusValue;
    String addrrd;
    double lattitude, longitude;
    MulAlarm alarm = new MulAlarm();
    ArrayList<MulFenceModel> itemList = new ArrayList<>();

    String flat,seclat,thrdlat,frthlat,fivlat,sixlat,sevenlat,eightlat,ninlat,tenlat;
    TreeSet<String> frstValue=new TreeSet<>();
    double maxfrstlat,maxseclat,maxthrdlat,maxfrthlat,maxfivlat,maxsixlat,maxsevlat,maxeightlat,maxninlat,maxtenlat,minfrslat,minseclat,minthrdlat,minfrthlat,minfivlat,minsixlat,minsevlat,mineightlat,minninlat,mintenlat;
    TreeSet<String> scndValue=new TreeSet<>();
    TreeSet<String> thrdValue=new TreeSet<>();
    TreeSet<String> fourValue=new TreeSet<>();
    TreeSet<String> fiveValue=new TreeSet<>();
    TreeSet<String> sixValue=new TreeSet<>();
    TreeSet<String> sevenValue=new TreeSet<>();
    boolean flagt=false;
    String addrd;
    Timer timer = new Timer();
    TimerTask updateProfile = new MulCustomTask(MulLocationUpdatingService.this);


    @Override
    public void onCreate() {
        super.onCreate();
        pref = new Pref(this);


        //  initFirebaseDatabase();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationCallback();
        timer.scheduleAtFixedRate(updateProfile, 0, 1000);
       // getMulValue();



    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);


       // getfenceValue();
        startLocaionUpdate();
        showNotification();

        alarm.setAlarm(this);



       /* } else if (intent.getAction().equals(Constants.ACTION.STOP_ACTION)) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            stopForeground(true);
            stopSelf();
        }*/


        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, EmployeeDashBoardActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        String channelId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel(this, "channel-01", "Channel Name");
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
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createNotificationChannel(Context context, String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        ;
        service.createNotificationChannel(chan);
        return channelId;
    }

    protected LocationRequest createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000 * 14 * 60);
        locationRequest.setFastestInterval(1000 * 14 * 60);
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
                lattitude = location.getLatitude();
                longitude = location.getLongitude();
                 addrd= getCompleteAddressString(lattitude,longitude).replaceAll("\\s+", "");;
                getMulValue();
            }
        };
    }


    private void startLocaionUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);


    }

    private void logout(double lat, double lon) {

        if (itemList.size()==3) {

            if (lat > maxfrstlat && lat < minfrslat) {

                fenceinforservice(addrd);
                //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();



            } else if (lat > maxseclat && lat < minseclat) {

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            } else {

                fenceoutforservice(addrd);


            }
        }

        if (itemList.size()==4){
            if (lat>maxfrstlat && lat<minfrslat){

                fenceinforservice(addrd);
                //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (lat>maxseclat && lat<minseclat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (lat>maxthrdlat && lat<minthrdlat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else {

                fenceoutforservice(addrd);

            }
        }


        if (itemList.size()==5){
            if (lat>maxfrstlat && lat<minfrslat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (lat>maxseclat && lat<minseclat){

                fenceinforservice(addrd);
                //Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (lat>maxthrdlat && lat<minthrdlat){

                fenceinforservice(addrd);
                //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (lat>maxfrthlat && lat<minfrthlat){

                fenceinforservice(addrd);
                //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else {

                fenceoutforservice(addrd);

            }
        }


        if (itemList.size()==6){
            if (lat>maxfrstlat && lat<minfrslat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();


            }else if (lat>maxseclat && lat<minseclat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (lat>maxthrdlat && lat<minthrdlat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (lat>maxfrthlat && lat<minfrthlat){

                fenceinforservice(addrd);
                //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (lat>maxfivlat && lat<minfivlat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else {

                fenceoutforservice(addrd);

            }
        }



        if (itemList.size()==7){
            if (lat>maxfrstlat && lat<minfrslat){

                fenceinforservice(addrd);
                //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (lat>maxseclat && lat<minseclat){

                fenceinforservice(addrd);
                //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (lat>maxthrdlat && lat<minthrdlat){

                fenceinforservice(addrd);
                //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();


            }else if (lat>maxfrthlat && lat<minfrthlat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (lat>maxfivlat && lat<minfivlat){

                fenceinforservice(addrd);
                //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (lat>maxsixlat && lat<minsixlat){

                fenceinforservice(addrd);
                //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else {

                fenceoutforservice(addrd);

            }
        }




        if (itemList.size()==8){
            if (lat>maxfrstlat && lat<minfrslat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();


            }else if (lat>maxseclat && lat<minseclat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (lat>maxthrdlat && lat<minthrdlat){

                fenceinforservice(addrd);
                //  Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }else if (lat>maxfrthlat && lat<minfrthlat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (lat>maxfivlat && lat<minfivlat){

                fenceinforservice(addrd);
                // Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (lat>maxsixlat && lat<minsixlat){

                fenceinforservice(addrd);
                //   Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else if (lat>maxsevlat && lat<minsevlat){

               fenceinforservice(addrd);
                //   Toast.makeText(getApplicationContext(), "in range1", Toast.LENGTH_LONG).show();

            }
            else {

                fenceoutforservice(addrd);

            }
        }

    }

    /*public double CalculationByDistanceforservice(LatLng StartP, LatLng EndP) {
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
    }*/

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

    private void fenceinforservice(String adress) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateTimeString = sdf.format(d);
        Date dof = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(dof);
        String date = formattedDate + "=" + currentDateTimeString;
        String surl = pref.getIpAddress()+"GHRMSApi/api/get_EmployeewiseGeofence?EmployeeId=" + pref.getEmpId() + "&Longitude=" + longitude + "&Latitude=" + lattitude + "&Address=" + adress + "&FenceType=IN&Createdon=" + date + "&Operation=3&SecurityCode=" + pref.getSecurityCode();

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

                                Log.d("epr", "fence");
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

    private void fenceoutforservice(String address) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateTimeString = sdf.format(d);

        Date dof = Calendar.getInstance().getTime();


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(dof);


        String date = formattedDate + "=" + currentDateTimeString;
        String surl = pref.getIpAddress()+"GHRMSApi/api/get_EmployeewiseGeofence?EmployeeId=" + pref.getEmpId() + "&Longitude=" + longitude + "&Latitude=" + lattitude + "&Address=" + address + "&FenceType=OUT&Createdon=" + date + "&Operation=3&SecurityCode=" + pref.getSecurityCode();
        Log.d("outyrl", surl);
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
                                Log.d("epr", "not fence");
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

    private void setNotification() {


        String surl = pref.getIpAddress()+"GHRMSApi/api/pushNotification";
        Log.d("outyrl", surl);
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

    private void getfenceValue(){
        Log.d("mul","1");
        flat=itemList.get(0).getLattitude();
        seclat=itemList.get(1).getLattitude();
        Log.d("flat",flat);
        Log.d("seclat",seclat);

        frstValue.add(flat);
        frstValue.add(seclat);
        maxfrstlat= Double.parseDouble(frstValue.first());
        minfrslat= Double.parseDouble(frstValue.last());


        thrdlat=itemList.get(2).getLattitude();

        scndValue.add(seclat);
        scndValue.add(thrdlat);

        minseclat=Double.parseDouble(scndValue.last());
        maxseclat=Double.parseDouble(scndValue.first());



        if (itemList.size()==4){
            frthlat=itemList.get(3).getLattitude();
            thrdValue.add(frthlat);
            thrdValue.add(thrdlat);

            maxthrdlat= Double.parseDouble(thrdValue.first());
            minthrdlat=Double.parseDouble(thrdValue.last());
            Log.d("maxvalue",thrdValue.first());
            Log.d("minvalue",thrdValue.last());



        }

        if (itemList.size()==5){
            frthlat=itemList.get(3).getLattitude();
            fivlat=itemList.get(4).getLattitude();
            fourValue.add(frthlat);
            fourValue.add(fivlat);

            maxfrthlat=Double.parseDouble(fourValue.first());
            minfrthlat=Double.parseDouble(fourValue.last());

            Log.d("maxfourvalue",fourValue.first());
            Log.d("minfourvalue",fourValue.last());



        }


        if (itemList.size()==6){
            fivlat=itemList.get(4).getLattitude();
            sixlat=itemList.get(5).getLattitude();
            fiveValue.add(sixlat);
            fiveValue.add(fivlat);

            maxfivlat=Double.parseDouble(fiveValue.first());
            minfivlat=Double.parseDouble(fiveValue.last());

            Log.d("maxfivevalue",fiveValue.first());
            Log.d("minfivevalue",fiveValue.last());



        }


        if (itemList.size()==7){

            sixlat=itemList.get(5).getLattitude();
            sevenlat=itemList.get(6).getLattitude();
            sixValue.add(sixlat);
            sixValue.add(sevenlat);

            maxsixlat=Double.parseDouble(sixValue.first());
            minsixlat=Double.parseDouble(sixValue.last());

            Log.d("maxsixvalue",sixValue.first());
            Log.d("minsixvalue",sixValue.last());



        }



        if (itemList.size()==8){


            sevenlat=itemList.get(6).getLattitude();
            eightlat=itemList.get(7).getLattitude();
            sevenValue.add(eightlat);
            sevenValue.add(sevenlat);

            maxsevlat=Double.parseDouble(sevenValue.first());
            minsevlat=Double.parseDouble(sevenValue.last());

            Log.d("maxssevenvalue",sevenValue.first());
            Log.d("minssevenalue",sevenValue.last());



        }








    }

    private void getMulValue() {
        String surl1 = pref.getIpAddress()+"GHRMSApi/api/get_EmployeeGeofenceMultipointConfigure?EmployeeId="+pref.getEmpId()+"&GeoFenceId=000&Operation=1&SecurityCode="+pref.getSecurityCode();

        Log.d("valuefetechurl", surl1);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl1,
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
                                JSONArray jArray=job1.optJSONArray("responseData");
                                for (int i =0;i<jArray.length();i++){
                                    JSONObject jObject=jArray.getJSONObject(i);
                                    double Latitude= Double.parseDouble(jObject.optString("Latitude"));
                                    double Longitude= Double.parseDouble(jObject.optString("Longitude"));
                                    if (lattitude>Latitude&& lattitude<Longitude){
                                        flagt=true;
                                    }else {

                                    }



                                    MulFenceModel mModel=new MulFenceModel("0","0");
                                    itemList.add(mModel);

                                }
                                if (flagt==true){
                                    fenceinforservice(addrd);
                                }else {
                                    fenceoutforservice(addrd);
                                }
                                pd.dismiss();
                                //getfenceValue();




                            } else {
                                pd.dismiss();
                                //  Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

                            }


                            // boolean _status = job1.getBoolean("status");


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // (EmployeeDashBoardActivity.this, "Volly Error", Toast.LENGTH_LONG).show();
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







}
