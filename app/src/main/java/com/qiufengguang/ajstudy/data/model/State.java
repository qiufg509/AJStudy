package com.qiufengguang.ajstudy.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * 页面状态
 *
 * @author qiufengguang
 * @since 2026/3/14 12:10
 */
public enum State implements Parcelable {
    LOADING,
    NO_NETWORK,
    EMPTY,
    ERROR;

    public static final Creator<State> CREATOR = new Creator<>() {
        @Override
        public State createFromParcel(Parcel in) {
            return State.valueOf(in.readString());
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name());
    }
}
