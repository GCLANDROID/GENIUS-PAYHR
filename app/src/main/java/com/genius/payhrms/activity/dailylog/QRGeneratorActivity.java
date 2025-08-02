package com.genius.payhrms.activity.dailylog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.utility.GPSTracker;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRGeneratorActivity extends AppCompatActivity {

    ImageView imgQR;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    GPSTracker gps;
    Double latitude,longitude;
    String latt,longt;
    String coordinates;
    Button btnDwnload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);
        initView();
    }

    private void initView(){
        btnDwnload=(Button)findViewById(R.id.btnDwnload);
        gps = new GPSTracker(QRGeneratorActivity.this);
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

        coordinates=latt+","+longt;
        imgQR=(ImageView) findViewById(R.id.imgQR);
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(coordinates, null, QRGContents.Type.TEXT, dimen);
        // getting our qrcode in the form of bitmap.
        //bitmap = qrgEncoder.encodeAsBitmap();
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.getBitmap(0);
            // Setting Bitmap to ImageView
            imgQR.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.v("QR ERROR", e.toString());
        }
        // the bitmap is set inside our image
        // view using .setimagebitmap method.
        imgQR.setImageBitmap(bitmap);
        //QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
        //qrgEncoder.setColorBlack(Color.RED);
        //qrgEncoder.setColorWhite(Color.BLUE);
        btnDwnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable draw = (BitmapDrawable) imgQR.getDrawable();
                Bitmap bitmap = draw.getBitmap();
                //Bitmap bitmap = qrgEncoder.getBitmap();

                //FileOutputStream outStream = null;
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Genius_QR");
                //Uri folderUri = Uri.fromFile(dir);
                //dir.mkdirs();
                if (!dir.exists()) {
                    dir.mkdirs(); // Create directory if not exists
                }
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Genius_QR");

                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    if (uri != null) {
                        try (OutputStream outStream = getContentResolver().openOutputStream(uri)) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                            AlertDialog.Builder builder = new AlertDialog.Builder(QRGeneratorActivity.this);
                            builder.setTitle("Message");
                            builder.setMessage("QR Code saved in Pictures/Genius_QR in File Manager");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                            //Toast.makeText(QRGeneratorActivity.this, "QR Code saved in Pictures/Genius_QR in File Manager", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(QRGeneratorActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    //TODO: For Android 9 and below (optional fallback)
                    File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File geniusQRDir = new File(picturesDir, "Genius_QR");
                    if (!geniusQRDir.exists()) {
                        geniusQRDir.mkdirs();
                    }
                    File imageFile = new File(geniusQRDir, fileName);
                    try (OutputStream out = new FileOutputStream(imageFile)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        // Notify media scanner
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));
                        AlertDialog.Builder builder = new AlertDialog.Builder(QRGeneratorActivity.this);
                        builder.setTitle("Message");
                        builder.setMessage("QR Code saved in Pictures/Genius_QR in File Manager");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                        //Toast.makeText(QRGeneratorActivity.this, "QR Code saved in Pictures/Genius_QR in File Manager", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(QRGeneratorActivity.this, "Error saving image", Toast.LENGTH_SHORT).show();
                    }
                    //Todo: Old code
                    //String fileName = String.format("%d.jpg", System.currentTimeMillis());
                    /*File outFile = new File(dir, fileName);
                    try {
                        outStream = new FileOutputStream(outFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        try {
                            outStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            outStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(outFile));
                        sendBroadcast(intent);
                        Toast.makeText(QRGeneratorActivity.this,"Your QR Code has been saved in Genius_QR Folder in File Manager",Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        });
    }
}