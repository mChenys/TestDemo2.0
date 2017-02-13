package mchenys.net.csdn.blog.testmoden20.test1;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mchenys.net.csdn.blog.testmoden20.R;


public class PullToRefreshListViewFooter extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;
    public final static int STATE_STOP = 3;

    private Context mContext;
    //	private String text = "正在加载更多";
    private String text = "没有更多了，<b>本喵</b>是万能的分界线。";
    private String loadingTip = "正在<b>加载</b>中......";
    private View mContentView;
    private View mProgressBar;
    private TextView mHintView;
    private RelativeLayout rlayout_noMore, rlayout_loading;
    private TextView tv_loadingTip;
    private ImageView iv_loadingIcon;
    private ImageView iv_footer_cat;
    private View v_top_divider;
    private ProgressBar mLoadingMorePb;

    public PullToRefreshListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setState(int state) {
        if (state == STATE_NORMAL) {// 正常时候，隐藏foor
            mContentView.setVisibility(View.GONE);

            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(Html.fromHtml(text));
        } else if (state == STATE_LOADING) {
            mContentView.setVisibility(View.VISIBLE);
            rlayout_loading.setVisibility(VISIBLE);
            rlayout_noMore.setVisibility(GONE);

            tv_loadingTip.setText(Html.fromHtml(loadingTip));
        } else if (state == STATE_STOP) {
            mContentView.setVisibility(View.VISIBLE);
            rlayout_loading.setVisibility(GONE);
            rlayout_noMore.setVisibility(VISIBLE);

            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(Html.fromHtml(text));
        }
    }

    public void setGoneOrVisible(int visible) {
        mContentView.setVisibility(visible);
    }

    public void setBottomMargin(int height) {
        if (height < 0)
            return;
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * normal status
     */
    public void normal() {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading() {
        mHintView.setVisibility(View.GONE);
//		mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    @SuppressLint("InflateParams")
    private void initView(Context context) {
        mContext = context;
        RelativeLayout moreView = null;
        moreView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.app_pull_listview_footer, null);

        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
        mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
        rlayout_noMore = (RelativeLayout) moreView.findViewById(R.id.rlayout_noMore);
        rlayout_loading = (RelativeLayout) moreView.findViewById(R.id.rlayout_loading);
        tv_loadingTip = (TextView) moreView.findViewById(R.id.tv_loading);
        iv_loadingIcon = (ImageView) moreView.findViewById(R.id.iv_loadingIcon);
        iv_footer_cat = (ImageView) moreView.findViewById(R.id.iv_footer_cat);
        v_top_divider = moreView.findViewById(R.id.v_top_divider);
        mLoadingMorePb = (ProgressBar) moreView.findViewById(R.id.pb_loading);
    }

    public String getText() {
        return text;
    }

    public void setLoadingIconVisible(int isVisible) {
        iv_loadingIcon.setVisibility(isVisible);
    }

    public void setLoadingTip(String text) {
        this.loadingTip = text;
        tv_loadingTip.setText(Html.fromHtml(text));

    }

    public void setLoadingTipTextColor(int color) {
        tv_loadingTip.setTextColor(color);
    }

    public void setHintViewText(String text) {
        this.text = text;
        mHintView.setText(Html.fromHtml(text));
    }

    public void setHintViewTextColor(int color) {
        mHintView.setTextColor(color);
    }

    public void setFooterCatVisible(int isVisible) {
        iv_footer_cat.setVisibility(isVisible);
    }

    public void setFooterBackgroundColor(int color) {
        mContentView.setBackgroundColor(color);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTopMargin(int height) {
        if (height < 0)
            return;
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.topMargin = height;
        mContentView.setLayoutParams(lp);
    }

    public int getTopMargin() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        return lp.topMargin;
    }

    public void setTopDividerVisible(boolean flag) {
        if (flag) {
            v_top_divider.setVisibility(VISIBLE);
        } else {
            v_top_divider.setVisibility(GONE);
        }
    }

    public void setLoadingMorePbVisiable(boolean visiable) {
        mLoadingMorePb.setVisibility(visiable ? VISIBLE : GONE);
    }

    public void setNoMoreDataBackgroundColor(int color) {
        rlayout_noMore.setBackgroundColor(color);
    }

    public void setLoadingMoreBackgroundColor(int color) {
        rlayout_loading.setBackgroundColor(color);
    }
}
