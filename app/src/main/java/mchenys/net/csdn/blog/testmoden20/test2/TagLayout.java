package mchenys.net.csdn.blog.testmoden20.test2;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mChenys on 2017/1/15.
 */
public class TagLayout extends FrameLayout implements GestureDetector.OnGestureListener {
    private static final String TAG = "TagLayout";
    private ImageView mBackground;
    private GestureDetector mDetector;
    private BaseTagView mCurrSelectTagView; //当前选中的tagview
    private BaseTagView mCurrEditTagView; //当前需要编辑的tagview
    private boolean mEnableLimitMove;//是否开启移动范围限制
    private boolean mEnableAutoChangeStyle;//是否开启自动样式切换
    private boolean mOnlyCenterChoose;//是否是仅点击中心选中
    private int mMaxTagCount;//最大tag数量
    private List<BaseTagView> mTagViews = new ArrayList<>();
    private List<Tag> mTagList;

    public interface OnTagOperationCallback {

        void onAdd(float ratioX, float ratioY);

        void onEdit(Tag tag);

        void onDelete(BaseTagView currTagView);
    }

    public void setOnTagOperationCallback(OnTagOperationCallback callback) {
        mOnTagOperationCallback = callback;
    }

    private OnTagOperationCallback mOnTagOperationCallback;


    public ImageView getBackgroundView() {
        return mBackground;
    }

    public void setEnableLimitMove(boolean enableLimitMove) {
        this.mEnableLimitMove = enableLimitMove;
    }

    public void setTagList(List<Tag> tagList) {
        this.mTagList = tagList;
    }

    public void setOnlyCenterChoose(boolean onlyCenterChoose) {
        mOnlyCenterChoose = onlyCenterChoose;
    }
    public void setEnableAutoChangeStyle(boolean enable) {
        this.mEnableAutoChangeStyle = enable;
    }

