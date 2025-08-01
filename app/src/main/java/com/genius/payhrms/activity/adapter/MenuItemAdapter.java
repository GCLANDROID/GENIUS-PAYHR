package com.genius.payhrms.activity.adapter;

import static com.genius.payhrms.activity.activity.UserDashBoardActivity.isAppMinimizeDashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.activity.ChangePasswordActivity;
import com.genius.payhrms.activity.activity.ELearningActivity;
import com.genius.payhrms.activity.activity.HolidayActivity;
import com.genius.payhrms.activity.activity.LoginActivity;
import com.genius.payhrms.activity.activity.QueryActivity;
import com.genius.payhrms.activity.activity.UserDashBoardActivity;
import com.genius.payhrms.activity.activity.VoiceAssistantActivity;
import com.genius.payhrms.activity.attendance.AttendanceActivity;
import com.genius.payhrms.activity.attendance.AttendanceCalenderDashboardActivity;

import com.genius.payhrms.activity.dailyactivity.DailyTaskDashBoardActivity;
import com.genius.payhrms.activity.dailylog.DailyLogCalenderDashboardActivity;
import com.genius.payhrms.activity.dailylog.OfflineDailyDashBoardActivity;
import com.genius.payhrms.activity.geofence.ConfigNumberActivity;
import com.genius.payhrms.activity.geofence.GeoFenceManageDashBoardActivity;
import com.genius.payhrms.activity.geofence.NotificationActivity;
import com.genius.payhrms.activity.leaveapplication.LeaveApplicationActivity;
import com.genius.payhrms.activity.leaveapplication.LeaveDashboardActivity;
import com.genius.payhrms.activity.leaveapplication.LeaveWebViewActivity;
import com.genius.payhrms.activity.model.MenuItemModel;
import com.genius.payhrms.activity.payroll.PayrollActivity;
import com.genius.payhrms.activity.payroll.SalaryActivity;
import com.genius.payhrms.activity.profile.ProfileActivity;
import com.genius.payhrms.activity.profile.ProfileDashboardActivity;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;

