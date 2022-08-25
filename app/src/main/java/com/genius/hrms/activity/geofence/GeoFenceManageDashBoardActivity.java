package com.genius.hrms.activity.geofence;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.developers.imagezipper.ImageZipper;
import com.genius.hrms.R;
import com.genius.hrms.activity.activity.EmployeeDashBoardActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.attendance.AttendanceActivity;
import com.genius.hrms.activity.attendance.AttendanceManageActivity;
import com.genius.hrms.activity.attendance.AttendanceReportActivity;
import com.genius.hrms.activity.attendance.BacklogActivity;
import com.genius.hrms.activity.dailylog.NumberTourActivity;
import com.genius.hrms.activity.dailylog.OfflineDailyLogManageActivity;
import com.genius.hrms.activity.dailylog.OfflineDailyLogReportActivity;
import com.genius.hrms.activity.dailylog.VisitLocationActivity;
import com.genius.hrms.activity.utility.GPSTracker;
import com.genius.hrms.activity.utility.LocationUpdaterService;
import com.genius.hrms.activity.utility.Pref;
import com.wajahatkarim3.longimagecamera.LongBackImageCameraActivity;
import com.wajahatkarim3.longimagecamera.LongImageCameraActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoFenceManageDashBoardActivity extends AppCompatActivity {
    ImageView imgBack, imgHome;
    LinearLayout llManage, llReport,llFenceApproval;
    Pref pref;
    String surl,surl1;
    String point;
    TextView tvTollBar,tvManage,tvReport;
    AlertDialog alerDialog1,alertDialog2,alerDialog2;
    ImageView imgPic;
    int pic1Flag;
    GPSTracker gps;
    Double latitude, longitude;
    String address;
    String latt, longt;
    Uri imageUri;
    private static final int CAMERA_REQUEST = 1;
    File file,imageZipperFile;
    LinearLayout llConfig,llLogBook,llBackLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence_manage_dash_board);
        initView();

        onClick();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        llManage = (LinearLayout) findViewById(R.id.llManage);
        llReport = (LinearLayout) findViewById(R.id.llReport);
        llFenceApproval=(LinearLayout)findViewById(R.id.llFenceApproval);
        llLogBook=(LinearLayout)findViewById(R.id.llLogBook);
        llBackLog=(LinearLayout)findViewById(R.id.llBackLog);
        llConfig=(LinearLayout)findViewById(R.id.llConfig);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome = (ImageView) findViewById(R.id.imgHome);
        point=getIntent().getStringExtra("point");
        tvTollBar=(TextView)findViewById(R.id.tvToolBar);
        tvManage=(TextView)findViewById(R.id.tvManage);
        tvReport=(TextView)findViewById(R.id.tvReport);
        gps = new GPSTracker(GeoFenceManageDashBoardActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            latt = String.valueOf(latitude);
            longitude = gps.getLongitude();
            longt = String.valueOf(longitude);
        } else {
// can't get location
// GPS or Network is not enabled
// Ask user to enable GPS/network in settings

        }
         address = getCompleteAddressString(latitude, longitude);

        if (pref.getLanguage().equals("hi")){
            tvTollBar.setText("गतिविधि");
            tvManage.setText("प्रबंधन");
            tvReport.setText("रिपोर्ट");
        }else {
            tvTollBar.setText("Activity");
            tvManage.setText("Manage");
            tvReport.setText("Report");
        }

        if (pref.getSecurityCode().equals("1157")){
            getApproverOrNot();
        }else {

        }
    }
    //GeoFenceDailyLogManageActivity

    private void onClick() {
        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.getSecurityCode().equals("1157")){
                    Intent intent = new Intent(GeoFenceManageDashBoardActivity.this, VisitLocationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(GeoFenceManageDashBoardActivity.this, GeoFenceDailyLogManageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("SLongitude",0.00);
                    intent.putExtra("SLatitude",0.00);
                    intent.putExtra("radius",0);
                    startActivity(intent);
                }

            }
        });

        llBackLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeoFenceManageDashBoardActivity.this, BacklogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AttendanceReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llLogBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NumberTourActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llFenceApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GeoFenceApprovalActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        llConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GeoFenceConfigEmpReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserDashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }


    private void getApproverOrNot() {
        final ProgressDialog pd = new ProgressDialog(GeoFenceManageDashBoardActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();

        String surl = pref.getIpAddress() + "ghrmsapi/api/Leave/LeaveApplicationApprover?CompanyID=" + pref.getEmpClintId() + "&EmployeeID=" + pref.getEmpId() + "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printurlbalance", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
//                        llLoader.setVisibility(View.GONE);

                        pd.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {



                                llFenceApproval.setVisibility(View.VISIBLE);


                                //llShow.setVisibility(View.VISIBLE);


                            } else {
                                llFenceApproval.setVisibility(View.GONE);
                                // llShow.setVisibility(View.GONE);
                            }

                            checkGeoFenceConfiguredOrNot();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void checkGeoFenceConfiguredOrNot() {
        final ProgressDialog pd = new ProgressDialog(GeoFenceManageDashBoardActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.show();

        String surl = pref.getIpAddress() + "ghrmsapi/api/Get_EmployeeGeoFence?AEMEmployeeID=" + pref.getEmpId() +  "&SecurityCode=" + pref.getSecurityCode();
        Log.d("printurlbalance", surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
//                        llLoader.setVisibility(View.GONE);

                        pd.dismiss();


                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText = job1.optString("responseText");

                            boolean responseStatus = job1.optBoolean("responseStatus");
                            if (responseStatus) {
                                llConfig.setVisibility(View.VISIBLE);


                            } else {

                               counterMapDialog();
                                llConfig.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert", error.toString());
            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void counterMapDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceManageDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_geofence_config, null);
        dialogBuilder.setView(dialogView);
        ImageView imgCamera = (ImageView) dialogView.findViewById(R.id.imgCamera);
        imgPic = (ImageView) dialogView.findViewById(R.id.imgPic);
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCamera();
            }
        });
        TextView tvAddress = (TextView) dialogView.findViewById(R.id.tvAddress);
        String addressT = "<font color='#EE0000'>Address: </font>";
        tvAddress.setText(Html.fromHtml(addressT + " " + address));
        String latT = "<font color='#EE0000'>Latitude: </font>";
        String longT = "<font color='#EE0000'>Longitude: </font>";
        TextView tvLatLong = (TextView) dialogView.findViewById(R.id.tvLatLong);
        tvLatLong.setText(Html.fromHtml(latT + latt + "   " + longT + longt));
        ImageView imgCancel = (ImageView) dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerDialog1.dismiss();
            }
        });
        Button btnSave = (Button) dialogView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pic1Flag == 1) {
                    alerDialog1.dismiss();
                    postCounterImage();
                } else {
                    Toast.makeText(GeoFenceManageDashBoardActivity.this, "Please Upload Counter Image", Toast.LENGTH_LONG).show();
                }
            }
        });


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
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

    public void dialogCamera(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceManageDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_camera, null);
        dialogBuilder.setView(dialogView);
        //TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        Button btnCamera1=(Button) dialogView.findViewById(R.id.btnCamera1);
        Button btnCamera2=(Button) dialogView.findViewById(R.id.btnCamera2);

        btnCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
