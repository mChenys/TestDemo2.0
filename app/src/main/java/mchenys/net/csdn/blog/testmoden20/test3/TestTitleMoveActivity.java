package mchenys.net.csdn.blog.testmoden20.test3;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mchenys.net.csdn.blog.testmoden20.R;
import mchenys.net.csdn.blog.testmoden20.test1.SpacesItemDecoration;

/**
 * Created by mChenys on 2017/1/12.
 */
public class TestTitleMoveActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TestTitleMoveActivity";

    private ListView mListView;

    private TextView mFromTv, mToTv, mTopTabHotTv, mLvTabHotTv, mTopTabNewTv, mLvTabNewTv;
    private FrameLayout mTopTab;
    private View mTitleBg;
    private int dp60;
    private View headerView1;
    private float maxTopMoveH; //顶部滑动的最大有效距离,用于计算百分比
    private float titleMoveDx, titleMoveDy;//添加控件移动的x,y最大距离
    private MyAdapter mAdapter;
    private List<String> mData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private boolean isPined;
    private boolean needChangeAlpha;
    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (maxTopMoveH == 0.0f) {
                titleMoveDx = mToTv.getX() - mFromTv.getX();
                titleMoveDy = mToTv.getY() - mFromTv.getY();
            }
            maxTopMoveH = headerView1.getHeight() - dp60 * 2;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            float fraction = Math.abs(headerView1.getY()) / maxTopMoveH;
            Log.d(TAG, "headerView1.getY():" + headerView1.getY());//0~正无穷
            if (fraction < 1) {
                //移动关注
                mFromTv.setTranslationX(fraction * titleMoveDx);
                mFromTv.setTranslationY(fraction * titleMoveDy);
                mTopTab.setVisibility(View.GONE);
                if (needChangeAlpha) {
                    mTitleBg.setAlpha(fraction);
                }
                isPined = false;
            } else if (fraction >= 1) {
                mTopTab.setVisibility(View.VISIBLE);
                mFromTv.setTranslationX(titleMoveDx);
                mFromTv.setTranslationY(titleMoveDy);
                mTitleBg.setAlpha(1.0f);
                needChangeAlpha=true;
                isPined = true;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_move);
        initView();
        initListener();
    }

    private void initListener() {
        mListView.setOnScrollListener(mOnScrollListener);

        mFromTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestTitleMoveActivity.this, "添加啊!", Toast.LENGTH_SHORT).show();
            }
        });

        mTopTabHotTv.setOnClickListener(this);
        mLvTabHotTv.setOnClickListener(this);
        mTopTabNewTv.setOnClickListener(this);
        mLvTabNewTv.setOnClickListener(this);

        headerView1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                headerView1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                maxTopMoveH = headerView1.getHeight() - dp60 * 2;
                titleMoveDx = mToTv.getX() - mFromTv.getX();
                titleMoveDy = mToTv.getY() - mFromTv.getY();
            }
        });
    }

    private void initView() {
        needChangeAlpha = true;
        dp60 = (int) getResources().getDimension(R.dimen.dp60);
        mListView = (ListView) findViewById(R.id.lv);
        mFromTv = (TextView) findViewById(R.id.tv_add_from);
        mToTv = (TextView) findViewById(R.id.tv_add_to);
        mTopTab = (FrameLayout) findViewById(R.id.fl_top_tab);
        mTitleBg = findViewById(R.id.view_title_bg);
        mTopTabHotTv = (TextView) findViewById(R.id.tv_top_hot);
        mTopTabNewTv = (TextView) findViewById(R.id.tv_top_new);

        headerView1 = View.inflate(this, R.layout.item_list_header1, null);
        mLvTabHotTv = (TextView) headerView1.findViewById(R.id.tv_lv_hot);
        mLvTabNewTv = (TextView) headerView1.findViewById(R.id.tv_lv_new);
        mListView.addHeaderView(headerView1);
        initRecycleView();
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return mRecyclerView;
            }
        });


    }


    @Override
    public void onClick(View v) {
        mData.clear();
        switch (v.getId()) {
            case R.id.tv_top_hot:
            case R.id.tv_lv_hot:
                Toast.makeText(TestTitleMoveActivity.this, "热门", Toast.LENGTH_SHORT).show();
                mData.addAll(getData(0));
                break;
            case R.id.tv_top_new:
            case R.id.tv_lv_new:
                Toast.makeText(TestTitleMoveActivity.this, "最新", Toast.LENGTH_SHORT).show();
                mData.addAll(getData(1));
                break;

        }
        mAdapter.notifyDataSetChanged();
        mTitleBg.setAlpha(1.0f);
        needChangeAlpha = false;
        mRecyclerView.setVisibility(View.INVISIBLE);
        if (!isPined) {
            mListView.setSelection(1);
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    mListView.smoothScrollBy(-2 * dp60, 100);
                }
            });
            mListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }, 250);
        }



    }

    private List<String> getData(int type) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            temp.add(type == 0 ? "热门" + (i + 1) : "最新" + (i + 1));
        }
        return temp;
    }
    private void initRecycleView() {
        mRecyclerView = new RecyclerView(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(16));
        mData.addAll(getData(0));
        mRecyclerView.setAdapter(mAdapter=new MyAdapter(mData,this));
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private final List<String> data;
        public MyAdapter(List<String> data, Context context) {
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(View.inflate(getBaseContext(), R.layout.item_recycle, null));
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tv.setText(data.get(position));
            if (position % 2 == 0) {
                holder.iv.setImageResource(R.drawable.bg2);
            } else if (position % 5 == 0) {
                holder.iv.setImageResource(R.drawable.bg3);
            } else {
                holder.iv.setImageResource(R.drawable.bg1);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(TestTitleMoveActivity.this, "position:"+position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        private ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }

    }

}
