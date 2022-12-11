package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.logging.Handler;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding);
        setClick();
    }

    private void setClick(){
        findViewById(R.id.onboarding_login).setOnClickListener((View.OnClickListener)this);
        findViewById(R.id.onboarding_reg).setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.onboarding_login: {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }break;
            case R.id.onboarding_reg: {
                Intent reg = new Intent(getApplicationContext(), RegActivity.class);
                startActivity(reg);
            }break;
        }
    }
}