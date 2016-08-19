package com.ctao.gestureslock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Toast;

import com.ctao.gestureslock.utils.DisplayUtils;

/**
 * Created by A Miracle on 2016/3/29.
 */
public class NLockPatterCustomView extends NLockPatterBaseView{

    /**原始点Paint*/				private Paint mLocusRoundOriginalPaint;
    /**原始点大小*/				private float mOriginalSize;
    /**选中点Paint*/				private Paint mLocusRoundClickPaint;
    /**选中点大小*/				private float mClickSize;
    /**错误点Paint*/				private Paint mLocusRoundClickErrorPaint;
    /**错误点大小*/				private float mClickErrorSize;

    /**选中连线Paint*/			private Paint mLocusLinePaint;
    /**错误连线Paint*/			private Paint mLocusLineErrorPaint;

    public NLockPatterCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NLockPatterCustomView(Context context) {
        super(context);
    }

    public NLockPatterCustomView(Context context, AttributeSet attrs) {
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
        float tmpWidth = mLocusRoundOriginalPaint.getStrokeWidth();
        mLocusRoundOriginalPaint.setStrokeWidth(tmpWidth * (1 + p.index));
        canvas.drawArc(new RectF(p.x - mOriginalSize - p.index, p.y - mOriginalSize - p.index, p.x + mOriginalSize + p.index, p.y + mOriginalSize + p.index), 0, 360, true, mLocusRoundOriginalPaint);
        mLocusRoundOriginalPaint.setStrokeWidth(tmpWidth);
    }

    @Override
    protected void drawPointCheck(Canvas canvas, Point p) {
        float tmpWidth = mLocusRoundClickPaint.getStrokeWidth();
        mLocusRoundClickPaint.setStrokeWidth(tmpWidth * (1 + p.index));
        canvas.drawArc(new RectF(p.x - mClickSize - p.index, p.y - mClickSize - p.index, p.x + mClickSize + p.index, p.y + mClickSize + p.index), 0, 360, true, mLocusRoundClickPaint);
        mLocusRoundClickPaint.setStrokeWidth(tmpWidth);
    }

    @Override
    protected void drawPointCheckError(Canvas canvas, Point p) {
        float tmpWidth = mLocusRoundClickErrorPaint.getStrokeWidth();
        mLocusRoundClickErrorPaint.setStrokeWidth(tmpWidth * (1 + p.index));
        canvas.drawArc(new RectF(p.x - mClickErrorSize - p.index, p.y - mClickErrorSize - p.index, p.x + mClickErrorSize + p.index, p.y + mClickErrorSize + p.index), 0, 360, true, mLocusRoundClickErrorPaint);
        mLocusRoundClickErrorPaint.setStrokeWidth(tmpWidth);
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
