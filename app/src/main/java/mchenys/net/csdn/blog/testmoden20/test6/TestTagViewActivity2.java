package mchenys.net.csdn.blog.testmoden20.test6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import java.util.List;

import mchenys.net.csdn.blog.testmoden20.R;
import mchenys.net.csdn.blog.testmoden20.test6.model.Tag;
import mchenys.net.csdn.blog.testmoden20.test6.view.TagView;

/**
 * Created by mChenys on 2017/1/19.
 */
public class TestTagViewActivity2 extends AppCompatActivity {
    LinearLayout mLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_view2);
        mLayout = (LinearLayout) findViewById(R.id.fl_content);

        addTagView(new Tag.TagDesc("1","45",1,45));
        addTagView(new Tag.TagDesc("1","135",1,135));
        addTagView(new Tag.TagDesc("1","225",1,225));
        addTagView(new Tag.TagDesc("1","315",1,315));

//        List<Tag.TagDesc> descList1 = new ArrayList<>();
//        descList1.add(new Tag.TagDesc("1","180",2,180f));
//        descList1.add(new Tag.TagDesc("2","90.001",3,90.001f));
//        descList1.add(new Tag.TagDesc("3","269.99",1,269.999f));
//        addTagView(descList1);
    }

    public void addTagView(List<Tag.TagDesc> descList) {
        TagView tagView = new TagView(this);
        tagView.setTagDescList(descList);
        mLayout.addView(tagView);
    }

    public void addTagView(Tag.TagDesc desc) {
        TagView tagView = new TagView(this);
        tagView.setTagDesc(desc);
        mLayout.addView(tagView);
    }
}
