package mchenys.net.csdn.blog.testmoden20.test3;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mchenys.net.csdn.blog.testmoden20.R;
import mchenys.net.csdn.blog.testmoden20.test1.SpacesItemDecoration;

/**
 * Created by mChenys on 2017/2/13.
 */
public class StaggeredGridFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<String> mData = new ArrayList<>();
    private MyAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == mRecyclerView) {
            initRecycleView();
        } else {
            ViewGroup parent = (ViewGroup) mRecyclerView.getParent();
            if (null != parent) {
                parent.removeView(mRecyclerView);
            }
        }
        return mRecyclerView;
    }

    private void initRecycleView() {
        mRecyclerView = new RecyclerView(getActivity());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(16));
        mRecyclerView.setAdapter(mAdapter = new MyAdapter(mData, getActivity()));
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<String> data;
        private Context mContext;

        public MyAdapter(List<String> data, Context context) {
            this.data = data;
            this.mContext = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recycle, parent, false));
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
                    Toast.makeText(getActivity(), "position:" + position, Toast.LENGTH_SHORT).show();
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

    public void refreshData(List<String> list) {
        mData.clear();
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}
