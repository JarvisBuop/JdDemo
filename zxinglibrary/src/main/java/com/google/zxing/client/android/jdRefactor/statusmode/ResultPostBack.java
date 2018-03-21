package com.google.zxing.client.android.jdRefactor.statusmode;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by JarvisDong on 2017/9/16.
 * 回调数据;
 */

public class ResultPostBack implements Parcelable{
    public String text;
    public byte[] rawBytes;
    public int numBits;
    public ResultPoint[] resultPoints;
    public BarcodeFormat format;
    public Map<ResultMetadataType, Object> resultMetadata;
    public long timestamp;

    public Bitmap barcode;
    public float scaleFactor;

    public ResultPostBack(String text, byte[] rawBytes, int numBits, ResultPoint[] resultPoints, BarcodeFormat format, Map<ResultMetadataType, Object> resultMetadata, long timestamp, Bitmap barcode, float scaleFactor) {
        this.text = text;
        this.rawBytes = rawBytes;
        this.numBits = numBits;
        this.resultPoints = resultPoints;
        this.format = format;
        this.resultMetadata = resultMetadata;
        this.timestamp = timestamp;
        this.barcode = barcode;
        this.scaleFactor = scaleFactor;
    }


    protected ResultPostBack(Parcel in) {
        text = in.readString();
        rawBytes = in.createByteArray();
        numBits = in.readInt();
        timestamp = in.readLong();
        barcode = in.readParcelable(Bitmap.class.getClassLoader());
        scaleFactor = in.readFloat();
    }

    public static final Creator<ResultPostBack> CREATOR = new Creator<ResultPostBack>() {
        @Override
        public ResultPostBack createFromParcel(Parcel in) {
            return new ResultPostBack(in);
        }

        @Override
        public ResultPostBack[] newArray(int size) {
            return new ResultPostBack[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeByteArray(rawBytes);
        dest.writeInt(numBits);
        dest.writeLong(timestamp);
        dest.writeParcelable(barcode, flags);
        dest.writeFloat(scaleFactor);
    }


    @Override
    public String toString() {
        return "ResultPostBack{" +
                "text='" + text + '\'' +
                ", rawBytes=" + Arrays.toString(rawBytes) +
                ", numBits=" + numBits +
                ", resultPoints=" + Arrays.toString(resultPoints) +
                ", format=" + format +
                ", resultMetadata=" + resultMetadata +
                ", timestamp=" + timestamp +
                ", barcode=" + barcode +
                ", scaleFactor=" + scaleFactor +
                '}';
    }
}
