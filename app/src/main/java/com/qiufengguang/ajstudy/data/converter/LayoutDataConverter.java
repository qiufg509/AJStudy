package com.qiufengguang.ajstudy.data.converter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.qiufengguang.ajstudy.card.article.ArticleCard;
import com.qiufengguang.ajstudy.card.banner.BannerCard;
import com.qiufengguang.ajstudy.card.empty.EmptyCard;
import com.qiufengguang.ajstudy.card.graphicl.GraphicCardL;
import com.qiufengguang.ajstudy.card.graphiclgrid.GraphicLGridCard;
import com.qiufengguang.ajstudy.card.graphicm.GraphicCardM;
import com.qiufengguang.ajstudy.card.graphics.GraphicCardS;
import com.qiufengguang.ajstudy.card.grid.GridCard;
import com.qiufengguang.ajstudy.card.luckywheel.LuckyWheelCard;
import com.qiufengguang.ajstudy.card.normal.NormalCard;
import com.qiufengguang.ajstudy.card.series.SeriesCard;
import com.qiufengguang.ajstudy.card.setting.SettingCard;
import com.qiufengguang.ajstudy.card.title.TitleCard;
import com.qiufengguang.ajstudy.card.user.SimpleUserCard;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.model.ArticleCardBean;
import com.qiufengguang.ajstudy.data.model.BannerBean;
import com.qiufengguang.ajstudy.data.model.GraphicCardBean;
import com.qiufengguang.ajstudy.data.model.GridCardBean;
import com.qiufengguang.ajstudy.data.model.LuckyWheelCardBean;
import com.qiufengguang.ajstudy.data.model.NormalCardBean;
import com.qiufengguang.ajstudy.data.model.SeriesCardBean;
import com.qiufengguang.ajstudy.data.model.SettingCardBean;
import com.qiufengguang.ajstudy.data.model.User;
import com.qiufengguang.ajstudy.data.remote.dto.LayoutDataDTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 卡片数据转换器
 *
 * @author qiufengguang
 * @since 2026/2/26 15:07
 */
public class LayoutDataConverter {
    private static final String TAG = "LayoutDataConverter";

    public static List<LayoutData<?>> convert(Gson gson, List<LayoutDataDTO> dtoList) {
        List<LayoutData<?>> result = new ArrayList<>();
        for (LayoutDataDTO dto : dtoList) {
            if (dto.getDataList() == null || dto.getDataList().isEmpty()) {
                continue;
            }
            int layoutId = dto.getLayoutId();
            String title = dto.getName();
            String detailId = dto.getDetailId();

            LayoutData<?> layoutData = parseByLayoutId(gson, layoutId, dto.getDataList(), title, detailId);
            if (layoutData != null) {
                result.add(layoutData);
            }
        }
        return result;
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    private static LayoutData<?> parseByLayoutId(
        Gson gson,
        int layoutId,
        JsonArray dataArray,
        String title,
        String detailId
    ) {
        try {
            switch (layoutId) {
                case BannerCard.LAYOUT_ID:
                    Type bannerType = new TypeToken<List<BannerBean>>() {
                    }.getType();
                    List<BannerBean> bannerList = gson.fromJson(dataArray, bannerType);
                    return LayoutDataFactory.createCollection(layoutId, bannerList);

                case GridCard.LAYOUT_ID:
                    Type gridType = new TypeToken<List<GridCardBean>>() {
                    }.getType();
                    List<GridCardBean> gridList = gson.fromJson(dataArray, gridType);
                    return LayoutDataFactory.createCollection(layoutId, gridList);

                case SeriesCard.LAYOUT_ID:
                    Type seriesType = new TypeToken<List<SeriesCardBean>>() {
                    }.getType();
                    List<SeriesCardBean> seriesList = gson.fromJson(dataArray, seriesType);
                    return LayoutDataFactory.createCollection(layoutId, seriesList, title, detailId);

                case LuckyWheelCard.LAYOUT_ID:
                    Type wheelType = new TypeToken<List<LuckyWheelCardBean>>() {
                    }.getType();
                    List<LuckyWheelCardBean> wheelList = gson.fromJson(dataArray, wheelType);
                    return LayoutDataFactory.createCollection(layoutId, wheelList, title);

                case ArticleCard.LAYOUT_ID:
                    ArticleCardBean articleBean = gson.fromJson(dataArray.get(0), ArticleCardBean.class);
                    return LayoutDataFactory.createSingle(layoutId, articleBean);

                case GraphicCardL.LAYOUT_ID:
                case GraphicCardM.LAYOUT_ID:
                case GraphicCardS.LAYOUT_ID:
                    GraphicCardBean graphicBean = gson.fromJson(dataArray.get(0), GraphicCardBean.class);
                    return LayoutDataFactory.createSingle(layoutId, graphicBean);

                case GraphicLGridCard.LAYOUT_ID:
                    Type graphicGridType = new TypeToken<List<GraphicCardBean>>() {
                    }.getType();
                    List<GraphicCardBean> graphicGridList = gson.fromJson(dataArray, graphicGridType);
                    return LayoutDataFactory.createCollection(layoutId, graphicGridList, title);

                case NormalCard.LAYOUT_ID:
                    Type normalType = new TypeToken<NormalCardBean>() {
                    }.getType();
                    NormalCardBean normalCardBean = gson.fromJson(dataArray.get(0), normalType);
                    return LayoutDataFactory.createSingle(layoutId, normalCardBean, title, detailId);

                case SimpleUserCard.LAYOUT_ID:
                    Type userType = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(dataArray.get(0), userType);
                    return LayoutDataFactory.createSingle(layoutId, user);

                case SettingCard.LAYOUT_ID:
                    Type settingType = new TypeToken<List<SettingCardBean>>() {
                    }.getType();
                    List<SettingCardBean> settingList = gson.fromJson(dataArray, settingType);
                    return LayoutDataFactory.createCollection(SettingCard.LAYOUT_ID, settingList);

                case TitleCard.LAYOUT_ID:
                    return LayoutDataFactory.createSingle(layoutId, null, title, detailId);

                case EmptyCard.LAYOUT_ID_1:
                case EmptyCard.LAYOUT_ID_2:
                    return LayoutDataFactory.createSingle(layoutId, null);

                default:
                    Log.w(TAG, "Unknown layoutId: " + layoutId);
                    return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Parse error for layoutId: " + layoutId, e);
            return null;
        }
    }
}