package com.genius.payhrms.activity.AndroidXCamera;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.genius.payhrms.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class AndroidXCameraActivity extends AppCompatActivity {

    private static final String TAG = "AndroidXCameraActivity";

    public static final int LONG_IMAGE_RESULT_CODE = 2000;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private CameraSelector cameraSelector;
    private ImageButton captureButton;
    private Camera camera;
    private PreviewView previewFinder;
    private Preview preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_xcamera);
        captureButton = findViewById(R.id.captureButton);
        previewFinder = findViewById(R.id.viewFinder);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage(view);
            }
        });
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Log.e("onPermissionsGranted", "Called");
                            cameraProviderFuture = ProcessCameraProvider.getInstance(AndroidXCameraActivity.this);
                            cameraProviderFuture.addListener(() -> {
                                try {
                                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                                    bindPreview(cameraProvider);
                                } catch (Exception e) {
                                    Log.e("CameraX", "Error binding camera", e);
                                }
                            }, ContextCompat.getMainExecutor(AndroidXCameraActivity.this));
                        } else {
                            Toast.makeText(AndroidXCameraActivity.this, "Camera permission is required.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        preview = new Preview.Builder().build();
        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        preview.setSurfaceProvider(previewFinder.getSurfaceProvider());

        camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    public void captureImage(View view) {
        File outputDirectory = getOutputDirectory();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";
        File outputFile = new File(outputDirectory, fileName);

        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(outputFile).build();

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Log.e(TAG, "onImageSaved: " + outputFile.getAbsoluteFile());
                        String url = outputFile.getPath();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.putExtra("image", outputFile.getPath());
                                setResult(LONG_IMAGE_RESULT_CODE, intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e("CameraX", "Error capturing image", exception);
                    }
                });
    }

    private File getOutputDirectory() {
        File mediaDir = new File(getApplicationContext().getExternalCacheDir() + File.separator + System.currentTimeMillis() + ".png");
        if (!mediaDir.exists()) {
            if (!mediaDir.mkdirs()) {
                Log.e("CameraX", "Failed to create directory");
                return null;
            }
        }
        return mediaDir;
    }

    //TODO: Flip Camera Functionality
    public void switchCamera(View view) {
        @SuppressLint("RestrictedApi") CameraSelector newCameraSelector =
                cameraSelector.getLensFacing() == CameraSelector.LENS_FACING_BACK
                        ? new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
                        : new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        try {
            // Unbind the current camera and bind the selected camera
            cameraProviderFuture.get().unbindAll();
            camera = cameraProviderFuture.get().bindToLifecycle((LifecycleOwner) this, newCameraSelector, preview, imageCapture);
            cameraSelector = newCameraSelector;
        } catch (Exception e) {
            Log.e("CameraX", "Error switching camera", e);
        }
    }

    public static void launch(AppCompatActivity activity) {
        Intent ii = new Intent(activity, AndroidXCameraActivity.class);
        activity.startActivityForResult(ii, LONG_IMAGE_RESULT_CODE);
    }
}