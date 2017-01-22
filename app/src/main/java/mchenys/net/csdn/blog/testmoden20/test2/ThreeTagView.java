package mchenys.net.csdn.blog.testmoden20.test2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mChenys on 2017/1/14.
 */
public class ThreeTagView extends BaseTagView {
    public static final int STYLE_LTLCRB = 1;//左上左中右下斜线
    public static final int STYLE_RTRCLB = 2;//右上右中左下斜线
    public static final int STYLE_LTLCLB = 3;//左上左中左下直线
    public static final int STYLE_RTRCRB = 4;//右上右中右下直线
    private static final String TAG = "ThreeTagView";


    public void setDefaultStyleByPosition(int parentWidth, float ratioX) {
        if (0 != parentWidth) {
            float touchX2ParentLeft = ratioX * parentWidth;
            if (touchX2ParentLeft < 1 / 2.0f * parentWidth) {
                mStyle = STYLE_LTLCLB;//左上左中左下直线
            } else {
                mStyle = STYLE_RTRCRB;//右上右中右下直线
            }
        }
    }

    public ThreeTagView(Context context) {
        this(context, null);
    }

    public ThreeTagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThreeTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void changeStyle() {
        Toast.makeText(getContext(), "切换样式", Toast.LENGTH_SHORT).show();
        if (mStyle < 4) {
            mStyle++;
        } else {
            mStyle = 1;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //高度
        mHeight = mDiagonalInvertedLength * 2 + 2 * mPadding + getTextHeight() + mPadding2Bottom + 3 * mLineWidth;
        //文本宽度
         mT1TextPadding = TextUtils.isEmpty(mTextName1) || TextUtils.isEmpty(mTextValue1) ? 0 : mPaddingText2Text;
         mT2TextPadding = TextUtils.isEmpty(mTextName2) || TextUtils.isEmpty(mTextValue2) ? 0 : mPaddingText2Text;
         mT3TextPadding = TextUtils.isEmpty(mTextName3) || TextUtils.isEmpty(mTextValue3) ? 0 : mPaddingText2Text;

        mTag1TextWidth = getTextMeasure(mTextName1).x + getTextMeasure(mTextValue1).x + mT1TextPadding;

        mTag2TextWidth = getTextMeasure(mTextName2).x + getTextMeasure(mTextValue2).x + mT2TextPadding;

        mTag3TextWidth = getTextMeasure(mTextName3).x + getTextMeasure(mTextValue3).x + mT3TextPadding;

        //取最大文本宽度
        int maxTextWidth = Math.max(mTag1TextWidth, Math.max(mTag2TextWidth, mTag3TextWidth));

        //圆心
        mCenterX = maxTextWidth + mPadding2Outing + mPadding2Diagonal + mDiagonalInvertedLength + mPadding;
        mCenterY = mDiagonalInvertedLength + mLineWidth + getTextHeight() + mPadding2Bottom + mPadding;

        //宽度
        mWidth = (int) (2 * mCenterX);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void drawText(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mTextStartPointList.clear();
        //文本1 值1的坐标
        float text1NameX = 0, text1NameY = 0;
        float text1ValueX = 0, text1ValueY = 0;
        //文本2 值2的坐标
        float text2NameX = 0, text2NameY = 0;
        float text2ValueX = 0, text2ValueY = 0;
        //文本3 值3的坐标
        float text3NameX = 0, text3NameY = 0;
        float text3ValueX = 0, text3ValueY = 0;

        switch (mStyle) {
            case STYLE_LTLCRB://左上左中右下
                //上
                text1NameX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue1).x
                        - mT1TextPadding - getTextMeasure(mTextName1).x;
                text1NameY = mCenterY - mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text1ValueX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue1).x;
                text1ValueY = text1NameY;
                //中
                text2NameX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue2).x
                        - mT2TextPadding - getTextMeasure(mTextName2).x;
                text2NameY = mCenterY - mPadding2Bottom - mLineWidth;
                text2ValueY = text2NameY;
                text2ValueX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue2).x;
                //下
                text3NameX = mCenterX + mDiagonalInvertedLength + mPadding2Diagonal;
                text3NameY = mCenterY + mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text3ValueY = text3NameY;
                text3ValueX = text3NameX + getTextMeasure(mTextName3).x + mT3TextPadding;
                break;
            case STYLE_RTRCLB://左下右中右上
                //上
                text1NameX = mCenterX + mDiagonalInvertedLength + mPadding2Diagonal;
                text1NameY = mCenterY - mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text1ValueX = text1NameX + getTextMeasure(mTextName1).x + mT1TextPadding;
                text1ValueY = text1NameY;
                //中
                text2NameX = text1NameX;
                text2NameY = mCenterY - mPadding2Bottom - mLineWidth;
                text2ValueY = text2NameY;
                text2ValueX = text2NameX + getTextMeasure(mTextName2).x + mT2TextPadding;
                //下
                text3NameX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue3).x
                        - mT3TextPadding - getTextMeasure(mTextName3).x;
                text3NameY = mCenterY + mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text3ValueX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue3).x;
                text3ValueY = text3NameY;
                break;
            case STYLE_LTLCLB://左上左中左下
                //上
                text1NameX = mCenterX - mPadding2Diagonal - getTextMeasure(mTextValue1).x - mT1TextPadding - getTextMeasure(mTextName1).x;
                text1NameY = mCenterY - mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text1ValueX = mCenterX - mPadding2Diagonal - getTextMeasure(mTextValue1).x;
                text1ValueY = text1NameY;
                //中
                text2NameX = mCenterX - mPadding2Diagonal - getTextMeasure(mTextValue2).x - mT2TextPadding - getTextMeasure(mTextName2).x;
                text2NameY = mCenterY - mPadding2Bottom - mLineWidth;
                text2ValueY = text2NameY;
                text2ValueX = mCenterX - mPadding2Diagonal - getTextMeasure(mTextValue2).x;
                //下
                text3NameX = mCenterX - mPadding2Diagonal - getTextMeasure(mTextValue3).x - mT3TextPadding - getTextMeasure(mTextName3).x;
                text3NameY = mCenterY + mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text3ValueY = text3NameY;
                text3ValueX = mCenterX - mPadding2Diagonal - getTextMeasure(mTextValue3).x;
                break;
            case STYLE_RTRCRB://右上右中右下
                //上
                text1NameX = mCenterX + mPadding2Diagonal;
                text1NameY = mCenterY - mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text1ValueX = text1NameX + getTextMeasure(mTextName1).x + mT1TextPadding;
                text1ValueY = text1NameY;
                //中
                text2NameX = text1NameX;
                text2NameY = mCenterY - mPadding2Bottom - mLineWidth;
                text2ValueY = text2NameY;
                text2ValueX = text2NameX + getTextMeasure(mTextName2).x + mT2TextPadding;
                //下
                text3NameX = text1NameX;
                text3NameY = mCenterY + mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text3ValueY = text3NameY;
                text3ValueX = text3NameX + getTextMeasure(mTextName3).x + mT3TextPadding;
                break;
        }
        if (!TextUtils.isEmpty(mTextName1)) {
            canvas.drawText(mTextName1, text1NameX, text1NameY, mPaint);//名称1
            mTextStartPointList.add(new PointF(text1NameX, text1NameY - getTextHeight()));
        }

        if (!TextUtils.isEmpty(mTextValue1)) {
            canvas.drawText(mTextValue1, text1ValueX, text1ValueY, mPaint);//值1
            if (TextUtils.isEmpty(mTextName1)) {
                mTextStartPointList.add(new PointF(text1ValueX, text1ValueY - getTextHeight()));
            }

        }

        if (!TextUtils.isEmpty(mTextName2)) {
            canvas.drawText(mTextName2, text2NameX, text2NameY, mPaint);//名称2
            mTextStartPointList.add(new PointF(text2NameX, text2NameY - getTextHeight()));
        }
        if (!TextUtils.isEmpty(mTextValue2)) {
            canvas.drawText(mTextValue2, text2ValueX, text2ValueY, mPaint);//值2
            if (TextUtils.isEmpty(mTextName2)) {
                mTextStartPointList.add(new PointF(text2ValueX, text2ValueY - getTextHeight()));
            }
        }

        if (!TextUtils.isEmpty(mTextName3)) {
            canvas.drawText(mTextName3, text3NameX, text3NameY, mPaint);//名称3
            mTextStartPointList.add(new PointF(text3NameX, text3NameY - getTextHeight()));
        }
        if (!TextUtils.isEmpty(mTextValue3)) {
            canvas.drawText(mTextValue3, text3ValueX, text3ValueY, mPaint);//值3
            if (TextUtils.isEmpty(mTextName3)) {
                mTextStartPointList.add(new PointF(text3ValueX, text3ValueY - getTextHeight()));
            }
        }
    }

    @Override
    protected void drawLine(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        Point cPoint1 = null;//上
        Point cPoint2 = null;//中
        Point cPoint3 = null;//下
        Path path = null;
        float endX;
        switch (mStyle) {
            case STYLE_LTLCRB://左上左中右下斜线
                cPoint1 = getCirclePoint(225);
                cPoint2 = getCirclePoint(180);
                cPoint3 = getCirclePoint(45);
                endX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue1).x
                        - mT1TextPadding - getTextMeasure(mTextName1).x - mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint1.x, cPoint1.y);
                path.lineTo(mCenterX - mDiagonalInvertedLength, mCenterY - mDiagonalInvertedLength);
                path.lineTo(endX, mCenterY - mDiagonalInvertedLength);
                canvas.drawPath(path, mPaint);

                endX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue2).x
                        - mT2TextPadding - getTextMeasure(mTextName2).x - mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint2.x, cPoint2.y);
                path.lineTo(endX, mCenterY);
                canvas.drawPath(path, mPaint);

                endX = mCenterX + mDiagonalInvertedLength + mPadding2Diagonal + getTextMeasure(mTextValue3).x
                        + mT3TextPadding + getTextMeasure(mTextName3).x + mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint3.x, cPoint3.y);
                path.lineTo(mCenterX + mDiagonalInvertedLength, mCenterY + mDiagonalInvertedLength);
                path.lineTo(endX, mCenterY + mDiagonalInvertedLength);
                canvas.drawPath(path, mPaint);
                break;
            case STYLE_RTRCLB://左下右中右上斜线
                cPoint1 = getCirclePoint(315);
                cPoint2 = getCirclePoint(0);
                cPoint3 = getCirclePoint(135);


                endX = mCenterX + mDiagonalInvertedLength + mPadding2Diagonal + getTextMeasure(mTextValue1).x
                        + mT1TextPadding + getTextMeasure(mTextName1).x + mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint1.x, cPoint1.y);
                path.lineTo(mCenterX + mDiagonalInvertedLength, mCenterY - mDiagonalInvertedLength);
                path.lineTo(endX, mCenterY - mDiagonalInvertedLength);
                canvas.drawPath(path, mPaint);

                endX = mCenterX + mDiagonalInvertedLength + mPadding2Diagonal + getTextMeasure(mTextValue2).x
                        + mT2TextPadding + getTextMeasure(mTextName2).x + mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint2.x, cPoint2.y);
                path.lineTo(endX, mCenterY);
                canvas.drawPath(path, mPaint);

                endX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue3).x
                        - mT3TextPadding - getTextMeasure(mTextName3).x - mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint3.x, cPoint3.y);
                path.lineTo(mCenterX - mDiagonalInvertedLength, mCenterY + mDiagonalInvertedLength);
                path.lineTo(endX, mCenterY + mDiagonalInvertedLength);
                canvas.drawPath(path, mPaint);
                break;

            case STYLE_LTLCLB://左上左中左下直线
                cPoint1 = getCirclePoint(270);
                cPoint2 = getCirclePoint(180);
                cPoint3 = getCirclePoint(90);
                endX = mCenterX - mPadding2Diagonal - getTextMeasure(mTextName1).x - getTextMeasure(mTextValue1).x -
                        mT1TextPadding - mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint1.x, cPoint1.y);
                path.lineTo(mCenterX, mCenterY - mDiagonalInvertedLength);
                path.lineTo(endX, mCenterY - mDiagonalInvertedLength);
                canvas.drawPath(path, mPaint);

                endX = mCenterX - mPadding2Diagonal - getTextMeasure(mTextName2).x -
                        getTextMeasure(mTextValue2).x - mT2TextPadding - mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint2.x, cPoint2.y);
                path.lineTo(endX, mCenterY);
                canvas.drawPath(path, mPaint);

                endX = mCenterX - mPadding2Diagonal - getTextMeasure(mTextName3).x -
                        getTextMeasure(mTextValue3).x - mT3TextPadding - mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint3.x, cPoint3.y);
                path.lineTo(mCenterX, mCenterY + mDiagonalInvertedLength);
                path.lineTo(endX, mCenterY + mDiagonalInvertedLength);
                canvas.drawPath(path, mPaint);
                break;

            case STYLE_RTRCRB://右上右中右下直线
                cPoint1 = getCirclePoint(270);
                cPoint2 = getCirclePoint(0);
                cPoint3 = getCirclePoint(90);
                endX = mCenterX + mPadding2Diagonal + getTextMeasure(mTextName1).x +
                        mT1TextPadding + getTextMeasure(mTextValue1).x + mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint1.x, cPoint1.y);
                path.lineTo(mCenterX, mCenterY - mDiagonalInvertedLength);
                path.lineTo(endX, mCenterY - mDiagonalInvertedLength);
                canvas.drawPath(path, mPaint);

                endX = mCenterX + mPadding2Diagonal + getTextMeasure(mTextName2).x +
                        mT2TextPadding + getTextMeasure(mTextValue2).x + mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint2.x, cPoint2.y);
                path.lineTo(endX, mCenterY);
                canvas.drawPath(path, mPaint);

                endX = mCenterX + mPadding2Diagonal + getTextMeasure(mTextName3).x +
                        mT3TextPadding + getTextMeasure(mTextValue3).x + mPadding2Outing;
                path = new Path();
                path.moveTo(cPoint3.x, cPoint3.y);
                path.lineTo(mCenterX, mCenterY + mDiagonalInvertedLength);
                path.lineTo(endX, mCenterY + mDiagonalInvertedLength);
                canvas.drawPath(path, mPaint);
                break;
        }
    }

    @Override
    protected boolean isClick2Edit(float px, float py, RectF offsetRect) {
        float offsetLeft = 0;
        float offsetRight = 0;
        float offsetTop = 0;
        float offsetBottom = 0;
        if (null != offsetRect) {
            offsetLeft = offsetRect.left;
            offsetRight = offsetRect.right;
            offsetTop = offsetRect.top;
            offsetBottom = offsetRect.bottom;
        }
        float left = (int) (getLeft() + mTextStartPointList.get(0).x + offsetLeft);
        float top = (int) (getTop() + mTextStartPointList.get(0).y + offsetTop);
        float right = (int) (left + mTag1TextWidth + offsetRight);
        float bottom = (int) (top + getTextHeight() + offsetBottom);
        RectF r1 = new RectF(left, top, right, bottom);

        left = (int) (getLeft() + mTextStartPointList.get(1).x + offsetLeft);
        top = (int) (getTop() + mTextStartPointList.get(1).y + offsetTop);
        right = (int) (left + mTag2TextWidth + offsetRight);
        bottom = (int) (top + getTextHeight() + offsetBottom);
        RectF r2 = new RectF(left, top, right, bottom);

        left = (int) (getLeft() + mTextStartPointList.get(2).x + offsetLeft);
        top = (int) (getTop() + mTextStartPointList.get(2).y + offsetTop);
        right = (int) (left + mTag3TextWidth + offsetRight);
        bottom = (int) (top + getTextHeight() + offsetBottom);
        RectF r3 = new RectF(left, top, right, bottom);
        Log.d(TAG, "px:" + px + " py:" + py + " r1:" + r1.toString() + " r2:" + r2.toString() + " r3:" + r3.toString());

        return r1.contains(px, py) || r2.contains(px, py) || r3.contains(px, py);
    }

    @Override
    protected int getMoveMinLimitLeftByStyle(int parentW) {
        return (int) (-mCenterX + mRadius);
    }

    @Override
    protected int getMoveMaxLimitLeftByStyle(int parentW) {
        int limitLeft = (int) (parentW - mCenterX - mRadius);
        if (limitLeft < 0) limitLeft = (int) (-(mCenterX - parentW) - mRadius);
        return limitLeft;
    }

    @Override
    protected int getMoveMinLimitTopByStyle(int width) {
        return (int) (-mCenterY + mRadius);
    }

    @Override
    protected int getMoveMaxLimitTopByStyle(int parentH) {
        return (int) (parentH - mCenterY - mRadius);
    }

}
