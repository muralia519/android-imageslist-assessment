# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\ADT_Eclipse_bundle\sdk/tools/proguard/proguard-android.txt
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

# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-keep class com.infy.android.telstrafeeds.model.** { *; }

-keep class com.infy.android.telstrafeeds.view.DividerItemDecoration
-keep public class * extends android.support.v7.widget.RecyclerView.ItemDecoration
-keep class android.support.v7.widget.RecyclerView

-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-keep class org.codehaus.** { *; }

# Keep Picasso
-keep class com.squareup.picasso.** { *; }
-keepclasseswithmembers class * {
    @com.squareup.picasso.** *;
}
-keepclassmembers class * {
    @com.squareup.picasso.** *;
}
 -dontwarn com.squareup.picasso.**

 #### -- OkHttp --
 -dontwarn com.squareup.okhttp.internal.**

 #### -- Apache Commons --
 -dontwarn org.apache.commons.logging.**
