package mchenys.net.csdn.blog.testmoden20.test7;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import mchenys.net.csdn.blog.testmoden20.R;

/**
 * Created by mChenys on 2017/1/20.
 */
public class TestRecordViewActivity extends AppCompatActivity {
    private RecordView mRecordView;
    private boolean isRecord;
    private TextView mTimeTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view);
        mRecordView = (RecordView) findViewById(R.id.recordView);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mRecordView.setMaxDuration(6);
        mRecordView.setOnRecordCallback(new RecordView.OnRecordCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onProgress(int total, int curr) {
                mTimeTv.setText(curr + "/" + total);
            }
        });
    }

    public void takePhoto(View view) {
        mRecordView.startCapture();
        isRecord = false;

    }

    public void startRecord(View view) {
        mRecordView.startRecord();
        isRecord = true;
    }

    public void switchCamera(View view) {
        mRecordView.switchCamera();
    }

    public void preView(View view) {
        String path = "";
        if (isRecord) {
            mRecordView.stopRecord();
            path = mRecordView.getRecordFilePath();
        } else {
            path = mRecordView.getRotateCaptureFilePath();
        }
        Intent intent = new Intent(this, PreViewActivity.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }
}
