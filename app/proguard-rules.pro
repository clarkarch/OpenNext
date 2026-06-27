# Ktor
-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { volatile <fields>; }
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.** { volatile <fields>; }
-keep,includedescriptorclasses class com.opennext.app.**$$serializer { *; }
-keepclassmembers class com.opennext.app.** { *** Companion; }
-keepclasseswithmembers class com.opennext.app.** { kotlinx.serialization.KSerializer serializer(...); }
-keep,includedescriptorclasses class com.opennext.app.shared.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
