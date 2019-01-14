package com.liuh.learn_arouter;

import android.net.Uri;
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
//                ARouter.getInstance().build("/app/SimpleActivity").withTransition(R.anim.enter_anim, R.anim.exit_anim).navigation();
            }
        });


        findViewById(R.id.btn_jump_with_argu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build(PathConstants.PATH_JUMP_ACCEPT_ARGU)
                        .withString("username", "zhangsan")
                        .withInt("userage", 3)
                        .withParcelable("student", new Student("lisi", 5))
                        .navigation();
            }
        });

        findViewById(R.id.btn_jump_by_uri).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(PathConstants.PATH_JUMP_BY_URI);

                ARouter.getInstance().build(uri).navigation();
            }
        });
    }
}
