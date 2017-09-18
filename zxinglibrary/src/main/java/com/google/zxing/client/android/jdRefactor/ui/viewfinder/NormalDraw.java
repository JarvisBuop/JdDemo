package com.google.zxing.client.android.jdRefactor.ui.viewfinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by JarvisDong on 2017/9/16.
 * 默认模式下的绘制;
 */

public class NormalDraw implements ViewFinderDraw {
    @Override
    public void drawOutside(Context context,Canvas canvas, Rect frame, int cWidth, int cHeight, Paint mPaint) {
        //画上下左右的遮罩层;
        canvas.drawRect(0, 0, cWidth, frame.top, mPaint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, mPaint);
        canvas.drawRect(frame.right + 1, frame.top, cWidth, frame.bottom + 1, mPaint);
        canvas.drawRect(0, frame.bottom + 1, cWidth, cHeight, mPaint);

    }

    @Override
    public void drawInside(Canvas canvas, Rect frame, Paint mPaint) {
        int middle = frame.height() / 2 + frame.top;
        canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, mPaint);
    }
}
