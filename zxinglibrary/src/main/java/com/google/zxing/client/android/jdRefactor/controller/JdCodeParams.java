package com.google.zxing.client.android.jdRefactor.controller;

import com.google.zxing.client.android.jdRefactor.statusmode.FrontLightMode2;

import java.io.Serializable;

/**
 * Created by JarvisDong on 2017/9/14.
 * OverView: 扫描配置类
 */

public class JdCodeParams {
    //条码类型设置;
    public static boolean BARCODE_GOODS = true;//一维码-商品;
    public static boolean BARCODE_INDUSTRY = true;//一维码-工业;
    public static boolean QRCODE = true; //二维码
    public static boolean DATAMATRIX = false;
    public static boolean AZTEC = false;
    public static boolean PDF417 = false;//测试

    //扫描成功设置
    public static boolean PLAY_AUDIO = true;//是否播放声音;
    public static boolean PLAY_VIBRATOR = false;//是否震动
    public static boolean COPY_2_CLIPBOARD = true;// 复制到粘贴板
//    public static boolean RETRIEVAL_MORE_INFO = true;//是否检索更多信息

    //扫描设置;
    public static FrontLightMode2 ISUSEFLASH = FrontLightMode2.OFF;//0:开;1:自动;2:关;
    public static boolean ISAUTO_FOCUS = true;//自动对焦
    public static boolean ISINVERT_COLOR = false;//反色;
    public static boolean ISMULTI_SCANMODE = false;//批量扫描模式;
//    public static boolean NOT_AUTO_ROTATE = true;//不自动旋转(无用;)

    //设备适配;
    public static boolean NOT_SUSTAIN_FOCUS = true;//不持续对焦;
    public static boolean NOT_EXPOSURE = true;//不曝光;
    public static boolean NOT_DISTANCE_MEASURE = true;//不使用距离测量;
    public static boolean NOT_BARCODE_MATCH = true;//不进行条形码场景匹配;

    public static void resetParams() {
        BARCODE_GOODS = true;//一维码-商品;
        BARCODE_INDUSTRY = true;//一维码-工业;
        QRCODE = true; //二维码
        DATAMATRIX = false;
        AZTEC = false;
        PDF417 = false;//测试

        PLAY_AUDIO = true;//是否播放声音;
        PLAY_VIBRATOR = false;//是否震动
        COPY_2_CLIPBOARD = true;// 复制到粘贴板

        ISUSEFLASH = FrontLightMode2.OFF;//0:开;1:自动;2:关;
        ISAUTO_FOCUS = true;//自动对焦
        ISINVERT_COLOR = false;//反色;
        ISMULTI_SCANMODE = false;//批量扫描模式;

        //设备适配;
        NOT_SUSTAIN_FOCUS = true;//不持续对焦;
        NOT_EXPOSURE = true;//不曝光;
        NOT_DISTANCE_MEASURE = true;//不使用距离测量;
        NOT_BARCODE_MATCH = true;//不进行条形码场景匹配;
    }

    public static class ParamsBuilder implements Serializable{
        private boolean BARCODE_GOODS = true;//一维码-商品;
        private boolean BARCODE_INDUSTRY = true;//一维码-工业;
        private boolean QRCODE = true; //二维码
        private boolean DATAMATRIX = false;
        private boolean AZTEC = false;
        private boolean PDF417 = false;//测试

        //扫描成功设置
        private boolean PLAY_AUDIO = true;//是否播放声音;
        private boolean PLAY_VIBRATOR = false;//是否震动

        //扫描设置;
        private FrontLightMode2 ISUSEFLASH = FrontLightMode2.OFF;//0:开;1:自动;2:关;
        private boolean ISINVERT_COLOR = false;//反色;
        private boolean ISMULTI_SCANMODE = true;//批量扫描模式;

        //设备适配;
        private boolean NOT_SUSTAIN_FOCUS = true;//不持续对焦;
        private boolean NOT_EXPOSURE = true;//不曝光;
        private boolean NOT_DISTANCE_MEASURE = true;//不使用距离测量;
        private boolean NOT_BARCODE_MATCH = true;//不进行条形码场景匹配;

        public ParamsBuilder enableBarCode(boolean enable) {
            BARCODE_GOODS = enable;
            BARCODE_INDUSTRY = enable;
            return this;
        }

        public ParamsBuilder enableQrCode(boolean enable) {
            QRCODE = enable;
            return this;
        }

        public ParamsBuilder enableDataMatrix(boolean enable) {
            DATAMATRIX = enable;
            return this;
        }

        public ParamsBuilder enableAztec(boolean enable) {
            AZTEC = enable;
            return this;
        }

        public ParamsBuilder enablePdf417(boolean enable) {
            PDF417 = enable;
            return this;
        }

        public ParamsBuilder enablePlayaudio(boolean enable) {
            PLAY_AUDIO = enable;
            return this;
        }

        public ParamsBuilder enablePlayvibrator(boolean enable) {
            PLAY_VIBRATOR = enable;
            return this;
        }

        public ParamsBuilder enableIsuseflash(FrontLightMode2 mode2) {
            ISUSEFLASH = mode2;
            return this;
        }

        public ParamsBuilder enableIsinvertcolor(boolean enable) {
            ISINVERT_COLOR = enable;
            return this;
        }

        public ParamsBuilder enableIsmultiscanmode(boolean enable) {
            ISMULTI_SCANMODE = enable;
            return this;
        }

        public ParamsBuilder lowDevice(boolean isLow) {
            NOT_SUSTAIN_FOCUS = isLow;
            NOT_EXPOSURE = isLow;
            NOT_DISTANCE_MEASURE = isLow;
            NOT_BARCODE_MATCH = isLow;
            return this;
        }

        public void commit() {
            JdCodeParams.BARCODE_GOODS = this.BARCODE_GOODS;
            JdCodeParams.BARCODE_INDUSTRY = this.BARCODE_INDUSTRY;
            JdCodeParams.QRCODE = this.QRCODE;
            JdCodeParams.DATAMATRIX = this.DATAMATRIX;
            JdCodeParams.AZTEC = this.AZTEC;
            JdCodeParams.PDF417 = this.PDF417;

            JdCodeParams.PLAY_AUDIO = this.PLAY_AUDIO;
            JdCodeParams.PLAY_VIBRATOR = this.PLAY_VIBRATOR;

            JdCodeParams.ISUSEFLASH = this.ISUSEFLASH;
            JdCodeParams.ISINVERT_COLOR = this.ISINVERT_COLOR;
            JdCodeParams.ISMULTI_SCANMODE = this.ISMULTI_SCANMODE;

            JdCodeParams.NOT_SUSTAIN_FOCUS = this.NOT_SUSTAIN_FOCUS;
            JdCodeParams.NOT_EXPOSURE = this.NOT_EXPOSURE;
            JdCodeParams.NOT_DISTANCE_MEASURE = this.NOT_DISTANCE_MEASURE;
            JdCodeParams.NOT_BARCODE_MATCH = this.NOT_BARCODE_MATCH;
        }
    }
}
