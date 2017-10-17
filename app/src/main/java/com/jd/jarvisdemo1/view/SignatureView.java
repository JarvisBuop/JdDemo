package com.jd.jarvisdemo1.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static android.graphics.Bitmap.createBitmap;


/**
 * Created by JarvisDong on 2017/10/17.
 * OverView:
 */

public class SignatureView extends View {

    private int mWidth;
    private int mHeight;
    private int mMeasureWidth;
    private int mMeasureHeight;
    private Bitmap bgBitmap;
    Canvas mCanvas;
    Path mPath;
    Paint mPaint;
    private Context mContext;

    public SignatureView(Context context) {
        this(context, null);
    }

    public SignatureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }


    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        mWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        mHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        Log.e("jarvis", mWidth + "/" + mHeight);
//        setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.timg1));
//        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.timg1).copy(Bitmap.Config.ARGB_8888, true);
//        bgBitmap = createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());

        bgBitmap = createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bgBitmap);
        mCanvas.drawColor(Color.WHITE);
        mPath = new Path();
        mPaint = new Paint();
        setPaint();
    }

    private void setPaint() {
        //设置画笔风格
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);//设置填充方式为描边
        mPaint.setStrokeJoin(Paint.Join.ROUND);//设置笔刷转弯处的连接风格
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置笔刷的图形样式(体现在线的端点上)
        mPaint.setStrokeWidth(30);//设置默认笔触的宽度为1像素
        mPaint.setAntiAlias(true);//设置抗锯齿效果
        mPaint.setDither(true);//使用抖动效果
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureWidth = getMeasuredWidth();
        mMeasureHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        mCanvas.drawPath(mPath, mPaint);
        canvas.drawCircle(200, 200, 200, mPaint);
    }

    float mLastX = 0;
    float mlastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float eventX = event.getX();
        float eventY = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = eventX;
                mlastY = eventY;
                mPath.moveTo(mLastX, mlastY);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(eventX - mLastX);
                float dy = Math.abs(eventY - mlastY);
                if (dx > 3 || dy > 3)
                    mPath.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                mPath.reset();
                break;
        }
        invalidate();
        return true;
    }


    //******************************************
    //*************外部方法;****************
    //******************************************
}
