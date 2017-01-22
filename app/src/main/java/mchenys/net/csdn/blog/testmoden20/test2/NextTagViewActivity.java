package mchenys.net.csdn.blog.testmoden20.test2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import mchenys.net.csdn.blog.testmoden20.R;

/**
 * Created by mChenys on 2017/1/15.
 */
public class NextTagViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TagLayout tagLayout = new TagLayout(this);
        tagLayout.getBackgroundView().setImageResource(R.drawable.bg_hzp);
        List<Tag> tagList = getIntent().getParcelableArrayListExtra("tagList");
        tagLayout.setTagList(tagList);
        tagLayout.setOnTagOperationCallback(null);
        setContentView(tagLayout);
    }
}
