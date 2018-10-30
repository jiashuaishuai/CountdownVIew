package com.example.shuaijia.countdownview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CountDownTextView tv_count_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_count_down = findViewById(R.id.tv_count_down);
        tv_count_down.cancel();//停止
        tv_count_down.setCall(new CountDownTextView.CountDownFinishCall() {
            @Override
            public void call() {
                Toast.makeText(MainActivity.this,"倒计时完毕",Toast.LENGTH_LONG).show();
            }
        });
        tv_count_down.start(90000);
    }
}
