package com.ejar.egou.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ejar.chapp.baselibrary.net.RetrofitManager;
import com.ejar.egou.R;

/**
 * Created by Administrator on 2017\12\9 0009.
 */

public class SplashAty extends AppCompatActivity {
    private ImageView splashImg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //不显示程序的标题栏
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        //不显示系统的标题栏
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_splash);
        splashImg = (ImageView) findViewById(R.id.splash_img);
        splashImg.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashAty.this, MainActivity.class));
                finish();
            }
        },3000);



    }
}
