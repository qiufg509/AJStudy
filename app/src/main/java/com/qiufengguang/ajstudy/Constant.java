package com.qiufengguang.ajstudy;

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

}
