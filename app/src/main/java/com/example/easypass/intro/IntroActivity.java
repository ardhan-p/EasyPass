package com.example.easypass.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.easypass.masterpassword.LoginActivity;
import com.example.easypass.R;
import com.example.easypass.masterpassword.CreateMasterPasswordActivity;
import com.google.android.material.tabs.TabLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager pager;
    Button nextBtn;
    int pagePosition = 0;
    boolean readyToStart = false;
    IntroViewPagerAdapter adapter;
    TabLayout indicator;

    private boolean checkIntroStatus() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("appPrefs", MODE_PRIVATE);
        return pref.getBoolean(getString(R.string.prefs_intro_status), false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // intent for starting master password creation and locked screen
        Intent masterPasswordActivityIntent = new Intent(this, CreateMasterPasswordActivity.class);
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        // Intent lockedPasswordActivityIntent

        if (checkIntroStatus()) {
            startActivity(loginActivityIntent);
            finish();
        }

        // hides top action bar for aesthetics
        getSupportActionBar().hide();

        // create intro screen objects for viewpager
        List<IntroScreen> screens = new ArrayList<>();
        screens.add(new IntroScreen("Welcome to EasyPass!", R.drawable.vault));
        screens.add(new IntroScreen("The app allows you to make passwords for your accounts and store them in a secure place!", R.drawable.locked));
        screens.add(new IntroScreen("Create new login credentials with the plus button.", R.drawable.intro1));
        screens.add(new IntroScreen("Input your account details and generate password.", R.drawable.intro2));
        screens.add(new IntroScreen("Password will be securely encrypted and will be available in the homepage!", R.drawable.intro3));

        // initialises viewpager with adapter with all of the screens
        pager = findViewById(R.id.introViewPager);
        adapter = new IntroViewPagerAdapter(screens, this);
        pager.setAdapter(adapter);

        // initialises tabview bullet indicator
        indicator = findViewById(R.id.introTabLayout);
        indicator.setupWithViewPager(pager);

        // button action listener
        nextBtn = findViewById(R.id.introNextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagePosition = pager.getCurrentItem();

                // button will prompt the user to go to master password creation screen
                if (readyToStart) {
                    startActivity(masterPasswordActivityIntent);
                }

                // if the page position is not at the end, it will go to the next page of the pager
                if (pagePosition < screens.size()) {
                    readyToStart = false;
                    pagePosition++;
                    pager.setCurrentItem(pagePosition);
                }

                // once pager has reached the end, the button will change to show that the user is prompted to create master password
                if (pagePosition == screens.size() - 1) {
                    readyToStart = true;
                    nextBtn.setText(getResources().getString(R.string.btn_get_started));
                    nextBtn.setTextColor(getResources().getColor(R.color.white));
                    nextBtn.setBackgroundColor(getResources().getColor(R.color.blue_btn_color));
                    indicator.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


}