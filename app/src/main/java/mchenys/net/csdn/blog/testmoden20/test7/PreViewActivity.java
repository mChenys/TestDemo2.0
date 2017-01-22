package mchenys.net.csdn.blog.testmoden20.test7;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import mchenys.net.csdn.blog.testmoden20.R;

/**
 * Created by mChenys on 2017/1/20.
 */
public class PreViewActivity extends AppCompatActivity {
    private static final String TAG = "PreViewActivity";
    private VideoView mVideoView;
    private ImageView mImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mVideoView = (VideoView) findViewById(R.id.vv);
        mImageView = (ImageView) findViewById(R.id.iv);
        String filepath = getIntent().getStringExtra("path");
        Log.d(TAG, "path:" + filepath);
        if(TextUtils.isEmpty(filepath)){
            Toast.makeText(PreViewActivity.this, "预览失败", Toast.LENGTH_SHORT).show();
        } else if (filepath.endsWith(".jpg")) {
            mImageView.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(filepath));
        } else if (filepath.endsWith(".mp4")) {
            mImageView.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.setVideoPath(filepath);
            mVideoView.start();
        }
    }
}
