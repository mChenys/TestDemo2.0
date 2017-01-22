package mchenys.net.csdn.blog.testmoden20.test2;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mchenys.net.csdn.blog.testmoden20.R;

/**
 * Created by mChenys on 2017/1/15.
 */
public class TagDialog extends Dialog {
    private EditText mName1Edt;
    private EditText mValue1Edt;
    private EditText mName2Edt;
    private EditText mValue2Edt;
    private EditText mName3Edt;
    private EditText mValue3Edt;

    private Button mCancelBtn;
    private Button mOkBtn;

    private Tag mCurrEditTag;

    public interface Callback {
        void onCommit();
    }

    private Callback mCallback;

    public TagDialog(Context context) {
        super(context);
        setContentView(R.layout.tag_add_dialog);
        getWindow().setWindowAnimations(R.style.dialogstyle);
        initView();
        initListener();
    }

    private void initView() {
        mName1Edt = (EditText) findViewById(R.id.edt_name1);
        mValue1Edt = (EditText) findViewById(R.id.edt_value1);
        mName2Edt = (EditText) findViewById(R.id.edt_name2);
        mValue2Edt = (EditText) findViewById(R.id.edt_value2);
        mName3Edt = (EditText) findViewById(R.id.edt_name3);
        mValue3Edt = (EditText) findViewById(R.id.edt_value3);
        mCancelBtn = (Button) findViewById(R.id.btn_cancel);
        mOkBtn = (Button) findViewById(R.id.btn_ok);
    }

    private void initListener() {
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (null != mCallback && null != mCurrEditTag) {
                    mCurrEditTag.setTag1(getName1(), getValue1());
                    mCurrEditTag.setTag2(getName2(), getValue2());
                    mCurrEditTag.setTag3(getName3(), getValue3());
                    mCallback.onCommit();
                }
            }
        });
    }

    public void show(Tag tag, Callback callback) {
        this.mCurrEditTag = tag;
        setTitle("添加标签");
        mName1Edt.setText("");
        mValue1Edt.setText("");
        mName2Edt.setText("");
        mValue2Edt.setText("");
        mName3Edt.setText("");
        mValue3Edt.setText("");
        mCallback = callback;
        super.show();
    }

    public void edit(Tag tag, Callback callback) {
        this.mCurrEditTag = tag;
        setTitle("修改标签");
        setTag1(tag.tagName1, tag.tagValue1);
        setTag2(tag.tagName2, tag.tagValue2);
        setTag3(tag.tagName3, tag.tagValue3);
        mCallback = callback;
        super.show();
    }

    public String getName1() {
        return mName1Edt.getText().toString().trim();
    }

    public String getValue1() {
        return mValue1Edt.getText().toString().trim();
    }

    public String getName2() {
        return mName2Edt.getText().toString().trim();
    }

    public String getValue2() {
        return mValue2Edt.getText().toString().trim();
    }

    public String getName3() {
        return mName3Edt.getText().toString().trim();
    }

    public String getValue3() {
        return mValue3Edt.getText().toString().trim();
    }

    public void setTag1(String name, String value) {
        mName1Edt.setText(name);
        mValue1Edt.setText(value);
    }

    public void setTag2(String name, String value) {
        mName2Edt.setText(name);
        mValue2Edt.setText(value);
    }

    public void setTag3(String name, String value) {
        mName3Edt.setText(name);
        mValue3Edt.setText(value);
    }
}
