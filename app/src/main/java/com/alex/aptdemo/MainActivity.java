package com.alex.aptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.alex.annotation.BindView;
import com.alex.annotation.Flag;

@Flag
public class MainActivity extends AppCompatActivity {

    @BindView(id = R.id.text)
    TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bind_MainActivity.bindView(this);
        tv_test.setText("this data from javapoet");
    }
}
