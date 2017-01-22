package mchenys.net.csdn.blog.testmoden20.test2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mchenys.net.csdn.blog.testmoden20.R;

/**
 * Created by mChenys on 2017/1/13.
 */
public class TestTagViewActivity extends AppCompatActivity {
    private TagLayout mLayout;
    private boolean flag;
    private TagDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_view);
        mLayout = (TagLayout) findViewById(R.id.ll_layout);
        mLayout.setTagList(getData());
        mLayout.setEnableLimitMove(true);
        mLayout.setEnableAutoChangeStyle(false);
        mLayout.setOnlyCenterChoose(true);
        mLayout.getBackgroundView().setImageResource(R.drawable.bg_hzp);
        mDialog = new TagDialog(this);
        mLayout.setOnTagOperationCallback(new TagLayout.OnTagOperationCallback() {
            @Override
            public void onAdd( float ratioX,  float ratioY) {
                Toast.makeText(TestTagViewActivity.this, "新增", Toast.LENGTH_SHORT).show();
                final Tag tag = new Tag();
                tag.setRatioX(ratioX);
                tag.setRatioY(ratioY);
                //新增
                mDialog.show(tag, new TagDialog.Callback() {
                    @Override
                    public void onCommit() {
                        mLayout.addTag(tag);
                    }
                });
            }

            @Override
            public void onEdit(final Tag tag) {
                //修改
                Toast.makeText(TestTagViewActivity.this, "修改", Toast.LENGTH_SHORT).show();
                mDialog.edit(tag, new TagDialog.Callback() {
                    @Override
                    public void onCommit() {
                        mLayout.editTagView(tag);
                    }
                });
            }

            @Override
            public void onDelete(BaseTagView currTagView) {
                mLayout.removeView(currTagView);
            }

        });
    }

    public List<Tag> getData() {
        List<Tag> tagList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Tag tag = new Tag();
            if (i % 2 == 0) {
                tag.setTag1("tagName" + i, "tagValue" + i);
                tag.setType(2);
            } else if (i % 3 == 0) {
                tag.setTag1("tagName" + i, "tagValue" + i);
                tag.setTag2("tagName" + i, "tagValue" + i);
                tag.setType(2);
            } else {
                tag.setTag1("tagName" + i, "tagValue" + i);
                tag.setTag2("tagName" + i, "tagValue" + i);
                tag.setTag3("tagName" + i, "tagValue" + i);
                tag.setType(2);
            }
            tag.setRatioX(i * 0.1f);
            tag.setRatioY(i * 0.1f);
            tagList.add(tag);
        }
        return tagList;
    }

    public void onSaveTo(View view) {
        ArrayList<Tag> tagList = mLayout.getAllTags();
        Intent intent = new Intent(this, NextTagViewActivity.class);
        intent.putParcelableArrayListExtra("tagList", tagList);
        startActivity(intent);
    }
}
