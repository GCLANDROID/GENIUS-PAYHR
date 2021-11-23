package com.genius.hrms.activity.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.genius.hrms.R;
import com.genius.hrms.activity.adapter.AddFaceReportAdapter;
import com.genius.hrms.activity.model.AddFaceModel;
import com.genius.hrms.activity.utility.AppController;
import com.genius.hrms.activity.utility.FileUtils;
import com.genius.hrms.activity.utility.Pref;
import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceRec;
import com.tzutalin.dlib.VisionDetRet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Copy the person image renamed to his name into the dlib image directory
public class AddPerson extends AppCompatActivity {

    EditText et_name, et_company, et_empid, et_phone, et_image;
    Button btn_select_image, btn_add;
    int BITMAP_QUALITY = 100;
    int MAX_IMAGE_SIZE = 500;
    ImageView imgSuccess;
    String TAG = "AddPerson";
    private Bitmap bitmap;
    private File destination = null;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    Pref pref;
    AlertDialog alerDialog1;
    LinearLayout llLoader,llMain,llNoData;
    RecyclerView rvItem;
    ArrayList<AddFaceModel> itemList=new ArrayList();
    int flag=0;
    LinearLayout root_layout;
    TextView text_view;
    ImageView imgBack,imgHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        pref=new Pref(AddPerson.this);

        text_view=(TextView)findViewById(R.id.text_view);
        root_layout=(LinearLayout)findViewById(R.id.root_layout);
       /* mToolTipsManager = new ToolTipsManager();
        ToolTip.Builder builder = new ToolTip.Builder(this, text_view, root_layout, "You can use only single image for face recognition purpose.To add new image please delete existing image first then upload new image.", ToolTip.POSITION_ABOVE);*/
        llLoader=(LinearLayout)findViewById(R.id.llLoader);
        llMain=(LinearLayout)findViewById(R.id.llMain);
        llNoData=(LinearLayout)findViewById(R.id.llNoData);

        rvItem=(RecyclerView)findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(AddPerson.this, LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        btn_select_image = (Button)findViewById(R.id.btn_select_image);
        btn_add = (Button)findViewById(R.id.btn_add);
        et_name = (EditText)findViewById(R.id.et_name);





        et_company = (EditText) findViewById(R.id.et_company);
        et_empid = (EditText) findViewById(R.id.et_empid);
        et_phone = (EditText) findViewById(R.id.et_phone );
        et_image = (EditText)findViewById(R.id.et_image);

        imgBack=(ImageView)findViewById(R.id.imgBack);
        imgHome=(ImageView)findViewById(R.id.imgHome);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddPerson.this,FRDashboard.class);
                startActivity(intent);
                finish();
            }
        });




        btn_select_image.setOnClickListener(mOnClickListener);
        btn_add.setOnClickListener(mOnClickListener);


        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                imgPath = null;
                et_image.setText("");
                //enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });





//        et_company.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                imgPath = null;
//                et_image.setText("");
//                enableSubmitIfReady();
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//        });
//
//
//
//
//
//        et_empid.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                imgPath = null;
//                et_image.setText("");
//                enableSubmitIfReady();
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//        });
//
//
//
//
//
//
//
//        et_phone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                imgPath = null;
//                et_image.setText("");
//                enableSubmitIfReady();
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//        });
//
//





        destination = new File(Constants.getDLibDirectoryPath() + "/temp.jpg");
        getItemList();
    }






    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_select_image:
                    selectImage();
                    break;
                case R.id.btn_add:
                    String targetPath = Constants.getDLibImageDirectoryPath() + "/"  + pref.getMasterId()   + ".jpg";
                    FileUtils.copyFile(imgPath,targetPath);
                    viewDialog();
                    //postAddFace();

                    //postAddFace();
                    break;
            }
        }
    };
    private void viewDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddPerson.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.show_image, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Image has been added successfully");
        imgSuccess=(ImageView) dialogView.findViewById(R.id.imgSuccess);
        File imgFile = new  File(et_image.getText().toString());
        if(imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgSuccess.setImageBitmap(myBitmap);
        }
        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();

                Intent intent = new Intent(AddPerson.this, FRDashboard.class);
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

    // Select image from camera and gallery
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddPerson.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        }

