package com.example.bouncingballs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiTouchView touchView = findViewById(R.id.multiTouchView);
        touchView.t = touchView.new MyThread();
        touchView.t.start();
    }

    public void onClick(View view) {
        MultiTouchView touchView = findViewById(R.id.multiTouchView);
        touchView.AddBall();
    }
}
