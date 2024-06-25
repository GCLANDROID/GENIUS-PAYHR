package com.genius.payhrms.activity.leaveapplication;



import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genius.payhrms.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SpecialHolidayApproverFragment extends Fragment {
    View view;
    LinearLayout llManage,llDetails;
    TextView tvManage,tvDetails;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_special_holiday_approver, container, false);
        initView();
        loadManageFragment();
        onClick();
        return view;
    }

    private void initView(){
        llDetails=(LinearLayout)view.findViewById(R.id.llDetails);
        llManage=(LinearLayout)view.findViewById(R.id.llManage);

        tvDetails=(TextView)view.findViewById(R.id.tvDetails);
        tvManage=(TextView)view.findViewById(R.id.tvManage);
    }

    private void onClick(){
        llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadReportFragment();
            }
        });

        llManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadManageFragment();
            }
        });
    }


    public void loadManageFragment() {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SpecialHolidayApprovalManageFragment pfragment=new SpecialHolidayApprovalManageFragment();
        transaction.replace(R.id.frameLayout1, pfragment);
        transaction.commit();

        tvManage.setTextColor(Color.parseColor("#075994"));
        tvDetails.setTextColor(Color.parseColor("#ffffff"));





    }

    public void loadReportFragment() {

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SpecialHolidayApprovalReportFragment pfragment=new SpecialHolidayApprovalReportFragment();
        transaction.replace(R.id.frameLayout1, pfragment);
        transaction.commit();

        tvManage.setTextColor(Color.parseColor("#ffffff"));
        tvDetails.setTextColor(Color.parseColor("#075994"));

    }


}
