package com.google.zxing.client.android.jdRefactor.ui.viewfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;

import com.google.zxing.client.android.R;

/**
 * Created by JarvisDong on 2017/9/16.
 * 自定义绘制;
 * ...
 * 圆形界面;
 */

public class JdCircleDraw implements ViewFinderDraw {
    int offsetArc = 20;//偏移量;
    int offset = 6;//偏移量;
    int audioTop = 0;
    int audioBottom = 0;


    int rate = 2;//移动的比率;
    int moveOffset = 3;//移动距离;
    int middle = 0;//红线移动距离;
    private Context context;
    private int colorRound;

    @Override
    public void drawOutside(Context context, Canvas canvas, Rect frame, int cWidth, int cHeight, Paint mPaint) {
        this.context = context;
        canvas.drawRect(0, 0, cWidth, cHeight, mPaint);

        Paint circlePaint = new Paint();
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        circlePaint.setXfermode(xfermode);
        canvas.drawCircle(frame.centerX(), frame.centerY(), frame.width() / 2, circlePaint);

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
        //计算水平的距离;
        int radius = frame.width() / 2;
        double circleInner = 0;
        if (middle <= frame.top + frame.height() / 2) {
            circleInner = Math.sqrt(radius * radius - Math.pow((radius - (middle - frame.top)), 2));
        } else {
            circleInner = Math.sqrt(radius * radius - Math.pow(((middle - frame.top) - radius), 2));
        }
        canvas.drawRect((float) (frame.left + 2 + (radius - circleInner)), middle - 1, (float) (frame.right - 2 - (radius - circleInner)), middle + 2, mPaint);
    }

    private void drawOutCustom(Canvas canvas, Rect frame, int cWidth, int cHeight, Paint mPaint) {
        //画音量键
        drawAudioKey(canvas, frame);

        //画外圆;
        mPaint.setColor(colorRound);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(frame.centerX(), frame.centerY(), frame.width() / 2, mPaint);
        mPaint.setStyle(Paint.Style.FILL);

        //画外圆动画;
        drawdynamicCircle(canvas, frame);
    }

    private void drawdynamicCircle(Canvas canvas, Rect frame) {
        Paint mPaint = new Paint();
        mPaint.setColor(colorRound);
        mPaint.setStrokeWidth(offsetArc / 5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        Path path = new Path();
        double scale = (middle - frame.top) * 1.0f / frame.height();
        RectF rectF = new RectF(frame.left - offsetArc, frame.top - offsetArc, frame.right + offsetArc, frame.bottom + offsetArc);
        Log.e("jarvis angle", getStartSweep(scale) + "/" + getFinalSweep(scale));
        path.addArc(rectF, getStartSweep(scale), getFinalSweep(scale));
        canvas.drawPath(path, mPaint);
    }

    private boolean isHas = false;

    private float getStartSweep(double scale) {
        if (scale <= 1.0 / 3 && scale >= 0 && !isHas) {
            return 0f;
        } else {
            isHas = true;
            return (float) (360f * (scale - 1.0 / 3));
        }
    }

    private float getFinalSweep(double scale) {
        if (scale <= 1.0 / 3 && scale >= 0 && !isHas) {
            return (float) (360f * scale);
        } else {
            return 120f;
        }
    }

    private void drawAudioKey(Canvas canvas, Rect frame) {
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
        StaticLayout staticLayout = new StaticLayout(string, textPaint1, bitmapUp.getWidth() * 2, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, false);
        canvas.save();
        canvas.translate(2 * offset + bitmapUp.getWidth(), frame.top / 2 - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
        canvas.restore();
//        canvas.drawText(string, 2 * offset + bitmapUp.getWidth(), frame.top / 2 + rect.height() / 2, textPaint1);
    }
}
