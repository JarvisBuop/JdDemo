package com.google.zxing.client.android.jdRefactor.controller;

import com.google.zxing.client.android.jdRefactor.ui.statusmode.FrontLightMode2;

/**
 * Created by JarvisDong on 2017/9/14.
 * OverView:
 */

public class JdCodeParams {
    //条码类型设置;
    public static boolean BARCODE_GOODS = true;//一维码-商品;
    public static boolean BARCODE_INDUSTRY = true;//一维码-工业;
    public static boolean QRCODE = true; //二维码
    public static boolean DATAMATRIX = true;
    public static boolean AZTEC = false;
    public static boolean PDF417 = false;//测试

    //扫描成功设置
    public static boolean PLAY_AUDIO = true;//是否播放声音;
    public static boolean PLAY_VIBRATOR = false;//是否震动
    public static boolean COPY_2_CLIPBOARD = true;// 复制到粘贴板
    public static boolean RETRIEVAL_MORE_INFO = true;//是否检索更多信息

    //扫描设置;
    public static FrontLightMode2 ISUSEFLASH = FrontLightMode2.OFF;//0:开;1:自动;2:关;
    public static boolean ISAUTO_FOCUS = true;//自动对焦
    public static boolean ISINVERT_COLOR = false;//反色;
    public static boolean ISMULTI_SCANMODE = false;//批量扫描模式;
    public static boolean NOT_AUTO_ROTATE = true;//不自动旋转

    //设备适配;
    public static boolean NOT_SUSTAIN_FOCUS = true;//不持续对焦;
    public static boolean NOT_EXPOSURE = true;//不曝光;
    public static boolean NOT_DISTANCE_MEASURE = true;//不使用距离测量;
    public static boolean NOT_BARCODE_MATCH = true;//不进行条形码场景匹配;

    //参数对象构造类;
    public static class Builder {

    }
}
