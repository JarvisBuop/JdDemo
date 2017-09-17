/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.jdRefactor.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.clipboard.ClipboardInterface;
import com.google.zxing.client.android.jdRefactor.codehelp.AmbientLightManager2;
import com.google.zxing.client.android.jdRefactor.codehelp.BeepManager2;
import com.google.zxing.client.android.jdRefactor.codehelp.InactivityTimer2;
import com.google.zxing.client.android.jdRefactor.controller.JdCodeParams;
import com.google.zxing.client.android.jdRefactor.handler.CaptureActivityHandler2;
import com.google.zxing.client.android.jdRefactor.statusmode.ResultPostBack;
import com.google.zxing.client.android.jdRefactor.ui.viewfinder.JdDraw;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static com.google.zxing.client.android.jdRefactor.controller.JdCodeParams.COPY_2_CLIPBOARD;
import static com.google.zxing.client.android.jdRefactor.controller.JdCodeParams.ISMULTI_SCANMODE;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 * <p>
 * SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
 * <p>
 * <p>
 * tips:
 * 1.摄像头初始化失败注意6.0权限;
 * 2.条形码扫不到注意横屏;
 */
public final class CaptureActivity2 extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = CaptureActivity2.class.getSimpleName();
    public static final String ENTER_CODE_PARAMS = "ENTER_PARAMS";//自定义设置参数;
    public static final String REQUEST_CODE_LIST = "CAPTURE_LIST";//批量扫描;key
    public static final String REQUEST_CODE_META = "CAPTURE_META";//单次扫描;key
    private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

    private CameraManager cameraManager;
    private CaptureActivityHandler2 handler;
    private ViewfinderView viewfinderView;
    private TextView statusView;
    private Result lastResult;
    private boolean hasSurface;
    private boolean copyToClipboard;
    private Collection<BarcodeFormat> decodeFormats;//判断一维码是否商品还是工业;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    private InactivityTimer2 inactivityTimer2;
    private BeepManager2 beepManager;
    private AmbientLightManager2 ambientLightManager;

    private ImageView imageView;
    private ArrayList<ResultPostBack> mMultiResultList;


    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.capture_jd);

        hasSurface = false;
        inactivityTimer2 = new InactivityTimer2(this);
        beepManager = new BeepManager2(this);
        ambientLightManager = new AmbientLightManager2(this);
        initIntent();
    }

    private void initIntent() {
        Serializable serializableExtra = getIntent().getSerializableExtra(ENTER_CODE_PARAMS);
        if (serializableExtra != null && serializableExtra instanceof JdCodeParams.ParamsBuilder) {
            JdCodeParams.ParamsBuilder builder = (JdCodeParams.ParamsBuilder) serializableExtra;
            builder.commit();
        } else {
            JdCodeParams.resetParams();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());//相机管理类;

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);//二维码视图类;
        viewfinderView.setCameraManager(cameraManager);

        statusView = (TextView) findViewById(R.id.status_view);//底部文字;
        imageView = (ImageView) findViewById(R.id.image_multi);

        handler = null;
        lastResult = null;

        initParams();

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
        }

        initAct();
    }

    private void initAct() {
        imageView.setVisibility(ISMULTI_SCANMODE ? View.VISIBLE : View.GONE);
        if (ISMULTI_SCANMODE) {
            mMultiResultList = new ArrayList<>();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(REQUEST_CODE_LIST, mMultiResultList);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
        //更改样式;
        viewfinderView.setViewDraw(new JdDraw());
    }

    private void initParams() {
//        //不自动旋转;
//        if (JdCodeParams.NOT_AUTO_ROTATE) {
//            setRequestedOrientation(getCurrentOrientation());
//        } else {
////      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//        }

        resetStatusView();

        beepManager.updatePrefs();
        ambientLightManager.start(cameraManager);

        inactivityTimer2.onResume();

        Intent intent = getIntent();

        copyToClipboard = COPY_2_CLIPBOARD;

//        decodeFormats = DecodeFormatManager2.getAllFormat(null);//刚开始扫描肯定没有初始化;
        decodeFormats = null;//刚开始扫描肯定没有初始化;
        characterSet = "utf-8";
        decodeHints = null;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

//    private int getCurrentOrientation() {
//        int rotation = getWindowManager().getDefaultDisplay().getRotation();
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            switch (rotation) {
//                case Surface.ROTATION_0:
//                case Surface.ROTATION_90:
//                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//                default:
//                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
//            }
//        } else {
//            switch (rotation) {
//                case Surface.ROTATION_0:
//                case Surface.ROTATION_270:
//                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//                default:
//                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
//            }
//        }
//    }


    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer2.onPause();
        ambientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        //historyManager = null; // Keep for onActivityResult
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer2.shutdown();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                break;
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                // Handle these events so they don't launch the Camera app
                return true;
            // Use volume up/down to turn on light
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                cameraManager.setTorch(false);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                cameraManager.setTorch(true);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     * 解码成功回调的方法
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer2.onActivity();
        lastResult = rawResult;

        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {
            // Then not from history, so beep/vibrate and we have an image to draw on
            beepManager.playBeepSoundAndVibrate();
            drawResultPoints(barcode, scaleFactor, rawResult);
        }

        //  TODO: 2017/9/15 批量扫描模式;
        if (fromLiveScan && ISMULTI_SCANMODE) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.msg_bulk_mode_scanned)/* + " (" + rawResult.getText() + ')'*/,
                    Toast.LENGTH_SHORT).show();
            //TransactionTooLargeException 防止数据过大;
            mMultiResultList.add(new ResultPostBack(rawResult.getText(), null, rawResult.getNumBits(), null,
                    rawResult.getBarcodeFormat(), null, rawResult.getTimestamp(), null, scaleFactor));
            // Wait a moment or else it will scan the same barcode continuously about 3 times
            restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
        }
        // TODO: 2017/9/15 复制到粘贴板;
        if (rawResult != null) maybeSetClipboard(rawResult.getText());

        //TODO 回调处理;
        if (!ISMULTI_SCANMODE) {
            Intent intent = new Intent();
            intent.putExtra(REQUEST_CODE_META, new ResultPostBack(rawResult.getText(), rawResult.getRawBytes(), rawResult.getNumBits(), rawResult.getResultPoints(),
                    rawResult.getBarcodeFormat(), rawResult.getResultMetadata(), rawResult.getTimestamp(), barcode, scaleFactor));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * Superimpose a line for 1D or dots for 2D to highlight the key features of the barcode.
     *
     * @param barcode     A bitmap of the captured image.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param rawResult   The decoded results which contains the points to draw.
     */
    private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.result_points));
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
            } else if (points.length == 4 &&
                    (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A ||
                            rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                // Hacky special case -- draw two lines, for the barcode and metadata
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
                drawLine(canvas, paint, points[2], points[3], scaleFactor);
            } else {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points) {
                    if (point != null) {
                        canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
                    }
                }
            }
        }
    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
        if (a != null && b != null) {
            canvas.drawLine(scaleFactor * a.getX(),
                    scaleFactor * a.getY(),
                    scaleFactor * b.getX(),
                    scaleFactor * b.getY(),
                    paint);
        }
    }

    private void maybeSetClipboard(CharSequence content) {
        if (copyToClipboard && !TextUtils.isEmpty(content)) {
            ClipboardInterface.setText(content, this);
        }
    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                //此方法开始扫描;
                handler = new CaptureActivityHandler2(this, decodeFormats, decodeHints, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    //相机初始化失败,弹框;
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    //重新启动扫码功能;
    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
        statusView.setText(R.string.msg_default_status);
        statusView.setVisibility(View.VISIBLE);
        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }
}
