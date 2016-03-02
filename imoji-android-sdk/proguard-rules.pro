# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/sajjadtabib/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# this line protects method signatures including generic types
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable,InnerClasses
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-dontwarn com.squareup.okhttp.**

-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class com.imoji.sdk.response.** { *; }
-keep class com.imoji.sdk.internal.** { *; }
-keep class com.imoji.sdk.objects.json.** { *; }
-keep class com.imoji.sdk.objects.** { *; }
-keep class com.imoji.sdk.ApiTask { *; }
-keep class com.imoji.sdk.ImojiSDK { *; }
-keep class com.imoji.sdk.RenderingOptions { *; }
-keep class com.imoji.sdk.Session { *; }
-keep class com.imoji.sdk.StoragePolicy { *; }
