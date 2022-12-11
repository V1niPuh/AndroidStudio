package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setClick();
    }

    private void setClick(){
        findViewById(R.id.login_signin).setOnClickListener(this);
        findViewById(R.id.login_profile).setOnClickListener(this);
        findViewById(R.id.login_reg).setOnClickListener(this);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.login_signin:{
                Intent main1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main1);
            }break;
            case R.id.login_reg:{
                Intent reg= new Intent(getApplicationContext(), RegActivity.class);
                startActivity(reg);
            }break;
        }
    }
}