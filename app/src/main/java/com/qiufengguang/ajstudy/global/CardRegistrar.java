package com.qiufengguang.ajstudy.global;

import com.qiufengguang.ajstudy.card.about.AboutCard;
import com.qiufengguang.ajstudy.card.article.ArticleCard;
import com.qiufengguang.ajstudy.card.banner.BannerCard;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.brief.BriefCard;
import com.qiufengguang.ajstudy.card.chat.AiMessageCard;
import com.qiufengguang.ajstudy.card.chat.UserMessageCard;
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
import com.qiufengguang.ajstudy.card.selecttheme.SelectThemeCard;
import com.qiufengguang.ajstudy.card.series.SeriesCard;
import com.qiufengguang.ajstudy.card.serverip.ServerIpCard;
import com.qiufengguang.ajstudy.card.setting.SettingCard;
import com.qiufengguang.ajstudy.card.state.StateCard;
import com.qiufengguang.ajstudy.card.text.TextCard;
import com.qiufengguang.ajstudy.card.title.TitleCard;
import com.qiufengguang.ajstudy.card.topicheader.TopicHeaderCard;
import com.qiufengguang.ajstudy.card.topicmulti.TopicMultiCard;
import com.qiufengguang.ajstudy.card.user.SimpleUserCard;
import com.qiufengguang.ajstudy.card.welcome.AiWelcomeCard;

/**
 * 卡片注册器
 * [性能专家重构]：支持按需注册逻辑，消除冷启动瞬时对象分配压力
 */
public class CardRegistrar {

    /**
     * 预注册：仅注册极少数首页必须卡片，其他延迟到使用时注册
     */
    public static void initialize() {
        // 核心卡片（首屏高概率出现）提前注册
        Card.register(StateCard.LAYOUT_ID, new StateCard.Creator());
        Card.register(BannerCard.LAYOUT_ID, new BannerCard.Creator());
        Card.register(GridCard.LAYOUT_ID, new GridCard.Creator());
        Card.register(NormalCard.LAYOUT_ID, new NormalCard.Creator());
    }

    /**
     * 按需注册逻辑：由 Card.getCreator 自动触发
     *
     * @param layoutId 卡片ID
     */
    public static void registerOnDemand(int layoutId) {
        switch (layoutId) {
            case LuckyWheelCard.LAYOUT_ID:
                Card.register(layoutId, new LuckyWheelCard.Creator());
                break;
            case GraphicLGridCard.LAYOUT_ID:
                Card.register(layoutId, new GraphicLGridCard.Creator());
                break;
            case SeriesCard.LAYOUT_ID:
                Card.register(layoutId, new SeriesCard.Creator());
                break;
            case SimpleUserCard.LAYOUT_ID:
                Card.register(layoutId, new SimpleUserCard.Creator());
                break;
            case SettingCard.LAYOUT_ID:
                Card.register(layoutId, new SettingCard.Creator());
                break;
            case GraphicCardL.LAYOUT_ID:
                Card.register(layoutId, new GraphicCardL.Creator());
                break;
            case GraphicCardM.LAYOUT_ID:
                Card.register(layoutId, new GraphicCardM.Creator());
                break;
            case GraphicCardS.LAYOUT_ID:
                Card.register(layoutId, new GraphicCardS.Creator());
                break;
            case ArticleCard.LAYOUT_ID:
                Card.register(layoutId, new ArticleCard.Creator());
                break;
            case TopicMultiCard.LAYOUT_ID:
                Card.register(layoutId, new TopicMultiCard.Creator());
                break;
            case EmptyCard.LAYOUT_ID:
                Card.register(layoutId, new EmptyCard.Creator());
                break;
            case TitleCard.LAYOUT_ID:
                Card.register(layoutId, new TitleCard.Creator());
                break;
            case AboutCard.LAYOUT_ID:
                Card.register(layoutId, new AboutCard.Creator());
                break;
            case BriefCard.LAYOUT_ID:
                Card.register(layoutId, new BriefCard.Creator());
                break;
            case ScreenshotCard.LAYOUT_ID:
                Card.register(layoutId, new ScreenshotCard.Creator());
                break;
            case TextCard.LAYOUT_ID:
                Card.register(layoutId, new TextCard.Creator());
                break;
            case RecommendCard.LAYOUT_ID:
                Card.register(layoutId, new RecommendCard.Creator());
                break;
            case CommentCard.LAYOUT_ID:
                Card.register(layoutId, new CommentCard.Creator());
                break;
            case TopicHeaderCard.LAYOUT_ID:
                Card.register(layoutId, new TopicHeaderCard.Creator());
                break;
            case ServerIpCard.LAYOUT_ID:
                Card.register(layoutId, new ServerIpCard.Creator());
                break;
            case SelectThemeCard.LAYOUT_ID:
                Card.register(layoutId, new SelectThemeCard.Creator());
                break;
            case AiWelcomeCard.LAYOUT_ID:
                Card.register(layoutId, new AiWelcomeCard.Creator());
                break;
            case UserMessageCard.LAYOUT_ID:
                Card.register(layoutId, new UserMessageCard.Creator());
                break;
            case AiMessageCard.LAYOUT_ID:
                Card.register(layoutId, new AiMessageCard.Creator());
                break;
        }
    }
}