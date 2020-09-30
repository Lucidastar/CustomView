package com.mine.customviewtohy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void custom1(View view){
        startActivity(new Intent(this,FirstActivity.class));
    }
    public void custom2(View view){
        startActivity(new Intent(this,SecondActivity.class));
    }
    public void custom3(View view){
        startActivity(new Intent(this,ThressActivity.class));
    }
    public void custom4(View view){
        startActivity(new Intent(this,FourActivity.class));
    }
    public void custom5(View view){
        startActivity(new Intent(this,FiveActivity.class));
    }
    public void custom6(View view){
        startActivity(new Intent(this,SixActivity.class));
    }
    public void custom7(View view){
        startActivity(new Intent(this,ServenActivity.class));
    }
    public void custom8(View view){
        startActivity(new Intent(this,SevenActivity.class));
    }
}
