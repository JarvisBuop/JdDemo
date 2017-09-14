package com.google.zxing.client.android.jdRefactor.controller;

/**
 * Created by JarvisDong on 2017/9/14.
 * OverView:
 */

public class JdCodeParams {
    //条码类型设置;
    public static final boolean BARCODE_GOODS = true;//一维码-商品;
    public static final boolean BARCODE_INDUSTRY = true;//一维码-工业;
    public static final boolean QRCODE = true; //二维码
    public static final boolean DATAMATRIX = true;
    public static final boolean AZTEC = false;
    public static final boolean PDF417 = false;//测试

    //扫描成功设置
    public static final boolean PLAY_AUDIO = true;//是否播放声音;
    public static final boolean PLAY_VIBRATOR = false;//是否震动
    public static final boolean COPY_2_CLIPBOARD = true;// 复制到粘贴板
    public static final boolean RETRIEVAL_MORE_INFO = true;//是否检索更多信息

    //扫描设置;
    public static final String ISUSEFLASH = "2";//0:开;1:自动;2:关;
    public static final boolean ISAUTO_FOCUS = true;//自动对焦
    public static final boolean ISINVERT_COLOR = false;//反色;
    public static final boolean ISMULTI_SCANMODE = false;//批量扫描模式;
    public static final boolean NOT_AUTO_ROTATE = true;//不自动旋转

    //设备适配;
    public static final boolean NOT_SUSTAIN_FOCUS = true;//不持续对焦;
    public static final boolean NOT_EXPOSURE = true;//不曝光;
    public static final boolean NOT_DISTANCE_MEASURE = true;//不使用距离测量;
    public static final boolean NOT_BARCODE_MATCH = true;//不进行条形码场景匹配;

    //参数对象构造类;
    public static class Builder {


    }
}
