package com.ctao.gestureslock.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 手势宮格锁BaseView
 * Created by A Miracle on 2016/3/29.
 */
public abstract class NLockPatterBaseView extends View {

    /**默认清除密码时间*/				protected long CLEAR_TIME = 300;

    /**轨迹球画完成事件监听 */			protected OnCompleteListener mCompleteListener;
    /**密码的最少长度*/				protected int mPasswordMinLength = 4;
    /**是否可操作*/					protected boolean isTouch = true;
    /**半径与间隔之比(r:spac)*/		protected float ratio_r_spac = 1;
    /**圆点个数矩阵列*/				protected Point[][] mPoints = new Point[3][3];
    /**选中的点*/					    protected ArrayList<Point> mSelectPoints = new ArrayList<Point>();
    /**是否覆盖绘制(不清除初始图片)*/	protected boolean isCoverDrawing;

    /**View的宽高*/				    protected float width, height;
    /**圆的半径*/					    protected float radius;
    /**点与点之间的间隔*/				protected float spacing;

    /**move中的点的x,y*/			    private int moveingX, moveingY;
    /**是否有正在移动的点*/			private boolean movingBePoint = false;

    /** 定时器 */					private Timer timer = new Timer();
    /**一个清除密码的任务*/			private TimerTask task;
    /**进行第一次进入的初始化操作*/		protected boolean isFirst = true;

    public NLockPatterBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NLockPatterBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NLockPatterBaseView(Context context) {
        super(context);
    }

    /** 设置轨迹球画完成事件 */
    public void setOnCompleteListener(OnCompleteListener completeListener) {
        mCompleteListener = completeListener;
    }

    /** 设置轨迹球密码的最少长度*/
    public void setPasswordMinLength(int passwordMinLength) {
        mPasswordMinLength = passwordMinLength;
    }

    /**设置是否覆盖绘制(不清除初始图片)*/
    public void setCoverDrawing(boolean coverDrawing){
        isCoverDrawing = coverDrawing;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        // 以最小的为准
        if (measuredWidth > measuredHeight) {
            measuredWidth = measuredHeight;
        }else {
            measuredHeight = measuredWidth;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isFirst){
            initCache();
            isFirst = false;
        }
        drawToCanvas(canvas);
    }

