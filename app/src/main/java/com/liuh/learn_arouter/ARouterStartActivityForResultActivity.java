package com.liuh.learn_arouter;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = PathConstants.PATH_STARTACTIVITY_FORRESULT)
public class ARouterStartActivityForResultActivity extends AppCompatActivity {

    @Autowired(name = "name")
    String name;

    @Autowired(name = "age")
    int age;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arouter_start_for_result);
        ARouter.getInstance().inject(this);

        tv = findViewById(R.id.tv);
        tv.setText(name + " " + age);
        setResult(Activity.RESULT_OK);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
