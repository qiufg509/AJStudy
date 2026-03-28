package com.qiufengguang.ajstudy.activity.second;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.ActivitySecondBinding;
import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.fragment.second.SecondGridFragment;
import com.qiufengguang.ajstudy.fragment.second.SecondStaggeredFragment;
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
        ActivitySecondBinding binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtil.makeStatusBarTransparent(this);
        if (savedInstanceState == null) {
            Bundle args = getIntent().getBundleExtra(Router.EXTRA_DATA);
            String pageId = Router.PAGE_ID.SECOND_GRID;
            if (args != null) {
                pageId = args.getString(Router.EXTRA_ID);
            }
            BaseListFragment f;
            if (TextUtils.equals(pageId, Router.PAGE_ID.SECOND_STAGGERED)) {
                f = SecondStaggeredFragment.newInstance(args);
            } else {
                f = SecondGridFragment.newInstance(args);
            }
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, f)
                .commitNow();
        }
    }
}