    /**初始化Cache信息*/
    private void initCache() {
        width = getWidth() - getPaddingLeft() - getPaddingRight();
        height = getHeight() - getPaddingTop() - getPaddingBottom();
        // 以最小的为准
        if (width > height) {
            width = height;
        }else {
            height = width;
        }

        //得出间距, 半径,由方程 width = (mPoints.length + 1) * spacing + (mPoints.length * 2) * (spacing * ratio_r_spac);
        spacing = width / ((mPoints.length + 1) + (mPoints.length * 2) * ratio_r_spac);
        radius = spacing * ratio_r_spac;

        // 初始化每个点
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints.length; j++) {
                Point point = mPoints[i][j] = new Point(getPaddingLeft()+spacing*(j+1)+radius*(2*j+1), getPaddingTop()+spacing*(i+1)+radius*(2*i+1));
                point.index = mPoints.length * i + j;
            }
        }

        onInitCache();
    }

    /**初始化数据信息, isFirst = false时不再调用*/
    protected abstract void onInitCache();

    /**绘制点、线、角度*/
    private void drawToCanvas(Canvas canvas) {
        // 画所有点
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                Point p = mPoints[i][j];
                if (p.state == Point.STATE_CHECK) {
                    if(isCoverDrawing){
                        drawPointNormal(canvas, p);
                    }
                    drawPointCheck(canvas, p);
                } else if (p.state == Point.STATE_CHECK_ERROR) {
                    if(isCoverDrawing){
                        drawPointNormal(canvas, p);
                    }
                    drawPointCheckError(canvas, p);
                } else {
                    drawPointNormal(canvas, p);
                }
            }
        }

        // 画连线
        if (mSelectPoints.size() > 0) {
            Point tp = mSelectPoints.get(0);
            for (int i = 1; i < mSelectPoints.size(); i++) {
                Point p = mSelectPoints.get(i);
                drawLine(canvas, tp, p);
                tp = p;
            }
            if (movingBePoint) {
                drawLine(canvas, tp, new Point(moveingX, moveingY));
            }
        }
    }

    /**绘制原始点*/
    protected abstract void drawPointNormal(Canvas canvas, Point p);

    /**绘制选中点*/
    protected abstract void drawPointCheck(Canvas canvas, Point p);

    /**绘制选中但错误点*/
    protected abstract void drawPointCheckError(Canvas canvas, Point p);

    /**画两点的连接*/
    protected abstract void drawLine(Canvas canvas, Point a, Point b);

    /**获取两点间的角度, 相对于水平*/
    public float getDegrees(Point a, Point b) {
		/*	   270
		 *	    ↑
		 * 180 ←㊣→ 0
		 * 	    ↓
		 *     90
		 */
        return (float)Math.toDegrees(Math.atan2(b.y - a.y, b.x - a.x));
    }

    /**检测*/
    private Point checkSelectPoint(float x, float y) {
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                Point p = mPoints[i][j];
                if (checkInRound(p.x, p.y, radius, x, y)) {
                    return p;
                }
            }
        }
        return null;
    }

    /** 点在圆肉 */
    private boolean checkInRound(float sx, float sy, float r, float x, float y) {
        return Math.sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouch) {
            return false;
        }

        movingBePoint = false;
        int ex = (int) event.getX();
        int ey = (int) event.getY();
        boolean isFinish = false; //是否完成一次操作
        Point point = checkSelectPoint(ex, ey);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 如果正在清除密码,则取消
                if (task != null) {
                    task.cancel();
                    task = null;
                }
                // 删除之前的点
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                if(point == null || point.state == Point.STATE_NORMAL){
                    movingBePoint = true;
                    moveingX = ex;
                    moveingY = ey;
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
                } else if (!point.equals(mSelectPoints.get(mSelectPoints.size() - 1))) {
                    movingBePoint = true;
                    moveingX = ex;
                    moveingY = ey;
                }
            }
        }else{
            if (mSelectPoints.size() == 1) {
                reset();
            } else if (mSelectPoints.size() < mPasswordMinLength && mSelectPoints.size() > 0) {
                markError();
                passwordShortHint();
            }else if (mCompleteListener != null) {
                if (mSelectPoints.size() >= mPasswordMinLength) {
                    isTouch = false;
                    mCompleteListener.onComplete(toPointString());
                }
            }
        }
        postInvalidate();
        return true;
    }

    /**密码太短提示*/
    protected abstract void passwordShortHint();

    /** 转换为String*/
    private String toPointString() {
        if (mSelectPoints.size() > mPasswordMinLength) {
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

    /** 清除密码 */
    public void clearPassword() {
        clearPassword(CLEAR_TIME);
    }

    /** 清除密码 */
    public void clearPassword(final long time) {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (time > 1) {
            task = new TimerTask() {
                public void run() {
                    reset();
                    postInvalidate();
                }
            };
            timer.schedule(task, time);
        } else {
            reset();
            postInvalidate();
        }
    }

    /** 重置 */
    private void reset() {
        for (Point p : mSelectPoints) {
            p.state = Point.STATE_NORMAL;
        }
        mSelectPoints.clear();
        isTouch = true;
    }

    /** 设置为输入错误 */
    public void markError() {
        markError(CLEAR_TIME);
    }

    /** 设置为输入错误 */
    public void markError(final long time) {
        for (Point p : mSelectPoints) {
            p.state = Point.STATE_CHECK_ERROR;
        }
        clearPassword(time);
    }

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
    }
}