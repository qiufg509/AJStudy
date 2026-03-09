-dontwarn org.commonmark.ext.gfm.strikethrough.Strikethrough

# ========== ViewModel ==========
-keepclassmembers public class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}
-keepclassmembers public class * extends androidx.lifecycle.AndroidViewModel {
    public <init>(android.app.Application);
}
-keep class * implements androidx.lifecycle.ViewModelProvider$Factory { *; }

# ========== Gson ==========
# ========== Gson ==========
-keep class com.qiufengguang.ajstudy.data.model.** { *; }
-keep class com.qiufengguang.ajstudy.data.base.** { *; }
-keepattributes Signature, *Annotation*
-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.Unsafe

# ========== Retrofit ==========
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep interface com.qiufengguang.ajstudy.api.** { *; }
-keepattributes *Annotation*
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-dontnote retrofit2.Platform
-keep class com.qiufengguang.ajstudy.data.callback.BodyRespCallback {
    public <init>(...);
    public void onResponse(retrofit2.Call, retrofit2.Response);
    public void onFailure(retrofit2.Call, java.lang.Throwable);
}
-keep class com.qiufengguang.ajstudy.data.callback.LayoutRespCallback {
    public <init>(...);
    public void onResponse(retrofit2.Call, retrofit2.Response);
    public void onFailure(retrofit2.Call, java.lang.Throwable);
}

# 保留普通回调接口的所有方法
-keep interface com.qiufengguang.ajstudy.data.callback.LoginCallback { *; }
-keep interface com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback { *; }


# 保留 LayoutDataConverter 及其公共静态方法
-keep class com.qiufengguang.ajstudy.data.converter.LayoutDataConverter {
    public static com.qiufengguang.ajstudy.data.base.PageData convert(com.google.gson.Gson, com.qiufengguang.ajstudy.data.remote.dto.RawRespData);
}

# 保留 RetrofitClient 及其所有公共静态方法
-keep class com.qiufengguang.ajstudy.data.remote.service.RetrofitClient {
    public static ** getInstance();
    public static ** getHomeApi();
    public static ** getKnowHowApi();
    public static ** getMeApi();
    public static ** getAppListApi();
    public static ** getArticleListApi();
    public static ** getAppDetailApi();
    public static ** getCommentApi();
    public static ** getRecommendApi();
    public static ** getArticleDetailApi();
    public static ** getStudyRecordApi();
    public static ** getFavoritesApi();
    public static ** getUserApi();
    public static ** getHelpFeedbackApi();
}

# 保留 data.remote.api 包下所有接口及其方法
-keep interface com.qiufengguang.ajstudy.data.remote.api.** { *; }
-keep class com.qiufengguang.ajstudy.data.remote.dto.** { *; }

# ========== Glide ==========
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
    <init>(...);
}
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
    *** rewind();
}
-keep @com.bumptech.glide.annotation.GlideModule class *

# ========== Navigation ==========
-keep class com.qiufengguang.ajstudy.**.fragment.**.*Args { *; }
-keep class com.qiufengguang.ajstudy.**.fragment.**.*Directions { *; }
-keep interface androidx.navigation.NavArgs
-keep class * implements androidx.navigation.NavArgs { *; }


# ========== 自定义 View 类 ==========
# 保留所有自定义 View 的类名和构造函数（布局文件反射需要）
-keep public class com.qiufengguang.ajstudy.view.** {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 如果某些 View 需要保留其他公共方法（如 setXxx 供代码调用），可添加：
-keepclassmembers public class com.qiufengguang.ajstudy.view.** {
    public *;
}

# ========== 自定义监听器/接口 ==========
# 保留内部接口（如 AnimationEndListener），确保回调方法不被混淆
-keep public interface com.qiufengguang.ajstudy.view.LuckyWheel$AnimationEndListener {
    public void endAnimation(android.content.Context, com.qiufengguang.ajstudy.data.model.LuckyWheelCardBean);
}

# ========== 抽象监听器类 ==========
# EndlessRecyclerViewScrollListener 中的方法会被 RecyclerView 调用，需保留
-keep public class com.qiufengguang.ajstudy.view.EndlessRecyclerViewScrollListener {
    public void onScrolled(androidx.recyclerview.widget.RecyclerView, int, int);
    public void onLoadMore(int, int);
}

# ========== 枚举（如果自定义 View 内部使用了枚举）=========
# DynamicToolbar 中使用了 Mode 枚举，需保留 values 和 valueOf 方法
-keepclassmembers enum com.qiufengguang.ajstudy.view.DynamicToolbar$Mode {
    *;
}
