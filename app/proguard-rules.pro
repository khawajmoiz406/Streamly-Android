# Agora
-keep class io.agora.rtc2.RtcEngine { *; }
-keep class io.agora.rtc2.** { *; }
-dontwarn io.agora.**

# Hilt
-keep class dagger.hilt.internal.** { *; }
-keep class * extends dagger.hilt.android.HiltAndroidApp
-keep @dagger.hilt.InstallIn class *

# Serialization
-keepattributes *Annotation*, Signature, InnerClasses

-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}

-keepclassmembers class **$$serializer {
    *;
}

# Firebase
-dontwarn com.google.firebase.**
-dontwarn com.google.firestore.**

# Desugar
-dontwarn com.google.devtools.build.android.desugar.runtime.ThrowableExtension