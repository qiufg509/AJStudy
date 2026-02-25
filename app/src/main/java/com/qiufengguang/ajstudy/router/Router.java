package com.qiufengguang.ajstudy.router;

public interface Router {
    String EXTRA_TITLE = "pageTitle";

    String EXTRA_URI = "pageUri";

    String EXTRA_RESTART = "restartTheme";

    String EXTRA_SECOND_PAGE = "secondPage";

    interface URI {
        String PAGE_ARTICLE_LIST = "articleList";

        String PAGE_COLOR_SCHEME = "colorScheme";

        String PAGE_APP_LIST = "appList";
    }
}