    public void setMaxTagCount(int maxTagCount) {
        mMaxTagCount = maxTagCount;
    }

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackground = new ImageView(context);
        mBackground.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(mBackground);
        mDetector = new GestureDetector(context, this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof BaseTagView) {
                removeView(view);
            }
        }

        if (null != mTagList) {
            for (int i = 0; i < mTagList.size(); i++) {
                Tag tag = mTagList.get(i);
                addTag(tag);
            }
        }
    }

    public void addTag(Tag tag) {
        BaseTagView tagView = null;
        if (tag.getTagValueCount() == 1) tagView = new SingleTagView(getContext());
        if (tag.getTagValueCount() == 2) tagView = new DoubleTagView(getContext());
        if (tag.getTagValueCount() == 3) tagView = new ThreeTagView(getContext());
        if (null != tagView) {
           // tag.adjustTagByCount();
            tagView.setTextNameValue1(tag.tagName1, tag.tagValue1);
            tagView.setTextNameValue2(tag.tagName2, tag.tagValue2);
            tagView.setTextNameValue3(tag.tagName3, tag.tagValue3);
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();

            if (tag.type == -1) {
                //表示新增
                tagView.setDefaultStyleByPosition(width, tag.ratioX);
                mCurrSelectTagView = tagView;
            } else {
                //还原
                tagView.setStyle(tag.type);
                mCurrSelectTagView = null;
            }
            float x = width * tag.ratioX;
            float y = height * tag.ratioY;
            Log.d(TAG, "addTag>>ratioX:" + tag.ratioX + " ratioY:" + tag.ratioY + " px:" + x + " py:" + y);
            int left = tagView.getLeftByParent(x);
            int top = tagView.getTopByParent(y);
            Log.d(TAG, "addTag>>left" + left + " top:" + top);
            LayoutParams lp = new LayoutParams(-2, -2);
            lp.leftMargin = left;
            lp.topMargin = top;
            addView(tagView, lp);
            mTagViews.add(tagView);

            //新增的调整位置
            if (null != mCurrSelectTagView) {
                mCurrSelectTagView.post(new Runnable() {
                    @Override
                    public void run() {
                        adjustBounds(mCurrSelectTagView.getLeft(), mCurrSelectTagView.getTop());
                    }
                });
            }
        }
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        mTagViews.remove(view);
    }

    /**
     * 获取所有已添加的Tag
     *
     * @return
     */
    public ArrayList<Tag> getAllTags() {
        ArrayList<Tag> allTagList = new ArrayList<>();
        for (BaseTagView tagView : mTagViews) {
            Tag tag = new Tag();
            tag.setRatioX((tagView.getLeft() + tagView.mCenterX) / (getWidth() * 1.0f));
            tag.setRatioY((tagView.getTop() + tagView.mCenterY) / (getHeight() * 1.0f));
            tag.setTag1(tagView.mTextName1, tagView.mTextValue1);
            tag.setTag2(tagView.mTextName2, tagView.mTextValue2);
            tag.setTag3(tagView.mTextName3, tagView.mTextValue3);
            tag.setType(tagView.getStyle());
            allTagList.add(tag);
        }
        return allTagList;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "onDown>>>>px:" + e.getX() + " py:" + e.getY());
        mCurrSelectTagView = null;
        mCurrEditTagView = null;
        for (int i = 0; i < mTagViews.size(); i++) {
            BaseTagView tagView = mTagViews.get(i);
            mCurrSelectTagView = tagView.getCenterChooseView(e.getX(), e.getY());
            if (mOnlyCenterChoose) {
                //仅支持中心选中
                if (null != mCurrSelectTagView) {
                    Log.d(TAG, "中心点击");
                    break;
                }
            } else {
                //支持中心选中和文本区域选中
                if (null != mCurrSelectTagView) {
                    Log.d(TAG, "中心点击");
                    break;
                }
                mCurrSelectTagView = tagView.getTextChooseView(e.getX(), e.getY(),null);
                if (null != mCurrSelectTagView) {
                    Log.d(TAG, "文本区域点击");
                    break;
                }
            }

        }
        for (int i = 0; i < mTagViews.size(); i++) {
            BaseTagView tagView = mTagViews.get(i);
            mCurrEditTagView = tagView.getTextChooseView(e.getX(), e.getY(),null);
            if (null != mCurrEditTagView) {
                Log.d(TAG, "编辑文本");
                break;
            }
        }

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp>>>px:" + e.getX() + " py:" + e.getY());
        if (null != mOnTagOperationCallback) {
            float[] touchRatio = getTouchRatio(e.getX(), e.getY());

            if (null == mCurrSelectTagView) {
                if (null == mCurrEditTagView) {
                    //新增TagView
                    mOnTagOperationCallback.onAdd(touchRatio[0], touchRatio[1]);
                    Log.d(TAG, "onSingleTapUp>>>rx:" + touchRatio[0] + " ry:" + touchRatio[1]);
                } else {
                    //修改tagView
                    if (mCurrEditTagView.isClick2Edit(e.getX(), e.getY(), null)) {
                        preEditTagView(touchRatio[0], touchRatio[1]);
                    }
                }
            } else {
                if (mCurrSelectTagView.isChangeStyle(e.getX(), e.getY())) {//修改样式
                    adjustBounds(mCurrSelectTagView.getLeft(), mCurrSelectTagView.getTop());
                } else if (mCurrEditTagView.isClick2Edit(e.getX(), e.getY(), null)) {//编辑文本
                    preEditTagView(touchRatio[0], touchRatio[1]);
                }
            }

        }
        return true;
    }

    private void preEditTagView(float touchRatioX, float touchRatioY) {
        //修改TagView
        Tag tag = new Tag();
        tag.setRatioX(touchRatioX);
        tag.setRatioY(touchRatioY);
        tag.setTag1(mCurrEditTagView.mTextName1, mCurrEditTagView.mTextValue1);
        tag.setTag2(mCurrEditTagView.mTextName2, mCurrEditTagView.mTextValue2);
        tag.setTag3(mCurrEditTagView.mTextName3, mCurrEditTagView.mTextValue3);
        tag.setType(mCurrEditTagView.getStyle());
        mOnTagOperationCallback.onEdit(tag);
    }


    /**
     * 编辑TagView
     *
     * @param tag
     */
    public void editTagView(Tag tag) {
        if (null != mCurrEditTagView) {
            mCurrEditTagView.setTextNameValue1(tag.tagName1, tag.tagValue1);
            mCurrEditTagView.setTextNameValue2(tag.tagName2, tag.tagValue2);
            mCurrEditTagView.setTextNameValue3(tag.tagName3, tag.tagValue3);
            mCurrEditTagView.requestLayout();
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (null != mOnTagOperationCallback && null != mCurrSelectTagView) {
            int offx = (int) -distanceX;
            int offy = (int) -distanceY;
            int newLeft = mCurrSelectTagView.getLeft() + offx;
            int newTop = mCurrSelectTagView.getTop() + offy;
            mCurrSelectTagView.bringToFront();
            if (mEnableAutoChangeStyle) {
                mCurrSelectTagView.autoChangeStyleByMove(newLeft, getWidth());
            }
            adjustBounds(newLeft, newTop);
            Log.d(TAG, "onScroll mCurrSelectTagView left:" + mCurrSelectTagView.getLeft() + " right:" + mCurrSelectTagView.getRight());
        }

        return true;
    }

    /**
     * 调整子View有效范围
     *
     * @param left
     * @param top
     */
    private void adjustBounds(int left, int top) {
        int newLeft = left;
        int newTop = top;
        if (mEnableLimitMove) {
            if (left < mCurrSelectTagView.getMoveMinLimitLeftByStyle(getWidth()))
                newLeft = mCurrSelectTagView.getMoveMinLimitLeftByStyle(getWidth());
            else if (left > mCurrSelectTagView.getMoveMaxLimitLeftByStyle(getWidth()))
                newLeft = mCurrSelectTagView.getMoveMaxLimitLeftByStyle(getWidth());
            if (top < mCurrSelectTagView.getMoveMinLimitTopByStyle(getHeight()))
                newTop = mCurrSelectTagView.getMoveMinLimitTopByStyle(getHeight());
            else if (top > mCurrSelectTagView.getMoveMaxLimitTopByStyle(getHeight()))
                newTop = mCurrSelectTagView.getMoveMaxLimitTopByStyle(getHeight());
        }
        // mCurrSelectTagView.layout(newLeft, newTop, newRight, newBottom);
        // 由于layout方法不会修改lp.leftMargin和lp.topMargin,所以一旦添加新的TagView就会把之前移动的恢复到原来的位置
        LayoutParams lp = (LayoutParams) mCurrSelectTagView.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(-2, -2);
        }
        lp.leftMargin = newLeft;
        lp.topMargin = newTop;
        Log.d(TAG, "adjustBounds>>oldLeft:" + left + "newLeft:" + newLeft + " oldTop:" + top + " newTop:" + newTop);
        mCurrSelectTagView.requestLayout();
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (null != mOnTagOperationCallback && null != mCurrSelectTagView) {
            mOnTagOperationCallback.onDelete(mCurrSelectTagView);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    /**
     * 获取按下点的比例
     *
     * @param x
     * @param y
     * @return
     */
    public float[] getTouchRatio(float x, float y) {
        float[] ratio = new float[2];
        ratio[0] = x / getMeasuredWidth();
        ratio[1] = y / getMeasuredHeight();
        return ratio;
    }

}
