package mchenys.net.csdn.blog.testmoden20.test7;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import mchenys.net.csdn.blog.testmoden20.R;

/**
 * Created by mChenys on 2017/1/20.
 */
public class TestRecordViewActivity extends AppCompatActivity implements View.OnClickListener {
    private RecordView mRecordView;
    private TextView mTimeTv;
    private SwitchLayout mSwitchLayout;
    private ImageView mFlashView;
    private ImageView mSwitchCameraIv;
    private ImageButton mCameraShutterButton;
    private ImageButton mRecordShutterButton;
    private boolean isRecording;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view);
        initView();
        initListener();
    }

    private void initView() {
        mRecordView = (RecordView) findViewById(R.id.recordView);
        mFlashView = (ImageView) findViewById(R.id.iv_flash);
        mSwitchCameraIv = (ImageView) findViewById(R.id.btn_switch_camera);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mRecordView.setMaxDuration(6);
        mSwitchLayout = (SwitchLayout) findViewById(R.id.switchLayout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setMax(6);
    }

    public void initListener() {
        mRecordView.setOnRecordCallback(new RecordView.OnRecordCallback() {
            @Override
            public void onFinish(String filePath) {
                preView(filePath);
                mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_record);
            }

            @Override
            public void onProgress(int total, int curr) {
                mTimeTv.setText(curr + "/" + total);
                mProgressBar.setProgress(curr);
            }
        });
        mRecordView.setOnCaptureCallback(new RecordView.OnCaptureCallback() {
            @Override
            public void onFinish(String filePath) {
                preView(filePath);
            }
        });
        mCameraShutterButton = (ImageButton) findViewById(R.id.btn_shutter_camera);
        mRecordShutterButton = (ImageButton) findViewById(R.id.btn_shutter_record);
        mCameraShutterButton.setOnClickListener(this);
        mRecordShutterButton.setOnClickListener(this);
        mSwitchCameraIv.setOnClickListener(this);
        mFlashView.setOnClickListener(this);
        mSwitchLayout.setOnSwitchCallback(new SwitchLayout.OnSwitchCallback() {
            @Override
            public void onLeft(View lv, View rv) {
                ((TextView) rv).setTextColor(Color.YELLOW);
                ((TextView) lv).setTextColor(Color.WHITE);
                //拍照
                mFlashView.setVisibility(View.VISIBLE);
                mTimeTv.setVisibility(View.GONE);
                mCameraShutterButton.setVisibility(View.VISIBLE);
                mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_record);
                mRecordShutterButton.setVisibility(View.GONE);
                mRecordView.stopRecord();
                isRecording=false;
            }

            @Override
            public void onRight(View lv, View rv) {
                ((TextView) rv).setTextColor(Color.WHITE);
                ((TextView) lv).setTextColor(Color.YELLOW);
                //录像
                mFlashView.setVisibility(View.GONE);
                mTimeTv.setVisibility(View.VISIBLE);
                mCameraShutterButton.setVisibility(View.GONE);
                mRecordShutterButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_flash://切换闪关灯
                switchFlash();
                break;
            case R.id.btn_switch_camera:
                mRecordView.switchCamera();//切换摄像头
                break;
            case R.id.btn_shutter_camera://拍照
                takePhoto();
                break;
            case R.id.btn_shutter_record://录像
                if (!isRecording) {
                    startRecord();
                } else {
                    stopRecord();
                }
                isRecording = !isRecording;

                break;
        }
    }

    private int lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (event.getRawX() - lastX);
                // 右滑
                if (deltaX > 50) {
                    mSwitchLayout.turnRight();
                }
                // 左滑
                else if (deltaX < -50) {
                    mSwitchLayout.turnLeft();
                }

        }

        return false;
    }

    public void switchFlash() {
        switch (mRecordView.switchFlashMode()) {
            case ON:
                mFlashView.setImageResource(R.drawable.btn_flash_on);
                break;
            case OFF:
                mFlashView.setImageResource(R.drawable.btn_flash_off);
                break;
            case TORCH:
                mFlashView.setImageResource(R.drawable.btn_flash_torch);
                break;
            case AUTO:
                mFlashView.setImageResource(R.drawable.btn_flash_auto);
                break;
        }

    }

    public void takePhoto() {
        mRecordView.startCapture();

    }

    public void startRecord() {
        mRecordView.startRecord();
        mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_recording);
    }

    public void stopRecord() {
        mRecordView.stopRecord();
        mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_record);
    }

    public void preView(String path) {
        Intent intent = new Intent(this, PreViewActivity.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }
}
