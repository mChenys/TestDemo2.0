package mchenys.net.csdn.blog.testmoden20.test1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.text.SimpleDateFormat;

import mchenys.net.csdn.blog.testmoden20.R;


public class PullToRefreshListView extends ListView implements OnScrollListener {


    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private PullAndRefreshListViewListener pullAndRefreshListViewListener;

    // -- header view
//    private PullToRefreshListViewHeader mHeaderView;
    private PullToRefreshListViewHeaderForModern mHeaderView;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    //    private TextView mHeaderTimeView;
    private int mHeaderViewHeight; // header view's height
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private boolean isOpenFloat = false;
    // -- footer view
    private PullToRefreshListViewFooter mFooterView;
    private boolean mEnablePullLoad;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;
    private final static int SCROLL_DURATION = 400; // scroll back duration
    // private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >=
    // 50px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    // feature.

    private long refreshTime = System.currentTimeMillis();
    private Context context = null;
    private String timeTag = "";
    private int totalItemCount = 1000;
    private static final int LAST_REFRESH_TIME = 1000;
    private boolean isCallback = false;

    public String getTimeTag() {
        return timeTag;
    }


    public void setTimeTag(String timeTag) {
        this.timeTag = timeTag;
        String last = getLastRefreshTime();
//        mHeaderTimeView.setText(last);
    }

    /**
     * @param context
     */
    public PullToRefreshListView(Context context) {
        this(context, null);
//        initWithContext(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
//        initWithContext(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }


    private int headCount;// 用来记录头部总数

    /**
     * 用来测量控件实际尺寸
     *
     * @param v
     * @param isWidth
     * @return
     */
    public int getViewWidthOrHeight(final View v, final boolean isWidth) {
        int param = 0;
        if (null != v) {
            int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            v.measure(w, h);
            if (isWidth) {
                param = v.getMeasuredWidth();
            } else {
                param = v.getMeasuredHeight();
            }
        }
        return param;
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);
        this.context = context;
        // init header view
        mHeaderView = new PullToRefreshListViewHeaderForModern(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
//        mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.xlistview_header_time);
        addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new PullToRefreshListViewFooter(context);
        mFooterView.hide();

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure XListViewFooter is the last footer view, and only add once.
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
        }
        super.setAdapter(adapter);
    }

