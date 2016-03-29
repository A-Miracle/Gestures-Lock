package com.ctao.gestureslock.utils;

import android.content.Context;

/**
 * Created by A Miracle on 2016/3/15.
 */
public class DisplayUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int converDip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi / 160F;// 屏幕密度
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素)的单位 转成为dp
     */
    public static int converPx2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().densityDpi / 160F;// 屏幕密度
        return (int) (pxValue / scale + 0.5f);
    }
}
