package com.hongfans.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hongfans.common.log.LogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        method1();
    }

    private void method1() {
        method2();
    }

    private void method2() {

        LogUtil.v("hello");
        LogUtil.d("hello");
        LogUtil.i("hello");
        LogUtil.w("hello");
        LogUtil.e("hello");

        LogUtil.print("print", "print this log");

//        LogUtil.saveLog("保存的日志第一行");
//        LogUtil.saveLog("保存的日志第二行");
    }
}
