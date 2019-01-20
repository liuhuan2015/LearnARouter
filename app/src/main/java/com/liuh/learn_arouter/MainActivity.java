package com.liuh.learn_arouter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
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

        findViewById(R.id.btn_jump_with_interceptor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(PathConstants.PATH_URL_INTERCEPTOR)
                        .navigation(MainActivity.this,
                                new NavigationCallback() {
                                    @Override
                                    public void onFound(Postcard postcard) {
                                        // 路由目标被发现时调用
                                        Log.e("-----", "onFound...");
                                        String group = postcard.getGroup();
                                        String path = postcard.getPath();
                                        Log.e("-----", "group: " + group + " path: " + path);
                                    }

                                    @Override
                                    public void onLost(Postcard postcard) {
                                        Log.e("-----", "onLost...");
                                    }

                                    @Override
                                    public void onArrival(Postcard postcard) {
                                        // 路由到达后调用
                                        Log.e("-----", "onArrival...");

                                    }

                                    @Override
                                    public void onInterrupt(Postcard postcard) {
                                        // 路由拦截时调用
                                        Log.e("-----", "onInterrupt...");
                                    }
                                });
            }
        });

        findViewById(R.id.btn_mock_startActivityForResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(PathConstants.PATH_STARTACTIVITY_FORRESULT)
                        .withString("name", "zhangsan")
                        .withInt("age", 3)
                        .navigation(MainActivity.this, 123);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 123:
                Log.e("-----", "onActivityResult---接收到第二个界面返回来的数据");
                break;
            default:
                break;
        }

    }
}
