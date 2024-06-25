package com.genius.payhrms.activity.geofence;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.GeoFenceReportEmpAdapter;
import com.genius.payhrms.activity.model.GeoFenceReportEmpModel;
import com.genius.payhrms.activity.utility.GPSTracker;
import com.genius.payhrms.activity.utility.Pref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wajahatkarim3.longimagecamera.LongBackImageCameraActivity;
import com.wajahatkarim3.longimagecamera.LongImageCameraActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeoFenceConfigEmpReportActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout llLoader, llMain, llNoData;
    RecyclerView rvItem;
    Pref pref;
    ArrayList<GeoFenceReportEmpModel> itemList = new ArrayList<>();
    ImageView imgBack;
    FloatingActionButton btnAdd;
    ImageView imgHome;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence_config_emp_report);
        initView();
        //getReportItem();
    }

    private void initView() {
        pref = new Pref(getApplicationContext());
        gps = new GPSTracker(GeoFenceConfigEmpReportActivity.this);
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
        llLoader = (LinearLayout) findViewById(R.id.llLoader);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        llNoData = (LinearLayout) findViewById(R.id.llNoData);
        rvItem = (RecyclerView) findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(GeoFenceConfigEmpReportActivity.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);
        imgBack=(ImageView)findViewById(R.id.imgBack);

        imgHome=(ImageView)findViewById(R.id.imgHome);
        btnAdd=(FloatingActionButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        if (view==btnAdd){
            counterMapDialog();
        }

    }


    private void counterMapDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceConfigEmpReportActivity.this, R.style.CustomDialogNew);
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
                    //postCounterImage();
                } else {
                    Toast.makeText(GeoFenceConfigEmpReportActivity.this, "Please Upload Counter Image", Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceConfigEmpReportActivity.this, R.style.CustomDialogNew);
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
                LongBackImageCameraActivity.launch(GeoFenceConfigEmpReportActivity.this);
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
                            imageZipperFile = new ImageZipper(GeoFenceConfigEmpReportActivity.this)
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
                        imageZipperFile = new ImageZipper(GeoFenceConfigEmpReportActivity.this)
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



    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GeoFenceConfigEmpReportActivity.this, R.style.CustomDialogNew);
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
                //getReportItem();

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