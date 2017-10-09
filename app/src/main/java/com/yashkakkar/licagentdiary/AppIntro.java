package com.yashkakkar.licagentdiary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yashkakkar.licagentdiary.login.AgentRegistration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AppIntro extends AppCompatActivity {

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    protected boolean _active = true;
    protected int _splashTime = 1600;
    private int _wait = 0;
    private Handler handler = new Handler();
    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_app_intro);
        unbinder = ButterKnife.bind(this);
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);

      Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {

                    while (_active && (_wait <= _splashTime)) {
                        handler.post(new Runnable() {
                            public void run() {
                                // textView.setText(progressStatus+"/"+progressBar.getMax());
                            }
                        });
                        sleep(1000);
                        if (_active) {
                            _wait += 100;
                        }
                    }
                } catch (Exception e) {

                } finally {
                    // Load Intro for first time if first=false if first= true then always skip the full Intro and go to main activity
                    SharedPreferences sd = getSharedPreferences("AppIntro",Context.MODE_PRIVATE);
                    // SharedPreferences.Editor editor = sd.edit();
                    if(sd.getBoolean("first",false)){
                        Intent i = new Intent(AppIntro.this, ActivityDashboard.class);
                        startActivity(i);
                        finish();
                    }else{
                        Intent i = new Intent(AppIntro.this, Intro.class);
                        startActivity(i);
                        finish();
                    }
                    finish();
                }
            }
        };

       splashTread.start();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

}
