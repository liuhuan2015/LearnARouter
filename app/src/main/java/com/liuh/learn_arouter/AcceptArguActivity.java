package com.liuh.learn_arouter;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = PathConstants.PATH_JUMP_ACCEPT_ARGU)
public class AcceptArguActivity extends BaseActivity {

    @Autowired(name = "username")
    String name;
    @Autowired(name = "userage")
    int age;
    @Autowired(name = "student")
    Student student;

    TextView mTvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_argu);

        ARouter.getInstance().inject(this);

        mTvContent = findViewById(R.id.tv_content);

        Log.e("-----", "姓名：" + name + " 年龄：" + age + " student: " + student);

        mTvContent.setText("姓名：" + name + " 年龄：" + age + " student: " + student);


    }
}
