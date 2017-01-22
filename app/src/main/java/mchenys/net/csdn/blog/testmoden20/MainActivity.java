package mchenys.net.csdn.blog.testmoden20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mchenys.net.csdn.blog.testmoden20.test1.TestListRecycleActivity;
import mchenys.net.csdn.blog.testmoden20.test2.TestTagViewActivity;
import mchenys.net.csdn.blog.testmoden20.test3.TestTitleMoveActivity;
import mchenys.net.csdn.blog.testmoden20.test4.TestDragGridViewActivity;
import mchenys.net.csdn.blog.testmoden20.test5.TestWindowShareActivity;
import mchenys.net.csdn.blog.testmoden20.test6.TestTagViewActivity2;
import mchenys.net.csdn.blog.testmoden20.test7.TestRecordViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testListRecycle(View view) {
        startActivity(new Intent(this,TestListRecycleActivity.class));
    }
    public void testTagView(View view) {
        startActivity(new Intent(this,TestTagViewActivity.class));
    }
    public void testTitleMove(View view) {
        startActivity(new Intent(this,TestTitleMoveActivity.class));
    }
    public void testDragView(View view) {
        startActivity(new Intent(this,TestDragGridViewActivity.class));
    }

    public void testShareWidow(View view) {
        startActivity(new Intent(this,TestWindowShareActivity.class));
    }

    public void testTagView2(View view) {
        startActivity(new Intent(this,TestTagViewActivity2.class));
    }
    public void testRecordView(View view) {
        startActivity(new Intent(this,TestRecordViewActivity.class));
    }
}
