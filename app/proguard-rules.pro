# ========== 基础混淆设置 ==========
-optimizationpasses 5
-allowaccessmodification
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# ========== 核心属性保留（必须） ==========
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod, Exceptions
-keepattributes SourceFile, LineNumberTable

# ========== 数据模型（Gson 反射） ==========
# 推荐在模型类上使用 @SerializedName，可避免保留字段名
# 如果不想改代码，则保留所有字段（但只限具体包，不用 **）
-keepclassmembers class com.qiufengguang.ajstudy.data.model.** {
    <fields>;
}
-keepclassmembers class com.qiufengguang.ajstudy.data.remote.dto.** {
    <fields>;
}
-keepclassmembers class com.qiufengguang.ajstudy.data.base.** {
    <fields>;
}
# 保留无参构造（Gson 需要）
-keep class com.qiufengguang.ajstudy.data.model.** { <init>(); }
-keep class com.qiufengguang.ajstudy.data.remote.dto.** { <init>(); }
-keep class com.qiufengguang.ajstudy.data.base.** { <init>(); }

# ========== Retrofit & OkHttp & RxJava（无需完整保留） ==========
# 只需保留注解和签名，库本身由 R8 自动适配
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn io.reactivex.rxjava3.**

# 保留你自己的 API 接口（Retrofit 通过动态代理调用）
-keep interface com.qiufengguang.ajstudy.data.remote.api.** { *; }

# ========== Gson 内部（无需 keep class com.google.gson.**） ==========
-dontwarn com.google.gson.**

# ========== Room ==========
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**

# ========== Android 四大组件 & ViewModel（由系统反射实例化） ==========
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends androidx.fragment.app.Fragment

-keepclassmembers public class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}
-keep class * implements androidx.lifecycle.ViewModelProvider$Factory { *; }

# ========== Navigation ==========
-keep class com.qiufengguang.ajstudy.**.fragment.**.*Args { *; }
-keep class com.qiufengguang.ajstudy.**.fragment.**.*Directions { *; }

# ========== 自定义 View（保留构造函数供 XML 实例化） ==========
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

# ========== Glide ==========
-dontwarn com.bumptech.glide.**
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule { <init>(...); }
-keep @com.bumptech.glide.annotation.GlideModule class *

# ========== Lottie（不需要完整保留） ==========
-dontwarn com.airbnb.lottie.**
# 只保留动画解析需要的类（如果需要，添加具体类）
# -keep class com.airbnb.lottie.LottieAnimationView { *; }

# ========== Markwon（不需要完整保留） ==========
-dontwarn org.commonmark.**
-dontwarn io.noties.markwon.**

# ========== 枚举（保留 values/valueOf 供反射） ==========
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ========== Parcelable ==========
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ========== Native 方法 ==========
-keepclassmembers class * {
    native <methods>;
}