package com.ctao.gestureslock.widget.base;


import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 手势宮格锁BaseView
 * Created by A Miracle on 2016/3/29.
 */
public abstract class NLockBaseView extends View {
    /**默认清除密码时间*/			protected long CLEAR_TIME = 300;

    /**进行第一次进入的初始化操作*/  protected boolean isFirst = true;
    /**是否可操作*/			    protected boolean isTouch = true;
    /**View的宽高*/			    protected int mWidth, mHeight;
    /**点与点之间的间隔*/		    protected float mSpacing;
    /**圆点个数矩阵列*/		    protected Point[][] mPoints = new Point[3][3];
    /**半径与间隔之比(r:spacing)*/ protected float mRatioThanSpacing = 1f;
    /**圆的半径*/				    protected float mRadius;
    /**选中的点*/			        protected List<Point> mSelectPoints = new ArrayList<>();
    /**是否有正在移动的点*/			private boolean isHaveMovingPoint;
    /**move中的点的x,y*/			private float mMoveingX, mMoveingY;
    /**是否覆盖绘制(不清除初始图片)*/protected boolean isCoverDrawing;
    /**一个清除密码的任务*/			private TimerTask mTask;
    /** 定时器 */					private Timer mTimer = new Timer();
    /**密码的最少长度*/			protected int mPasswordMinLength = 4;
    /**轨迹球画完成事件监听 */		protected OnCompleteListener mListener;

    public NLockBaseView(Context context) {
        this(context, null);
    }

