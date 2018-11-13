# Keep any GlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule

# Eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Jsonpath
-dontwarn com.jayway.jsonpath.spi.json.GsonJsonProvider
-dontwarn com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider
-dontwarn com.jayway.jsonpath.spi.json.JacksonJsonProvider
-dontwarn com.jayway.jsonpath.spi.json.TapestryJsdonProvider
-dontwarn com.jayway.jsonpath.spi.json.JsonOrgJsonProvider
-dontwarn com.jayway.jsonpath.spi.json.TapestryJsonProvider
-dontwarn com.jayway.jsonpath.spi.mapper.GsonMappingProvider*
-dontwarn com.jayway.jsonpath.spi.mapper.JacksonMappingProvider
-dontwarn com.jayway.jsonpath.spi.mapper.JsonOrgMappingProvider

# Necessary for ignoring json-smart's logging dependencies
-dontwarn org.slf4j.*

# AndroidX
-keep public class androidx.** { *; }