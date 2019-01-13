package com.liuh.learn_arouter;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path = "/app/MainActivity")
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ARouter.getInstance().inject(this);

        findViewById(R.id.btn_goto_simple_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/app/SimpleActivity").navigation();
            }
        });


        findViewById(R.id.btn_jump_with_argu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(PathConstants.path_jump_accept_argu)
                        .withString("username", "zhangsan")
                        .withInt("userage", 3)
                        .withParcelable("student", new Student("lisi", 5))
                        .navigation();
            }
        });

    }
}
