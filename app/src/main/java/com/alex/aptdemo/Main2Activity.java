package com.alex.aptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.alex.annotation.BindView;

public class Main2Activity extends AppCompatActivity {


    @BindView(id = R.id.text10)
    TextView tv_test;
    @BindView(id = R.id.text11)
    TextView tv_test2;
    @BindView(id = R.id.text12)
    TextView tv_test3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
}
