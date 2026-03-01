package com.qiufengguang.ajstudy.activity.detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * 详情页适配器
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class DetailFragmentAdapter extends FragmentStateAdapter {

    private List<Fragment> fragments;

    public DetailFragmentAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    public void addFragments(List<Fragment> newFragments) {
        if (this.fragments == null) {
            this.fragments = newFragments;
        } else {
            this.fragments.addAll(newFragments);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments != null ? fragments.size() : 0;
    }
}