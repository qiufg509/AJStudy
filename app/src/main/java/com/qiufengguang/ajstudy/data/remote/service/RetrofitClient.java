package com.qiufengguang.ajstudy.data.remote.service;

import android.content.Context;
import android.text.TextUtils;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.remote.api.AppDetailApi;
import com.qiufengguang.ajstudy.data.remote.api.AppListApi;
import com.qiufengguang.ajstudy.data.remote.api.ArticleDetailApi;
import com.qiufengguang.ajstudy.data.remote.api.ArticleListApi;
import com.qiufengguang.ajstudy.data.remote.api.CommentApi;
import com.qiufengguang.ajstudy.data.remote.api.FavoritesApi;
import com.qiufengguang.ajstudy.data.remote.api.HelpFeedbackApi;
import com.qiufengguang.ajstudy.data.remote.api.HomeApi;
import com.qiufengguang.ajstudy.data.remote.api.KnowHowApi;
import com.qiufengguang.ajstudy.data.remote.api.MeApi;
import com.qiufengguang.ajstudy.data.remote.api.RecommendApi;
import com.qiufengguang.ajstudy.data.remote.api.StudyRecordApi;
import com.qiufengguang.ajstudy.data.remote.api.UserApi;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.utils.AppExecutors;
import com.qiufengguang.ajstudy.utils.JsonUtils;
import com.qiufengguang.ajstudy.utils.SpUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 客户端
 * [性能专家重构]：Api 代理缓存、OkHttp 连接池复用、Gson 实例复用
 *
 * @author qiufengguang
 */
public class RetrofitClient {
    private static final String DEFAULT_SERVER_IP = "127.0.0.1";
    private static volatile Retrofit sRetrofit;
    private static volatile OkHttpClient sOkHttpClient;
    private static final Map<Class<?>, Object> API_CACHE = new ConcurrentHashMap<>();

    private static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            synchronized (RetrofitClient.class) {
                if (sRetrofit == null) {
                    sRetrofit = createRetrofit();
                }
            }
        }
        return sRetrofit;
    }

    private static Retrofit createRetrofit() {
        String serverIp = SpUtils.getInstance().getString(Constant.Sp.KEY_SERVER_IP, "");
        Context context = GlobalApp.getContext();
        String ip = TextUtils.isEmpty(serverIp) ? DEFAULT_SERVER_IP : serverIp;
        String baseUrl = context != null ? context.getString(R.string.base_url, ip)
            : String.format("http://%s:8080/", ip);

        return new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(JsonUtils.getGson())) // [性能重构]：复用全局单例 Gson
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();
    }

    private static OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (RetrofitClient.class) {
                if (sOkHttpClient == null) {
                    // [性能重构]：配置高性能线程池与连接池
                    Dispatcher dispatcher = new Dispatcher(AppExecutors.getInstance().networkIO());
                    dispatcher.setMaxRequests(64);
                    dispatcher.setMaxRequestsPerHost(16);

                    sOkHttpClient = new OkHttpClient.Builder()
                        .dispatcher(dispatcher)
                        .connectionPool(new ConnectionPool(
                            5, 5, TimeUnit.MINUTES))
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .build();
                }
            }
        }
        return sOkHttpClient;
    }

    /**
     * 获取 API 实例（带高性能缓存）
     */
    @SuppressWarnings("unchecked")
    private static <T> T getApi(Class<T> service) {
        Object api = API_CACHE.get(service);
        if (api == null) {
            synchronized (API_CACHE) {
                api = API_CACHE.get(service);
                if (api == null) {
                    api = getRetrofit().create(service);
                    API_CACHE.put(service, api);
                }
            }
        }
        return (T) api;
    }

    // ==================== 业务 API 入口 ====================

    public static HomeApi getHomeApi() {
        return getApi(HomeApi.class);
    }

    public static KnowHowApi getKnowHowApi() {
        return getApi(KnowHowApi.class);
    }

    public static MeApi getMeApi() {
        return getApi(MeApi.class);
    }

    public static AppListApi getAppListApi() {
        return getApi(AppListApi.class);
    }

    public static ArticleListApi getArticleListApi() {
        return getApi(ArticleListApi.class);
    }

    public static AppDetailApi getAppDetailApi() {
        return getApi(AppDetailApi.class);
    }

    public static CommentApi getCommentApi() {
        return getApi(CommentApi.class);
    }

    public static RecommendApi getRecommendApi() {
        return getApi(RecommendApi.class);
    }

    public static ArticleDetailApi getArticleDetailApi() {
        return getApi(ArticleDetailApi.class);
    }

    public static StudyRecordApi getStudyRecordApi() {
        return getApi(StudyRecordApi.class);
    }

    public static FavoritesApi getFavoritesApi() {
        return getApi(FavoritesApi.class);
    }

    public static UserApi getUserApi() {
        return getApi(UserApi.class);
    }

    public static HelpFeedbackApi getHelpFeedbackApi() {
        return getApi(HelpFeedbackApi.class);
    }

    /**
     * 重置客户端（用于 IP 切换场景）
     */
    public static void reset() {
        synchronized (RetrofitClient.class) {
            sRetrofit = null;
            API_CACHE.clear();
        }
    }
}