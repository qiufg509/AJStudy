package com.qiufengguang.ajstudy.data.converter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.qiufengguang.ajstudy.card.about.AboutCard;
import com.qiufengguang.ajstudy.card.article.ArticleCard;
import com.qiufengguang.ajstudy.card.banner.BannerCard;
import com.qiufengguang.ajstudy.card.brief.BriefCard;
import com.qiufengguang.ajstudy.card.comment.CommentCard;
import com.qiufengguang.ajstudy.card.empty.EmptyCard;
import com.qiufengguang.ajstudy.card.graphicl.GraphicCardL;
import com.qiufengguang.ajstudy.card.graphiclgrid.GraphicLGridCard;
import com.qiufengguang.ajstudy.card.graphicm.GraphicCardM;
import com.qiufengguang.ajstudy.card.graphics.GraphicCardS;
import com.qiufengguang.ajstudy.card.grid.GridCard;
import com.qiufengguang.ajstudy.card.luckywheel.LuckyWheelCard;
import com.qiufengguang.ajstudy.card.normal.NormalCard;
import com.qiufengguang.ajstudy.card.recommend.RecommendCard;
import com.qiufengguang.ajstudy.card.screenshot.ScreenshotCard;
import com.qiufengguang.ajstudy.card.series.SeriesCard;
import com.qiufengguang.ajstudy.card.setting.SettingCard;
import com.qiufengguang.ajstudy.card.text.TextCard;
import com.qiufengguang.ajstudy.card.title.TitleCard;
import com.qiufengguang.ajstudy.card.user.SimpleUserCard;
import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.model.AboutCardBean;
import com.qiufengguang.ajstudy.data.model.ArticleCardBean;
import com.qiufengguang.ajstudy.data.model.BannerBean;
import com.qiufengguang.ajstudy.data.model.BriefCardBean;
import com.qiufengguang.ajstudy.data.model.CommentCardBean;
import com.qiufengguang.ajstudy.data.model.DetailAppData;
import com.qiufengguang.ajstudy.data.model.DetailHead;
import com.qiufengguang.ajstudy.data.model.GraphicCardBean;
import com.qiufengguang.ajstudy.data.model.GridCardBean;
import com.qiufengguang.ajstudy.data.model.LuckyWheelCardBean;
import com.qiufengguang.ajstudy.data.model.NormalCardBean;
import com.qiufengguang.ajstudy.data.model.RecommendCardBean;
import com.qiufengguang.ajstudy.data.model.ScreenshotCardBean;
import com.qiufengguang.ajstudy.data.model.SeriesCardBean;
import com.qiufengguang.ajstudy.data.model.SettingCardBean;
import com.qiufengguang.ajstudy.data.model.TextCardBean;
import com.qiufengguang.ajstudy.data.model.User;
import com.qiufengguang.ajstudy.data.remote.dto.LayoutDataDTO;
import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 卡片数据转换器
 *
 * @author qiufengguang
 * @since 2026/2/26 15:07
 */
public class LayoutDataConverter {
    private static final String TAG = "LayoutDataConverter";

