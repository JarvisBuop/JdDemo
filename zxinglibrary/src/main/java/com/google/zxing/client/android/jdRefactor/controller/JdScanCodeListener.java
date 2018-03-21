package com.google.zxing.client.android.jdRefactor.controller;

import android.graphics.Bitmap;

import com.google.zxing.Result;

/**
 * Created by JarvisDong on 2017/9/15.
 * OverView: 扫描回调;
 */

public interface JdScanCodeListener {
    /**
     * @param rawResult 扫码信息;
     * @param barcode 预览图;
     * @param scaleFactor 缩放因子;
     */
    void scanPostBack(Result rawResult, Bitmap barcode, float scaleFactor);

}
