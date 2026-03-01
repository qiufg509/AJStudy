package com.qiufengguang.ajstudy.router;

public interface Router {
    String EXTRA_TITLE = "pageTitle";

    String EXTRA_URI = "pageUri";

    String EXTRA_DIRECTORY = "directory";

    String EXTRA_RESTART = "restartTheme";

    String EXTRA_DATA = "data";

    interface PAGE_ID {
        String SECONDARY = "secondary";

        String APP_DETAIL = "appDetail";

        String ARTICLE_DETAIL = "articleDetail";
    }

    interface URI {
        String PAGE_HOME = "home";

        String PAGE_KNOW_HOW = "knowHow";

        String PAGE_ME = "me";

        String PAGE_ARTICLE_LIST = "articleList";

        String PAGE_APP_LIST = "appList";

        String PAGE_APP_DETAIL = "appDetail";

        String PAGE_ARTICLE_DETAIL = "articleDetail";

        String PAGE_STUDY_RECORD = "studyRecord";

        String PAGE_FAVORITES = "favorites";

        String PAGE_USER = "user";

        String PAGE_COLOR_SCHEME = "colorScheme";

        String PAGE_LAB = "lab";

        String PAGE_HELP_FEEDBACK = "helpFeedback";
    }
}
