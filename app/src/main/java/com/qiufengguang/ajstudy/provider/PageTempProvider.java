package com.qiufengguang.ajstudy.provider;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.savedstate.SavedStateRegistry;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PageTempProvider<T> implements SavedStateRegistry.SavedStateProvider {
    private static final String TAG = "PageTempProvider";
    private final Class<T> clz;

    public PageTempProvider(Class<T> clz) {
        this.clz = clz;
    }

    @NonNull
    @Override
    public Bundle saveState() {
        return new Bundle();
    }

    @Nullable
    private T restoreTempFile(Bundle bundle) {
        if (bundle == null || clz == null) {
            return null;
        }
        T instance = null;
        try {
            instance = clz.newInstance();
            for (String key : bundle.keySet()) {
                Field field = clz.getDeclaredField(key);
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                Object value = getValueFromBundle(bundle, key, fieldType);
                if (value != null) {
                    field.set(instance, value);
                }
            }
        } catch (IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            Log.e(TAG, "Cannot convert bundle to object" + e.getMessage());
        }
        return instance;
    }


    private static Object getValueFromBundle(Bundle bundle, String key, Class<?> fieldType) {
        try {
            if (fieldType == String.class) {
                return bundle.getString(key);
            } else if (fieldType == int.class || fieldType == Integer.class) {
                return bundle.getInt(key);
            } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                return bundle.getBoolean(key);
            } else if (fieldType == long.class || fieldType == Long.class) {
                return bundle.getLong(key);
            } else if (fieldType == double.class || fieldType == Double.class) {
                return bundle.getDouble(key);
            } else if (fieldType == float.class || fieldType == Float.class) {
                return bundle.getFloat(key);
            } else if (Parcelable.class.isAssignableFrom(fieldType)) {
                // 使用API 33及以上版本的getParcelable方法
                Method method = Bundle.class.getMethod("getParcelable", String.class, Class.class);
                return method.invoke(bundle, key, fieldType);
            } else if (Serializable.class.isAssignableFrom(fieldType)) {
                return bundle.getSerializable(key);
            } else {
                // 可根据需要添加更多类型支持
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Cannot fill " + key + " " + e.getMessage());
            return null;
        }
    }
}