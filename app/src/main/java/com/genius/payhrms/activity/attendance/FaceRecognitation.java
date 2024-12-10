package com.genius.payhrms.activity.attendance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.utility.FileUtils;
import com.genius.payhrms.activity.utility.Pref;
import com.google.android.cameraview.CameraView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FaceRecognitation extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imgBack,imgHome;

    private static final String TAG = "FaceRecognitation";
    private static final int INPUT_SIZE = 500;

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    private int mCurrentFlash;

    private CameraView mCameraView;

    private Handler mBackgroundHandler;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
                        mCameraView.takePicture();

                    }
                    break;
//                case R.id.add_person:
//                    Intent i = new Intent(FaceRecognitation.this, AddPerson.class);
//                    startActivity(i);
//                    finish();
//                    break;
                case R.id.imgBack:
                    onBackPressed();
                case R.id.imgHome:
                    Intent i=new Intent(FaceRecognitation.this,MarkInAttendance.class);
                    startActivity(i);
            }
        }
    };


    ArrayList<String> names;
    File folder;
    String address;
    Pref pref;
    String resultFlag="";
    AlertDialog alerDialog1;
    byte[] byteArray;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognitation);
        checkPermissions();

        imgBack=findViewById(R.id.imgBack);
        imgHome=findViewById(R.id.imgHome);
        imgBack.setOnClickListener(mOnClickListener);
        imgHome.setOnClickListener(mOnClickListener);
        mCameraView = (CameraView) findViewById(R.id.camera);
        mCameraView.setFacing(mCameraView.FACING_FRONT);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        Button fab = (Button) findViewById(R.id.take_picture);
        if (fab != null) {
            fab.setOnClickListener(mOnClickListener);
        }
//        Button add_person = (Button) findViewById(R.id.add_person);
//        if (add_person != null) {
//            add_person.setOnClickListener(mOnClickListener);
//        }
        pref=new Pref(FaceRecognitation.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

    }
//Pass Capture Image between Preview Activity

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            String msg = getResultMessage(names);

            Intent i = new Intent(FaceRecognitation.this, PreviewActivity.class);
            i.putExtra("BitmapImage", imageBitmap);
            i.putExtra("info", msg);
            i.putExtra("resultFlag", resultFlag);
            startActivity(i);
        }
    }

    //END
    //private FaceRec mFaceRec;

    private void changeProgressDialogMessage(final ProgressDialog pd, final String msg) {
        Runnable changeMessage = new Runnable() {
            @Override
            public void run() {
                pd.setMessage(msg);
            }
        };
        runOnUiThread(changeMessage);
    }
    private class initRecAsync extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(FaceRecognitation.this);

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "initRecAsync onPreExecute called");
            dialog.setMessage("Initializing...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        protected Void doInBackground(Void... args) {
// create dlib_rec_example directory in sd card and copy model files

            /*File image_folder = new File(Constants.getDLibImageDirectoryPath());
            image_folder.mkdirs();
            if (!new File(Constants.getFaceShapeModelPath()).exists()) {
                com.genius.payhrms.activity.utility.FileUtils.copyFileFromRawToOthers(FaceRecognitation.this, R.raw.shape_predictor_5_face_landmarks, Constants.getFaceShapeModelPath());
            }
            if (!new File(Constants.getFaceDescriptorModelPath()).exists()) {
                FileUtils.copyFileFromRawToOthers(FaceRecognitation.this, R.raw.dlib_face_recognition_resnet_model_v1, Constants.getFaceDescriptorModelPath());
            }

            mFaceRec = new FaceRec(Constants.getDLibDirectoryPath());
            changeProgressDialogMessage(dialog, "Please wait...");
            mFaceRec.train();*/
            return null;
        }

        protected void onPostExecute(Void result) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume called");
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
            mCameraView.setFacing(mCameraView.FACING_FRONT);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            new initRecAsync().execute();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause called");
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy called");
        super.onDestroy();
       /* if (mFaceRec != null) {
            mFaceRec.release();
        }*/
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// do something
            }
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private String getResultMessage(ArrayList<String> names) {
        String msg = new String();
        if (names.isEmpty()) {
            msg = "No face detected or Unknown person";
            resultFlag="1";

        } else {
            if (names.contains(pref.getMasterId()+".jpg")){
                msg  = "Face recognized successfully";
                resultFlag="2";


            }else {
                msg = "No face detected or Unknown person";
                resultFlag="1";
            }

            Toast.makeText(FaceRecognitation.this,msg,Toast.LENGTH_LONG).show();
        }
        return msg;
    }


    private class recognizeAsync extends AsyncTask<Bitmap, Void, ArrayList<String>> {
        ProgressDialog dialog = new ProgressDialog(FaceRecognitation.this);
        private int mScreenRotation = 0;
        private boolean mIsComputing = false;
        private Bitmap mCroppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Recognizing...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        protected ArrayList<String> doInBackground(Bitmap... bp) {

            /*drawResizedBitmap(bp[0], mCroppedBitmap);
            Log.d(TAG, "byte to bitmap");

            long startTime = System.currentTimeMillis();
            List<VisionDetRet> results;
            results = mFaceRec.recognize(mCroppedBitmap);
            long endTime = System.currentTimeMillis();
            Log.d(TAG, "Time cost: " + String.valueOf((endTime - startTime) / 1000f) + " sec");

            ArrayList<String> names = new ArrayList();
            for (VisionDetRet n : results) {
                names.add(n.getLabel());
            }
            return names;*/
            return names;
        }

        @SuppressLint({"WrongThread", "WrongConstant"})
        protected void onPostExecute(ArrayList<String> names) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mCroppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                String message = getResultMessage(names);
             /*   Intent in1 = new Intent(FaceRecognitation.this, PreviewActivity.class);
                in1.putExtra("image",byteArray);
                in1.putExtra("info",message);
                in1.putExtra("resultFlag",resultFlag);
                in1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in1);*/
                Toast.makeText(FaceRecognitation.this,message,Toast.LENGTH_LONG).show();
                successAlert(message);

            }
        }

        private void drawResizedBitmap(final Bitmap src, final Bitmap dst) {

            Display getOrient = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int orientation = Configuration.ORIENTATION_PORTRAIT;
            Point point = new Point();
            getOrient.getSize(point);
            int screen_width = point.x;
            int screen_height = point.y;
            Log.d(TAG, String.format("screen size (%d,%d)", screen_width, screen_height));
            if (screen_width < screen_height) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
                if (screen_height<1500){
                    mScreenRotation= 0;
                }else {
                    mScreenRotation = 270;

                }

            } else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
                mScreenRotation = 0;
            }

