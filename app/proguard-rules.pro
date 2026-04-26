# ========== 基础混淆设置 ==========
# 优化次数，默认5
-optimizationpasses 5
# 允许改变访问修饰符（有助于提高混淆度）
-allowaccessmodification
# 混淆时不产生混合大小写的类名
-dontusemixedcaseclassnames
# 不跳过非公共库的类
-dontskipnonpubliclibraryclasses
# 记录日志
-verbose

# ========== 核心属性保留 ==========
# 保留注解、泛型签名、抛出的异常
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod, Exceptions
# 保留源文件和行号（用于崩溃堆栈解析，建议保留）
-keepattributes SourceFile, LineNumberTable

# ========== Retrofit & OkHttp & RxJava ==========
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-dontwarn okio.**
-keep class okio.** { *; }
# 保留所有自定义 API 接口，Retrofit 运行时通过反射读取注解
-keep interface com.qiufengguang.ajstudy.data.remote.api.** { *; }

# RxJava
-dontwarn io.reactivex.rxjava3.**
-keep class io.reactivex.rxjava3.** { *; }

# ========== Gson & Data Models ==========
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe { *; }
# 保留数据模型类名和字段（因为未使用 @SerializedName，反射解析需要字段名一致）
-keep class com.qiufengguang.ajstudy.data.model.** {
    <fields>;
    <init>(...);
}
-keep class com.qiufengguang.ajstudy.data.remote.dto.** {
    <fields>;
    <init>(...);
}
-keep class com.qiufengguang.ajstudy.data.base.** {
    <fields>;
    <init>(...);
}

# ========== Room Database ==========
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**

# ========== Android 组件 & ViewModel ==========
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

# ========== Navigation Component ==========
-keep class com.qiufengguang.ajstudy.**.fragment.**.*Args { *; }
-keep class com.qiufengguang.ajstudy.**.fragment.**.*Directions { *; }
-keep interface androidx.navigation.NavArgs
-keep class * implements androidx.navigation.NavArgs { *; }

# ========== 自定义 View & UI 库 ==========
# 保留所有自定义 View 的构造函数，供布局文件反射实例化
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule { <init>(...); }
-keep @com.bumptech.glide.annotation.GlideModule class *
-dontwarn com.bumptech.glide.**

# Lottie
-keep class com.airbnb.lottie.** { *; }

# Markwon / Commonmark
-dontwarn org.commonmark.**
-keep class org.commonmark.** { *; }
-dontwarn io.noties.markwon.**
-keep class io.noties.markwon.** { *; }

# ========== 其他通用规则 ==========
# 保留枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# 保留 Parcelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
# 保留 Native 方法
-keepclassmembers class * {
    native <methods>;
}