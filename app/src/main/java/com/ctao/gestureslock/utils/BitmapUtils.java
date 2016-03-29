package com.ctao.gestureslock.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by A Miracle on 2016/3/14.
 */
public class BitmapUtils {

    /**
     * 缩放图片
     */
    public static Bitmap zoomBitmapSize(Bitmap bitmap, float width, float height) {
        if (width <= 0 || height <= 0) {
            return bitmap;
        }
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(width / bitmap.getWidth(), height / bitmap.getHeight());
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 缩放图片
     */
    public static Bitmap zoomBitmapScale(Bitmap bitmap, float sx, float sy) {
        if (sx <= 0 || sy <= 0) {
            return bitmap;
        }
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sx);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
