package mchenys.net.csdn.blog.testmoden20.test1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import mchenys.net.csdn.blog.testmoden20.R;


public class PullToRefreshListViewHeaderForModern extends PullToRefreshListViewHeader {
    private LinearLayout mContainer;
    private int mState = STATE_NORMAL;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private Animation mRotateAnim;

    private final int ROTATE_ANIM_DURATION = 300;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;
    private RelativeLayout header_content;
    private ImageView img_modern;
    private RelativeLayout xlistview_header_content;

    public PullToRefreshListViewHeaderForModern(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public PullToRefreshListViewHeaderForModern(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.app_pull_listview_header_for_modern, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);
        xlistview_header_content = (RelativeLayout) findViewById(R.id.xlistview_header_content);
        header_content = (RelativeLayout) findViewById(R.id.xlistview_header_content);
        img_modern = (ImageView) findViewById(R.id.img_choice);

    }

    private int lastHeight = 0;

    public void setState(int state, int height) {

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    mRotateDownAnim = new RotateAnimation(0.0f, height,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f);
                    mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
                    mRotateDownAnim.setFillAfter(true);
                    img_modern.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    img_modern.clearAnimation();
                    mRotateAnim = new RotateAnimation(height, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f);
                    mRotateAnim.setDuration(ROTATE_ANIM_DURATION);
                    mRotateAnim.setFillAfter(true);
                    img_modern.startAnimation(mRotateAnim);
                }
                break;
            case STATE_READY:
                mRotateUpAnim = new RotateAnimation(lastHeight, height,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                img_modern.startAnimation(mRotateUpAnim);
                lastHeight = height;
                break;
            case STATE_REFRESHING:
                img_modern.clearAnimation();
                mRotateAnim = new RotateAnimation(height, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                mRotateAnim.setDuration(ROTATE_ANIM_DURATION);
                mRotateAnim.setFillAfter(true);
                mRotateAnim.setRepeatCount(-1);
                img_modern.startAnimation(mRotateAnim);
                break;
            default:
        }

        mState = state;
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer
                .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

}
