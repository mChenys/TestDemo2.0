package mchenys.net.csdn.blog.testmoden20.test6.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import mchenys.net.csdn.blog.testmoden20.test6.model.Tag;

/**
 * Created by mChenys on 2017/1/19.
 */
public class TagView extends View {

    private static final String TAG = "BaseTagView";

    protected boolean isAngleCircleFill;//锚点是否是实心的
    protected List<Tag.TagDesc> mTagDescList;

    private float mCenterX, mCenterY;//锚点圆心坐标

    private float mAnchorRadius;//锚点的半径
    private float mBiasRadius;//斜线的半径
    private float mPadding;//控件的内边距
    private float mPadding2P2;//文本距离p2的边距
    private float mPadding2P3;//文本距离p3的边距
    private float mPadding2Text;//文本之间的边距
    private float mPadding2Top;//文本上边距
    private float mPadding2Bottom;//文本底边距
    private float mLineStrokeWidth;//线厚度
    private float mWidth;//控件的宽度
    private float mHeight;//控件的高度
    private int mTextColor;//字体颜色
    private float mTextSize;//字体大小
    private final int mLineColor;
    private final int mCircleColor;
    private Paint mPaint;

    public void setAngleCircleFill(boolean angleCircleFill) {
        isAngleCircleFill = angleCircleFill;
    }

    public void setTagDescList(List<Tag.TagDesc> tagDescList) {
        mTagDescList = tagDescList;
    }

