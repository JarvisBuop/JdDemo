package com.jd.jarvisdemo1.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.jdRefactor.controller.JdCodeParams;
import com.google.zxing.client.android.jdRefactor.controller.JdEncodeBuilder;
import com.google.zxing.client.android.jdRefactor.statusmode.ResultPostBack;
import com.google.zxing.client.android.jdRefactor.ui.CaptureActivity2;
import com.jd.jarvisdemo1.R;

import java.util.ArrayList;

import static com.google.zxing.client.android.jdRefactor.controller.JdEncodeBuilder.decodeQRBitmap2Str;
import static com.google.zxing.client.android.jdRefactor.controller.JdEncodeBuilder.encodeStr2QRBitmap;
import static com.google.zxing.client.android.jdRefactor.ui.CaptureActivity2.ENTER_CODE_PARAMS;

/**
 * Created by JarvisDong on 2017/9/13.
 * OverView:
 */

public class ZxingAct extends AppCompatActivity implements View.OnClickListener {
    private ImageView viewById;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxingact);
        viewById = (ImageView) findViewById(R.id.img);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterQRcode(CaptureActivity2.class, 0);//dan
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = encodeStr2QRBitmap("感谢你为人民作出的贡献!", 300, 300);
                if (bitmap != null) {
                    viewById.setImageBitmap(bitmap);
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

    private void enterQRcode(Class newClass, int request) {
        Intent intent = new Intent(this, newClass);
        if (request == 1) {
            JdCodeParams.ParamsBuilder paramsBuilder = new JdCodeParams.ParamsBuilder()
                    .enableBarCode(true)
                    .enableQrCode(true)
                    .enablePlayaudio(false)
                    .enablePlayvibrator(true)
                    .enableIsmultiscanmode(true);
            intent.putExtra(ENTER_CODE_PARAMS, paramsBuilder);
            startActivityForResult(intent, request);
            return;
        }
        startActivityForResult(intent, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (resultCode == RESULT_OK && requestCode == 1) {//多选返回的是列表数据;
            ArrayList<ResultPostBack> serializableExtra = (ArrayList<ResultPostBack>) data.getSerializableExtra(CaptureActivity2.REQUEST_CODE_LIST);
            if (serializableExtra != null) {
                Log.e("jarvispost", serializableExtra.toString());
            } else {
                Log.e("jarvispost", "nul");
            }
        } else if (requestCode == 0) {//单选返回的室对象;
            ResultPostBack parcelableExtra = data.getParcelableExtra(CaptureActivity2.REQUEST_CODE_META);
            if (parcelableExtra != null) {
                Log.e("jarvispost", parcelableExtra.toString());

            } else {
                Log.e("jarvispost", "null");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn3://识别
                Bitmap bm = JdEncodeBuilder.getBitmap(viewById);
                if (bm != null) {
                    Result result = decodeQRBitmap2Str(bm);
                    if (result != null)
                        Toast.makeText(this, result.getText(), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn4://通用生成
                try {
                    Bitmap text = JdEncodeBuilder.encodeAsBitmap("chinacharcter err", BarcodeFormat.CODE_128, 500, 500);
                    if (text != null)
                        viewById.setImageBitmap(text);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn5://通用识别
                Bitmap bm2 = JdEncodeBuilder.getBitmap(viewById);
                if (bm2 != null) {
                    try {
                        Result result = JdEncodeBuilder.decodeFromBitmap(bm2, BarcodeFormat.CODE_128);
                        if (result != null)
                            Toast.makeText(this, result.getText(), Toast.LENGTH_LONG).show();
                    } catch (ReaderException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn6:
                enterQRcode(CaptureActivity2.class, 1);
                break;
        }
    }
}
