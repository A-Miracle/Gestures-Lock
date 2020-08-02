package a.miracle.lock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import a.miracle.lock.NLockBaseView;
import a.miracle.lock.R;
import a.miracle.lock.utils.DisplayUtils;


/**
 * Created by A Miracle on 2016/3/29.
 */
public class NLockCustomView extends NLockBaseView {

    /**原始点Paint*/				protected Paint mLocusRoundOriginalPaint;
    /**原始点大小*/				protected float mOriginalSize;
    /**选中点Paint*/				protected Paint mLocusRoundClickPaint;
    /**选中点大小*/				protected float mClickSize;
    /**错误点Paint*/				protected Paint mLocusRoundClickErrorPaint;
    /**错误点大小*/				protected float mClickErrorSize;

    /**文本TextPaint*/           protected TextPaint mTextPaint;
    /**文本*/			        protected String[] mTexts;

    public NLockCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NLockCustomView(Context context) {
        super(context);
    }

    public NLockCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onInitCache() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(DisplayUtils.sp2px(24));
        mTextPaint.density = getResources().getDisplayMetrics().density;

        mTexts = new String[]{
                "这", "是", "哪", "我", "是", "谁", "我", "在", "哪"
        };

        mLocusRoundOriginalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        /*mLocusRoundOriginalPaint.setStyle(Paint.Style.STROKE);
        mLocusRoundOriginalPaint.setColor(getResources().getColor(R.color.colorOriginal));*/
        mLocusRoundOriginalPaint.setStrokeWidth(DisplayUtils.dp2px(1.5f));
        mOriginalSize = DisplayUtils.dp2px(30);

        mLocusRoundClickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        /*mLocusRoundClickPaint.setStyle(Paint.Style.STROKE);
        mLocusRoundClickPaint.setColor(getResources().getColor(R.color.colorSelected));*/
        mLocusRoundClickPaint.setStrokeWidth(DisplayUtils.dp2px(1.5f));
        mClickSize = DisplayUtils.dp2px(30);

        mLocusRoundClickErrorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        /*mLocusRoundClickErrorPaint.setStyle(Paint.Style.STROKE);
        mLocusRoundClickErrorPaint.setColor(getResources().getColor(R.color.colorSelectedError));*/
        mLocusRoundClickErrorPaint.setStrokeWidth(DisplayUtils.dp2px(1.5f));
        mClickErrorSize = DisplayUtils.dp2px(30);

    }

    protected void drawText(Canvas canvas, Point p, int color) {
        String text = mTexts[p.index % mTexts.length];
        mTextPaint.setColor(color);
        float measureText = mTextPaint.measureText(text);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float measureHeight = fontMetrics.top + fontMetrics.bottom;
        canvas.drawText(text, p.x - measureText * 0.5f, p.y - measureHeight * 0.5f, mTextPaint);
    }

    @Override
    protected void drawPointNormal(Canvas canvas, Point p) {
        /*canvas.drawArc(new RectF(
                        p.x - mOriginalSize - p.index,
                        p.y - mOriginalSize - p.index,
                        p.x + mOriginalSize + p.index,
                        p.y + mOriginalSize + p.index),
                0, 360, true, mLocusRoundOriginalPaint);*/
        int color = getResources().getColor(R.color.colorOriginal);
        mLocusRoundOriginalPaint.setStyle(Paint.Style.STROKE);
        mLocusRoundOriginalPaint.setColor(color);
        canvas.drawCircle(p.x, p.y, mOriginalSize, mLocusRoundOriginalPaint);

        drawText(canvas, p, color);

        mLocusRoundOriginalPaint.setStyle(Paint.Style.FILL);
        mLocusRoundOriginalPaint.setColor(mixtureColor(color, 0.2f));
        canvas.drawCircle(p.x, p.y, mOriginalSize, mLocusRoundOriginalPaint);
    }

    @Override
    protected void drawPointCheck(Canvas canvas, Point p) {
        /*canvas.drawArc(new RectF(
                        p.x - mClickSize - p.index,
                        p.y - mClickSize - p.index,
                        p.x + mClickSize + p.index,
                        p.y + mClickSize + p.index),
                0, 360, true, mLocusRoundClickPaint);*/
        int color = getResources().getColor(R.color.colorSelected);
        mLocusRoundClickPaint.setStyle(Paint.Style.STROKE);
        mLocusRoundClickPaint.setColor(color);
        canvas.drawCircle(p.x, p.y, mClickSize, mLocusRoundClickPaint);

        drawText(canvas, p, color);

        mLocusRoundClickPaint.setStyle(Paint.Style.FILL);
        mLocusRoundClickPaint.setColor(mixtureColor(color, 0.2f));
        canvas.drawCircle(p.x, p.y, mClickSize, mLocusRoundClickPaint);
    }

    @Override
    protected void drawPointCheckError(Canvas canvas, Point p) {
        /*canvas.drawArc(new RectF(
                        p.x - mClickErrorSize - p.index,
                        p.y - mClickErrorSize - p.index,
                        p.x + mClickErrorSize + p.index,
                        p.y + mClickErrorSize + p.index),
                0, 360, true, mLocusRoundClickErrorPaint);*/
        int color = getResources().getColor(R.color.colorSelectedError);
        mLocusRoundClickErrorPaint.setStyle(Paint.Style.STROKE);
        mLocusRoundClickErrorPaint.setColor(color);
        canvas.drawCircle(p.x, p.y, mClickErrorSize, mLocusRoundClickErrorPaint);

        drawText(canvas, p, color);

        mLocusRoundClickErrorPaint.setStyle(Paint.Style.FILL);
        mLocusRoundClickErrorPaint.setColor(mixtureColor(color, 0.2f));
        canvas.drawCircle(p.x, p.y, mClickErrorSize, mLocusRoundClickErrorPaint);
    }

    @Override
    protected void drawLine(Canvas canvas, Point a, Point b) {
    }
}
