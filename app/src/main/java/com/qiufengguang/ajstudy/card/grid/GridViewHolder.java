package com.qiufengguang.ajstudy.card.grid;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.GridCardBean;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 格网卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/19 14:03
 */
public class GridViewHolder extends RecyclerView.ViewHolder {

    private GridCardWrapper cardWrapper;

    private WeakReference<LifecycleOwner> lifecycleOwnerRef;

    public GridViewHolder(@NonNull RecyclerView itemView, @NonNull LifecycleOwner lifecycleOwner) {
        super(itemView);
        this.lifecycleOwnerRef = new WeakReference<>(lifecycleOwner);

        int spacing = itemView.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_s);
        cardWrapper = new GridCardWrapper.Builder()
            .setRecyclerView(itemView)
            .setItemType(GridCardWrapper.TYPE_TEXT)
            .setSpacing(spacing)
            .setListener(bean -> {
//                NavController navController = Navigation.findNavController(requireActivity(),
//                    R.id.nav_host_fragment_activity_main);
//                Bundle bundle = new Bundle();
//                bundle.putString("Course", bean.getNavigatePage());
//                navController.navigate(R.id.navigation_know_how, bundle);
            })
            .create();
        cardWrapper.show();
        observeLifecycle();
    }

    public void bind(List<GridCardBean> beans) {
        if (beans != null && cardWrapper != null) {
            cardWrapper.setData(beans);
        }
    }

    private void observeLifecycle() {
        LifecycleOwner lifecycleOwner = lifecycleOwnerRef.get();
        if (lifecycleOwner == null) {
            return;
        }
        lifecycleOwner.getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                if (cardWrapper != null) {
                    cardWrapper.release();
                    cardWrapper = null;
                }
                lifecycleOwner.getLifecycle().removeObserver(this);
            }
        });
    }

    public void cleanup() {
        if (cardWrapper != null) {
            cardWrapper.release();
            cardWrapper = null;
        }
        lifecycleOwnerRef = null;
    }
}