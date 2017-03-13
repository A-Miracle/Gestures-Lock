package com.ctao.gestureslock.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.ctao.gestureslock.R;
import com.ctao.gestureslock.utils.BitmapUtils;
import com.ctao.gestureslock.widget.base.NLockBaseView;

/**
 * Created by A Miracle on 2016/8/18.
 */
public class NLockSmallView extends NLockBaseView {

    /**圆点初始状态时的图片*/			private Bitmap mLocusNormal;
    /**圆点点击时的图片*/				private Bitmap mLocusSelected;
    /**画笔*/						private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public NLockSmallView(Context context) {
        super(context);
    }

    public NLockSmallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NLockSmallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInit() {
        mPasswordMinLength = 0;
        isTouch = false;
    }

    @Override
    protected void onInitCache() {
        mLocusNormal = BitmapUtils.zoomBitmapSize(decodeBitmap(R.mipmap.small_circle_unselected), mRadius * 2, mRadius * 2);
        mLocusSelected = BitmapUtils.zoomBitmapSize(decodeBitmap(R.mipmap.small_circle_selected), mRadius * 2, mRadius * 2);
    }

    private Bitmap decodeBitmap(int res) {
        return BitmapFactory.decodeResource(getResources(), res);
    }

    @Override
    protected void drawPointNormal(Canvas canvas, Point p) {
        canvas.drawBitmap(mLocusNormal, p.x - mLocusNormal.getWidth() / 2, p.y - mLocusNormal.getHeight() / 2, mPaint);
    }

    @Override
    protected void drawPointCheck(Canvas canvas, Point p) {
        canvas.drawBitmap(mLocusSelected, p.x - mLocusSelected.getWidth() / 2, p.y - mLocusSelected.getHeight() / 2, mPaint);
    }

    @Override
    protected void drawPointCheckError(Canvas canvas, Point p) {
    }

    @Override
    protected void drawLine(Canvas canvas, Point a, Point b) {
    }

    /**设置点的样式*/
    public void setPassword(String password){
        for (Point[] points : mPoints) {
            for (Point p : points) {
                p.state = Point.STATE_NORMAL;
            }
        }
        if(!TextUtils.isEmpty(password)){
            String[] split = password.split(",");
            for (String s : split) {
                int i = Integer.parseInt(s);
                if(i < 3){
                    mPoints[0][i].state = Point.STATE_CHECK;
                }else if(i < 6){
                    mPoints[1][i-3].state = Point.STATE_CHECK;
                }else{
                    mPoints[2][i-6].state = Point.STATE_CHECK;
                }
            }
        }
        postInvalidate();
    }
}