    @Override
    public int getFooterViewsCount() {
        // TODO Auto-generated method stub
        return super.getFooterViewsCount();
    }

    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.GONE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.hide();
        } else {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(PullToRefreshListViewFooter.STATE_NORMAL);
        }
    }

    public void stopRefresh(final boolean success) {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            long times = System.currentTimeMillis() - refreshTime;
            if (times >= LAST_REFRESH_TIME) {
                resetHeaderHeight();
                if (success) {
                    changeRefreshTime();
                }
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetHeaderHeight();
                        if (success) {
                            changeRefreshTime();
                        }
                    }
                }, LAST_REFRESH_TIME - times);
            }
        }
    }

    private void changeRefreshTime() {
        // 2 重置刷新时间
        setRefreshTime();
        String last = getLastRefreshTime();
//        mHeaderTimeView.setText(last);
    }

    @Override
    public void addHeaderView(View v) {
        // TODO Auto-generated method stub
        super.addHeaderView(v);
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.hide();
            mFooterView.setState(PullToRefreshListViewFooter.STATE_NORMAL);
            mFooterView.setState(PullToRefreshListViewFooter.STATE_STOP);
        }
    }

    public void notMoreData() {
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.setState(PullToRefreshListViewFooter.STATE_STOP);
        }
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private int lastHeight = 0;

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            int visibleHeight = mHeaderView.getVisiableHeight();
            Log.i("test", "visibleHeight==" + visibleHeight);
            if (mHeaderView.getVisiableHeight() > 0) {
                mHeaderView.setState(PullToRefreshListViewHeader.STATE_READY, visibleHeight);
//                lastHeight = visibleHeight;
            } else {
                mHeaderView.setState(PullToRefreshListViewHeader.STATE_NORMAL, visibleHeight);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    /**
     * 自动下拉刷新并加载数据
     */
    public void showHeaderAndRefresh() {
        // 1 改变当前显示的时间，以便下一次显示
        String last = getLastRefreshTime();
//        mHeaderTimeView.setText(last);
        mPullRefreshing = true;
        mHeaderView.setState(PullToRefreshListViewHeader.STATE_REFRESHING, mHeaderView.getVisiableHeight());
        if (pullAndRefreshListViewListener != null) {
            refreshTime = System.currentTimeMillis();
            pullAndRefreshListViewListener.onRefresh();
        }
        mScroller.startScroll(0, 0, 0, mHeaderViewHeight, 200);
        invalidate();
    }

    public void refresh() {
        if (pullAndRefreshListViewListener != null) {
            refreshTime = System.currentTimeMillis();
            pullAndRefreshListViewListener.onRefresh();
        }
    }

    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setState(PullToRefreshListViewFooter.STATE_LOADING);
        if (pullAndRefreshListViewListener != null) {
            mFooterView.show();
            setSelection(getAdapter().getCount());
            pullAndRefreshListViewListener.onLoadMore();
        }
    }


    private int direction = 0;
    public static int upDirection = 0;
    public static int downDirection = 1;

    public int getDirection() {
        return direction;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return true;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();

                getCurrentDirection(deltaY);// 判断是上拉还是下拉
                if (!mEnablePullRefresh && deltaY >= 0 && getFirstVisiblePosition() == 0) {
                    return false;
                }
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                } else if (getLastVisiblePosition() == mTotalItemCount - 2) {
                    // last item, already pulled up or want to pull up.
                    // updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1; // reset
                /**
                 * 这里判断ListView的可见position小于1时进行刷新，因为当前ListView添加的PullToRefreshListViewHeader是一个线性布局,
                 * 而这个PullToRefreshListViewHeader中又嵌套了一个View,所以默认的刷新位置为<=1
                 */
                if (getFirstVisiblePosition() <= 1) {
                    // invoke refresh
                    isCallback = false;
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(PullToRefreshListViewHeader.STATE_REFRESHING, mHeaderView.getVisiableHeight());
                        if (pullAndRefreshListViewListener != null) {
                            refreshTime = System.currentTimeMillis();
//                            pullAndRefreshListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                } else if (getLastVisiblePosition() == mTotalItemCount - 2) {
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断是上拉还是下拉
     *
     * @param deltaY
     */
    private void getCurrentDirection(float deltaY) {
        if (deltaY >= 0) {
            direction = downDirection;
        } else {
            direction = upDirection;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if ((mScroller.getCurrY() == mHeaderViewHeight) && mPullRefreshing && !isCallback) {
                isCallback = true;
                pullAndRefreshListViewListener.onRefresh();
            }
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
        int lastVisiPos = this.getLastVisiblePosition();
        if (lastVisiPos == this.totalItemCount - 1 && !mPullLoading && !mPullRefreshing && mEnablePullLoad && getDirection() == upDirection) {
            startLoadMore();
        }
        // if (mOnScrollListener != null) {
        // mOnScrollListener.onScrollStateChanged(view, scrollState);
        // }
    }

    boolean isHaveGallery = false;// 是否添加焦点图

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        this.totalItemCount = totalItemCount;
    }


    public void setPullAndRefreshListViewListener(PullAndRefreshListViewListener l) {
        pullAndRefreshListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface PullAndRefreshListViewListener {
        public void onRefresh();

        public void onLoadMore();
    }

    private void setRefreshTime() {
        long currentTime = System.currentTimeMillis();
        PreferencesUtils.setPreferences(this.context, "PullToRefresh", timeTag, currentTime);
    }

    private String getLastRefreshTime() {
        long lastRefreshTime = PreferencesUtils.getPreference(this.context, "PullToRefresh", timeTag, System.currentTimeMillis());
        String time = dateFormat.format(lastRefreshTime);
        return time;
    }

    public void hideFooterView() {
        if (null != mFooterView) {
            mFooterView.hide();
        }
    }

    /**
     * ----------------------------------------------以下是新加的浮动窗效果代码
     *
     * @author user
     */
    public static interface OnPositionChangedListener {

        public void onPositionChanged(PullToRefreshListView listView, int position, View scrollBarPanel);

    }

    private OnScrollListener mOnScrollListener = null;

    private View mScrollBarPanel = null;
    private int mScrollBarPanelPosition = 0;

    private OnPositionChangedListener mPositionChangedListener;
    private int mLastPosition = -1;

    private Animation mInAnimation = null;
    private Animation mOutAnimation = null;

    private final Handler mHandler = new Handler();


    public void setOnPositionChangedListener(OnPositionChangedListener onPositionChangedListener) {
        mPositionChangedListener = onPositionChangedListener;
    }

    public void setScrollBarPanel(View scrollBarPanel) {
        mScrollBarPanel = scrollBarPanel;
        mScrollBarPanel.setVisibility(View.GONE);
        requestLayout();
    }

    public void setScrollBarPanel(int resId) {
        setScrollBarPanel(LayoutInflater.from(getContext()).inflate(resId, this, false));
    }

    public View getScrollBarPanel() {
        return mScrollBarPanel;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mScrollBarPanel != null) {
            final int x = getMeasuredWidth() - mScrollBarPanel.getMeasuredWidth();
            mScrollBarPanel.layout(x, mScrollBarPanelPosition, x + mScrollBarPanel.getMeasuredWidth(), mScrollBarPanelPosition + mScrollBarPanel.getMeasuredHeight());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mScrollBarPanel != null && mScrollBarPanel.getVisibility() == View.VISIBLE) {
            drawChild(canvas, mScrollBarPanel, getDrawingTime());
        }
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    /**
     * 设置头和脚的文字信息
     *
     * @return
     */
    public PullToRefreshListViewHeader getmHeaderView() {
        return mHeaderView;
    }

    public void setmHeaderView(PullToRefreshListViewHeaderForModern mHeaderView) {
        this.mHeaderView = mHeaderView;
    }

    public PullToRefreshListViewFooter getmFooterView() {
        return mFooterView;
    }

    public void setmFooterView(PullToRefreshListViewFooter mFooterView) {
        this.mFooterView = mFooterView;
    }

    public boolean isHaveGallery() {
        return isHaveGallery;
    }

    public void setHaveGallery(boolean isHaveGallery) {// 设置是否添加过焦点图
        this.isHaveGallery = isHaveGallery;
    }

    public void setFooterViewBottomMargin(float dp) {
        getmFooterView().setBottomMargin(DisplayUtils.convertDIP2PX(getContext(), dp));

    }

    /**
     * 1.3添加,设置没有更多的样式,默认是带猫
     *
     * @param style 0:默认样式,1:不带猫的样式
     */
    public void setNoMoreStyle(int style) {
        if (style == 1) {
            getmFooterView().setFooterBackgroundColor(Color.parseColor("#F5F5F5"));
            getmFooterView().setHintViewText("没有更多了");
            getmFooterView().setHintViewTextColor(Color.parseColor("#D9D9D9"));
            getmFooterView().setFooterCatVisible(View.GONE);
            getmFooterView().setNoMoreDataBackgroundColor(Color.parseColor("#F5F5F5"));
        }
    }

    /**
     * 1.3添加,设置加载更多的样式,默认是带猫
     *
     * @param style 0:默认样式,1:不带猫的样式
     */
    public void setLoadMoreStyle(int style) {
        if (style == 1) {
            getmFooterView().setFooterBackgroundColor(Color.parseColor("#F5F5F5"));
            getmFooterView().setLoadingTip("加载更多");
            getmFooterView().setLoadingTipTextColor(Color.parseColor("#D9D9D9"));
            getmFooterView().setLoadingIconVisible(View.GONE);
            getmFooterView().setLoadingMorePbVisiable(true);
            getmFooterView().setLoadingMoreBackgroundColor(Color.parseColor("#F5F5F5"));
        }
    }
}
