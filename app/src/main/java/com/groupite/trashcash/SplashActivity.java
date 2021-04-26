package com.groupite.trashcash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import prihanofficial.com.kokis.logics.Kokis;

public class SplashActivity extends AppCompatActivity {

    private Long splashTime = 300L;
    private Handler handler;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        textView = findViewById(R.id.version);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                goToMainActivity();
            }
        },splashTime);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        int versionCode = BuildConfig.VERSION_CODE;
//        String versionName = BuildConfig.VERSION_NAME;
//        String x = Integer.toString(versionCode) + "." + versionName;
//
//        textView.setText(x);
    }

    private void goToMainActivity(){
      if(Kokis.kgetKokisBoolean("isLogged",false)){
          Intent intent = new Intent(this,MainActivity.class);
          startActivity(intent);
          finish();
      }else {
          Intent intent = new Intent(this,LoginActivity.class);
          startActivity(intent);
          finish();
      }
    }
}