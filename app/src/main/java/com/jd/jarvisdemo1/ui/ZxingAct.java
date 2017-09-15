package com.jd.jarvisdemo1.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.client.android.jdRefactor.controller.JdEncodeBuilder;
import com.google.zxing.client.android.jdRefactor.ui.CaptureActivity2;
import com.jd.jarvisdemo1.R;

/**
 * Created by JarvisDong on 2017/9/13.
 * OverView:
 */

public class ZxingAct extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxingact);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterQRcode(CaptureActivity2.class);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = JdEncodeBuilder.generateBitmap("感谢你为人民作出的贡献!", 300, 300);
                if (bitmap != null) {
                    ((ImageView) findViewById(R.id.img)).setImageBitmap(bitmap);
                }
            }
        });
        Button btn3 = (Button) findViewById(R.id.btn3);
        Button btn4 = (Button) findViewById(R.id.btn4);
        Button btn5 = (Button) findViewById(R.id.btn5);
        Button btn6 = (Button) findViewById(R.id.btn6);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
    }

    private void enterQRcode(Class newClass) {
        startActivity(new Intent(this, newClass));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn3:
                break;
            case R.id.btn4:
                break;
            case R.id.btn5:
                break;
            case R.id.btn6:
                break;
        }
    }
}
