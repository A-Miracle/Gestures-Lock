package a.miracle.lock.widget;

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

import a.miracle.lock.NLockBaseView;
import a.miracle.lock.R;
import a.miracle.lock.utils.BitmapUtils;
import a.miracle.lock.utils.DisplayUtils;

/**
 * Created by A Miracle on 2016/8/18.
 */
public class NLockView extends NLockBaseView {

    /**圆点初始状态时的图片*/			protected Bitmap mLocusNormal;
    /**圆点点击时的图片*/				protected Bitmap mLocusSelected;
    /**出错时圆点的图片*/				protected Bitmap mLocusError;
    /**画笔*/						protected Paint mPaint;

    public NLockView(Context context) {
        super(context);
    }

    public NLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onInitCache() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(DisplayUtils.dp2px(1.5f));

        mLocusNormal = BitmapUtils.zoomBitmapSize(
                decodeBitmap(R.mipmap.circle_normal), mRadius * 2, mRadius * 2);
        mLocusSelected = BitmapUtils.zoomBitmapSize(
                decodeBitmap(R.mipmap.circle_selected), mRadius * 2, mRadius * 2);
        mLocusError = BitmapUtils.zoomBitmapSize(
                decodeBitmap(R.mipmap.circle_wrong), mRadius * 2, mRadius * 2);
    }

    protected Bitmap decodeBitmap(int res) {
        return BitmapFactory.decodeResource(getResources(), res);
    }

    @Override
    protected void drawPointNormal(Canvas canvas, Point p) {
        canvas.drawBitmap(mLocusNormal,
                p.x - mLocusNormal.getWidth() * 0.5f,
                p.y - mLocusNormal.getHeight() * 0.5f, mPaint);
    }

    @Override
    protected void drawPointCheck(Canvas canvas, Point p) {
        clearCanvas(canvas, p);
        canvas.drawBitmap(mLocusSelected,
                p.x - mLocusSelected.getWidth() * 0.5f,
                p.y - mLocusSelected.getHeight() * 0.5f, mPaint);

    }

    @Override
    protected void drawPointCheckError(Canvas canvas, Point p) {
        clearCanvas(canvas, p);
        canvas.drawBitmap(mLocusError,
                p.x - mLocusError.getWidth() * 0.5f,
                p.y - mLocusError.getHeight() * 0.5f, mPaint);

    }

    @Override
    protected void drawLine(Canvas canvas, Point a, Point b) {
        int tepColor = mPaint.getColor();
        // 简单画线
        if (a.state == Point.STATE_CHECK) {
            mPaint.setColor(Color.parseColor("#FF009BF1"));
        } else {
            mPaint.setColor(Color.parseColor("#FFFF3200"));
        }
        canvas.drawLine(a.x, a.y, b.x, b.y, mPaint);
        mPaint.setColor(tepColor);
    }

    protected void clearCanvas(Canvas canvas, Point p) {
        // TODO 这里本想将点下面的线清除(奈何却是黑底)
        // 欢迎大神给出指导
        canvas.save();
        Xfermode xfermode = mPaint.getXfermode();
        int color = mPaint.getColor();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setColor(Color.TRANSPARENT);
        canvas.drawCircle(p.x, p.y, mRadius, mPaint);
        mPaint.setXfermode(xfermode);
        mPaint.setColor(color);
        canvas.restore();
    }
}
