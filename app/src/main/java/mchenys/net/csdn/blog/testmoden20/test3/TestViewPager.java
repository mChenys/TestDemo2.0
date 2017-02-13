package mchenys.net.csdn.blog.testmoden20.test3;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mchenys.net.csdn.blog.testmoden20.R;

/**
 * Created by mChenys on 2017/2/13.
 */
public class TestViewPager extends AppCompatActivity {
    private ViewPager mViewPager;
    private StaggeredGridFragment[] fragments = new StaggeredGridFragment[]{new StaggeredGridFragment(), new StaggeredGridFragment()};
    private int currType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        ListView listView = (ListView) findViewById(R.id.lv);

        final View view = View.inflate(TestViewPager.this, R.layout.layout_vp, null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });

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
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = new TextView(getBaseContext());
                textView.setText("hhdhdhdh");
                return textView;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragments[0].refreshData(getData(0));
            }
        }, 1000);
    }

    private List<String> getData(int type) {
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            temp.add(type == 0 ? "热门" + (i + 1) : "最新" + (i + 1));
        }
        return temp;
    }
}
