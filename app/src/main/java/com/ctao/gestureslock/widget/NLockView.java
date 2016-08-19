package com.ctao.gestureslock.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;

import com.ctao.gestureslock.R;
import com.ctao.gestureslock.utils.BitmapUtils;

/**
 * Created by A Miracle on 2016/8/18.
 */
public class NLockView extends NLockPatterBaseView {

    /**圆点初始状态时的图片*/			private Bitmap locus_round_original;
    /**圆点点击时的图片*/				private Bitmap locus_round_click;
    /**出错时圆点的图片*/				private Bitmap locus_round_click_error;
    /**画笔*/						private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public NLockView(Context context) {
        super(context);
    }

    public NLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onInitCache() {
        locus_round_original = BitmapUtils.zoomBitmapSize(decodeBitmap(R.mipmap.circle_normal), radius * 2, radius * 2);
        locus_round_click = BitmapUtils.zoomBitmapSize(decodeBitmap(R.mipmap.circle_selected), radius * 2, radius * 2);
        locus_round_click_error = BitmapUtils.zoomBitmapSize(decodeBitmap(R.mipmap.circle_wrong), radius * 2, radius * 2);
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
        clearCanvas(canvas, p);
        canvas.drawBitmap(locus_round_click, p.x - locus_round_click.getWidth() / 2, p.y - locus_round_click.getHeight() / 2, mPaint);

    }

    @Override
    protected void drawPointCheckError(Canvas canvas, Point p) {
        clearCanvas(canvas, p);
        canvas.drawBitmap(locus_round_click_error, p.x - locus_round_click_error.getWidth() / 2, p.y - locus_round_click_error.getHeight() / 2, mPaint);

    }

    @Override
    protected void drawLine(Canvas canvas, Point a, Point b) {
        int tepColor = mPaint.getColor();
        //简单画线
        if (a.state == Point.STATE_CHECK) {
            mPaint.setColor(Color.parseColor("#009bf1"));
        } else {
            mPaint.setColor(Color.parseColor("#ff3200"));
        }
        canvas.drawLine(a.x, a.y, b.x, b.y, mPaint);
        mPaint.setColor(tepColor);
    }

    private void clearCanvas(Canvas canvas, Point p) {
        // 将点下面的线清除(前提是父容器必须有设置背景)
        Xfermode xfermode = mPaint.getXfermode();
        int color = mPaint.getColor();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setColor(Color.TRANSPARENT);
        canvas.drawCircle(p.x, p.y, radius, mPaint);
        mPaint.setXfermode(xfermode);
        mPaint.setColor(color);
    }
}
