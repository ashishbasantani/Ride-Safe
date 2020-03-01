package com.example.ridesafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        ShowSplashScreen s=new ShowSplashScreen();
        s.start();
    }
    private class ShowSplashScreen extends Thread{
        @Override
        public void run() {
            try{
                sleep(2000);
            }
            catch(Exception e){}
            Intent i=new Intent(SplashScreen.this,Login.class);
            startActivity(i);
            SplashScreen.this.finish();
        }
    }
}
