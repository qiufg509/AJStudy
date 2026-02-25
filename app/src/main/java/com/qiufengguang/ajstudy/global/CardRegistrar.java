package com.qiufengguang.ajstudy.global;

import com.qiufengguang.ajstudy.card.article.ArticleCard;
import com.qiufengguang.ajstudy.card.banner.BannerCard;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.empty.EmptyCard;
import com.qiufengguang.ajstudy.card.graphicl.GraphicCardL;
import com.qiufengguang.ajstudy.card.graphiclgrid.GraphicLGridCard;
import com.qiufengguang.ajstudy.card.graphicm.GraphicCardM;
import com.qiufengguang.ajstudy.card.grid.GridCard;
import com.qiufengguang.ajstudy.card.luckywheel.LuckyWheelCard;
import com.qiufengguang.ajstudy.card.normal.NormalCard;
import com.qiufengguang.ajstudy.card.series.SeriesCard;
import com.qiufengguang.ajstudy.card.setting.SettingCard;
import com.qiufengguang.ajstudy.card.title.TitleCard;
import com.qiufengguang.ajstudy.card.user.SimpleUserCard;

/**
 * 卡片注册器
 *
 * @author qiufengguang
 * @since 2026/1/30 18:43
 */
public class CardRegistrar {

    public static void initialize() {
        Card.register(BannerCard.LAYOUT_ID, new BannerCard.Creator());
        Card.register(GridCard.LAYOUT_ID, new GridCard.Creator());
        Card.register(LuckyWheelCard.LAYOUT_ID, new LuckyWheelCard.Creator());
        Card.register(GraphicLGridCard.LAYOUT_ID, new GraphicLGridCard.Creator());
        Card.register(SeriesCard.LAYOUT_ID, new SeriesCard.Creator());
        Card.register(NormalCard.LAYOUT_ID, new NormalCard.Creator());
        Card.register(SimpleUserCard.LAYOUT_ID, new SimpleUserCard.Creator());
        Card.register(SettingCard.LAYOUT_ID, new SettingCard.Creator());
        Card.register(GraphicCardL.LAYOUT_ID, new GraphicCardL.Creator());
        Card.register(GraphicCardM.LAYOUT_ID, new GraphicCardM.Creator());
        Card.register(ArticleCard.LAYOUT_ID, new ArticleCard.Creator());
        Card.register(EmptyCard.LAYOUT_ID_1, new EmptyCard.WrapCreator());
        Card.register(EmptyCard.LAYOUT_ID_2, new EmptyCard.FullCreator());
        Card.register(TitleCard.LAYOUT_ID, new TitleCard.Creator());
    }
}
