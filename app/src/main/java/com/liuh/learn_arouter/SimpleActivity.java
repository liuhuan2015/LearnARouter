package com.liuh.learn_arouter;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = PathConstants.PATH_JUMP_SIMPLE)
public class SimpleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
    }
}
