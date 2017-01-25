package mchenys.net.csdn.blog.testmoden20.test7;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by mChenys on 2017/1/23.
 */
public class SwitchLayout extends FrameLayout {
    private static final String TAG = "SwitchLayout";
    private View mLeftTv;
    private View mRightTv;
    private float mViewPadding;
    private int mSwitchWidth;
    private OnSwitchCallback mOnSwitchCallback;

    public interface OnSwitchCallback {
        //切换到左边
        void onLeft(View lv, View rv);

        //切换到右边
        void onRight(View lv, View rv);
    }

    public void setOnSwitchCallback(OnSwitchCallback c) {
        mOnSwitchCallback = c;
        if (null != c) {
            mOnSwitchCallback.onLeft(mLeftTv, mRightTv);
        }
    }

    public SwitchLayout(Context context) {
        this(context, null);
    }

    public SwitchLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftTv = getChildAt(0);
        mRightTv = getChildAt(1);
        mLeftTv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                turnRight();
                return false;
            }
        });
        mRightTv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                turnLeft();
                return false;
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int halfWidth = (int) (mLeftTv.getMeasuredWidth() + mViewPadding + 0.5f * mRightTv.getMeasuredWidth());
        int w = 2 * halfWidth;
        int h = Math.max(mLeftTv.getMeasuredHeight(), mRightTv.getMeasuredHeight());
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int lTop = (int) (0.5f * (getHeight() - mLeftTv.getHeight()));
        int rTop = (int) (0.5f * (getHeight() - mRightTv.getHeight()));
        mLeftTv.layout(0, lTop, mLeftTv.getWidth(), lTop + mLeftTv.getHeight());
        int rLeft = (int) (mLeftTv.getRight() + mViewPadding);
        mRightTv.layout(rLeft, rTop, rLeft + mRightTv.getWidth(), rTop + mRightTv.getHeight());
        mSwitchWidth = (int) (0.5f * (getWidth() - mLeftTv.getWidth()));
    }

    public void turnLeft() {
        if (getScrollX() != 0) {
            scrollBy(-getScrollX(), 0);
            if (null != mOnSwitchCallback) {
                mOnSwitchCallback.onLeft(mLeftTv, mRightTv);
            }
        }
    }

    public void turnRight() {
        if (getScrollX() != -mSwitchWidth) {
            scrollBy(-mSwitchWidth, 0);
            if (null != mOnSwitchCallback) {
                mOnSwitchCallback.onRight(mLeftTv, mRightTv);
            }
        }
    }

    private int lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (event.getRawX() - lastX);
                // 右滑
                if (deltaX > 50) {
                    turnRight();
                }
                // 左滑
                else if (deltaX < -50) {
                    turnLeft();
                }

        }

        return true;
    }
}
