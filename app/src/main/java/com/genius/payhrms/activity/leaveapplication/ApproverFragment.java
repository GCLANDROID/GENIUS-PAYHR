package com.genius.payhrms.activity.leaveapplication;


import static com.genius.payhrms.activity.utility.Util.SECRET_KEY;
import static com.genius.payhrms.activity.utility.Util.encrypt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.ApproverAdapter;
import com.genius.payhrms.activity.model.ApprovalModel;
import com.genius.payhrms.activity.utility.Api;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ApproverFragment extends Fragment {
    private static final String TAG = "ApproverFragment";
    LinearLayout llLoader, llMain, llNoData;
    RecyclerView rvItem;
    View view;
    Pref pref;
    ArrayList<ApprovalModel> itemList = new ArrayList<>();
    ApproverAdapter lAdaapter;
    ArrayList<String> mIdList = new ArrayList<>();
    Button btnReject, btnApprove;
    LinearLayout llShow;
    String mId;
    AlertDialog alerDialog1;
    AlertDialog.Builder builder;
    Button btnDelete;
    boolean isDialogShowing = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_approver, container, false);
        Log.e("TAG", "onCreateView: Dayco Matthews");
        initView();
        onClick();
        return view;
    }

    private void initView() {
        pref = new Pref(getContext());
        llLoader = (LinearLayout) view.findViewById(R.id.llLoader);
        llMain = (LinearLayout) view.findViewById(R.id.llMain);
        llNoData = (LinearLayout) view.findViewById(R.id.llNoData);
        rvItem = (RecyclerView) view.findViewById(R.id.rvItem);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItem.setLayoutManager(layoutManager);

        llShow = (LinearLayout) view.findViewById(R.id.llShow);
        btnReject = (Button) view.findViewById(R.id.btnReject);
        btnApprove = (Button) view.findViewById(R.id.btnApprove);
        btnDelete = (Button) view.findViewById(R.id.btnDelete);
        builder = new AlertDialog.Builder(getContext());


        JSONObject object=new JSONObject();
        try {

            object.put("CompanyID",pref.getEmpClintId());
            object.put("ApproverID",pref.getEmpId());
            object.put("SecurityCode",pref.getSecurityCode());
            getItem(object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (pref.getLanguage().equals("hi")) {
            btnReject.setText("अस्वीकार");
            btnApprove.setText("मंजूर");
            btnDelete.setText("हटाएं");
        } else {
            btnReject.setText("Reject");
            btnApprove.setText("Approve");
            btnDelete.setText("Delete");
        }

        btnDelete.setVisibility(View.GONE);
    }

    private void getItem(JSONObject jsonObject) {
        mId="";
        mIdList.clear();
        llLoader.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llShow.setVisibility(View.GONE);
        String URL="";
        if (pref.getSecurityCode().equals(SecurityCode.Future_Foundation) || pref.getSecurityCode().equals(SecurityCode.Arun_Nursery)){
            URL = Api.sApproverLeaveListWise;
        } else {
            URL = Api.sApproverLeaveItemapi;
        }
        AndroidNetworking.post(URL)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        itemList.clear();
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            String responseData=job1.optString("Response_Data");
                            try {
                                JSONArray jsonArray=new JSONArray(responseData);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    String ApplicationMID = obj.optString("ApplicationMID");
                                    String Name = obj.optString("Name");
                                    String LeaveName = obj.optString("LeaveName");
                                    String LeaveSDate = obj.optString("LeaveSDate");
                                    String LeaveEDate = obj.optString("LeaveEDate");
                                    String LeaveValue = obj.optString("LeaveValue");
                                    String Reason = obj.optString("Reason");
                                    String ApprovalStatus = obj.optString("ApprovalStatus");
                                    String Documentlink="";
                                    if (pref.getSecurityCode().equals(SecurityCode.Future_Foundation) || pref.getSecurityCode().equals(SecurityCode.Arun_Nursery)){
                                        Documentlink= String.valueOf(obj.optJSONArray("Documents"));
                                    } else {
                                        Documentlink=obj.optString("Documentlink");
                                    }

                                    int IsLink= obj.getInt("IsLink");

                                    ApprovalModel aModel = new ApprovalModel(ApplicationMID, Name, LeaveName, LeaveSDate, LeaveEDate, LeaveValue, Reason, ApprovalStatus);
                                    aModel.setIsLink(IsLink);
                                    aModel.setDocumentlink(Documentlink);
                                    itemList.add(aModel);
                                }
                                lAdaapter = new ApproverAdapter(itemList, ApproverFragment.this, getActivity());
                                rvItem.setAdapter(lAdaapter);
                                llLoader.setVisibility(View.GONE);
                                llMain.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        if (error.getErrorCode()==401){
                            JSONObject obj=new JSONObject();
                            try {
                                obj.put("MasterID",encrypt(pref.getMasterId(),SECRET_KEY));
                                obj.put("Password",encrypt(pref.getPassword(),SECRET_KEY));
                                obj.put("IMEI","0");
                                obj.put("DeviceID","0");
                                obj.put("DeviceType","A");
                                obj.put("SecurityCode",pref.getSecurityCode());
                                login(obj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            llLoader.setVisibility(View.GONE);
                            llMain.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);

                        }


                    }
                });
    }

    private void login(JSONObject jsonObject) {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();
        AndroidNetworking.post(Api.sLoginapi)
                .addJSONObjectBody(jsonObject)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        Log.e("response12", "@@@@@@" + job1);
                        pd.dismiss();

                        int Response_Code = job1.optInt("Response_Code");
                        if (Response_Code == 101) {
                            // Toast.makeText(getApplicationContext(),responseText,Toast.LENGTH_LONG).show();

                            JSONArray responseData = job1.optJSONArray("Response_Data");
                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject obj = responseData.optJSONObject(i);
                                String Genius_Access_Token=obj.optString("Genius_Access_Token");
                                pref.saveAccessToken(Genius_Access_Token);

                                // boolean _status = job1.getBoolean("status");


                                JSONObject object=new JSONObject();
                                try {

                                    object.put("CompanyID",pref.getEmpClintId());
                                    object.put("ApproverID",pref.getEmpId());
                                    object.put("SecurityCode",pref.getSecurityCode());
                                    getItem(object);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                // do anything with response
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();


                    }
                });
    }

    public void updateAttendanceStatus(int position, boolean status) {
        itemList.get(position).setSelected(status);
        if (itemList.get(position).isSelected() == true) {
            mIdList.add(itemList.get(position).getmId());


        } else {
            mIdList.remove(position);
        }

        mId = mIdList.toString().replace("[", "").replace("]", "").replaceAll("\\s+", "");
        if (mIdList.size() > 0) {
            llShow.setVisibility(View.VISIBLE);
        } else {
            llShow.setVisibility(View.GONE);
        }
        lAdaapter.notifyDataSetChanged();
    }




    private void approveFunction(JSONObject jsonObject) {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.post(Api.sLeaveApprovedapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        pd.dismiss();
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            approveAlert(Response_Message);


                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                         Toast.makeText(getContext(),Response_Message,Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                       pd.dismiss();


                    }
                });
    }



    private void rejectFunction(JSONObject jsonObject) {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.post(Api.sLeaveApprovedapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        pd.dismiss();
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            rejectAlert(Response_Message);


                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                            Toast.makeText(getContext(),Response_Message,Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        pd.dismiss();


                    }
                });
    }

    public void deleteFunction(String mid) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();


        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("CompanyID",pref.getEmpClintId());
            jsonObject.put("StrAppMID",mid);
            jsonObject.put("ApproverID",pref.getEmpId());
            jsonObject.put("SecurityCode",pref.getSecurityCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(Api.sLeaveDeleteByApproverapi)
                .addJSONObjectBody(jsonObject)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()

                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {


                        JSONObject job1 = response;
                        pd.dismiss();
                        Log.e("response12", "@@@@@@" + job1);


                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            deleteAlert(Response_Message);


                            // boolean _status = job1.getBoolean("status");


                            // do anything with response
                        }else {

                            Toast.makeText(getContext(),Response_Message,Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        pd.dismiss();


                    }
                });
    }


    private void approveAlert(String Response_Message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        if (Response_Message.isEmpty() || Response_Message.equalsIgnoreCase("null") || Response_Message.equalsIgnoreCase("No Error")){
            tvInvalidDate.setText("Leave has been approved successfully");
        } else {
            tvInvalidDate.setText(Response_Message);
        }

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                JSONObject object=new JSONObject();
                try {

                    object.put("CompanyID",pref.getEmpClintId());
                    object.put("ApproverID",pref.getEmpId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    getItem(object);

                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void deleteAlert(String Response_Message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        if (Response_Message.isEmpty() || Response_Message.equalsIgnoreCase("null") || Response_Message.equalsIgnoreCase("No Error")){
            tvInvalidDate.setText("Leave has been deleted successfully");
        } else {
            tvInvalidDate.setText(Response_Message);
        }

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                JSONObject object=new JSONObject();
                try {

                    object.put("CompanyID",pref.getEmpClintId());
                    object.put("ApproverID",pref.getEmpId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    getItem(object);

                } catch (JSONException e) {
                    e.printStackTrace();
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


    private void rejectAlert(String Response_Message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvInvalidDate.setText("");
        } else {
            if (Response_Message.isEmpty() || Response_Message.equalsIgnoreCase("null") || Response_Message.equalsIgnoreCase("No Error")){
                tvInvalidDate.setText("Leave has been rejected successfully");
            } else {
                tvInvalidDate.setText(Response_Message);
            }
        }


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                JSONObject object=new JSONObject();
                try {

                    object.put("CompanyID",pref.getEmpClintId());
                    object.put("ApproverID",pref.getEmpId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    getItem(object);

                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void onClick() {
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("CompanyID",pref.getEmpClintId());
                    jsonObject.put("StrAppMID",mId);
                    jsonObject.put("ApproverID",pref.getEmpId());
                    jsonObject.put("ApprovalStatus",1);
                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                    approveFunction(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to reject ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //rejectFunction();

                                JSONObject jsonObject=new JSONObject();
                                try {
                                    jsonObject.put("CompanyID",pref.getEmpClintId());
                                    jsonObject.put("StrAppMID",mId);
                                    jsonObject.put("ApproverID",pref.getEmpId());
                                    jsonObject.put("ApprovalStatus",0);
                                    jsonObject.put("SecurityCode",pref.getSecurityCode());
                                    rejectFunction(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Rejection Alert");
                alert.show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to delete ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Delete Alert");
                alert.show();
            }
        });
    }
    public void imageAlert(String doc) {
        Dialog dialogView = new Dialog(getContext(),R.style.CustomDialogNew2);
        dialogView.setContentView(R.layout.dialog_image);
        dialogView.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogView.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogView.setCancelable(false);
        if (!isDialogShowing){
            dialogView.show();
            isDialogShowing = true;
        }
        PhotoView viewImage = dialogView.findViewById(R.id.viewImage);
        PDFView pdfView = dialogView.findViewById(R.id.pdfView);
        LinearLayout llLoading = dialogView.findViewById(R.id.llLoading);
        TextView txtPdfPageCount = dialogView.findViewById(R.id.txtPdfPageCount);
        String[] parts = doc.split(",");
        String part1 = parts[1];
        String[] partsB = part1.split("\\$");
        String doclink=partsB[0];
        Log.e(TAG, "part1: "+part1);
        Log.e(TAG, "partsB[1]: "+partsB[1]);
        Log.e(TAG, "doclink: "+doclink);

        /*byte[] decodedString = Base64.decode(doclink, Base64.DEFAULT);
        Bitmap selfieImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        viewImage.setImageBitmap(selfieImage);*/
        byte[] decodedString = Base64.decode(doclink, Base64.DEFAULT);
        if (partsB[1].contains("pdf")){
            //Log.e(TAG, "showPdfView: png: "+base64string);
            Log.e(TAG, "showPdfView: pdf");
            //Log.e(TAG, "showPdfView: pdf: "+base64string);
            llLoading.setVisibility(View.VISIBLE);

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
            //byte[] decodedString = Base64.decode(doclink, Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            viewImage.setImageBitmap(image);
            viewImage.setVisibility(View.VISIBLE);
            txtPdfPageCount.setVisibility(View.GONE);
            llLoading.setVisibility(View.GONE);
        }

        ImageView imgCancel=(ImageView)dialogView.findViewById(R.id.imgCancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView.dismiss();
                isDialogShowing = false;
            }
        });

        /*alerDialog1 = dialogBuilder.create();
        alerDialog1.setCancelable(true);
        Window window = alerDialog1.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        alerDialog1.show();*/
    }
}