    public void setTagDesc(Tag.TagDesc desc) {
        if(null ==mTagDescList) mTagDescList = new ArrayList<>();
        mTagDescList.add(desc);
    }
    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPadding = 5;//控件的内边距
        mPadding2P2 = 5;//文本距离p1的边距
        mPadding2P3 = 5;//文本距离p3的边距
        mPadding2Text = 5;//文本之间的边距
        mPadding2Top = 5;//文本上边距
        mPadding2Bottom = 10;//文本底边距
        mAnchorRadius = 10;//锚点圆半径
        mBiasRadius = 4 * mAnchorRadius;//斜线所在圆半径
        mLineStrokeWidth = 1;
        mLineColor = Color.BLACK;
        mCircleColor = Color.BLACK;
        mTextColor = Color.BLACK;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, dm);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineStrokeWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
        控件的宽度= 2*(斜线的半径+最大文本宽度+文本左边距+文本右边距+控件内边距)
        控件的高度=2*(斜线的半径+最大文本的高度+文本的上边距+文本的底边距+控件的内边距)+ 线的厚度*1/2/3
         */
        mWidth = 2 * (mBiasRadius + getMaxTextWidth(mTagDescList) + mPadding2P2 + mPadding2P3 + mPadding);
        mHeight = 2 * (mBiasRadius + getMaxTextHeight(mTagDescList) + mPadding2Top + mPadding2Bottom + mPadding) +
                mTagDescList.size() * mLineStrokeWidth;

        mCenterY = mHeight / 2.0f;
        mCenterX = mWidth / 2.0f;

        setMeasuredDimension((int) mWidth, (int) mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != mTagDescList) {
            drawAnchor(canvas);//绘制锚点
            for (Tag.TagDesc desc : mTagDescList) {
                if (desc.angle == -1) {
                    continue;
                }
                drawLine(canvas, getTextWidth(desc), desc.angle);
                drawText(canvas, desc.tagName, desc.tagValue, desc.angle);

            }
        }
    }



    private void drawAnchor(Canvas canvas) {
        if (isAngleCircleFill) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setColor(mCircleColor);
        canvas.drawCircle(mCenterX, mCenterY, mAnchorRadius, mPaint);
        //canvas.drawCircle(mCenterX, mCenterY, mBiasRadius, mPaint);
    }

    private void drawLine(Canvas canvas, float textWidth, float angle) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        PointF p1 = getCirclePoint(angle, mAnchorRadius);
        PointF p2 = getCirclePoint(angle, mBiasRadius);
        if (angle == 0 || angle == 180) {
            p2.x = p1.x;
        }
        PointF p3 = new PointF();
        p3.y = p2.y;
        if (angle == 0 || angle == 90 || angle == 270) {
            p3.x = mCenterX + mAnchorRadius + mPadding2P2 + textWidth + mPadding2P3;
        } else if (angle == 180 || angle == 90.001f || angle == 269.999f) {
            p3.x = mCenterX - mAnchorRadius - mPadding2P2 - textWidth - mPadding2P3;
        } else if (angle > 90.001f && angle < 269.999f) {
            //左半边
            p3.x = p2.x - mPadding2P2 - textWidth - mPadding2P3;
        } else {
            p3.x = p2.x + mPadding2P2 + textWidth + mPadding2P3;
        }
        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        canvas.drawPath(path, mPaint);
    }

    private void drawText(Canvas canvas, String tagName, String tagValue, float angle) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        float nameStartX;
        float valueStartX;
        PointF p2 = getCirclePoint(angle, mBiasRadius);
        float textStartY = p2.y - mPadding2Bottom;
        float textPadding = TextUtils.isEmpty(tagName) ? 0 : mPadding2Text;
        float nameWidth = getTextMeasure(tagName).x;
        float valueWidth = getTextMeasure(tagValue).x;
        if (angle == 0 || angle == 90 || angle == 270) {
            nameStartX = mCenterX + mAnchorRadius + mPadding2P2;
            valueStartX = nameStartX + getTextMeasure(tagName).x + textPadding;
        } else if (angle == 180 || angle == 90.001f || angle == 269.999f) {
            valueStartX = mCenterX - mAnchorRadius - mPadding2P2 - valueWidth;
            nameStartX = valueStartX - textPadding - nameWidth;
        } else if (angle > 90.001f && angle < 269.999f) {
            //表示左半边
            valueStartX = p2.x - mPadding2Text - valueWidth;
            nameStartX = valueStartX - textPadding - nameWidth;
        } else {
            //右半边
            nameStartX = p2.x + mPadding2P2;
            valueStartX = nameStartX + nameWidth + textPadding;
        }

        if (!TextUtils.isEmpty(tagName)) {
            canvas.drawText(tagName, nameStartX, textStartY, mPaint);
        }
        if (!TextUtils.isEmpty(tagValue)) {
            canvas.drawText(tagValue, valueStartX, textStartY, mPaint);
        }
        Log.d(TAG, "p2.x:" + p2.x + " nameStartX:" + nameStartX);
    }


    /**
     * 求圆周上某点的坐标
     *
     * @param angle
     * @return
     */
    protected PointF getCirclePoint(float angle, float radius) {
        float x = (float) (mCenterX + radius * Math.cos(angle * Math.PI / 180));
        float y = (float) (mCenterY + radius * Math.sin(angle * Math.PI / 180));
        Log.d(TAG, "x:" + x + " y:" + y + " cx:" + mCenterX + " cy:" + mCenterY);
        return new PointF(x, y);
    }

    protected Point getTextMeasure(String text) {
        if (TextUtils.isEmpty(text)) return new Point(0, 0);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        Rect textBound = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), textBound);
        return new Point(textBound.width(), textBound.height());
    }

    private float getTextWidth(Tag.TagDesc desc) {
        float textPadding = TextUtils.isEmpty(desc.tagName) ? 0 : mPadding2Text;
        return getTextMeasure(desc.tagName).x + textPadding + getTextMeasure(desc.tagValue).x;
    }

    private float getTextHeight(Tag.TagDesc desc) {
        return Math.max(getTextMeasure(desc.tagName).y, getTextMeasure(desc.tagValue).y);
    }

    private float getMaxTextWidth(List<Tag.TagDesc> descList) {
        float maxWidth = 0;
        if (null != descList) {
            for (Tag.TagDesc desc : descList) {
                if (getTextWidth(desc) > maxWidth) {
                    maxWidth = getTextWidth(desc);
                }
            }
        }
        return maxWidth;
    }

    private float getMaxTextHeight(List<Tag.TagDesc> descList) {
        float maxHeight = 0;
        if (null != descList) {
            for (Tag.TagDesc desc : descList) {
                if (getTextHeight(desc) > maxHeight) {
                    maxHeight = getTextHeight(desc);
                }
            }
        }
        return maxHeight;
    }


}
