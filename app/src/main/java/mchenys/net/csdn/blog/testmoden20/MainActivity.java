package mchenys.net.csdn.blog.testmoden20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mchenys.net.csdn.blog.testmoden20.test1.TestListRecycleActivity;
import mchenys.net.csdn.blog.testmoden20.test2.TestTagViewActivity;

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
}
