package com.google.zxing.client.android.jdRefactor.ui.viewfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.TypedValue;

import com.google.zxing.client.android.R;

/**
 * Created by JarvisDong on 2017/9/16.
 * 自定义绘制;
 */

public class JdDraw implements ViewFinderDraw {
    int offset = 6;//偏移量;
    int audioTop = 0;
    int audioBottom = 0;


    int rate = 2;//移动的比率;
    int moveOffset = 3;//移动距离;
    int middle = 0;//红线移动距离;
    private Context context;

    @Override
    public void drawOutside(Context context, Canvas canvas, Rect frame, int cWidth, int cHeight, Paint mPaint) {
        this.context = context;
        //画上下左右的遮罩层;
        canvas.drawRect(0, 0, cWidth, frame.top, mPaint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, mPaint);
        canvas.drawRect(frame.right + 1, frame.top, cWidth, frame.bottom + 1, mPaint);
        canvas.drawRect(0, frame.bottom + 1, cWidth, cHeight, mPaint);

        drawOutCustom(canvas, frame, cWidth, cHeight, mPaint);
    }

    @Override
    public void drawInside(Canvas canvas, Rect frame, Paint mPaint) {
        //上下移动扫描线;
        middle = middle + rate * moveOffset;
        if (middle <= frame.top || middle >= frame.bottom) {
            middle = frame.top;
        }
        canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, mPaint);
    }

    private void drawOutCustom(Canvas canvas, Rect frame, int cWidth, int cHeight, Paint mPaint) {
        //画音量键
        Bitmap bitmapUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_up);
        Bitmap bitmapDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_down);
        audioTop = audioTop - (rate - 1) * moveOffset;
        if (audioTop <= (frame.top / 2 - 2 * bitmapUp.getHeight()) || audioTop >= (frame.top / 2 - bitmapUp.getHeight())) {
            audioTop = frame.top / 2 - bitmapUp.getHeight();
        }
        audioBottom = audioBottom + (rate - 1) * moveOffset;
        if (audioBottom <= (frame.top / 2 ) || audioBottom >= (frame.top / 2 + bitmapUp.getHeight())) {
            audioBottom = frame.top / 2;
        }

        canvas.drawBitmap(bitmapUp, offset, audioTop, null);
        canvas.drawBitmap(bitmapDown, offset, audioBottom , null);
        String string = context.getString(R.string.jd_flash);
        TextPaint textPaint1 = new TextPaint();
        textPaint1.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,10,context.getResources().getDisplayMetrics()));
        textPaint1.setColor(Color.WHITE);
        Rect rect = new Rect();
        textPaint1.getTextBounds(string,0,string.length(),rect);
        canvas.drawText(string,2*offset +bitmapUp.getWidth(),frame.top/2+rect.height()/2,textPaint1);

    }
}
