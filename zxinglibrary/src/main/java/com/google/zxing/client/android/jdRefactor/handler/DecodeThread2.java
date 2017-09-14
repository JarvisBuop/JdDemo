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

package com.google.zxing.client.android.jdRefactor.handler;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.client.android.jdRefactor.codehelp.DecodeFormatManager2;
import com.google.zxing.client.android.jdRefactor.controller.JdCodeParams;
import com.google.zxing.client.android.jdRefactor.ui.act.CaptureActivity2;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.google.zxing.client.android.jdRefactor.controller.JdCodeParams.BARCODE_GOODS;
import static com.google.zxing.client.android.jdRefactor.controller.JdCodeParams.BARCODE_INDUSTRY;
import static com.google.zxing.client.android.jdRefactor.controller.JdCodeParams.DATAMATRIX;
import static com.google.zxing.client.android.jdRefactor.controller.JdCodeParams.PDF417;
import static com.google.zxing.client.android.jdRefactor.controller.JdCodeParams.QRCODE;

/**
 * This thread does all the heavy lifting of decoding the images.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class DecodeThread2 extends Thread {

  public static final String BARCODE_BITMAP = "barcode_bitmap";
  public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";

  private final CaptureActivity2 activity;
  private final Map<DecodeHintType,Object> hints;
  private Handler handler;
  private final CountDownLatch handlerInitLatch;

  public DecodeThread2(CaptureActivity2 activity,
                Collection<BarcodeFormat> decodeFormats,
                Map<DecodeHintType,?> baseHints,
                String characterSet,
                ResultPointCallback resultPointCallback) {

    this.activity = activity;
    handlerInitLatch = new CountDownLatch(1);

    hints = new EnumMap<>(DecodeHintType.class);
    if (baseHints != null) {
      hints.putAll(baseHints);
    }

    // The prefs can't change while the thread is running, so pick them up once here.
    if (decodeFormats == null || decodeFormats.isEmpty()) {

      decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
      if (BARCODE_GOODS) {
        decodeFormats.addAll(DecodeFormatManager2.PRODUCT_FORMATS);
      }
      if (BARCODE_INDUSTRY) {
        decodeFormats.addAll(DecodeFormatManager2.INDUSTRIAL_FORMATS);
      }
      if (QRCODE) {
        decodeFormats.addAll(DecodeFormatManager2.QR_CODE_FORMATS);
      }
      if (DATAMATRIX) {
        decodeFormats.addAll(DecodeFormatManager2.DATA_MATRIX_FORMATS);
      }
      if (JdCodeParams.AZTEC) {
        decodeFormats.addAll(DecodeFormatManager2.AZTEC_FORMATS);
      }
      if (PDF417) {
        decodeFormats.addAll(DecodeFormatManager2.PDF417_FORMATS);
      }
    }
    hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

    if (characterSet != null) {
      hints.put(DecodeHintType.CHARACTER_SET, characterSet);
    }
    hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
    Log.i("DecodeThread", "Hints: " + hints);
  }

  Handler getHandler() {
    try {
      handlerInitLatch.await();
    } catch (InterruptedException ie) {
      // continue?
    }
    return handler;
  }

  @Override
  public void run() {
    Looper.prepare();
    handler = new DecodeHandler2(activity, hints);
    handlerInitLatch.countDown();
    Looper.loop();
  }

}