//Assert.assertEquals(dst.getWidth(), dst.getHeight());
            final float minDim = Math.min(src.getWidth(), src.getHeight());

            final Matrix matrix = new Matrix();

// We only want the center square out of the original rectangle.
            final float translateX = -Math.max(0, (src.getWidth() - minDim) / 2);
            final float translateY = -Math.max(0, (src.getHeight() - minDim) / 2);
            matrix.preTranslate(translateX, translateY);

            final float scaleFactor = dst.getHeight() / minDim;
            matrix.postScale(scaleFactor, scaleFactor);

// Rotate around the center if necessary.
            if (mScreenRotation != 0) {
                matrix.postTranslate(-dst.getWidth() / 2.0f, -dst.getHeight() / 2.0f);
                matrix.postRotate(mScreenRotation);
                matrix.postTranslate(dst.getWidth() / 2.0f, dst.getHeight() / 2.0f);
            }else {
                matrix.postTranslate(-dst.getWidth() / 2.0f, -dst.getHeight() / 2.0f);
                matrix.postRotate(mScreenRotation);
                matrix.postTranslate(dst.getWidth() / 2.0f, dst.getHeight() / 2.0f);
            }

            final Canvas canvas = new Canvas(dst);
            canvas.drawBitmap(src, matrix, null);
        }
    }


    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            Toast.makeText(cameraView.getContext(), R.string.picture_taken, Toast.LENGTH_SHORT)
                    .show();
            Bitmap bp = BitmapFactory.decodeByteArray(data, 0, data.length);
            new recognizeAsync().execute(bp);
        }

    };

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                                                             String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                    if (permissions == null) {
                                        throw new IllegalArgumentException();
                                    }
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions, args.getInt(ARG_REQUEST_CODE));
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();
        }

    }


    private void successAlert(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FaceRecognitation.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.succes_alert, null);
        dialogBuilder.setView(dialogView);
        LinearLayout llOk = (LinearLayout) dialogView.findViewById(R.id.llOk);
        llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultFlag.equals("2")) {
                    alerDialog1.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MarkInAttendance.class);
                    intent.putExtra("image", byteArray);
                    startActivity(intent);
                    finish();
                }else {
                    alerDialog1.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MarkInAttendance.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        TextView tvSuccess = (TextView) dialogView.findViewById(R.id.tvSuccess);
        tvSuccess.setText(msg);


        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(false);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }







}
