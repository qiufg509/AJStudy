package com.qiufengguang.ajstudy.global;

import com.qiufengguang.ajstudy.card.banner.BannerCard;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.grid.GridCard;
import com.qiufengguang.ajstudy.card.largegraphic.LargeGraphicCard;
import com.qiufengguang.ajstudy.card.normal.NormalCard;
import com.qiufengguang.ajstudy.card.series.SeriesCard;
import com.qiufengguang.ajstudy.card.user.SimpleUserCard;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.NormalCardBean;
import com.qiufengguang.ajstudy.data.SeriesCardBean;
import com.qiufengguang.ajstudy.data.User;

/**
 * 卡片注册器
 *
 * @author qiufengguang
 * @since 2026/1/30 18:43
 */
public class CardRegistrar {

    public static void initialize() {
        Card.register(BannerBean.LAYOUT_ID, new BannerCard.Creator());
        Card.register(GridCardBean.LAYOUT_ID, new GridCard.Creator());
        Card.register(LargeGraphicCardBean.LAYOUT_ID, new LargeGraphicCard.Creator());
        Card.register(NormalCardBean.LAYOUT_ID, new NormalCard.Creator());
        Card.register(SeriesCardBean.LAYOUT_ID, new SeriesCard.Creator());
        Card.register(User.LAYOUT_ID, new SimpleUserCard.Creator());
    }
}
