package mchenys.net.csdn.blog.testmoden20.test5;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import mchenys.net.csdn.blog.testmoden20.R;

/**
 * Created by mChenys on 2017/1/18.
 */
public class TestWindowShareActivity extends AppCompatActivity {

    private static final String TAG = "TestWindowShareActivity";
    public ImageView target;
    private FrameLayout sourceLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_window_share);
        sourceLayout = (FrameLayout) findViewById(R.id.fl_soure_layout);
        target = (ImageView) findViewById(R.id.iv_target);

        MyApplication app = (MyApplication) getApplication();
        app.setTarget(target);
    }


    public void moveUp(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 1.0f, 0.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 1.0f, 0.5f);
        ObjectAnimator transY = ObjectAnimator.ofFloat(target, "translationY", 0, -0.25f * getResources().getDisplayMetrics().heightPixels);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY).with(transY);
        animSet.setDuration(500);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
        animSet.start();
    }

    public void openNext(View view) {
        if (target.getScaleX() == 0.5f) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 0.5f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 0.5f, 1f);
            ObjectAnimator transY = ObjectAnimator.ofFloat(target, "translationY", -0.25f * getResources().getDisplayMetrics().heightPixels, 0);

            AnimatorSet animSet = new AnimatorSet();
            animSet.play(scaleX).with(scaleY).with(transY);
            animSet.setDuration(500);
            animSet.start();
            animSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Intent intent = new Intent(TestWindowShareActivity.this, NextPageActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                }
            });
        } else {
            Intent intent = new Intent(TestWindowShareActivity.this, NextPageActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        MyApplication app = (MyApplication) getApplication();
        sourceLayout.addView(app.getTarget());
    }

    public void restore(View view) {
        if (target.getScaleX() == 0.5f) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 0.5f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 0.5f, 1f);
            ObjectAnimator transY = ObjectAnimator.ofFloat(target, "translationY", -0.25f * getResources().getDisplayMetrics().heightPixels, 0);

            AnimatorSet animSet = new AnimatorSet();
            animSet.play(scaleX).with(scaleY).with(transY);
            animSet.setDuration(500);
            animSet.start();
        }

    }


}