import javax.xml.transform.stream.StreamSource;

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
        if (pref.getLanguage().equals("hi") || pref.getLanguage().equalsIgnoreCase("ta")) {
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
            myViewHolder.imgMenu.setImageResource(R.drawable.profile_new);
        }
        else if (itemList.get(i).getMenuId()==5){
            myViewHolder.imgMenu.setImageResource(R.drawable.attendance_new);
        }
        else if (itemList.get(i).getMenuId()==6){
            myViewHolder.imgMenu.setImageResource(R.drawable.payroll_new);
        }
        else if (itemList.get(i).getMenuId()==7){
            myViewHolder.imgMenu.setImageResource(R.drawable.daily_log_new);
        }
        else if (itemList.get(i).getMenuId()==8){
            myViewHolder.imgMenu.setImageResource(R.drawable.daily_log_new);
        }
        else if (itemList.get(i).getMenuId()==9){
            myViewHolder.imgMenu.setImageResource(R.drawable.fence_new);
        }
        else if (itemList.get(i).getMenuId()==10){
            myViewHolder.imgMenu.setImageResource(R.drawable.leaveapplication_new);
        }
        else if (itemList.get(i).getMenuId()==11){
            myViewHolder.imgMenu.setImageResource(R.drawable.notifcation_new);
        }
        else if (itemList.get(i).getMenuId()==12){
            myViewHolder.imgMenu.setImageResource(R.drawable.holiday_new);
        }
        else if (itemList.get(i).getMenuId()==13){
            myViewHolder.imgMenu.setImageResource(R.drawable.translate);
        }
        else if (itemList.get(i).getMenuId()==14){
            myViewHolder.imgMenu.setImageResource(R.drawable.elearning_new);
        }
        else if (itemList.get(i).getMenuId()==4){
            myViewHolder.imgMenu.setImageResource(R.mipmap.chaticon_payhr);
        }
        else  if (itemList.get(i).getMenuId()==2){
            myViewHolder.imgMenu.setImageResource(R.drawable.exit_new);
        }
        else  if (itemList.get(i).getMenuId()==3){
            myViewHolder.imgMenu.setImageResource(R.drawable.resetpassword_new);
        } else  if (itemList.get(i).getMenuId()==101){
            myViewHolder.imgMenu.setImageResource(R.drawable.questionicon);
        }  else  if (itemList.get(i).getMenuId()==16){
            myViewHolder.imgMenu.setImageResource(R.drawable.voiceassistant_new);
        }else  if (itemList.get(i).getMenuId()==212){
            myViewHolder.imgMenu.setImageResource(R.drawable.resignation);
        }else  if (itemList.get(i).getMenuId()==105){
            myViewHolder.imgMenu.setImageResource(R.drawable.form16);
        }else {
            myViewHolder.itemView.setVisibility(View.GONE);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //payroll
                if (itemList.get(i).getMenuId()==6){
                    if (pref.getSecurityCode().equals("1163")){
                        Intent intent=new Intent(mContex, SalaryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }else {
                        Intent intent=new Intent(mContex, PayrollActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }
                } else if (itemList.get(i).getMenuId()==1){
                    //profile
                    if (pref.getSecurityCode().equals("1080")) {
                        Intent intent = new Intent(mContex, ProfileDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContex, ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }
                } else if (itemList.get(i).getMenuId()==2){
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
                } else if (itemList.get(i).getMenuId() == 5) {
                    //attendance
                    isAppMinimizeDashboard = true;
                    Intent intent = new Intent(mContex, AttendanceCalenderDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);

                } else if (itemList.get(i).getMenuId() == 7) {
                    //Dailylog
                    isAppMinimizeDashboard = true;
                    if (pref.getSecurityCode().equals("1156") ||pref.getSecurityCode().equals("1000")||pref.getSecurityCode().equals("1160")||pref.getSecurityCode().equals("1168")|| pref.getSecurityCode().equals("5000") /*||pref.getSecurityCode().equals("1167")*/||pref.getSecurityCode().equals("1172")||pref.getSecurityCode().equals("1173")){
                        Intent intent = new Intent(mContex, DailyLogCalenderDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }else {
                        Intent intent = new Intent(mContex, OfflineDailyDashBoardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }
                }
                else if (itemList.get(i).getMenuId()==8){
                    //dailyactivity
                    Intent intent=new Intent(mContex, DailyTaskDashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }
                else if (itemList.get(i).getMenuId()==9){
                    //geofence
                    if (pref.getSecurityCode().equals("1157") || pref.getSecurityCode().equals("1163")){
                            Intent intent=new Intent(mContex, GeoFenceManageDashBoardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("point", "sin");
                            mContex.startActivity(intent);
                    }else {
                        Intent intent=new Intent(mContex, ConfigNumberActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }

                } else if (itemList.get(i).getMenuId()==10){
                    //leave
                    if (pref.getSecurityCode().equals("5555")){
                        Intent intent = new Intent(mContex, LeaveWebViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }else if (pref.getSecurityCode().equals("1000")||pref.getSecurityCode().equals("1080")){
                        Intent intent = new Intent(mContex, LeaveDashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }else if (pref.getSecurityCode().equals("1167")){
                        Intent intent = new Intent(mContex, LeaveApplicationActivity.class);
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
                    if (pref.getSecurityCode().equals("1156")){
                        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://genpayhr.geniusconsultant.com/WesternEnterprises/ESS/Holiday/HolidayCalMobile.aspx?CalenderYear=2025&EmployeeID=2070000002"));
                        urlIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(urlIntent);
                    }else {
                        //holiday
                        Intent intent = new Intent(mContex, HolidayActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContex.startActivity(intent);
                    }
                } else if (itemList.get(i).getMenuId()==13){
                    //language
                    ((UserDashBoardActivity) mContex).shoeDialog();
                } else if (itemList.get(i).getMenuId()==14){
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
                }else if (itemList.get(i).getMenuId()==16){
                    //voiceassistant
                    Intent intent=new Intent(mContex, VoiceAssistantActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);
                }else if (itemList.get(i).getMenuId()==105){
                    ((UserDashBoardActivity)mContex).getFormSixten();
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
