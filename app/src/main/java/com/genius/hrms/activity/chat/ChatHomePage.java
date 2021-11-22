package com.genius.hrms.activity.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.genius.hrms.R;
import com.google.android.material.tabs.TabLayout;

public class ChatHomePage extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home_page);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        setupPagerFragment();
        init();
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