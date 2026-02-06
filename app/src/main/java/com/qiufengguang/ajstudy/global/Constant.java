package com.qiufengguang.ajstudy.global;

/**
 * 全局静态常量
 *
 * @author qiufengguang
 * @since 2025/11/26 22:19
 */
public interface Constant {
    /**
     * 启动登录页面action
     */
    String START_ACTIVITY_ACTION_LOGIN = "com.qfguang.ajstudy.action.LOGIN";

    /**
     * 启动页面uri
     */
    String START_ACTIVITY_URI = "ajstudy://com.qfguang";

    /**
     * token失效时长 1周
     */
    long TOKEN_EXPIRY_TIME = 7 * 24 * 60 * 60 * 1000L;

    /**
     * 栅格定义
     */
    interface Grid {
        /**
         * 4栅格
         */
        int COLUMN_DEFAULT = 4;

        /**
         * 8栅格
         */
        int COLUMN_8 = 8;

        /**
         * 12栅格
         */
        int COLUMN_12 = 12;
    }

    /**
     * 每行显示个数per line number
     */
    interface Pln {
        int DEF_4 = 1;
        int DEF_8 = 2;
        int DEF_12 = 3;

        int GRID_4 = 5;
        int GRID_8 = 8;
        int GRID_12 = 10;
    }

    /**
     * 本地测试数据
     */
    interface Data {
        /**
         * 知识点列表测试数据文件
         */
        String LIST_CONTENT_FILE = "know_how_list.json";

        /**
         * 详情页测试数据文件
         */
        String DETAIL_XHS = "detail_page_xhs.json";
        String DETAIL_WX = "detail_page_wx.json";
        String DETAIL_HGDJ = "detail_page_hgdj.json";
        String DETAIL_SJZXD = "detail_page_sjzxd.json";

        String DETAIL_SPRING = "朱自清《春》.md";

        /**
         * markdown文件读取目录
         */
        String DOCUMENT_STUDY_DIR = "KotlinCourse";
    }

    /**
     * SharedPreferences存储相关常量
     */
    interface Sp {
        String PREF_CONFIG = "config_prefs";

        String PREF_USER = "user_prefs";

        String KEY_THEME_INDEX = "theme_index";
    }
}
