package com.alex.aptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.alex.annotation.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(id = R.id.text)
    TextView tv_test;
    @BindView(id = R.id.text2)
    TextView tv_test2;
    @BindView(id = R.id.text3)
    TextView tv_test3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bind_MainActivity.bindView(this);
        tv_test.setText("this data from javapoet");
        tv_test2.setText("this data from javapoet+++++++++");
        tv_test3.setText("this data from javapoet---------");
    }
}
