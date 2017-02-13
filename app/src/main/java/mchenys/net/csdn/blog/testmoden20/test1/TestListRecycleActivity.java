package mchenys.net.csdn.blog.testmoden20.test1;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mchenys.net.csdn.blog.testmoden20.R;

/**
 * Created by mChenys on 2017/1/12.
 */
public class TestListRecycleActivity extends AppCompatActivity {
    private static final String TAG = "TestListRecycleActivity";
    private RecyclerView mRecyclerView;
    private PullToRefreshListView listView;
    private List<String> list = new ArrayList<>();
    private Handler mHandler = new Handler();
    private int mItemWidth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recycle);
        listView = (PullToRefreshListView) findViewById(R.id.lv);
        listView.setPullLoadEnable(true);
        listView.addHeaderView(View.inflate(this, R.layout.item_list_header, null));
        initRecycleView();
        listView.setAdapter(new BaseAdapter() {
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
        listView.setPullAndRefreshListViewListener(new PullToRefreshListView.PullAndRefreshListViewListener() {
            @Override
            public void onRefresh() {
                list.clear();
                for (int i = 'a'; i < 'g'; i++) {
                    list.add(String.valueOf((char) i));
                }
                mRecyclerView.getAdapter().notifyDataSetChanged();
                listView.stopRefresh(true);
                listView.stopLoadMore();
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 'a'; i < 'g'; i++) {
                            list.add("new " + String.valueOf((char) i));
                        }
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        listView.stopRefresh(true);
                        listView.stopLoadMore();
                    }
                }, 1000);

            }
        });
    }

    private void initRecycleView() {
        for (int i = 'a'; i < 'g'; i++) {
            list.add(String.valueOf((char) i));
        }
        mRecyclerView = new RecyclerView(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(16));
        mRecyclerView.setAdapter(new MyAdapter(list, this));
        this.mItemWidth = (int) ((getResources().getDisplayMetrics().widthPixels - 4*16) * 0.5f);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<String> data;
        private LayoutInflater mInflater;


        public MyAdapter(List<String> data, Context context) {
            this.data = data;
            this.mInflater = LayoutInflater.from(context);

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new MyViewHolder(mInflater.inflate(R.layout.item_recycle,parent,false));
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
                    Toast.makeText(TestListRecycleActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
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
            iv.getLayoutParams().width = mItemWidth;
            iv.requestLayout();
        }

    }
}
