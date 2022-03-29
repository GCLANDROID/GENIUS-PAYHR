package com.genius.hrms.activity.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.activity.ChangePasswordActivity;
import com.genius.hrms.activity.activity.ChatActivity;
import com.genius.hrms.activity.activity.ELearningActivity;
import com.genius.hrms.activity.activity.HolidayActivity;
import com.genius.hrms.activity.activity.LoginActivity;
import com.genius.hrms.activity.activity.QueryActivity;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.genius.hrms.activity.attendance.AttendanceActivity;
import com.genius.hrms.activity.chat.ChatHomePage;
import com.genius.hrms.activity.dailyactivity.DailyTaskDashBoardActivity;
import com.genius.hrms.activity.dailylog.OfflineDailyDashBoardActivity;
import com.genius.hrms.activity.geofence.ConfigNumberActivity;
import com.genius.hrms.activity.geofence.EmpMappingActivity;
import com.genius.hrms.activity.geofence.NotificationActivity;
import com.genius.hrms.activity.leaveapplication.ApproverFragment;
import com.genius.hrms.activity.leaveapplication.LeaveApplicationActivity;
import com.genius.hrms.activity.leaveapplication.LeaveApplicationDashboardActivity;
import com.genius.hrms.activity.leaveapplication.LeaveDashboardActivity;
import com.genius.hrms.activity.leaveapplication.LeaveWebViewActivity;
import com.genius.hrms.activity.model.ApprovalModel;
import com.genius.hrms.activity.model.MenuItemModel;
import com.genius.hrms.activity.model.MenuModule;
import com.genius.hrms.activity.payroll.PayrollActivity;
import com.genius.hrms.activity.profile.ProfileActivity;
import com.genius.hrms.activity.profile.ProfileDashboardActivity;
import com.genius.hrms.activity.profile.ProfileUpdateActivity;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MyViewHolder> {
    ArrayList<MenuItemModel>itemList=new ArrayList<>();
    Context mContex;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Pref pref=new Pref(mContex);
        final ProgressDialog pd=new ProgressDialog(mContex);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
//AIzaSyCjK28Qn10raw876c8jHJiLOy4yDXEH7ww
        //AIzaSyABnTOmX0ySqLO0OBnsNVZZBrKbz0cohO8
        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler1 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyC9W7jzcbE-h8jZPsmpDaJRxEZjoRQwEhM")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getMenuName(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler1.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvMenuItem.setText(hLoc);


                        }
                    });
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pd.show();


                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    pd.dismiss();


                }


            }.execute();
        }else {
            myViewHolder.tvMenuItem.setText(itemList.get(i).getMenuName());
        }

        if (itemList.get(i).getMenuId()==1){
            myViewHolder.imgMenu.setImageResource(R.mipmap.profile_payhr);
        }
        else if (itemList.get(i).getMenuId()==5){
            myViewHolder.imgMenu.setImageResource(R.mipmap.attendance_payhr);
        }
        else if (itemList.get(i).getMenuId()==6){
            myViewHolder.imgMenu.setImageResource(R.mipmap.payroll_payhr);
        }
        else if (itemList.get(i).getMenuId()==7){
            myViewHolder.imgMenu.setImageResource(R.mipmap.dailylog_payhr);
        }
        else if (itemList.get(i).getMenuId()==8){
            myViewHolder.imgMenu.setImageResource(R.mipmap.dailylog_payhr);
        }
        else if (itemList.get(i).getMenuId()==9){
            myViewHolder.imgMenu.setImageResource(R.mipmap.geofence_payhr);
        }
        else if (itemList.get(i).getMenuId()==10){
            myViewHolder.imgMenu.setImageResource(R.mipmap.leaveapplication_payhr);
        }
        else if (itemList.get(i).getMenuId()==11){
            myViewHolder.imgMenu.setImageResource(R.mipmap.subordinatereport_payhr);
        }
        else if (itemList.get(i).getMenuId()==12){
            myViewHolder.imgMenu.setImageResource(R.mipmap.holidaylist_payhr);
        }
        else if (itemList.get(i).getMenuId()==13){
            myViewHolder.imgMenu.setImageResource(R.mipmap.bilingual_payhr);
        }
        else if (itemList.get(i).getMenuId()==14){
            myViewHolder.imgMenu.setImageResource(R.mipmap.elearning_payhr);
        }
        else if (itemList.get(i).getMenuId()==4){
            myViewHolder.imgMenu.setImageResource(R.mipmap.chaticon_payhr);
        }
        else  if (itemList.get(i).getMenuId()==2){
            myViewHolder.imgMenu.setImageResource(R.mipmap.logout_payhr);
        }
        else  if (itemList.get(i).getMenuId()==3){
            myViewHolder.imgMenu.setImageResource(R.mipmap.changepassword_payhr);
        }
        else  if (itemList.get(i).getMenuId()==101){
            myViewHolder.imgMenu.setImageResource(R.drawable.questionicon);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemList.get(i).getMenuId()==6){
                    //payroll
                    Intent intent=new Intent(mContex, PayrollActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }else if (itemList.get(i).getMenuId()==1){
                    //profile
                    if (pref.getSecurityCode().equals("1080")) {
                        Intent intent = new Intent(mContex, ProfileDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }else
                    {
                        Intent intent = new Intent(mContex, ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);

                    }
                }
                else if (itemList.get(i).getMenuId()==2){
                    //logout
                    pref.setFirstTimeLaunch(false);
                    pref.saveLoginFlag("2");
                    Intent intent=new Intent(mContex, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId()==3){
                    //changepassword
                    Intent intent=new Intent(mContex, ChangePasswordActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("goingstatus","2");
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId()==4){
                    //chat
                    Intent intent=new Intent(mContex, ChatHomePage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId()==5){
                    //attendance
                    Intent intent=new Intent(mContex, AttendanceActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId()==7){
                    //dailylog
                    Intent intent=new Intent(mContex, OfflineDailyDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId()==8){
                    //dailyactivity
                    Intent intent=new Intent(mContex, DailyTaskDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId()==9){
                    //geofence
                    Intent intent=new Intent(mContex, ConfigNumberActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId()==10){
                    //leave
                    if (pref.getSecurityCode().equals("5555")){
                        Intent intent = new Intent(mContex, LeaveWebViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }else if (pref.getSecurityCode().equals("1000")||pref.getSecurityCode().equals("1080")){

                        Intent intent = new Intent(mContex, LeaveDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }else {
                        Intent intent = new Intent(mContex, LeaveDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }
                }
                else if (itemList.get(i).getMenuId()==11){
                    //sub report
                    Intent intent=new Intent(mContex, NotificationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId()==12){
                    //holiday
                    Intent intent=new Intent(mContex, HolidayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }

                else if (itemList.get(i).getMenuId()==13){
                    //language
                    ((UserDashBoardActivity) mContex).shoeDialog();

                }
                else if (itemList.get(i).getMenuId()==14){
                    //elearning
                    Intent intent=new Intent(mContex, ELearningActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);

                } else if (itemList.get(i).getMenuId()==101){
                    //elearning
                    Intent intent=new Intent(mContex, QueryActivity.class);
                    intent.putExtra("goingFlag","2");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);

                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenuItem;
        LinearLayout llTick,llGreen,llYellow;
        ImageView imgMenu;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuItem=(TextView)itemView.findViewById(R.id.tvMenuItem);
            imgMenu=(ImageView)itemView.findViewById(R.id.imgMenu);




        }
    }

    public MenuItemAdapter(ArrayList<MenuItemModel> itemList,  Context mContext) {
        this.itemList = itemList;
        this.mContex=mContext;
    }

    public void filterList(ArrayList<MenuItemModel> filterdNames) {
        this.itemList = filterdNames;
        notifyDataSetChanged();
    }
}
