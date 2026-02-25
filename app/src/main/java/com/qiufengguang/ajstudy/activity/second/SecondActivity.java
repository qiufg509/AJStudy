package com.qiufengguang.ajstudy.activity.second;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.fragment.second.SecondFragment;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;

/**
 * 二级页面
 *
 * @author qiufengguang
 * @since 2026/1/31 21:10
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        StatusBarUtil.makeStatusBarTransparent(this);
        if (savedInstanceState == null) {
            Bundle args = getIntent().getBundleExtra(Router.EXTRA_SECOND_PAGE);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, SecondFragment.newInstance(args))
                .commitNow();
        }
    }
}