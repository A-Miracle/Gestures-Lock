package com.ctao.gestureslock.widget.history;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.ctao.gestureslock.utils.DisplayUtils;
import com.ctao.gestureslock.widget.base.NLockBaseView;

/**
 * 全部Color类型手势宫格锁
 * Created by A Miracle on 2016/3/29.
 */
public class NLockColorView extends NLockBaseView {

    /**原始点Paint*/				private Paint mLocusRoundOriginalPaint;
    /**原始点大小*/				private float mOriginalSize;
    /**选中点Paint*/				private Paint mLocusRoundClickPaint;
    /**选中点大小*/				private float mClickSize;
    /**错误点Paint*/				private Paint mLocusRoundClickErrorPaint;
    /**错误点大小*/				private float mClickErrorSize;

    /**选中连线Paint*/			private Paint mLocusLinePaint;
    /**错误连线Paint*/			private Paint mLocusLineErrorPaint;

    public NLockColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NLockColorView(Context context) {
        super(context);
    }

    public NLockColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onInitCache() {

        mLocusRoundOriginalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLocusRoundOriginalPaint.setStyle(Paint.Style.STROKE);
        mLocusRoundOriginalPaint.setColor(Color.WHITE);
        mLocusRoundOriginalPaint.setStrokeWidth(DisplayUtils.converDip2px(getContext(), 1.5f));
        mOriginalSize = DisplayUtils.converDip2px(getContext(), 3);

        mLocusRoundClickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLocusRoundClickPaint.setStyle(Paint.Style.STROKE);
        mLocusRoundClickPaint.setColor(Color.BLUE);
        mLocusRoundClickPaint.setStrokeWidth(DisplayUtils.converDip2px(getContext(), 1.5f));
        mClickSize = DisplayUtils.converDip2px(getContext(), 17);

        mLocusRoundClickErrorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLocusRoundClickErrorPaint.setStyle(Paint.Style.STROKE);
        mLocusRoundClickErrorPaint.setColor(Color.RED);
        mLocusRoundClickErrorPaint.setStrokeWidth(DisplayUtils.converDip2px(getContext(), 2));
        mClickErrorSize = DisplayUtils.converDip2px(getContext(), 17);

        mLocusLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLocusLinePaint.setColor(Color.BLUE);
        mLocusLinePaint.setStrokeWidth(DisplayUtils.converDip2px(getContext(), 1.5f));
        mLocusLinePaint.setStrokeCap(Paint.Cap.ROUND);

        mLocusLineErrorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLocusLineErrorPaint.setColor(Color.RED);
        mLocusLineErrorPaint.setStrokeWidth(DisplayUtils.converDip2px(getContext(), 1.5f));
        mLocusLineErrorPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void drawPointNormal(Canvas canvas, Point p) {
        canvas.drawArc(new RectF(p.x-mOriginalSize, p.y-mOriginalSize, p.x+mOriginalSize, p.y+mOriginalSize), 0, 360, true, mLocusRoundOriginalPaint);
    }

    @Override
    protected void drawPointCheck(Canvas canvas, Point p) {
        canvas.drawArc(new RectF(p.x-mClickSize, p.y-mClickSize, p.x+mClickSize, p.y+mClickSize), 0, 360, true, mLocusRoundClickPaint);
    }

    @Override
    protected void drawPointCheckError(Canvas canvas, Point p) {
        canvas.drawArc(new RectF(p.x-mClickErrorSize, p.y-mClickErrorSize, p.x+mClickErrorSize, p.y+mClickErrorSize), 0, 360, true, mLocusRoundClickErrorPaint);
    }

    @Override
    protected void drawLine(Canvas canvas, Point a, Point b) {
        if (a.state == Point.STATE_CHECK) {
            canvas.drawLine(a.x, a.y, b.x, b.y, mLocusLinePaint);
        } else {
            canvas.drawLine(a.x, a.y, b.x, b.y, mLocusLineErrorPaint);
        }
    }
}
