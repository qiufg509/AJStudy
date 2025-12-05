package com.qiufengguang.ajstudy.data;

import androidx.annotation.IdRes;

/**
 * 登录跳转动作
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public class LoginAction {
    public static final String ORIGINAL_PAGE = "originalPage";

    public static final String DESTINATION_ACTION = "destinationAction";

    public static final String DESTINATION_ID = "destinationId";

    private boolean isLoggedIn;

    private String originalPage;

    private String destinationAction;

    private @IdRes int destinationId;

    public LoginAction() {
    }

    public LoginAction(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getOriginalPage() {
        return originalPage;
    }

    public void setOriginalPage(String originalPage) {
        this.originalPage = originalPage;
    }

    public String getDestinationAction() {
        return destinationAction;
    }

    public void setDestinationAction(String destinationAction) {
        this.destinationAction = destinationAction;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }
}
