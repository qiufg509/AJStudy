package com.qiufengguang.ajstudy.utils;

import androidx.annotation.StyleRes;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.global.Constant;

import org.jetbrains.annotations.Range;

/**
 * 主题工具
 *
 * @author qiufengguang
 * @since 2025/12/17 14:35
 */
public class ThemeUtils {

    /**
     * 应用主题
     */
    private static final int[] APP_THEME = {
        R.style.Theme_AJStudy_Red,
        R.style.Theme_AJStudy_Blue,
        R.style.Theme_AJStudy_Green,
        R.style.Theme_AJStudy_Orange,
        R.style.Theme_AJStudy_Pink,
        R.style.Theme_AJStudy_Black,
        R.style.Theme_AJStudy_Amber,
        R.style.Theme_AJStudy_Indigo,
        R.style.Theme_AJStudy_Lime,
        R.style.Theme_AJStudy_Purple
    };

    /**
     * 启动页主题
     */
    private static final int[] SPLASH_THEME = {
        R.style.Theme_Splash_Red,
        R.style.Theme_Splash_Blue,
        R.style.Theme_Splash_Green,
        R.style.Theme_Splash_Orange,
        R.style.Theme_Splash_Pink,
        R.style.Theme_Splash_Black,
        R.style.Theme_Splash_Amber,
        R.style.Theme_Splash_Indigo,
        R.style.Theme_Splash_Lime,
        R.style.Theme_Splash_Purple
    };

    /**
     * 主页主题
     */
    private static final int[] MIAN_THEME = {
        R.style.Theme_Main_Red,
        R.style.Theme_Main_Blue,
        R.style.Theme_Main_Green,
        R.style.Theme_Main_Orange,
        R.style.Theme_Main_Pink,
        R.style.Theme_Main_Black,
        R.style.Theme_Main_Amber,
        R.style.Theme_Main_Indigo,
        R.style.Theme_Main_Lime,
        R.style.Theme_Main_Purple
    };

    /**
     * 登录页主题
     */
    private static final int[] LOGIN_THEME = {
        R.style.Theme_Login_Red,
        R.style.Theme_Login_Blue,
        R.style.Theme_Login_Green,
        R.style.Theme_Login_Orange,
        R.style.Theme_Login_Pink,
        R.style.Theme_Login_Black,
        R.style.Theme_Login_Amber,
        R.style.Theme_Login_Indigo,
        R.style.Theme_Login_Lime,
        R.style.Theme_Login_Purple
    };

    /**
     * 默认主题色-下标1蓝色
     */
    public static final int THEME_INDEX_DEFAULT = 1;

    private ThemeUtils() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    public static @StyleRes int getSplashTheme() {
        SpUtils spUtils = SpUtils.getInstance();
        if (spUtils == null) {
            return SPLASH_THEME[THEME_INDEX_DEFAULT];
        }
        int selectedIndex = spUtils.getInt(Constant.Sp.KEY_THEME_INDEX, THEME_INDEX_DEFAULT);
        if (selectedIndex >= SPLASH_THEME.length || selectedIndex < 0) {
            return SPLASH_THEME[THEME_INDEX_DEFAULT];
        }
        return SPLASH_THEME[selectedIndex];
    }

    public static @StyleRes int getMianTheme() {
        int selectedIndex = SpUtils.getInstance().getInt(Constant.Sp.KEY_THEME_INDEX, THEME_INDEX_DEFAULT);
        if (selectedIndex >= MIAN_THEME.length || selectedIndex < 0) {
            return MIAN_THEME[THEME_INDEX_DEFAULT];
        }
        return MIAN_THEME[selectedIndex];
    }

    public static @StyleRes int getLoginTheme() {
        int selectedIndex = SpUtils.getInstance().getInt(Constant.Sp.KEY_THEME_INDEX, THEME_INDEX_DEFAULT);
        if (selectedIndex >= LOGIN_THEME.length || selectedIndex < 0) {
            return LOGIN_THEME[THEME_INDEX_DEFAULT];
        }
        return LOGIN_THEME[selectedIndex];
    }

    public static @StyleRes int getAppTheme() {
        int selectedIndex = SpUtils.getInstance().getInt(Constant.Sp.KEY_THEME_INDEX, THEME_INDEX_DEFAULT);
        if (selectedIndex >= APP_THEME.length || selectedIndex < 0) {
            return APP_THEME[THEME_INDEX_DEFAULT];
        }
        return APP_THEME[selectedIndex];
    }


    public static int getSelectedThemeIndex() {
        int selectedIndex = SpUtils.getInstance().getInt(Constant.Sp.KEY_THEME_INDEX, THEME_INDEX_DEFAULT);
        if (selectedIndex >= APP_THEME.length || selectedIndex < 0) {
            return THEME_INDEX_DEFAULT;
        }
        return selectedIndex;
    }

    public static void setSelectedThemeIndex(@Range(from = 0, to = 9) int themeIndex) {
        SpUtils.getInstance().putInt(Constant.Sp.KEY_THEME_INDEX, themeIndex);
    }
}