//                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,CAMERA_REQUEST);
                cameraIntent();
            }
        });
        btnCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
                LongBackImageCameraActivity.launch(GeoFenceManageDashBoardActivity.this);
            }
        });

        alertDialog2 = dialogBuilder.create();
        alertDialog2.setCancelable(true);
        Window window = alertDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alertDialog2.show();
    }

    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        try {
                            String imageurl = /*"file://" +*/ getRealPathFromURI(imageUri);
                            file = new File(imageurl);
                            imageZipperFile = new ImageZipper(GeoFenceManageDashBoardActivity.this)
                                    .setQuality(100)
                                    .setMaxWidth(300)
                                    .setMaxHeight(300)
                                    .compressToFile(file);
                            //Log.d("imageSixw", String.valueOf(getReadableFileSize(compressedImageFile.length())));
                            BitmapFactory.Options o = new BitmapFactory.Options();
                            o.inSampleSize = 2;
                            Bitmap bm = cropToSquare(BitmapFactory.decodeFile(imageurl, o));
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                           /* byte[] b = baos.toByteArray();
                            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);*/

                            imgPic.setImageBitmap(bm);
                            pic1Flag=1;



                            // _pref.saveImage(encodedImage);
                            //saveImage(encodedImage);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
                break;
            case LongBackImageCameraActivity.LONG_IMAGE_RESULT_CODE_BACK:


                if (resultCode == RESULT_OK && requestCode == LongBackImageCameraActivity.LONG_IMAGE_RESULT_CODE_BACK) {
                    String imageFileName = data.getStringExtra(LongImageCameraActivity.IMAGE_PATH_KEY);
                    Log.d("imageFileName", imageFileName);
                    Bitmap d = BitmapFactory.decodeFile(imageFileName);
                    int newHeight = (int) (d.getHeight() * (512.0 / d.getWidth()));
                    Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
                    imgPic.setImageBitmap(putImage);
                    file = (File) data.getExtras().get("picture");
                    Log.d("fjjgk", file.toString());
                    pic1Flag=1;

                    try {
                        imageZipperFile = new ImageZipper(GeoFenceManageDashBoardActivity.this)
                                .setQuality(100)
                                .setMaxWidth(300)
                                .setMaxHeight(300)
                                .compressToFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }





                }
                break;
        }




    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private String getRealPathFromURIPath(Uri contentURI) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentURI, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
        return cropImg;
    }

    private void postCounterImage() {

        final ProgressDialog pd = new ProgressDialog(GeoFenceManageDashBoardActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.upload(pref.getIpAddress()+"ghrmsapi/api/Post_EmployeeGeoFence")
                .addMultipartParameter("AEMEmployeeID", pref.getEmpId())
                .addMultipartParameter("Longitude", longt)
                .addMultipartParameter("Latitude", latt)
                .addMultipartParameter("Address", address)
                .addMultipartFile("Fname", imageZipperFile)
                .addMultipartParameter("SecurityCode", pref.getSecurityCode())

                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        pd.show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        boolean responseStatus = job1.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert(responseText);
                            pd.dismiss();

                        } else {
                            pd.dismiss();
                            Toast.makeText(GeoFenceManageDashBoardActivity.this, responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
                    }
                });
    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceManageDashBoardActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvInvalidDate.setText(text);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog2.dismiss();
                checkGeoFenceConfiguredOrNot();

            }
        });

        alerDialog2 = dialogBuilder.create();
        alerDialog2.setCancelable(false);
        Window window = alerDialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog2.show();
    }

}
