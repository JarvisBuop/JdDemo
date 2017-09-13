package com.jd.jarvisdemo1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jd.jarvisdemo1.R;

/**
 * Created by JarvisDong on 2017/9/13.
 * OverView:
 */

public class ZxingAct extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxingact);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ZxingAct.this,CaptureActivity.class));
            }
        });
    }
}
