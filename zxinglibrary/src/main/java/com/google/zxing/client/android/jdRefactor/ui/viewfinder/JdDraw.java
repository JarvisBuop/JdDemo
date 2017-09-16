package com.google.zxing.client.android.jdRefactor.ui.viewfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;

import com.google.zxing.client.android.R;

/**
 * Created by JarvisDong on 2017/9/16.
 * 自定义绘制;
 * 移动线
 * 音量键
 * 四角;
 */

public class JdDraw implements ViewFinderDraw {
    int offset = 6;//偏移量;
    int audioTop = 0;
    int audioBottom = 0;


    int rate = 2;//移动的比率;
    int moveOffset = 3;//移动距离;
    int middle = 0;//红线移动距离;
    private Context context;

    private int colorRound;
    private boolean isDrawOut = false;
    int blockLengh = 9 * offset;//长边长度;

    @Override
    public void drawOutside(Context context, Canvas canvas, Rect frame, int cWidth, int cHeight, Paint mPaint) {
        this.context = context;

        //画上下左右的遮罩层;
        canvas.drawRect(0, 0, cWidth, frame.top, mPaint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, mPaint);
        canvas.drawRect(frame.right + 1, frame.top, cWidth, frame.bottom + 1, mPaint);
        canvas.drawRect(0, frame.bottom + 1, cWidth, cHeight, mPaint);

        colorRound = context.getResources().getColor(R.color.four_laser);
        drawOutCustom(canvas, frame, cWidth, cHeight, mPaint);
    }

    @Override
    public void drawInside(Canvas canvas, Rect frame, Paint mPaint) {
        //上下移动扫描线;
        if (middle <= frame.top || middle >= frame.bottom) {
            middle = frame.top;
        }
        middle = middle + rate * moveOffset;
        canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, mPaint);
    }

    private void drawOutCustom(Canvas canvas, Rect frame, int cWidth, int cHeight, Paint mPaint) {
        //画音量键
        drawArrowBitmap(canvas, frame);
        //画四角;
        mPaint.setColor(colorRound);
        drawDirRound(canvas, frame, mPaint,isDrawOut,offset,blockLengh);
    }

    private void drawArrowBitmap(Canvas canvas, Rect frame) {
        Bitmap bitmapUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_up);
        Bitmap bitmapDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_down);
        audioTop = audioTop - (rate - 1) * moveOffset;
        if (audioTop <= (frame.top / 2 - 2 * bitmapUp.getHeight()) || audioTop >= (frame.top / 2 - bitmapUp.getHeight())) {
            audioTop = frame.top / 2 - bitmapUp.getHeight();
        }
        audioBottom = audioBottom + (rate - 1) * moveOffset;
        if (audioBottom <= (frame.top / 2) || audioBottom >= (frame.top / 2 + bitmapUp.getHeight())) {
            audioBottom = frame.top / 2;
        }

        canvas.drawBitmap(bitmapUp, offset, audioTop, null);
        canvas.drawBitmap(bitmapDown, offset, audioBottom, null);
        String string = context.getString(R.string.jd_flash);

        TextPaint textPaint1 = new TextPaint();
        textPaint1.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, context.getResources().getDisplayMetrics()));
        textPaint1.setColor(Color.WHITE);
        StaticLayout staticLayout = new StaticLayout(string,textPaint1,bitmapUp.getWidth()*2, Layout.Alignment.ALIGN_NORMAL,1.0f,0f,false);
        canvas.save();
        canvas.translate(2 * offset + bitmapUp.getWidth(), frame.top / 2 - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
        canvas.restore();
//        canvas.drawText(string, 2 * offset + bitmapUp.getWidth(), frame.top / 2 + rect.height() / 2, textPaint1);
    }

    /**
     *
     * @param canvas 画布;
     * @param frame //内框大小
     * @param mPaint //画笔;
     * @param isDrawOut 是否画在内框外部ture 为外部,false 为内部;
     * @param offset  短边长度;
     * @param blockLengh 长边长度;
     */
    public static void drawDirRound(Canvas canvas, Rect frame, Paint mPaint,boolean isDrawOut,int offset,int blockLengh) {
        if (isDrawOut) {
            canvas.drawRect(frame.left - offset, frame.top - offset, frame.left + blockLengh - offset, frame.top, mPaint);//左上;
            canvas.drawRect(frame.left-offset,frame.top,frame.left,frame.top+(blockLengh-offset),mPaint);
            canvas.drawRect(frame.right -(blockLengh-offset),frame.top-offset,frame.right+offset,frame.top,mPaint);//右上
            canvas.drawRect(frame.right,frame.top,frame.right+offset,frame.top+(blockLengh-offset),mPaint);
            canvas.drawRect(frame.left-offset,frame.bottom-(blockLengh-offset),frame.left,frame.bottom,mPaint);//左下;
            canvas.drawRect(frame.left-offset,frame.bottom,frame.left+(blockLengh-offset),frame.bottom+offset,mPaint);
            canvas.drawRect(frame.right,frame.bottom-(blockLengh-offset),frame.right+offset,frame.bottom,mPaint);//右下;
            canvas.drawRect(frame.right-(blockLengh-offset),frame.bottom,frame.right+offset,frame.bottom+offset,mPaint);
        } else {
            canvas.drawRect(frame.left,frame.top,frame.left+blockLengh,frame.top+offset,mPaint);//左上
            canvas.drawRect(frame.left,frame.top+offset,frame.left+offset,frame.top+blockLengh,mPaint);
            canvas.drawRect(frame.right-blockLengh,frame.top,frame.right,frame.top+offset,mPaint);//右上;
            canvas.drawRect(frame.right-offset,frame.top+offset,frame.right,frame.top+blockLengh,mPaint);
            canvas.drawRect(frame.left,frame.bottom-blockLengh,frame.left+offset,frame.bottom-offset,mPaint);//左下;
            canvas.drawRect(frame.left,frame.bottom-offset,frame.left+blockLengh,frame.bottom,mPaint);
            canvas.drawRect(frame.right-offset,frame.bottom-blockLengh,frame.right,frame.bottom-offset,mPaint);//右下;
            canvas.drawRect(frame.right-blockLengh,frame.bottom-offset,frame.right,frame.bottom,mPaint);
        }
    }
}
