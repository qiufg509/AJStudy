package com.qiufengguang.ajstudy.global;

/**
 * 全局静态常量
 *
 * @author qiufengguang
 * @since 2025/11/26 22:19
 */
public interface Constant {
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

        int GRAPHIC_M_4 = 2;
        int GRAPHIC_M_8 = 4;
        int GRAPHIC_M_12 = 8;
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
