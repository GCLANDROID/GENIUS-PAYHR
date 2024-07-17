package com.genius.payhrms.activity.leaveapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.adapter.ApproverAdapter;
import com.genius.payhrms.activity.adapter.OtherApproverAdapter;
import com.genius.payhrms.activity.model.OtherApproverModel;
import com.genius.payhrms.activity.utility.Api;
import com.genius.payhrms.activity.utility.Pref;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class OtherLeaveApproverFragment extends Fragment {
    private static final String TAG = "OtherLeaveApproverFragm";
    LinearLayout llLoader, llMain, llNoData;
    RecyclerView rvItem;
    View view;
    Pref pref;

    ApproverAdapter lAdaapter;
    ArrayList<String> mIdList = new ArrayList<>();
    Button btnReject, btnApprove;
    LinearLayout llShow;
    String mId;
    AlertDialog alerDialog1;
    AlertDialog.Builder builder;
    Button btnDelete;

    ArrayList<OtherApproverModel> otherApproverList;
    OtherApproverAdapter otherApproverAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_other_leave_approver, container, false);
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
            object.put("EmployeeId",pref.getEmpId());
            object.put("SecurityCode",pref.getSecurityCode());
            getList(object);
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

    private void onClick() {
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("CompanyId",pref.getEmpClintId());
                    jsonObject.put("AppAID",mId);
                    jsonObject.put("ApprovalStatus","1");  // Todo: Required
                    jsonObject.put("ApprovalStatusDetails","Approved"); // Todo: Required
                    jsonObject.put("ApprovedBY",pref.getEmpId()); // Todo: Required
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
                builder.setMessage("Do you want to reject ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //rejectFunction();
                                JSONObject jsonObject=new JSONObject();
                                try {
                                    jsonObject.put("CompanyId",pref.getEmpClintId());
                                    jsonObject.put("AppAID",mId);
                                    jsonObject.put("ApprovalStatus","-1");  // Todo: Required
                                    jsonObject.put("ApprovalStatusDetails","Rejected"); // Todo: Required
                                    jsonObject.put("ApprovedBY",pref.getEmpId()); // Todo: Required
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


    }

    private void rejectFunction(JSONObject jsonObject) {
        Log.e(TAG, "rejectFunction: "+jsonObject.toString() );
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.post(Api.sSaveAdjustmentApprovalRejected)
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
                        Log.e(TAG, "Approval_Reject" + job1);
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            rejectAlert();
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

    private void rejectAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);
        if (pref.getLanguage().equals("hi")) {
            tvInvalidDate.setText("");
        } else {
            tvInvalidDate.setText("Leave has been rejected successfully");
        }


        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                JSONObject object=new JSONObject();
                try {
                    object.put("CompanyID",pref.getEmpClintId());
                    object.put("EmployeeId",pref.getEmpId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    getList(object);
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

    private void approveFunction(JSONObject jsonObject) {
        Log.e(TAG, "approveFunction: "+jsonObject.toString() );
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.post(Api.sSaveAdjustmentApprovalRejected)
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
                        Log.e(TAG, "Approver_function" + job1);

                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        if (Response_Code == 101) {
                            approveAlert();
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

    private void getList(JSONObject object) {
        Log.e(TAG, "getList: object: "+object);
        Log.e(TAG, "getList: TOKEN: "+pref.getAccessToken());
        rvItem.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llLoader.setVisibility(View.VISIBLE);
        llShow.setVisibility(View.GONE);
        AndroidNetworking.post(Api.sGetAdjutmentApplicationForApprover)
                .addJSONObjectBody(object)
                .addHeaders("Authorization", "Bearer "+pref.getAccessToken())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "GET_LIST: "+response.toString());
                        JSONObject job1 = response;
                        int Response_Code = job1.optInt("Response_Code");
                        String Response_Message=job1.optString("Response_Message");
                        otherApproverList = new ArrayList<>();

                        if (Response_Code == 101) {
                            String responseData = job1.optString("Response_Data");
                            try {
                                JSONObject jsonArray = new JSONObject(responseData);
                                String Table=jsonArray.optString("Table");
                                JSONArray adjustmentApprovedArray = new JSONArray(Table);
                                if (adjustmentApprovedArray.length() > 0){
                                    for (int i = 0; i < adjustmentApprovedArray.length(); i++) {
                                        JSONObject obj = adjustmentApprovedArray.getJSONObject(i);
                                        otherApproverList.add(new OtherApproverModel(
                                                obj.optInt("SL"),
                                                obj.optString("AID"),
                                                obj.optString("Name"),
                                                obj.optString("AdjustmentType"),
                                                obj.optString("EmpCode"),
                                                obj.optString("AppliedDate"),
                                                obj.optString("StartDate"),
                                                obj.optString("EndDate"),
                                                obj.optString("InTime"),
                                                obj.optString("OutTime"),
                                                obj.optString("NoOfDays"),
                                                obj.optString("Reason"),
                                                obj.optString("clientname"),
                                                obj.optString("Destination"),
                                                obj.optString("TravelDetails"),
                                                obj.optString("AdvanceAmount"),
                                                (obj.optString("ApprovedBY") == null)?"N/A":obj.optString("ApprovedBY"),
                                                obj.optString("ApprovedOn"),
                                                obj.optString("ApprovalStatus"),
                                                obj.optInt("Isdelete"),
                                                obj.optInt("OD"),
                                                obj.optInt("IsApprove"),
                                                obj.optString("ApprovalRemarks"),
                                                obj.optString("DocumentName"),
                                                obj.optString("Documentlink"),
                                                obj.optString("offdate")
                                        ));
                                    }
                                    setAdapter();
                                    rvItem.setVisibility(View.VISIBLE);
                                    llNoData.setVisibility(View.GONE);
                                    llLoader.setVisibility(View.GONE);
                                } else {
                                    rvItem.setVisibility(View.GONE);
                                    llNoData.setVisibility(View.VISIBLE);
                                    llLoader.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "GET_LIST_error: "+anError);
                        rvItem.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                        llLoader.setVisibility(View.GONE);
                    }
                });
    }

    private void setAdapter() {
        otherApproverAdapter = new OtherApproverAdapter(getActivity(),OtherLeaveApproverFragment.this,otherApproverList);
        rvItem.setAdapter(otherApproverAdapter);
    }

    public void updateAttendanceStatus(int position, boolean status) {
        otherApproverList.get(position).setSelected(status);
        if (otherApproverList.get(position).isSelected() == true) {
            mIdList.add(otherApproverList.get(position).getAID());
        } else {
            mIdList.remove(position);
        }
        mId = mIdList.toString().replace("[", "").replace("]", "").replaceAll("\\s+", "");
        Log.e(TAG, "updateAttendanceStatus: "+mId);
        if (mIdList.size() > 0) {
            llShow.setVisibility(View.VISIBLE);
        } else {
            llShow.setVisibility(View.GONE);
        }
        otherApproverAdapter.notifyDataSetChanged();
    }

    private void approveAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Leave has been approved successfully");

        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                JSONObject object=new JSONObject();
                try {
                    object.put("CompanyID",pref.getEmpClintId());
                    object.put("EmployeeId",pref.getEmpId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    getList(object);
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

    public void deleteFunction(String aid) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.show();

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("CompanyId",pref.getEmpClintId());
            jsonObject.put("ApplicationMID",aid);
            jsonObject.put("EmployeeId",pref.getEmpId());
            jsonObject.put("SecurityCode",pref.getSecurityCode());
            //rejectFunction(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(Api.sDeleteAdjutmentApplicationForApprover)
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
                            deleteAlert();
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


    private void deleteAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.CustomDialogNew);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_success, null);
        dialogBuilder.setView(dialogView);
        TextView tvInvalidDate = (TextView) dialogView.findViewById(R.id.tvSuccess);

        tvInvalidDate.setText("Leave has been deleted successfully");



        Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerDialog1.dismiss();
                JSONObject object=new JSONObject();
                try {
                    object.put("CompanyID",pref.getEmpClintId());
                    object.put("EmployeeId",pref.getEmpId());
                    object.put("SecurityCode",pref.getSecurityCode());
                    getList(object);
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


    public void showPdfView(String type, String base64string) throws IOException {
        Dialog dialogView = new Dialog(getActivity(),R.style.CustomDialogNew2);
        dialogView.setContentView(R.layout.image_pdf_show_dialog);
        dialogView.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogView.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogView.setCancelable(false);

        ImageView viewImage = dialogView.findViewById(R.id.viewImage);
        ImageView imgCancel = dialogView.findViewById(R.id.imgCancel);
        PDFView pdfView = dialogView.findViewById(R.id.pdfView);
        LinearLayout llLoading = dialogView.findViewById(R.id.llLoading);
        TextView txtPdfPageCount = dialogView.findViewById(R.id.txtPdfPageCount);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
            }
        });

        //Log.e(TAG, "showPdfView: "+base64string);
        Log.e(TAG, "showPdfView: "+type);

        if (type.equals("pdf") || type.equals("application/pdf")){
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
                        public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
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
                    .load();
            pdfView.setVisibility(View.VISIBLE);
            viewImage.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "showPdfView: png: "+base64string);
            byte[] decodedString = Base64.decode(base64string, Base64.DEFAULT);
            Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            viewImage.setImageBitmap(image);
            viewImage.setVisibility(View.VISIBLE);
            txtPdfPageCount.setVisibility(View.GONE);
            llLoading.setVisibility(View.GONE);
        }

        dialogView.show();
    }



}