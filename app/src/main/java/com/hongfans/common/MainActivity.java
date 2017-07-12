package com.hongfans.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hongfans.libcommon.log.LogUtil;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LogUtil.init("TAG", true, true);

        LogUtil.e("hello");

    }
}
