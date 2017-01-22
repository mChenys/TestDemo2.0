package mchenys.net.csdn.blog.testmoden20.test2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mChenys on 2017/1/14.
 */
public abstract class BaseTagView extends View {
    private static final String TAG = "BaseTagView";
    protected float mVerticalFraction = 1;//垂直渐变的百分比
    protected float mHorizontalFraction = 1;//水平渐变的百分比
    protected float mCenterX, mCenterY;//圆心坐标
    protected int mRadius;//圆心半径
    protected Rect mPCenterRect;//中间圆的触摸区域
    protected int mLineColor;//线的颜色
    protected int mCircleColor;//圆的颜色
    protected int mTextColor;//文字的颜色
    protected int mTextSize;//文字大小
    protected int mPaddingText2Text;//标签名称和值的间距
    protected int mDiagonalLength;//斜线长度
    protected int mDiagonalInvertedLength;//斜线投影的长度
    protected int mPadding2Diagonal;//文本离斜线的边距
    protected int mPadding2Bottom;//文本理底部线的边距
    protected int mPadding2Top;//文本理的上边距
    protected int mPadding2Outing;//文本离斜线另一端的边距
    protected int mPadding;//控件的内边距
    protected int mLineWidth;//线宽
    protected Paint mPaint;
    protected String mTextName1;//标签1名称 上
    protected String mTextValue1;//标签1的值 上
    protected String mTextName2;//标签2名称 中或下
    protected String mTextValue2;//标签2的值  中或下
    protected String mTextName3;//标签2名称 下
    protected String mTextValue3;//标签2的值 下

    protected int mWidth;//控件宽度
    protected int mHeight;//控件长度
    protected int mStyle = -1;//样式
    //绘制文本的起点坐标,用户判断用户的点击区域是否在文范围内
    protected List<PointF> mTextStartPointList = new ArrayList<>();
    //分别表示标签1,2,3的文本宽度(包括name,value,textpadding)
    protected int mTag1TextWidth;
    protected int mTag2TextWidth;
    protected int mTag3TextWidth;
    //分别表示标签1,2,3的文本name和value的间距
    protected int mT1TextPadding;
    protected int mT2TextPadding;
    protected int mT3TextPadding;

    public void setTextNameValue1(String textName1, String textValue1) {
        mTextName1 = textName1;
        mTextValue1 = textValue1;
    }

    public void setTextNameValue2(String textName2, String textValue2) {
        mTextName2 = textName2;
        mTextValue2 = textValue2;
    }

    public void setTextNameValue3(String textName3, String textValue3) {
        mTextName3 = textName3;
        mTextValue3 = textValue3;
    }

    public BaseTagView(Context context) {
        this(context, null);
    }

