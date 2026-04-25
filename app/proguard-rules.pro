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
# 保留数据模型类名、字段、构造函数和公共方法（供外部调用）
-keep class com.qiufengguang.ajstudy.data.model.** {
    <fields>;
    <init>(...);
}
-keep class com.qiufengguang.ajstudy.data.base.BaseCardBean {
    <fields>;
    <init>(...);
}
-keep class com.qiufengguang.ajstudy.data.base.PageData {
    <fields>;
    <init>(...);
}
-keepattributes Signature, *Annotation*
-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.Unsafe

# ========== Retrofit ==========
# 核心类禁止混淆
-keep class retrofit2.Call
-keep class retrofit2.Response
# 保留所有自定义API接口及其方法
-keep interface com.qiufengguang.ajstudy.data.remote.api.** { *; }
# 保留转换器静态方法
-keep class com.qiufengguang.ajstudy.data.converter.LayoutDataConverter {
    public static com.qiufengguang.ajstudy.data.base.PageData convert(com.google.gson.Gson, com.qiufengguang.ajstudy.data.remote.dto.RawRespData);
}
# 保留RetrofitClient的静态方法
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
# 保留DTO类（字段保留，方法可混淆）
-keep class com.qiufengguang.ajstudy.data.remote.dto.** {
    <fields>;
    <init>(...);
}
-keepattributes *Annotation*
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-dontnote retrofit2.Platform

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
# 保留所有自定义View的构造函数（布局反射需要）
-keep public class com.qiufengguang.ajstudy.view.** {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# 如果存在ViewHolder通过反射创建，保留其构造函数
-keepclassmembers public class * extends androidx.recyclerview.widget.RecyclerView$ViewHolder {
    public <init>(android.view.View);
}

# ========== 自定义监听器/接口 ==========
-keep public interface com.qiufengguang.ajstudy.view.LuckyWheel$AnimationEndListener {
    public void endAnimation(android.content.Context, com.qiufengguang.ajstudy.data.model.LuckyWheelCardBean);
}
-keep public class com.qiufengguang.ajstudy.view.EndlessRecyclerViewScrollListener {
    public void onScrolled(androidx.recyclerview.widget.RecyclerView, int, int);
    public void onLoadMore(int, int);
}

# ========== 枚举通用保留 ==========
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ========== Parcelable 实现类保留 ==========
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ========== 优化指令 ==========
-optimizationpasses 5
-allowaccessmodification