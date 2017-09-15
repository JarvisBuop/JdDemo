package com.google.zxing.client.android.jdRefactor.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by JarvisDong on 2017/9/15.
 * OverView: 生成or识别二维码类;
 * 1.文字->图片;
 * 2.图片->文字;
 */

public class JdEncodeBuilder {

    //************************************************
    //***************通用多功能生成和识别码图*********
    //************************************************

    /**
     * 生成对应的码图,多适配;
     * String ->bitmap
     * AZTEC,
     * CODABAR,
     * CODE_39,
     * CODE_93,
     * CODE_128,
     * DATA_MATRIX,
     * EAN_8,
     * EAN_13,
     * ITF,
     * MAXICODE,
     * PDF_417,
     * QR_CODE,
     * RSS_14,
     * RSS_EXPANDED,
     * UPC_A,
     * UPC_E,
     * UPC_EAN_EXTENSION;
     */
    public static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int widthtar, int heighttar) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(contentsToEncode, format, widthtar, heighttar, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    /**
     * 解析本地码
     * bitmap ->String
     * AZTEC,
     * CODABAR,
     * CODE_39,
     * CODE_93,
     * CODE_128,
     * DATA_MATRIX,
     * EAN_8,
     * EAN_13,
     * ITF,
     * MAXICODE,
     * PDF_417,
     * QR_CODE,
     * RSS_14,
     * RSS_EXPANDED,
     * UPC_A,
     * UPC_E,
     * UPC_EAN_EXTENSION;
     *
     * @param contents
     * @param format
     * @return
     * @throws WriterException
     */
    public static Result decodeFromBitmap(Bitmap contents, BarcodeFormat format) throws ReaderException{
        if (contents == null) {
            return null;
        }
        int bitmapH = contents.getHeight();
        int bitmapW = contents.getWidth();
        byte[] yuv420sp = getYUV420sp(bitmapW, bitmapH, contents);
        if(yuv420sp == null)  return null;

        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(yuv420sp, bitmapW, bitmapH, 0, 0, bitmapW, bitmapH, false);
        Result decode = null;
        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                decode = new MultiFormatReader().decode(bitmap, hints);
            } catch (ReaderException re) {
                // continue
            }
        }
        return decode;
    }

    //**********************************************************
    //*****************转换工具************************
    //**********************************************************

    public static Bitmap otherToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        // 白色底色   应对透明图
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, w, h, paint);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap otherToBitmap(Context context, int drawable) {
        Drawable drawable1 = context.getResources().getDrawable(drawable);
        return otherToBitmap(drawable1);
    }

    /**
     * YUV420sp
     *
     * @param inputWidth
     * @param inputHeight
     * @param scaled
     * @return
     */
    public static byte[] getYUV420sp(int inputWidth, int inputHeight,
                                     Bitmap scaled) {
        if(!isBmRecycled(scaled)) return null;
        int[] argb = new int[inputWidth * inputHeight];

        scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);

        byte[] yuv = new byte[inputWidth * inputHeight * 3 / 2];

        encodeYUV420SP(yuv, argb, inputWidth, inputHeight);

        scaled.recycle();

        return yuv;
    }

    private static boolean isBmRecycled(Bitmap bitmap){
        if(bitmap!=null && !bitmap.isRecycled()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * RGB转YUV420sp
     *
     * @param yuv420sp inputWidth * inputHeight * 3 / 2
     * @param argb     inputWidth * inputHeight
     * @param width
     * @param height
     */
    private static void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width,
                                       int height) {
        // 帧图片的像素大小
        final int frameSize = width * height;
        // ---YUV数据---
        int Y, U, V;
        // Y的index从0开始
        int yIndex = 0;
        // UV的index从frameSize开始
        int uvIndex = frameSize;

        // ---颜色数据---
//      int a, R, G, B;
        int R, G, B;
        //
        int argbIndex = 0;
        //

        // ---循环所有像素点，RGB转YUV---
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                // a is not used obviously
//              a = (argb[argbIndex] & 0xff000000) >> 24;
                R = (argb[argbIndex] & 0xff0000) >> 16;
                G = (argb[argbIndex] & 0xff00) >> 8;
                B = (argb[argbIndex] & 0xff);
                //
                argbIndex++;

                // well known RGB to YUV algorithm
                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;

                //
                Y = Math.max(0, Math.min(Y, 255));
                U = Math.max(0, Math.min(U, 255));
                V = Math.max(0, Math.min(V, 255));

                // NV21 has a plane of Y and interleaved planes of VU each
                // sampled by a factor of 2
                // meaning for every 4 Y pixels there are 1 V and 1 U. Note the
                // sampling is every other
                // pixel AND every other scanline.
                // ---Y---
                yuv420sp[yIndex++] = (byte) Y;
                // ---UV---
                if ((j % 2 == 0) && (i % 2 == 0)) {
                    //
                    yuv420sp[uvIndex++] = (byte) V;
                    //
                    yuv420sp[uvIndex++] = (byte) U;
                }
            }
        }
    }


    //**************************************************
    //****************二维码生成和识别工具**************
    //**************************************************
    ///生成二维码图;
    public static Bitmap encodeStr2QRBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    //识别二维码图;
    public static Result decodeQRBitmap2Str(Bitmap bitmap) {
        QRCodeReader qrCodeReader = new QRCodeReader();
        byte[] yuv420sp = getYUV420sp(bitmap.getWidth(), bitmap.getHeight(), bitmap);
        if(yuv420sp == null) return null;
        Result decode = null;
        try {
            Map<DecodeHintType, String> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(yuv420sp,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    0, 0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    false);
            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
            decode = qrCodeReader.decode(bitmap1, hints);
        } catch (ReaderException e) {

        }
        return decode;
    }

    public static Bitmap getBitmap(ImageView imageView){
        return ((BitmapDrawable) ((ImageView) imageView).getDrawable()).getBitmap();
    }
}
