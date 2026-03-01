package com.qiufengguang.ajstudy.fragment.base;

import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.card.empty.EmptyCard;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 基类Fragment对应的ViewModel
 *
 * @author qiufengguang
 * @since 2025/11/26 22:19
 */
public abstract class BaseViewModel extends ViewModel {
    protected Call<RawRespData> currentCall;

    @Override
    protected void onCleared() {
        super.onCleared();
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }
    }

    protected List<LayoutData<?>> fetchEmptyData() {
        SingleLayoutData<?> emptyCardData = LayoutDataFactory.createSingle(EmptyCard.LAYOUT_ID_2, null);
        List<LayoutData<?>> dataList = new ArrayList<>();
        dataList.add(emptyCardData);
        return dataList;
    }
}