    public NLockBaseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NLockBaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit();
    }

    @Override
    protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){ // wrap_content, wrap_content
            int widthPixels = getResources().getDisplayMetrics().widthPixels;
            setMeasuredDimension(widthPixels, widthPixels);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth > measuredHeight) { // 以最小的为准
            measuredWidth = measuredHeight;
        }else {
            measuredHeight = measuredWidth;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected final void onDraw(Canvas canvas) {
        if(isFirst){
            initCache();
            isFirst = false;
        }

        drawToCanvas(canvas);
    }

    /** 初始化Cache信息 */
    private void initCache() {
        mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        mHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        // 以最小的为准
        if (mWidth > mHeight) {
            mWidth = mHeight;
        }else {
            mHeight = mWidth;
        }

        //得出间距, 半径, 由方程 mWidth = (mPoints.length + 1) * mSpacing + (mPoints.length * 2) * (mSpacing * mRatioThanSpacing);
        mSpacing = mWidth / ((mPoints.length + 1) + (mPoints.length * 2) * mRatioThanSpacing);
        mRadius = mSpacing * mRatioThanSpacing;

        // 初始化每个点
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints.length; j++) {
                Point point = mPoints[i][j] = new Point(
                        getPaddingLeft() + mSpacing * (j + 1) + mRadius * (2 * j + 1),
                        getPaddingTop() + mSpacing * (i + 1) + mRadius * (2 * i + 1));
                point.index = mPoints.length * i + j;
            }
        }

        onInitCache();
    }

    /** draw*/
    private void drawToCanvas(Canvas canvas) {
        // 画连线
        if (mSelectPoints.size() > 0) {
            Point tp = mSelectPoints.get(0);
            for (int i = 1; i < mSelectPoints.size(); i++) {
                Point p = mSelectPoints.get(i);
                drawLine(canvas, tp, p);
                tp = p;
            }

            if (isHaveMovingPoint) {
                drawLine(canvas, tp, new Point(mMoveingX, mMoveingY));
            }
        }

        // 画所有点
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                Point p = mPoints[i][j];
                if (p.state == Point.STATE_CHECK) {
                    if(isCoverDrawing){
                        drawPointNormal(canvas, p);
                    }
                    drawPointCheck(canvas, p);
                }else if (p.state == Point.STATE_CHECK_ERROR) {
                    if(isCoverDrawing){
                        drawPointNormal(canvas, p);
                    }
                    drawPointCheckError(canvas, p);
                }else {
                    drawPointNormal(canvas, p);
                }
            }
        }
    }

    @Override
    public final boolean onTouchEvent(MotionEvent event) {
        if (!isTouch) {
            return super.onTouchEvent(event);
        }

        isHaveMovingPoint = false;
        float ex = event.getX();
        float ey = event.getY();
        boolean isFinish = false; //是否完成一次操作
        Point point = checkSelectPoint(ex, ey);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                }

                // 删除之前的点
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                if (point == null || point.state == Point.STATE_NORMAL) {
                    isHaveMovingPoint = true;
                    mMoveingX = ex;
                    mMoveingY = ey;
                }
                break;
            case MotionEvent.ACTION_UP:
                isFinish = true;
                break;
        }

        if (!isFinish) {
            if(point != null){
                if (point.state == Point.STATE_NORMAL) {
                    point.state = Point.STATE_CHECK;
                    mSelectPoints.add(point);
                }else if (!point.equals(mSelectPoints.get(mSelectPoints.size() - 1))){
                    isHaveMovingPoint = true;
                    mMoveingX = ex;
                    mMoveingY = ey;
                }
            }
        }else{
            if (mSelectPoints.size() == 1) {
                reset();
            }else if(mSelectPoints.size() < mPasswordMinLength && mSelectPoints.size() > 0){
                if (mListener != null){
                    mListener.onShortHint();
                }
                markError();
            }else if (mSelectPoints.size() >= mPasswordMinLength) {
                isTouch = false;
                if (mListener != null) {
                    mListener.onComplete(toPointString());
                }
            }
        }
        postInvalidate();
        return true;
    }

    /** 设置轨迹球画完成事件 */
    public void setOnCompleteListener(OnCompleteListener listener) {
        mListener = listener;
    }

    /** 设置轨迹球密码的最少长度*/
    public void setPasswordMinLength(int passwordMinLength) {
        mPasswordMinLength = passwordMinLength;
    }

    /** 获取轨迹球密码的最少长度*/
    public int getPasswordMinLength() {
        return mPasswordMinLength;
    }

    /**设置是否覆盖绘制(不清除初始图片)*/
    public void setCoverDrawing(boolean coverDrawing){
        isCoverDrawing = coverDrawing;
    }

    /** 设置为输入错误 */
    public void markError() {
        markError(CLEAR_TIME);
    }

    /** 设置为输入错误 */
    public void markError(long time) {
        for (Point p : mSelectPoints) {
            p.state = Point.STATE_CHECK_ERROR;
        }
        clearPassword(time);
    }

    /** 清除密码 */
    public void clearPassword(long time) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (time > 1) {
            mTask = new TimerTask() {
                public void run() {
                    reset();
                    postInvalidate();
                }
            };
            mTimer.schedule(mTask, time);
        } else {
            reset();
            postInvalidate();
        }
    }

    /** 清除密码 */
    public void clearPassword() {
        clearPassword(CLEAR_TIME);
    }

    /**获取两点间的角度, 相对于水平*/
    protected float getDegrees(Point a, Point b) {
		/*	   270
		 *	    ↑
		 * 180 ←㊣→ 0
		 * 	    ↓
		 *     90
		 */
        return (float)Math.toDegrees(Math.atan2(b.y - a.y, b.x - a.x));
    }

    /** 检测选中 */
    protected Point checkSelectPoint(float x, float y) {
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                Point p = mPoints[i][j];
                if (checkInRound(p.x, p.y, mRadius, x, y)) {
                    return p;
                }
            }
        }
        return null;
    }

    /** 点在圆肉 */
    protected boolean checkInRound(float sx, float sy, float r, float x, float y) {
        return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
    }

    /** 转换为String */
    protected String toPointString() {
        if (mSelectPoints.size() >= mPasswordMinLength) {
            StringBuffer sf = new StringBuffer();
            for (Point p : mSelectPoints) {
                sf.append(",");
                sf.append(p.index);
            }
            return sf.deleteCharAt(0).toString();
        } else {
            return "";
        }
    }

    /** 重置 */
    protected void reset() {
        for (Point p : mSelectPoints) {
            p.state = Point.STATE_NORMAL;
        }
        mSelectPoints.clear();
        isTouch = true;
    }

    /** 画两点的连接 */
    protected abstract void drawLine(Canvas canvas, Point a, Point b);

    /** 绘制原始点 */
    protected abstract void drawPointNormal(Canvas canvas, Point p);

    /** 绘制选中点 */
    protected abstract void drawPointCheck(Canvas canvas, Point p);

    /** 绘制选中但错误点 */
    protected abstract void drawPointCheckError(Canvas canvas, Point p);

    /** 构造初始化 */
    protected void onInit() {}

    /** 初始化数据信息, isFirst = false 时不再调用 */
    protected void onInitCache(){}

    public static class Point {
        /**未选中*/public static int STATE_NORMAL = 0;
        /**选中*/public static int STATE_CHECK = 1;
        /**已经选中,但是输错误*/public static int STATE_CHECK_ERROR = 2;

        /**圆心点x*/public float x;
        /**圆心点y*/public float y;
        /**点的状态*/public int state = 0;
        /**点的下标*/public int index = 0;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof Point){
                Point point = (Point) o;
                return point.x == this.x && point.y == this.y;
            }
            return false;
        }
    }

    /** 轨迹球画完成事件 */
    public interface OnCompleteListener {
        /** 画完了 */
        void onComplete(String password);
        /** 长度过短提示 */
        void onShortHint();
    }
}