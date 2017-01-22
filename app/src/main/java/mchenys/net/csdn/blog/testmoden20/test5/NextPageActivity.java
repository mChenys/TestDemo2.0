package mchenys.net.csdn.blog.testmoden20.test5;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import mchenys.net.csdn.blog.testmoden20.R;

/**
 * Created by mChenys on 2017/1/18.
 */
public class NextPageActivity extends AppCompatActivity {
    private static final String TAG = "NextPageActivity";
    FrameLayout layout;
    int lastX, lastY;
    int screenWidth, screenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_page);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.taobao.com/");

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        layout = (FrameLayout) findViewById(R.id.fl_layout);
        MyApplication app = (MyApplication) getApplication();
        View target = app.getTarget();

        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layout.getLayoutParams();
                lp.leftMargin = screenWidth - layout.getWidth();
                layout.requestLayout();


            }
        });
        layout.addView(target);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //获取到手指处的横坐标和纵坐标
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //计算移动的距离
                        int offX = x - lastX;
                        int offY = y - lastY;
                        int newLeft = layout.getLeft() + offX;
                        int newTop = layout.getTop() + offY;
                        if (newLeft < 0) newLeft = 0;
                        if (newLeft > screenWidth - layout.getWidth())
                            newLeft = screenWidth - layout.getWidth();
                        if (newTop < 0) newTop = 0;
                        if (newTop > screenHeight - layout.getHeight())
                            newTop = screenHeight - layout.getHeight();

                        ViewGroup.MarginLayoutParams lp =
                                (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
                        lp.leftMargin = newLeft;
                        lp.topMargin = newTop;
                        layout.requestLayout();
                        break;
                }
                return false;
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
