package mchenys.net.csdn.blog.testmoden20.test7;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import mchenys.net.csdn.blog.testmoden20.R;

/**
 * Created by mChenys on 2017/1/20.
 */
public class PreViewActivity extends AppCompatActivity {
    private static final String TAG = "PreViewActivity";
    private VideoView mVideoView;
    private ImageView mImageView;
    private String mFilepath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mVideoView = (VideoView) findViewById(R.id.vv);
        mImageView = (ImageView) findViewById(R.id.iv);
        mFilepath = getIntent().getStringExtra("path");
        TextView sizeTv = (TextView) findViewById(R.id.tv_size);

        Log.d(TAG, "path:" + mFilepath);
        if (TextUtils.isEmpty(mFilepath)) {
            Toast.makeText(PreViewActivity.this, "预览失败", Toast.LENGTH_SHORT).show();
            return;
        } else if (mFilepath.endsWith(".jpg")) {
            mImageView.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);

            mImageView.setImageBitmap(BitmapFactory.decodeFile(mFilepath));
        } else if (mFilepath.endsWith(".mp4")) {
            mImageView.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.setVideoPath(mFilepath);
            mVideoView.start();
        }
        File file = new File(mFilepath);
        sizeTv.setText("文件大小:" + (file.length() / (1024 * 1024.0)) + "Mb");

    }

    public void onDelete(View view) {
        if (TextUtils.isEmpty(mFilepath)) {
            Toast.makeText(PreViewActivity.this, "无效操作", Toast.LENGTH_SHORT).show();
        } else {
            File file = new File(mFilepath);
            if (file.delete()) {
                Toast.makeText(PreViewActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
