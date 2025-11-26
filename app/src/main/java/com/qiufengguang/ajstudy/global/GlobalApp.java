package com.qiufengguang.ajstudy.global;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class GlobalApp extends Application implements ViewModelStoreOwner {
    private final ViewModelStore viewModelStore = new ViewModelStore();

    private static WeakReference<Context> contextReference;

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contextReference = new WeakReference<>(this.getApplicationContext());
    }

    public static Context getContext() {
        return Objects.isNull(contextReference) ? null : contextReference.get();
    }

    @Override
    public void onTerminate() {
        viewModelStore.clear();
        contextReference.clear();
        super.onTerminate();
    }
}
