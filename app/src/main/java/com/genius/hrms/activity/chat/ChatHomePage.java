package com.genius.hrms.activity.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.genius.hrms.R;
import com.genius.hrms.activity.activity.UserDashBoardActivity;
import com.google.android.material.tabs.TabLayout;

public class ChatHomePage extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    ImageView imgHome,imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home_page);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        imgBack=findViewById(R.id.imgBack);
        imgHome=findViewById(R.id.imgHome);
        onClick();

        setupPagerFragment();
        init();
    }
    private void onClick(){
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ChatHomePage.this, UserDashBoardActivity.class);
                startActivity(i);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void setupPagerFragment() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), ViewPagerAdapter.POSITION_UNCHANGED);

        viewPagerAdapter.addFragment(new IndividualChatFragment(this), "Individual");
        viewPagerAdapter.addFragment(new GroupChatFragment(), "Group");


        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }
    private void init() {




    }
}