    public static PageData convert(Gson gson, RawRespData rawRespData) {
        PageData pageData = new PageData();
        pageData.setCount(rawRespData.getCount());
        pageData.setHasNextPage(rawRespData.getHasNextPage());
        pageData.setName(rawRespData.getName());
        pageData.setTotalPages(rawRespData.getTotalPages());
        pageData.setTabs(rawRespData.getTabs());
        pageData.setRtnCode(rawRespData.getRtnCode());
        List<LayoutData<?>> dataList = new ArrayList<>();
        for (LayoutDataDTO dto : rawRespData.getLayoutData()) {
            if (dto.getDataList() == null || dto.getDataList().isEmpty()) {
                continue;
            }
            int layoutId = dto.getLayoutId();
            String title = dto.getName();
            String detailId = dto.getDetailId();

            List<LayoutData<?>> layoutList = parseByLayoutId(gson, layoutId, dto.getDataList());
            if (layoutList != null && !layoutList.isEmpty()) {
                dataList.addAll(layoutList);
            } else {
                LayoutData<?> layoutData = parseByLayoutId(
                    gson, layoutId, dto.getDataList(), title, detailId
                );
                if (layoutData != null) {
                    dataList.add(layoutData);
                }
            }
        }
        pageData.setLayoutData(dataList);
        return pageData;
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    private static List<LayoutData<?>> parseByLayoutId(
        Gson gson,
        int layoutId,
        JsonArray dataArray
    ) {
        try {
            List<BaseCardBean> beans;
            switch (layoutId) {
//                case NormalCard.LAYOUT_ID:
//                    Type normalType = new TypeToken<NormalCardBean>() {
//                    }.getType();
//                    NormalCardBean normalCardBean = gson.fromJson(dataArray.get(0), normalType);
//                    return LayoutDataFactory.createSingle(layoutId, normalCardBean, title, detailId);

                case CommentCard.LAYOUT_ID:
                    Type commentType = new TypeToken<List<CommentCardBean>>() {
                    }.getType();
                    beans = gson.fromJson(dataArray, commentType);
                    break;
                case RecommendCard.LAYOUT_ID:
                    Type recommendType = new TypeToken<List<RecommendCardBean>>() {
                    }.getType();
                    beans = gson.fromJson(dataArray, recommendType);
                    break;

                default:
                    return null;
            }
            return Optional.ofNullable(beans)
                .orElse(Collections.emptyList())
                .stream()
                .map(bean ->
                    LayoutDataFactory.createSingle(layoutId, bean))
                .collect(Collectors.toList());
        } catch (Exception e) {
            Log.e(TAG, "Parse error for layoutId: " + layoutId, e);
            return null;
        }
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

                case DetailHead.LAYOUT_ID:
                    Type detailHeadType = new TypeToken<DetailHead>() {
                    }.getType();
                    DetailHead detailHead = gson.fromJson(dataArray.get(0), detailHeadType);
                    return LayoutDataFactory.createSingle(DetailHead.LAYOUT_ID, detailHead);

                case DetailAppData.LAYOUT_ID:
                    Type detailAppDataType = new TypeToken<DetailAppData>() {
                    }.getType();
                    DetailAppData detailAppData = gson.fromJson(dataArray.get(0), detailAppDataType);
                    return LayoutDataFactory.createSingle(DetailAppData.LAYOUT_ID, detailAppData);

                case ScreenshotCard.LAYOUT_ID:
                    Type screenshotType = new TypeToken<ScreenshotCardBean>() {
                    }.getType();
                    ScreenshotCardBean screenshotList = gson.fromJson(dataArray.get(0), screenshotType);
                    return LayoutDataFactory.createSingle(ScreenshotCard.LAYOUT_ID, screenshotList);

                case BriefCard.LAYOUT_ID:
                    Type briefType = new TypeToken<BriefCardBean>() {
                    }.getType();
                    BriefCardBean briefCardBean = gson.fromJson(dataArray.get(0), briefType);
                    return LayoutDataFactory.createSingle(BriefCard.LAYOUT_ID, briefCardBean);

                case AboutCard.LAYOUT_ID:
                    Type aboutType = new TypeToken<AboutCardBean>() {
                    }.getType();
                    AboutCardBean aboutCardBean = gson.fromJson(dataArray.get(0), aboutType);
                    return LayoutDataFactory.createSingle(AboutCard.LAYOUT_ID, aboutCardBean, title);

                case TextCard.LAYOUT_ID:
                    Type textType = new TypeToken<TextCardBean>() {
                    }.getType();
                    TextCardBean textCardBean = gson.fromJson(dataArray.get(0), textType);
                    return LayoutDataFactory.createSingle(TextCard.LAYOUT_ID, textCardBean, title);

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