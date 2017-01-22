package mchenys.net.csdn.blog.testmoden20.test5;

import android.app.Application;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mChenys on 2017/1/19.
 */
public class MyApplication extends Application {

    public View target;

    public void setTarget(View target) {
        this.target = target;
    }

    public View getTarget() {
        if (null != target) {
            ViewGroup parent = (ViewGroup) target.getParent();
            if (null != parent) {
                parent.removeView(target);
            }
        }
        return target;
    }
}
