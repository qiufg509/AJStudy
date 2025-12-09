package com.qiufengguang.ajstudy.global;

/**
 * 全局静态常量
 *
 * @author qiufengguang
 * @since 2025/11/26 22:19
 */
public interface Constant {

    /**
     * 栅格定义
     */
    interface Grid {
        /**
         * 4栅格
         */
        int column_default = 4;

        /**
         * 8栅格
         */
        int column_8 = 8;

        /**
         * 12栅格
         */
        int column_12 = 12;
    }

    /**
     * 本地列表数据文件
     */
    String LIST_CONTENT_FILE = "dashboard_list_data.json";

    /**
     * 本地列表详情页数据文件
     */
    String DETAIL_PAGE_FILE = "detail_page_data.json";

    /**
     * 启动登录页面action
     */
    String START_ACTIVITY_ACTION_LOGIN = "com.qiufengguang.ajstudy.action.LOGIN";

    /**
     * 启动页面uri
     */
    String START_ACTIVITY_URI = "ajstudy://com.qfguang";

    /**
     * token失效时长 1周
     */
    long TOKEN_EXPIRY_TIME = 7 * 24 * 60 * 60 * 1000L;
}
