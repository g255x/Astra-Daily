# ====================================================================
#  ProGuard 规则
# ====================================================================

# ====================================================================
#  保留 Fabric 入口点
# ====================================================================
-keep class dev.Astra.Astra { *; }
-keep class dev.Astra.mod.Mod { *; }

# ====================================================================
#  保留所有 Mixin 类
# ====================================================================
-keep class dev.Astra.asm.mixins.** { *; }
-keep class dev.Astra.asm.mixins.sodium.** { *; }

# ====================================================================
#  保留 Accessor 类
# ====================================================================
-keep class dev.Astra.asm.accessors.** { *; }

# ====================================================================
#  保留事件系统
# ====================================================================
-keepnames class dev.Astra.api.events.**
-keepclassmembers class * {
    @dev.Astra.api.events.eventbus.EventListener <methods>;
}

# ====================================================================
#  保留 GUI 类
# ====================================================================
-keep class dev.Astra.mod.gui.clickgui.** { *; }

# ====================================================================
#  保留枚举的显示方法
# ====================================================================
-keepclassmembers enum dev.Astra.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
    <fields>;
}

# ====================================================================
#  保留序列化字段
# ====================================================================
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ====================================================================
#  保留注解
# ====================================================================
-keepattributes *Annotation*
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations

# ====================================================================
#  保留 J2CC 注解方法
# ====================================================================
-keepclasseswithmembers class * {
    @j2cc.Nativeify <methods>;
}
-keep class j2cc.** { *; }

# ====================================================================
#  属性保留
# ====================================================================
-keepattributes Exceptions, InnerClasses, Signature, Deprecated
-keepattributes LineNumberTable, EnclosingMethod
-keepattributes EventHandler, Override, Synthetic
-keepattributes SourceFile, SourceDir

# ====================================================================
#  基本配置
# ====================================================================
-ignorewarnings
-verbose
-optimizationpasses 3
-optimizations !code/allocation/variable

# ====================================================================
#  重打包（不要加 -allowaccessmodification，会破坏 Mixin）
# ====================================================================
-repackageclasses 'dev.Astra'

# ====================================================================
#  排除第三方库
# ====================================================================
-dontwarn baritone.**
-keep class baritone.** { *; }

-dontwarn com.google.**
-keep class com.google.** { *; }

-dontwarn org.apache.**
-keep class org.apache.** { *; }

-dontwarn satin.**
-keep class satin.** { *; }

-dontwarn com.llamalad7.**
-keep class com.llamalad7.** { *; }

# ====================================================================
#  保留 MC / Fabric 类
# ====================================================================
-keep class net.minecraft.** { *; }
-keep class net.fabricmc.** { *; }
-keep class com.mojang.** { *; }
-keep class org.spongepowered.asm.** { *; }

# ====================================================================
#  Mixin 注解保留
# ====================================================================
-keepclasseswithmembers class * {
    @org.spongepowered.asm.mixin.* <methods>;
}