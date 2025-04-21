package com.genius.payhrms.activity.MultipleDocumentView;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.MultipleDocumentView.adapter.DocumentViewAdapter;
import com.genius.payhrms.activity.MultipleDocumentView.adapter.DocumentViewModel;

import java.util.ArrayList;

public class LeaveDetailsActivity extends AppCompatActivity {
    private static final String TAG = "LeaveDetailsActivity";
    RecyclerView rvDocumentView;
    ArrayList<DocumentViewModel> docList = new ArrayList<>();
    AlertDialog alerDialog1;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_details);
        initView();
    }

    private void initView() {
        String doc_link = getIntent().getStringExtra("doc_link");
        Log.e(TAG, "doc_link: "+doc_link);
        String array[] = doc_link.split("@");
        for(String doc : array){
            Log.e(TAG, "doc: "+doc);
            /*String doc_array[] = doc.split("_");
            String name = doc_array[0];
            String base64String = doc_array[1];
            String type = doc_array[2];

            Log.e(TAG, "Name: "+name+" type: "+type);*/

            String[] parts = doc.split(",");
            String part1 = parts[1];
            String[] partsB = part1.split("\\$");
            //String doclink=partsB[0];
            String name = partsB[1];
            String base64 = partsB[0];
            String type = parts[0];
            DocumentViewModel documentViewModel = new DocumentViewModel(name,base64,type);
            docList.add(documentViewModel);
        }
        rvDocumentView = findViewById(R.id.rvDocumentView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvDocumentView.setLayoutManager(linearLayoutManager);

        DocumentViewAdapter documentViewAdapter = new DocumentViewAdapter(this, docList);
        rvDocumentView.setAdapter(documentViewAdapter);
    }


   /* public void imageAlert(String doc) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_image, null);
        dialogBuilder.setView(dialogView);
        ImageView imgDoc=(ImageView)dialogView.findViewById(R.id.imgDoc);
        String[] parts = doc.split(",");
        String part1 = parts[1];
        String[] partsB = part1.split("\\$");
        String doclink=partsB[0];

        byte[] decodedString = Base64.decode(doclink, Base64.DEFAULT);
        Bitmap selfieImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imgDoc.setImageBitmap(selfieImage);

        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
            }
        });

        alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();
    }*/
}