//                        else if (options[item].equals("Choose From Gallery")) {
//                            dialog.dismiss();
//                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
//                        }


                        else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                Bitmap scaledBitmap = scaleDown(bitmap, MAX_IMAGE_SIZE, true);
                et_image.setText(destination.getAbsolutePath());
                new detectAsync().execute(scaledBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Bitmap scaledBitmap = scaleDown(bitmap, MAX_IMAGE_SIZE, true);
                //imgSuccess.setImageBitmap(bitmap);
                et_image.setText(getRealPathFromURI(selectedImage));
                new detectAsync().execute(scaledBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private FaceRec mFaceRec;

    private class detectAsync extends AsyncTask<Bitmap, Void, String> {
        ProgressDialog dialog = new ProgressDialog(AddPerson.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Detecting face...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        protected String doInBackground(Bitmap... bp) {
            mFaceRec = new FaceRec(Constants.getDLibDirectoryPath());
            List<VisionDetRet> results;
            results = mFaceRec.detect(bp[0]);
            String msg = null;
            if (results.size()==0) {
                msg = "No face was detected or face was too small. Please select a different image";
            } else if (results.size() > 1) {
                msg = "More than one face was detected. Please select a different image";
            } else {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bp[0].compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY, bytes);
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgPath = destination.getAbsolutePath();
            }
            return msg;
        }

        protected void onPostExecute(String result) {
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
                if (result!=null) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddPerson.this);
                    builder1.setMessage(result);
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    imgPath = null;
                    et_image.setText("");
                }
                // enableSubmitIfReady();
            }

        }
    }

    private void successAlert(String text) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddPerson.this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText(text);

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();

                Intent intent = new Intent(AddPerson.this, FRDashboard.class);
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

    private void postAddFace() {
        flag=1;
        final ProgressDialog pd = new ProgressDialog(AddPerson.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        Log.d("empid",pref.getEmpId());

        AndroidNetworking.upload("https://www.cloud.geniusconsultant.com/GeniusESS/API/FacialRecognition/AddFacialRecognition")
                .addMultipartParameter("EmployeeID", pref.getEmpId())
                .addMultipartParameter("CreatedBy", pref.getEmpId())
                .addMultipartFile("FaceFile", destination)

                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        String responseText = job1.optString("responseText");
                        Log.d("responseText", responseText);
                        boolean responseStatus=job1.optBoolean("responseStatus");
                        if (responseStatus) {
                            successAlert("Image has been successfully added.");
                            pd.dismiss();

                        } else {
                            pd.dismiss();
                            Toast.makeText(AddPerson.this, responseText, Toast.LENGTH_LONG).show();

                        }


                        // boolean _status = job1.getBoolean("status");


                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Log.d("error",error.getMessage());
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
                    }
                });
    }


    private void getItemList(){
        Log.d("Arpan","arpan");
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);

        String surl = "https://www.cloud.geniusconsultant.com/GeniusESS/API/ManageFacialRecognition/FacialRecognition?EmployeeID="+pref.getEmpId()+"&role=2";
        Log.d("input",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();
                                JSONArray responseData=job1.optJSONArray("responseData");
                                for (int i = 0; i < responseData.length(); i++){
                                    JSONObject obj=responseData.getJSONObject(i);
                                    String url=obj.optString("FaceFile");

                                    AddFaceModel adModel=new AddFaceModel(url);
                                    itemList.add(adModel);



                                }

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                                setAdapter();


                            }

                            else {

                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                llLoader.setVisibility(View.GONE);
                llMain.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);


                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }

    private void setAdapter(){
        AddFaceReportAdapter reportAdater=new AddFaceReportAdapter(itemList,AddPerson.this);
        rvItem.setAdapter(reportAdater);
    }






    public void deleFace(){
        flag=2;
        final ProgressDialog pd=new ProgressDialog(AddPerson.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        String surl = "https://www.cloud.geniusconsultant.com/GeniusESS/API/ManageFacialRecognition/FacialRecognition?EmployeeID="+pref.getEmpId()+"&role=5";
        Log.d("input",surl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, surl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responseAttendance", response);
                        pd.dismiss();

                        // attendabceInfiList.clear();

                        try {
                            JSONObject job1 = new JSONObject(response);
                            Log.e("response12", "@@@@@@" + job1);
                            String responseText=job1.optString("responseText");

                            boolean responseStatus=job1.optBoolean("responseStatus");
                            if (responseStatus){
                                // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();


                                successAlert("Image delete successfully done");

                            }

                            else {


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(AttendanceReportActivity.this, "Volly Error", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



                // Toast.makeText(AttendanceReportActivity.this, "volly 2"+error.toString(), Toast.LENGTH_LONG).show();
                Log.e("ert",error.toString());
            }
        }) {

        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req");
    }
}
