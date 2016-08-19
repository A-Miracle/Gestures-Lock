package com.ctao.gestureslock.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Toast;

import com.ctao.gestureslock.R;
import com.ctao.gestureslock.utils.BitmapUtils;


/**
 * 全部Bitmap类型手势宫格锁
 * Created by A Miracle on 2016/3/29.
 */
public class NLockPatterBitmapView extends NLockPatterBaseView{

    /**圆点初始状态时的图片*/			private Bitmap locus_round_original;
    /**圆点点击时的图片*/				private Bitmap locus_round_click;
    /**出错时圆点的图片*/				private Bitmap locus_round_click_error;
    /**正常状态下线的图片*/			private Bitmap locus_line;
    /**错误状态下的线的图片*/			private Bitmap locus_line_error;
    /**正常状态下线的半圆角图片*/		private Bitmap locus_line_semicircle;
    /**错误状态下线的半圆角图片*/		private Bitmap locus_line_semicircle_error;
    /**正常状态下线的移动方向*/			private Bitmap locus_arrow;
    /**正常状态下线的移动方向*/			private Bitmap locus_arrow_error;

    /**正常连线的透明度*/				private int lineAlpha = 250;
    /**错误连线的透明度*/				private int lineAlpha_error = 250;

    /**矩阵*/						private Matrix mMatrix = new Matrix();
    /**画笔*/						private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public NLockPatterBitmapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NLockPatterBitmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NLockPatterBitmapView(Context context) {
        super(context);
        init();
    }

    private void init() {
        ratio_r_spac = 0.7f;
    }

    @Override
    protected void onInitCache() {

        locus_round_original = BitmapUtils.zoomBitmapSize(BitmapFactory.decodeResource(getResources(), R.mipmap.circle_normal), radius * 2, radius * 2);
        locus_round_click =  BitmapUtils.zoomBitmapSize(BitmapFactory.decodeResource(getResources(), R.mipmap.circle_selected), radius * 2, radius * 2);
        locus_round_click_error =  BitmapUtils.zoomBitmapSize(BitmapFactory.decodeResource(getResources(), R.mipmap.circle_wrong), radius * 2, radius * 2);

        locus_line = BitmapUtils.zoomBitmapSize(BitmapFactory.decodeResource(getResources(), R.mipmap.line), radius / 3, radius / 3);
        locus_line_error =BitmapUtils.zoomBitmapSize(BitmapFactory.decodeResource(getResources(), R.mipmap.line_error), radius / 3, radius / 3);

        locus_line_semicircle = BitmapUtils.zoomBitmapSize(BitmapFactory.decodeResource(getResources(), R.mipmap.line_semicircle), radius / 4, radius / 4);
        locus_line_semicircle_error =BitmapUtils.zoomBitmapSize(BitmapFactory.decodeResource(getResources(), R.mipmap.line_semicircle_error), radius / 4, radius / 4);

        locus_arrow = BitmapUtils.zoomBitmapSize(BitmapFactory.decodeResource(getResources(), R.mipmap.arrow), radius, radius);
        locus_arrow_error = BitmapUtils.zoomBitmapSize(BitmapFactory.decodeResource(getResources(), R.mipmap.arrow_error), radius, radius);
    }

    @Override
    protected void drawPointNormal(Canvas canvas, Point p) {
        canvas.drawBitmap(locus_round_original, p.x - locus_round_original.getWidth()/2, p.y - locus_round_original.getHeight()/2, mPaint);
    }

    @Override
    protected void drawPointCheck(Canvas canvas, Point p) {
        canvas.drawBitmap(locus_round_click, p.x - locus_round_click.getWidth()/2, p.y - locus_round_click.getHeight()/2, mPaint);
    }

    @Override
    protected void drawPointCheckError(Canvas canvas, Point p) {
        canvas.drawBitmap(locus_round_click_error, p.x - locus_round_click_error.getWidth()/2, p.y - locus_round_click_error.getHeight()/2, mPaint);
    }

    @Override
    protected void drawLine(Canvas canvas, Point a, Point b) {
        float distance = (float) distance(a.x, a.y, b.x, b.y);
        float degrees = getDegrees(a, b);
        canvas.rotate(degrees, a.x, a.y);

        int tepAlpha = mPaint.getAlpha();
        if (a.state == Point.STATE_CHECK) {
            mPaint.setAlpha(lineAlpha);
            mMatrix.setScale(distance/ locus_line.getWidth(), 1);
            mMatrix.postTranslate(a.x, a.y - locus_line.getHeight() / 2.0f);
            canvas.drawBitmap(locus_line, mMatrix, mPaint);
            canvas.drawBitmap(locus_line_semicircle, a.x + distance - locus_line_semicircle.getWidth()/2, a.y - locus_line_semicircle.getHeight() / 2.0f, mPaint);
            canvas.drawBitmap(locus_arrow, a.x+radius/2, a.y - locus_arrow.getHeight() / 2.0f, mPaint);
        } else {
            mPaint.setAlpha(lineAlpha_error);
            mMatrix.setScale(distance/ locus_line_error.getWidth(), 1);
            mMatrix.postTranslate(a.x, a.y - locus_line_error.getHeight() / 2.0f);
            canvas.drawBitmap(locus_line_error, mMatrix, mPaint);
            canvas.drawBitmap(locus_line_semicircle_error, a.x + distance - locus_line_semicircle_error.getWidth()/2, a.y - locus_line_semicircle_error.getHeight() / 2.0f, mPaint);
            canvas.drawBitmap(locus_arrow_error, a.x+radius/2, a.y - locus_arrow.getHeight() / 2.0f, mPaint);
        }
        mPaint.setAlpha(tepAlpha);
        canvas.rotate(-degrees, a.x, a.y);
    }

    /** 两点间的距离 */
    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2) + Math.abs(y1 - y2) * Math.abs(y1 - y2));
    }
}
