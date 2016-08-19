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

/**
 * Created by A Miracle on 2016/8/18.
 */
public class NLockSmallView extends NLockPatterBaseView {

    /**圆点初始状态时的图片*/			private Bitmap locus_round_original;
    /**圆点点击时的图片*/				private Bitmap locus_round_click;
    /**画笔*/						private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public NLockSmallView(Context context) {
        super(context);
        init();
    }

    public NLockSmallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NLockSmallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPasswordMinLength = 0;
        isTouch = false;
    }

    @Override
    protected void onInitCache() {
        locus_round_original = BitmapUtils.zoomBitmapSize(decodeBitmap(R.mipmap.small_circle_unselected), radius * 2, radius * 2);
        locus_round_click = BitmapUtils.zoomBitmapSize(decodeBitmap(R.mipmap.small_circle_selected), radius * 2, radius * 2);
    }

    private Bitmap decodeBitmap(int res) {
        return BitmapFactory.decodeResource(getResources(), res);
    }

    @Override
    protected void drawPointNormal(Canvas canvas, Point p) {
        canvas.drawBitmap(locus_round_original, p.x - locus_round_original.getWidth() / 2, p.y - locus_round_original.getHeight() / 2, mPaint);
    }

    @Override
    protected void drawPointCheck(Canvas canvas, Point p) {
        canvas.drawBitmap(locus_round_click, p.x - locus_round_click.getWidth() / 2, p.y - locus_round_click.getHeight() / 2, mPaint);
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
