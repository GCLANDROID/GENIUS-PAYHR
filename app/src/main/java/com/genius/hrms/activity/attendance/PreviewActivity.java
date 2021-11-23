package com.genius.hrms.activity.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.genius.hrms.R;
import com.genius.hrms.activity.utility.GPSTracker;
import com.genius.hrms.activity.utility.Pref;
import com.wajahatkarim3.longimagecamera.TouchImageView;

import java.util.List;
import java.util.Locale;

import cdflynn.android.library.checkview.CheckView;

public class PreviewActivity extends AppCompatActivity {

    String imageFileName;

    TouchImageView imgLongPreview;


    ImageView imgClose, imgPreview;
    GPSTracker gps;
    double latitude,longitude;
    String address;


    /* private static final String SERVER_PATH = AppData.url;
     private AttendanceService uploadService;*/
    Pref pref;
    String addr;
    AlertDialog alerDialog1;
    Button btnOk;
    String resultFlag;
    CheckView check;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        TextView textView = (TextView) findViewById(R.id.info);
        pref=new Pref(PreviewActivity.this);




        final byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String msg = getIntent().getStringExtra("info");
        resultFlag=getIntent().getStringExtra("resultFlag");
        textView.setText(msg);
        check=(CheckView)findViewById(R.id.check);
        check.check();








        imgPreview=(ImageView) findViewById(R.id.imgPreview);
        imgPreview.setImageBitmap(bitmap);

        imgClose = (ImageView) findViewById(R.id.imgClose);
        imgLongPreview = (TouchImageView) findViewById(R.id.imgPreview);


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PreviewActivity.this, FRDashboard.class);
                startActivity(intent);
                finish();
            }
        });
        btnOk=(Button)findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultFlag.equals("1")){
                    Intent intent=new Intent(PreviewActivity.this,FRDashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else {
                    Intent intent=new Intent(PreviewActivity.this,FRDashboard.class);
                    intent.putExtra("image",byteArray);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        });








//

    }



    private int count = 0;
    @Override
    public void onBackPressed() {
        count++;
        if (count >=1) {
        /* If count is greater than 1 ,you can either move to the next
        activity or just quit. */
            Intent intent = new Intent(PreviewActivity.this, AttendanceManageActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Press back again to Leave!", Toast.LENGTH_SHORT).show();

            // resetting the counter in 2s
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    count = 0;
                }
            }, 2000);
        }
        super.onBackPressed();
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





    private void successAlert(String t) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PreviewActivity.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);


        tvInvalidDate.setText(t);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                Intent intent=new Intent(PreviewActivity.this,AttendanceReportActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }


}
