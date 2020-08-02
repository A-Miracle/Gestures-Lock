package a.miracle.lock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import a.miracle.lock.NLockBaseView;
import a.miracle.lock.R;
import a.miracle.lock.utils.DisplayUtils;

/**
 * 全部Color类型手势宫格锁
 * Created by A Miracle on 2016/3/29.
 */
public class NLockColorView extends NLockBaseView {

    /**原始点Paint*/				protected Paint mLocusRoundOriginalPaint;
    /**原始点大小*/				protected float mOriginalSize;
    /**选中点Paint*/				protected Paint mLocusRoundClickPaint;
    /**选中点大小*/				protected float mClickSize;
    /**错误点Paint*/				protected Paint mLocusRoundClickErrorPaint;
    /**错误点大小*/				protected float mClickErrorSize;

    /**选中连线Paint*/			protected Paint mLocusLinePaint;
    /**错误连线Paint*/			protected Paint mLocusLineErrorPaint;

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
        mLocusRoundOriginalPaint.setStyle(Paint.Style.FILL);
        mLocusRoundOriginalPaint.setColor(getResources().getColor(R.color.colorOriginal));
        mOriginalSize = DisplayUtils.dp2px(5);

        mLocusRoundClickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        /*mLocusRoundClickPaint.setStyle(Paint.Style.FILL);
        mLocusRoundClickPaint.setColor(getResources().getColor(R.color.colorSelected));*/
        mLocusRoundClickPaint.setStrokeWidth(DisplayUtils.dp2px(20));
        mClickSize = DisplayUtils.dp2px(10);

        mLocusRoundClickErrorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        /*mLocusRoundClickErrorPaint.setStyle(Paint.Style.FILL);
        mLocusRoundClickErrorPaint.setColor(getResources().getColor(R.color.colorSelectedError));*/
        mLocusRoundClickErrorPaint.setStrokeWidth(DisplayUtils.dp2px(20));
        mClickErrorSize = DisplayUtils.dp2px(10);

        mLocusLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLocusLinePaint.setStyle(Paint.Style.STROKE);
        mLocusLinePaint.setColor(getResources().getColor(R.color.colorSelected));
        mLocusLinePaint.setStrokeWidth(DisplayUtils.dp2px(2));
        mLocusLinePaint.setStrokeCap(Paint.Cap.ROUND);

        mLocusLineErrorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLocusLineErrorPaint.setStyle(Paint.Style.STROKE);
        mLocusLineErrorPaint.setColor(getResources().getColor(R.color.colorSelectedError));
        mLocusLineErrorPaint.setStrokeWidth(DisplayUtils.dp2px(2));
        mLocusLineErrorPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    @Override
    protected void drawPointNormal(Canvas canvas, Point p) {
        /*canvas.drawArc(new RectF(
                        p.x - mOriginalSize,
                        p.y - mOriginalSize,
                        p.x + mOriginalSize,
                        p.y + mOriginalSize),
                0, 360,
                true, mLocusRoundOriginalPaint);*/
        canvas.drawCircle(p.x, p.y, mOriginalSize, mLocusRoundOriginalPaint);
    }

    @Override
    protected void drawPointCheck(Canvas canvas, Point p) {
        /*canvas.drawArc(new RectF(
                        p.x - mClickSize,
                        p.y - mClickSize,
                        p.x + mClickSize,
                        p.y + mClickSize),
                0, 360,
                true, mLocusRoundClickPaint);*/

        mLocusRoundClickPaint.setStyle(Paint.Style.FILL);
        mLocusRoundClickPaint.setColor(getResources().getColor(R.color.colorSelected));
        canvas.drawCircle(p.x, p.y, mClickSize, mLocusRoundClickPaint);

        mLocusRoundClickPaint.setStyle(Paint.Style.STROKE);
        mLocusRoundClickPaint.setColor(mixtureColor(getResources().getColor(R.color.colorSelected), 0.2f));
        canvas.drawCircle(p.x, p.y, mClickSize + mLocusRoundClickPaint.getStrokeWidth() * 0.5f, mLocusRoundClickPaint);
    }

    @Override
    protected void drawPointCheckError(Canvas canvas, Point p) {
        /*canvas.drawArc(new RectF(
                        p.x - mClickErrorSize,
                        p.y - mClickErrorSize,
                        p.x + mClickErrorSize,
                        p.y + mClickErrorSize),
                0, 360,
                true, mLocusRoundClickErrorPaint);*/

        mLocusRoundClickErrorPaint.setStyle(Paint.Style.FILL);
        mLocusRoundClickErrorPaint.setColor(getResources().getColor(R.color.colorSelectedError));
        canvas.drawCircle(p.x, p.y, mClickErrorSize, mLocusRoundClickErrorPaint);

        mLocusRoundClickErrorPaint.setStyle(Paint.Style.STROKE);
        mLocusRoundClickErrorPaint.setColor(mixtureColor(getResources().getColor(R.color.colorSelectedError), 0.2f));
        canvas.drawCircle(p.x, p.y, mClickErrorSize + mLocusRoundClickErrorPaint.getStrokeWidth() * 0.5f, mLocusRoundClickErrorPaint);
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
