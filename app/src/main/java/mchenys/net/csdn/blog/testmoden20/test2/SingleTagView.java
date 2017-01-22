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
import android.widget.Toast;

/**
 * Created by mChenys on 2017/1/13.
 */
public class SingleTagView extends BaseTagView {
    private static final String TAG = "SingleTagView";
    public static final int STYLE_RB = 1;//右下角斜线
    public static final int STYLE_LB = 2;//左下角斜线
    public static final int STYLE_RT = 3;//右上角斜线
    public static final int STYLE_LT = 4;//左上角斜线

    public void setDefaultStyleByPosition(int parentWidth, float ratioX) {
        if (0 != parentWidth) {
            float touchX2ParentLeft = ratioX * parentWidth;
            if (touchX2ParentLeft < 1 / 2.0f * parentWidth) {
                mStyle = STYLE_LB;//按下是靠左,那么默认是左下
            } else {
                mStyle = STYLE_RB;//按下是靠右,那么默认是右下
            }
        }
    }

    public SingleTagView(Context context) {
        this(context, null);
    }

    public SingleTagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //高度
        mHeight = mDiagonalInvertedLength * 2 + 2 * mPadding + getTextHeight() + mPadding2Bottom + 2 * mLineWidth;

        mT1TextPadding = TextUtils.isEmpty(mTextName1) || TextUtils.isEmpty(mTextValue1) ? 0 : mPaddingText2Text;
        mTag1TextWidth = getTextMeasure(mTextName1).x + getTextMeasure(mTextValue1).x + mT1TextPadding;

        //圆心
        mCenterX = mTag1TextWidth + mPadding2Outing + mPadding2Diagonal + mDiagonalInvertedLength + mPadding;
        mCenterY = mDiagonalInvertedLength + mLineWidth + getTextHeight() + mPadding2Bottom + mPadding;

        //宽度
        mWidth = (int) (2 * mCenterX);

        setMeasuredDimension(mWidth, mHeight);
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
    protected void drawText(Canvas canvas) {
        mTextStartPointList.clear();
        mPaint.setStyle(Paint.Style.FILL);
        //name坐标
        float text1NameX = 0, text1NameY = 0;
        //value坐标
        float text1ValueX = 0, text1ValueY = 0;
        switch (mStyle) {
            case STYLE_LB://左下
                text1NameX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue1).x
                        - mT1TextPadding - getTextMeasure(mTextName1).x;
                text1NameY = mCenterY + mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text1ValueX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue1).x;
                text1ValueY = text1NameY;
                break;
            case STYLE_RB://右下
                text1NameX = mCenterX + mDiagonalInvertedLength + mPadding2Diagonal;
                text1NameY = mCenterY + mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text1ValueX = text1NameX + getTextMeasure(mTextName1).x + mT1TextPadding;
                text1ValueY = text1NameY;
                break;
            case STYLE_LT://左上
                text1NameX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue1).x
                        - mT1TextPadding - getTextMeasure(mTextName1).x;
                text1NameY = mCenterY - mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text1ValueX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue1).x;
                text1ValueY = text1NameY;
                break;
            case STYLE_RT://右上
                text1NameX = mCenterX + mDiagonalInvertedLength + mPadding2Diagonal;
                text1NameY = mCenterY - mDiagonalInvertedLength - mPadding2Bottom - mLineWidth;
                text1ValueX = text1NameX + getTextMeasure(mTextName1).x + mT1TextPadding;
                text1ValueY = text1NameY;
                break;
        }
        if (!TextUtils.isEmpty(mTextName1)) {
            canvas.drawText(mTextName1, text1NameX, text1NameY, mPaint);//名称1
            mTextStartPointList.add(new PointF(text1NameX, text1ValueY - getTextHeight()));
        }
        if (!TextUtils.isEmpty(mTextValue1)) {
            canvas.drawText(mTextValue1, text1ValueX, text1ValueY, mPaint);//值1
            if (TextUtils.isEmpty(mTextName1))
                mTextStartPointList.add(new PointF(text1ValueX, text1ValueY - getTextHeight()));

        }

    }

    @Override
    protected void drawLine(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        Path lineP = new Path();
        Point cPoint = null;
        float endX;
        switch (mStyle) {
            case STYLE_LB://左下
                cPoint = getCirclePoint(135);
                endX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue1).x
                        - mT1TextPadding - getTextMeasure(mTextName1).x - mPadding2Outing;
                lineP.moveTo(cPoint.x, cPoint.y);
                lineP.lineTo(mCenterX - mDiagonalInvertedLength, mCenterY + mDiagonalInvertedLength);
                lineP.lineTo(endX, mCenterY + mDiagonalInvertedLength);

//                lineP.moveTo(cPoint.x, cPoint.y);
//                lineP.lineTo(cPoint.x - mVerticalFraction * mDiagonalInvertedLength, cPoint.y + mVerticalFraction * mDiagonalInvertedLength);
//                lineP.lineTo(mCenterX - mDiagonalInvertedLength - mHorizontalFraction * (mCenterX - mDiagonalInvertedLength),
//                        mCenterY + mDiagonalInvertedLength);
                canvas.drawPath(lineP, mPaint);

                break;
            case STYLE_RB:
                cPoint = getCirclePoint(45);
                endX = mCenterX + mDiagonalInvertedLength + mPadding2Diagonal + getTextMeasure(mTextValue1).x
                        + mT1TextPadding + getTextMeasure(mTextName1).x + mPadding2Outing;
                lineP.moveTo(cPoint.x, cPoint.y);
                lineP.lineTo(mCenterX + mDiagonalInvertedLength, mCenterY + mDiagonalInvertedLength);
                lineP.lineTo(endX, mCenterY + mDiagonalInvertedLength);
                canvas.drawPath(lineP, mPaint);
                break;
            case STYLE_LT:
                cPoint = getCirclePoint(225);
                endX = mCenterX - mDiagonalInvertedLength - mPadding2Diagonal - getTextMeasure(mTextValue1).x
                        - mT1TextPadding - getTextMeasure(mTextName1).x - mPadding2Outing;
                lineP.moveTo(cPoint.x, cPoint.y);
                lineP.lineTo(mCenterX - mDiagonalInvertedLength, mCenterY - mDiagonalInvertedLength);
                lineP.lineTo(endX, mCenterY - mDiagonalInvertedLength);

                canvas.drawPath(lineP, mPaint);
                break;
            case STYLE_RT:
                cPoint = getCirclePoint(315);
                endX = mCenterX + mDiagonalInvertedLength + mPadding2Diagonal + getTextMeasure(mTextValue1).x
                        + mT1TextPadding + getTextMeasure(mTextName1).x + mPadding2Outing;
                lineP.moveTo(cPoint.x, cPoint.y);
                lineP.lineTo(mCenterX + mDiagonalInvertedLength, mCenterY - mDiagonalInvertedLength);
                lineP.lineTo(endX, mCenterY - mDiagonalInvertedLength);
                canvas.drawPath(lineP, mPaint);
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
        float left = getLeft() + mTextStartPointList.get(0).x + offsetLeft;
        float top = getTop() + mTextStartPointList.get(0).y + offsetTop;
        float right = left + mTag1TextWidth + offsetRight;
        float bottom = top + getTextHeight() + offsetBottom;
        return new RectF(left, top, right, bottom).contains(px, py);
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