    public BaseTagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLineColor = Color.BLACK;
        mCircleColor = Color.BLACK;
        mTextColor = Color.BLACK;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, dm);
        mRadius = 10;
        //mDiagonalLength = 30;
        // mDiagonalInvertedLength = (int) (mDiagonalLength * Math.sin(45));
        mPadding2Diagonal = 10;
        mPadding2Bottom = 10;
        mPadding2Top = 5;
        mPadding2Outing = 10;
        mPaddingText2Text = 10;
        mPadding = 0;
        mLineWidth = 2;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //斜线投影的长度
        mDiagonalInvertedLength = getTextMeasure(mTextName1).y + mPadding2Bottom + mPadding2Top + mRadius;
        //斜线的长度
        mDiagonalLength = (int) (mDiagonalInvertedLength / Math.cos(45));
    }

    /**
     * 求圆周上某点的坐标
     *
     * @param angle
     * @return
     */
    protected Point getCirclePoint(int angle) {
        int x1 = (int) (mCenterX + mRadius * Math.cos(angle * Math.PI / 180));
        int y1 = (int) (mCenterY + mRadius * Math.sin(angle * Math.PI / 180));
        Log.d(TAG, "x1:" + x1 + " y1:" + y1 + " cx:" + mCenterX + " cy:" + mCenterY);
        return new Point(x1, y1);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.drawColor(Color.WHITE);
        drawCircle(canvas);
        drawLine(canvas);
        drawText(canvas);
    }


    private void drawCircle(Canvas canvas) {
        mPaint.setColor(mCircleColor);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
    }


    protected abstract void setDefaultStyleByPosition(int parentWidth, float ratioX);

    protected abstract void changeStyle();

    protected abstract void drawText(Canvas canvas);

    protected abstract void drawLine(Canvas canvas);

    protected abstract boolean isClick2Edit(float px, float py, RectF offsetRect);

    protected abstract int getMoveMinLimitLeftByStyle(int parentW);

    protected abstract int getMoveMaxLimitLeftByStyle(int parentW);

    protected abstract int getMoveMinLimitTopByStyle(int parentH);

    protected abstract int getMoveMaxLimitTopByStyle(int parentH);

    protected Point getTextMeasure(String text) {
        if (TextUtils.isEmpty(text)) return new Point(0, 0);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        Rect textBound = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), textBound);
        return new Point(textBound.width(), textBound.height());
    }

    public int getTextHeight() {
        if (!TextUtils.isEmpty(mTextValue1)) return getTextMeasure(mTextValue1).y;
        else if (!TextUtils.isEmpty(mTextName1)) return getTextMeasure(mTextName1).y;
        else return 0;
    }

    /**
     * 渐显动画
     */
    public void fadeIn(final boolean vertical) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (vertical) {
                    mVerticalFraction = animation.getAnimatedFraction();
                } else {
                    mHorizontalFraction = animation.getAnimatedFraction();
                }
                invalidate();
            }
        });
        animator.setDuration(800);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fadeIn(false);
            }
        });
        animator.start();
    }

    /**
     * 渐隐
     */
    public void fadeOut(final boolean vertical) {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (vertical) {
                    mVerticalFraction = animation.getAnimatedFraction();
                } else {
                    mHorizontalFraction = animation.getAnimatedFraction();
                }
                invalidate();
            }
        });
        animator.setDuration(800);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fadeIn(false);
            }
        });
        animator.start();
    }

    public int getStyle() {
        return mStyle;
    }

    public void setStyle(int style) {
        mStyle = style;
        invalidate();
    }

    /**
     * 是否更换样式
     *
     * @param px 相对父控件的x坐标
     * @param py 相对父控件的y坐标
     * @return
     */
    public boolean isChangeStyle(float px, float py) {
        setCenterRectByParent();
        if (mPCenterRect.contains((int) px, (int) py)) {
            changeStyle();
            invalidate();
            return true;
        }
        return false;
    }

    /**
     * 自动根据移动位置切换样式
     *
     * @param newLeft
     * @param parentW
     */
    public void autoChangeStyleByMove(int newLeft, int parentW) {
        float ratioX = (newLeft + mCenterX) / parentW;
        setDefaultStyleByPosition(parentW, ratioX);
        invalidate();
    }

    /**
     * 获取相对父控件的left
     *
     * @param px 仅在添加时前点击父控件的x坐标,其它情况直接调用getLeft
     * @return
     */
    public int getLeftByParent(float px) {
        if (mCenterX == 0) measure(0, 0);
        return (int) (px - mCenterX);
    }

    /**
     * 获取相对父控件的top
     *
     * @param py 仅在添加时点击父控件的y坐标,其他情况直接调用getTop
     * @return
     */
    public int getTopByParent(float py) {
        if (mCenterY == 0) measure(0, 0);
        return (int) (py - mCenterY);
    }

    /**
     * 获取相对父控件点击圆心时的tagView
     *
     * @param px
     * @param py
     * @return
     */
    public BaseTagView getCenterChooseView(float px, float py) {
        setCenterRectByParent();
        if (mPCenterRect.contains((int) px, (int) py)) {
            return this;
        }
        return null;
    }

    public BaseTagView getTextChooseView(float px, float py, RectF offsetRect) {
        if (isClick2Edit(px, py, offsetRect)) {
            return this;
        }
        return null;
    }

    /**
     * 设置相对父控件的圆心区域
     */
    public void setCenterRectByParent() {
        int pCx = (int) (getLeft() + mCenterX);//TagView顶点left+圆心x坐标就是相对父控件的圆心x坐标
        int pCy = (int) (getTop() + mCenterY);
        mPCenterRect = new Rect(pCx - mRadius, pCy - mRadius, pCx + mRadius, pCy + mRadius);
    }

}
