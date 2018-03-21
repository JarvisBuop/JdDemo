package com.google.zxing.client.android.jdRefactor.ui.viewfinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by JarvisDong on 2017/9/16.
 * viewfindview 中绘制接口;
 */

public interface ViewFinderDraw {
    void drawOutside(Context context,Canvas canvas, Rect frame, int cWidth, int cHeight, Paint mPaint);

    void drawInside(Canvas canvas, Rect frame,Paint mPaint);
}
