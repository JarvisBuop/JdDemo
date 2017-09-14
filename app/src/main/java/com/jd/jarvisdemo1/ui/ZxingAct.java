package com.jd.jarvisdemo1.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.book.SearchBookContentsActivity;
import com.google.zxing.client.android.encode.EncodeActivity;
import com.google.zxing.client.android.share.ShareActivity;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jd.jarvisdemo1.R;

import java.util.HashMap;
import java.util.Map;

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
                enterQRcode(CaptureActivity.class);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = generateBitmap("感谢你为人民作出的贡献!", 300, 300);
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

    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn3:
                enterQRcode(SearchBookContentsActivity.class);
                break;
            case R.id.btn4:
                enterQRcode(EncodeActivity.class);
                break;
            case R.id.btn5:
                enterQRcode(ShareActivity.class);
                break;
            case R.id.btn6:
                enterQRcode(CaptureActivity.class);
                break;
        }
    }
}
