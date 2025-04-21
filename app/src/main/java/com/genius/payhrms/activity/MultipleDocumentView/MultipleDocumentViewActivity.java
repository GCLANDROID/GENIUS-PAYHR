package com.genius.payhrms.activity.MultipleDocumentView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.MultipleDocumentView.adapter.DocumentViewAdapter;
import com.genius.payhrms.activity.MultipleDocumentView.adapter.DocumentViewModel;
import com.genius.payhrms.activity.activity.EmplyoeeCalendarDashboarActivity;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.model.ApprovalModel;
import com.genius.payhrms.activity.utility.Pref;
import com.genius.payhrms.activity.utility.SecurityCode;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MultipleDocumentViewActivity extends AppCompatActivity {
    private static final String TAG = "MultipleDocumentViewAct";
    RecyclerView rvDocumentView;
    ArrayList<DocumentViewModel> docList = new ArrayList<>();
    AlertDialog alerDialog1;
    AlertDialog.Builder builder;
    ImageView imgBack,imgHome;
    Pref pref;
    ApprovalModel approvalModel;
    //Dialog dialogView;
    boolean isDialogShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_document_view);
        try {
            initView();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void initView() throws JSONException {
        pref = new Pref(this);
        String doc_link = pref.getLargeData();
        Log.e(TAG, "doc_link: "+doc_link);
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);
        if (isJsonArray(doc_link)){
            JSONArray jsonArray=new JSONArray(doc_link);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String DocName = object.getString("DocName");
                String ByteData = object.getString("ByteData");
                String[] type = DocName.split("\\.");
                Log.e(TAG, "type: "+type[1]);
                DocumentViewModel documentViewModel = new DocumentViewModel(DocName,ByteData,type[1]);
                docList.add(documentViewModel);
            }
        } else {
            String array[] = doc_link.split("@");

            for (String doc : array) {
                Log.e(TAG, "doc: " + doc);
                String doc_array[] = doc.split("_");
                /*String name = doc_array[0];
                String base64String = doc_array[1];
                String type = doc_array[2];*/

                //Log.e(TAG, "Name: " + name + " type: " + type);

                String[] parts = doc.split(",");
                String part1 = parts[1];
                String[] partsB = part1.split("\\$");
                //String doclink=partsB[0];
                String name = partsB[1];
                String base64 = partsB[0];
                String type = parts[0];
                DocumentViewModel documentViewModel = new DocumentViewModel(name, base64, type);
                docList.add(documentViewModel);
            }
        }


        rvDocumentView = findViewById(R.id.rvDocumentView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvDocumentView.setLayoutManager(linearLayoutManager);

        DocumentViewAdapter documentViewAdapter = new DocumentViewAdapter(this, docList);
        rvDocumentView.setAdapter(documentViewAdapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getSecurityCode().equals(SecurityCode.Arun_Nursery)
                        || pref.getSecurityCode().toString().equals(SecurityCode.Future_Foundation)){
                    Intent intent=new Intent(getApplicationContext(), EmplyoeeCalendarDashboarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //finish();
                } else {
                    Intent intent=new Intent(getApplicationContext(), UserDashBoardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //finish();
                }
            }
        });
    }

    public void imageAlert(String base64string,String type) {
        /*AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_image, null);
        dialogBuilder.setView(dialogView);*/
        /*final ProgressDialog pg=new ProgressDialog(MultipleDocumentViewActivity.this);
        pg.setMessage("Loading..");
        pg.setCancelable(false);
        pg.show();*/

        Dialog dialogView = new Dialog(this,R.style.CustomDialogNew2);
        dialogView.setContentView(R.layout.dialog_image);
        dialogView.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogView.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogView.setCancelable(false);


        PhotoView viewImage = dialogView.findViewById(R.id.viewImage);
        ImageView imgCancel = dialogView.findViewById(R.id.imgCancel);
        PDFView pdfView = dialogView.findViewById(R.id.pdfView);
        LinearLayout llLoading = dialogView.findViewById(R.id.llLoading);
        TextView txtPdfPageCount = dialogView.findViewById(R.id.txtPdfPageCount);
        /*byte[] decodedString = Base64.decode(doclink, Base64.DEFAULT);
        Bitmap selfieImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imgDoc.setImageBitmap(selfieImage);*/

        if (type.contains("pdf")){
            //Log.e(TAG, "showPdfView: png: "+base64string);
            Log.e(TAG, "showPdfView: pdf");
            //Log.e(TAG, "showPdfView: pdf: "+base64string);
            llLoading.setVisibility(View.VISIBLE);
            byte[] decodedString = Base64.decode(base64string, Base64.DEFAULT);
            pdfView.fromBytes(decodedString).onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            Log.e(TAG, "onPageChanged: Current Page: " + page + " Total number of page: " + pageCount);
                            txtPdfPageCount.setText(page+1+" / "+pageCount);
                        }
                    }).onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages) {
                            Log.e(TAG, "onInitiallyRendered: nbPages: " + nbPages);
                            llLoading.setVisibility(View.GONE);
                            txtPdfPageCount.setVisibility(View.VISIBLE);
                        }
                    }).onTap(new OnTapListener() {
                        @Override
                        public boolean onTap(MotionEvent e) {
                            Log.e(TAG, "onTap: called.");
                            if (txtPdfPageCount.getVisibility() == View.VISIBLE) {
                                txtPdfPageCount.setVisibility(View.GONE);
                            } else {
                                txtPdfPageCount.setVisibility(View.VISIBLE);
                            }
                            return false;
                        }
                    })
                    .spacing(15)
                    .pageSnap(true)
                    .autoSpacing(true)
                    .pageFling(true)
                    .load();
            pdfView.setVisibility(View.VISIBLE);
            viewImage.setVisibility(View.GONE);
        } else {
            byte[] decodedString = Base64.decode(base64string, Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            viewImage.setImageBitmap(image);
            viewImage.setVisibility(View.VISIBLE);
            txtPdfPageCount.setVisibility(View.GONE);
            llLoading.setVisibility(View.GONE);
        }

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView.dismiss();
                isDialogShowing = false;
            }
        });

        if (!isDialogShowing){
            dialogView.show();
            isDialogShowing = true;
        }

        //dialogView.show();

        /*alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pref.saveLargeData("");
    }

    public static boolean isJsonArray(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        try {
            new JSONArray(input);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}