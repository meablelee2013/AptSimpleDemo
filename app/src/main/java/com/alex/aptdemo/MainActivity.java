package com.alex.aptdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.alex.annotation.Flag;

@Flag
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
