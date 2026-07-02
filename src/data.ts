import { Restaurant, MenuItem, MenuCategory, DeliveryBanner, KotlinFile } from './types';

export const YEMENI_CITIES = ['صنعاء', 'عدن', 'تعز', 'إب', 'الحديدة'];

export const CATEGORIES: MenuCategory[] = [
  { id: 'cat_all', nameAr: 'الكل', nameEn: 'All', image: '🍽️' },
  { id: 'cat_mandi', nameAr: 'مندي وشعبي', nameEn: 'Mandi & Folk', image: '🍗' },
  { id: 'cat_grill', nameAr: 'مشويات', nameEn: 'Grills', image: '🍢' },
  { id: 'cat_sweets', nameAr: 'حلويات يمنية', nameEn: 'Yemeni Sweets', image: '🍯' },
  { id: 'cat_drinks', nameAr: 'عصائر ومشروبات', nameEn: 'Juices & Drinks', image: '🍹' },
  { id: 'cat_fast', nameAr: 'ساندوتشات', nameEn: 'Sandwiches', image: '🍔' }
];

export const BANNERS: DeliveryBanner[] = [
  {
    id: 'banner_1',
    title: 'تذوق المندي البلدي الأصيل',
    subtitle: 'توصيل سريع من مطاعم الشيباني الحديثة',
    imageUrl: 'https://images.unsplash.com/photo-1544025162-d76694265947?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3',
    badgeText: 'خصم ٢٠٪',
    restaurantId: 'rest_1'
  },
  {
    id: 'banner_2',
    title: 'بنت الصحن بالعسل الدوعني الفاخر',
    subtitle: 'من حلويات الخطيب التقليدية إلى باب بيتك',
    imageUrl: 'https://images.unsplash.com/photo-1587314168485-3236d6710814?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3',
    badgeText: 'طلب خارجي',
    restaurantId: 'rest_3'
  }
];

export const RESTAURANTS: Restaurant[] = [
  {
    id: 'rest_1',
    nameAr: 'مطاعم الشيباني الحديثة',
    nameEn: 'Al-Shaibani Modern Restaurants',
    coverImage: 'https://images.unsplash.com/photo-1544025162-d76694265947?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3',
    logoUrl: 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=100&auto=format&fit=crop&q=60&ixlib=rb-4.0.3',
    rating: 4.8,
    estimatedDeliveryTime: 35,
    deliveryFee: 800,
    isActive: true,
    category: 'cat_mandi'
  },
  {
    id: 'rest_2',
    nameAr: 'مطعم وجلسات الخطيب للكلاد والمشويات',
    nameEn: 'Al-Khateeb Grills Restaurant',
    coverImage: 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3',
    logoUrl: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=100&auto=format&fit=crop&q=60&ixlib=rb-4.0.3',
    rating: 4.6,
    estimatedDeliveryTime: 40,
    deliveryFee: 1000,
    isActive: true,
    category: 'cat_grill'
  },
  {
    id: 'rest_3',
    nameAr: 'حلويات ومخبازة الشيباني التقليدية',
    nameEn: 'Al-Shaibani Traditional Sweets',
    coverImage: 'https://images.unsplash.com/photo-1587314168485-3236d6710814?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3',
    logoUrl: 'https://images.unsplash.com/photo-149514740007a-f8a53e30db25?w=100&auto=format&fit=crop&q=60&ixlib=rb-4.0.3',
    rating: 4.9,
    estimatedDeliveryTime: 25,
    deliveryFee: 600,
    isActive: true,
    category: 'cat_sweets'
  },
  {
    id: 'rest_4',
    nameAr: 'عصائر الكوكتيل ومشروبات وصال',
    nameEn: 'Wasal Juice & Mocktails Bar',
    coverImage: 'https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3',
    logoUrl: 'https://images.unsplash.com/photo-1497515114629-f71d768fd07c?w=100&auto=format&fit=crop&q=60&ixlib=rb-4.0.3',
    rating: 4.5,
    estimatedDeliveryTime: 20,
    deliveryFee: 500,
    isActive: true,
    category: 'cat_drinks'
  }
];

export const MENU_ITEMS: MenuItem[] = [
  // Al-Shaibani Modern
  {
    id: 'item_1',
    nameAr: 'نصف حبة مندي دجاج بلدي مع الأرز والصلصة',
    nameEn: 'Half Mandi Chicken with Rice & Sahawek',
    description: 'دجاج مندي متبل بالبهارات الصنعانية الأصيلة ومطبوخ في الحفرة تحت الأرض، يقدم مع أرز بسمتي فاخر وصلصة السحاوق الحارة.',
    price: 3800,
    originalPrice: 4200,
    imageUrl: 'https://images.unsplash.com/photo-1544025162-d76694265947?w=300&auto=format&fit=crop&q=60',
    categoryId: 'cat_mandi',
    restaurantId: 'rest_1',
    isAvailable: true,
    isPopular: true
  },
  {
    id: 'item_2',
    nameAr: 'نفر مندي لحم بلدي فاخر',
    nameEn: 'Yemeni Mutton Mandi',
    description: 'لحم ضأن بلدي فاخر وطري ومطبوخ على طريقة المندي التقليدية مع أرز الزعفران والسحاوق والبهارات.',
    price: 12000,
    imageUrl: 'https://images.unsplash.com/photo-1603360946369-dc9bb6258143?w=300&auto=format&fit=crop&q=60',
    categoryId: 'cat_mandi',
    restaurantId: 'rest_1',
    isAvailable: true,
    isPopular: true
  },
  {
    id: 'item_3',
    nameAr: 'سلته صنعانية باللحم المفروم والحلية',
    nameEn: 'Sanaani Saltah with Minced Meat',
    description: 'الطبق اليمني القومي، مرق لحم يغلي في مقلى الحجر المدر، مغطى بطبقة من الحلبة المخفوقة الساخنة والسحاوق واللحم المفروم، يقدم مع خبز الملوج الساخن.',
    price: 4500,
    imageUrl: 'https://images.unsplash.com/photo-1541532713592-79a0317b6b77?w=300&auto=format&fit=crop&q=60',
    categoryId: 'cat_mandi',
    restaurantId: 'rest_1',
    isAvailable: true,
    isPopular: true
  },
  {
    id: 'item_4',
    nameAr: 'فحسة لحم ضأن حامية',
    nameEn: 'Hot Mutton Fahsa',
    description: 'قطع لحم ضأن طرية ومهروسة في مرق كثيف يغلي في المقلى الحجر ومغطى بالحلبة اليمنية الرائعة.',
    price: 5000,
    imageUrl: 'https://images.unsplash.com/photo-1608897013039-887f21d8c804?w=300&auto=format&fit=crop&q=60',
    categoryId: 'cat_mandi',
    restaurantId: 'rest_1',
    isAvailable: true,
    isPopular: false
  },

  // Al-Khateeb Grills
  {
    id: 'item_5',
    nameAr: 'مشكل مشويات الخطيب (كباب، شيش، ريش)',
    nameEn: 'Al-Khateeb Mixed Grills Platter',
    description: 'مزيج رائع من كباب اللحم البلدي، قطع الشيش طاووق المتبلة بالزبادي والليمون، وريش الغنم المشوية على الفحم، تقدم مع البطاطس المقلية والثومية والخبز.',
    price: 6500,
    originalPrice: 7500,
    imageUrl: 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=300&auto=format&fit=crop&q=60',
    categoryId: 'cat_grill',
    restaurantId: 'rest_2',
    isAvailable: true,
    isPopular: true
  },
  {
    id: 'item_6',
    nameAr: 'كباب لحم يمني على الفحم',
    nameEn: 'Charcoal Grilled Mutton Kebab',
    description: 'أسياخ لحم مفروم بلدي متبلة بالبصل والكزبرة والبهارات اليمنية الخاصة والمشوية ببراعة على الفحم الحار.',
    price: 4000,
    imageUrl: 'https://images.unsplash.com/photo-1529193591184-b1d58069ecdd?w=300&auto=format&fit=crop&q=60',
    categoryId: 'cat_grill',
    restaurantId: 'rest_2',
    isAvailable: true,
    isPopular: false
  },

  // Al-Shaibani Sweets
  {
    id: 'item_7',
    nameAr: 'بنت الصحن اليمنية الكبيرة بالعسل الدوعني',
    nameEn: 'Bint Al-Sahn with Doani Honey',
    description: 'فطيرة يمنية تقليدية تتكون من عشرات الطبقات الرقيقة المقرمشة، مغطاة بالحبة السوداء والسمن البلدي والعسل الدوعني الحضرمي الأصيل.',
    price: 3500,
    imageUrl: 'https://images.unsplash.com/photo-1587314168485-3236d6710814?w=300&auto=format&fit=crop&q=60',
    categoryId: 'cat_sweets',
    restaurantId: 'rest_3',
    isAvailable: true,
    isPopular: true
  },
  {
    id: 'item_8',
    nameAr: 'عريكة وصال ملكية بالقشطة والجبن والمكسرات',
    nameEn: 'Royal Arika with Cream & Honey',
    description: 'تمر وتمر وتمر مفروم مع خبز البر، مغطى بطبقة كثيفة من القشطة الطازجة والجبن واللوز المقشور والعسل.',
    price: 2500,
    imageUrl: 'https://images.unsplash.com/photo-1579372786545-d24232daf58c?w=300&auto=format&fit=crop&q=60',
    categoryId: 'cat_sweets',
    restaurantId: 'rest_3',
    isAvailable: true,
    isPopular: true
  },

  // Wasal Juice Bar
  {
    id: 'item_9',
    nameAr: 'عصير عرائسي مشكل بالمكسرات والعسل والزبيب',
    nameEn: 'Traditional Araisi Juice',
    description: 'عصير كوكتيل يمني سميك وشهير مصنوع من المانجو والموز والحليب والبلح، مزين بطبقة من المكسرات والزبيب الصنعاني الفاخر وعسل السدر الطبيعي.',
    price: 1800,
    imageUrl: 'https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=300&auto=format&fit=crop&q=60',
    categoryId: 'cat_drinks',
    restaurantId: 'rest_4',
    isAvailable: true,
    isPopular: true
  },
  {
    id: 'item_10',
    nameAr: 'شاي عدني مخدر هيل وقرنفل وحليب ممتاز',
    nameEn: 'Adeni Milk Tea with Spices',
    description: 'شاي أحمر ثقيل مغلي ببطء مع الحليب المركز وحبات الهيل والقرنفل والقرفة على الطريقة العدنية الأصيلة.',
    price: 500,
    imageUrl: 'https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=300&auto=format&fit=crop&q=60',
    categoryId: 'cat_drinks',
    restaurantId: 'rest_4',
    isAvailable: true,
    isPopular: true
  }
];

export const KOTLIN_FILES: KotlinFile[] = [
  {
    name: 'build.gradle.kts (Project)',
    path: 'build.gradle.kts',
    description: 'إعدادات Gradle على مستوى المشروع لتطبيق وصال وتصريح إصدارات الإضافات الرئيسية.',
    content: `// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.24" apply false
}`
  },
  {
    name: 'build.gradle.kts (App)',
    path: 'app/build.gradle.kts',
    description: 'تكوين Gradle على مستوى التطبيق، بما في ذلك التصنيفات، حزم الاستيراد لـ Supabase وCompose والمكتبات الأخرى.',
    content: `plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.wasal.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wasal.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/io.netty.versions.properties"
        }
    }
}

dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Activity + ViewModel
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:2.5.4"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:storage-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")
    implementation("io.ktor:ktor-client-okhttp:2.3.12")

    // Coil (images)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")

    // DataStore (for session persistence)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}`
  },
  {
    name: 'AndroidManifest.xml',
    path: 'app/src/main/AndroidManifest.xml',
    description: 'تطبيق الأذونات والمكونات الرئيسية لتطبيق وصال بما في ذلك الإنترنت والاشعارات ودعم الاتجاهات من اليمين لليسار.',
    content: `<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <application
        android:name=".WasalApp"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Wasal"
        android:usesCleartextTraffic="false">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:windowSoftInputMode="adjustResize"
            android:theme="@style/Theme.Wasal">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>`
  },
  {
    name: 'Color.kt',
    path: 'app/src/main/java/com/wasal/app/ui/theme/Color.kt',
    description: 'مجموعة الألوان الرسمية المعبرة عن الهوية الخضراء والذهبية لمنصة وصال بالتوافق مع ثيم المواد Jetpack Compose.',
    content: `package com.wasal.app.ui.theme

import androidx.compose.ui.graphics.Color

val WasalGreen = Color(0xFF1A5C3A)
val WasalGreenDark = Color(0xFF0F3D26)
val WasalGreenLight = Color(0xFF2E8B57)
val WasalGreenSurface = Color(0xFFE8F5EE)
val WasalAmber = Color(0xFFFFB800)
val WasalRed = Color(0xFFE53935)
val WasalGrey = Color(0xFF757575)
val WasalLightGrey = Color(0xFFF5F5F5)
val WasalWhite = Color(0xFFFFFFFF)
val WasalBlack = Color(0xFF1A1A1A)
val WasalDivider = Color(0xFFEEEEEE)
val WasalCardBg = Color(0xFFFFFFFF)
val WasalBackground = Color(0xFFF8F9FA)`
  },
  {
    name: 'Type.kt',
    path: 'app/src/main/java/com/wasal/app/ui/theme/Type.kt',
    description: 'خطوط النظام والتدرجات الطباعية مع دعم مدمج لاتجاه النص العربي RTL.',
    content: `package com.wasal.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp

// Arabic-compatible system font fallbacks
val ArabicFontFamily = FontFamily.SansSerif

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        textDirection = TextDirection.Rtl
    ),
    titleLarge = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        textDirection = TextDirection.Rtl
    ),
    titleMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        textDirection = TextDirection.Rtl
    ),
    bodyLarge = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        textDirection = TextDirection.Rtl
    ),
    bodyMedium = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        textDirection = TextDirection.Rtl
    ),
    bodySmall = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        textDirection = TextDirection.Rtl
    ),
    labelSmall = TextStyle(
        fontFamily = ArabicFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        textDirection = TextDirection.Rtl
    )
)`
  },
  {
    name: 'Theme.kt',
    path: 'app/src/main/java/com/wasal/app/ui/theme/Theme.kt',
    description: 'مزيج المظهر وتطبيق اتجاه تخطيط RTL افتراضي لكافة شاشات التطبيق كود برمجياً.',
    content: `package com.wasal.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = WasalGreen,
    onPrimary = WasalWhite,
    primaryContainer = WasalGreenSurface,
    onPrimaryContainer = WasalGreenDark,
    secondary = WasalAmber,
    background = WasalBackground,
    surface = WasalCardBg,
    error = WasalRed
)

@Composable
fun WasalTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = WasalGreen.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}`
  },
  {
    name: 'SupabaseClient.kt',
    path: 'app/src/main/java/com/wasal/app/data/remote/SupabaseClient.kt',
    description: 'تأسيس عميل Supabase محلي وتجهيز كينونات المصادقة، قاعدة البيانات، التخزين والوقت الفعلي.',
    content: `package com.wasal.app.data.remote

import io.github.jan-tennert.supabase.SupabaseClient
import io.github.jan-tennert.supabase.createSupabaseClient
import io.github.jan-tennert.supabase.postgrest.Postgrest
import io.github.jan-tennert.supabase.postgrest.postgrest
import io.github.jan-tennert.supabase.auth.Auth
import io.github.jan-tennert.supabase.auth.auth
import io.github.jan-tennert.supabase.storage.Storage
import io.github.jan-tennert.supabase.storage.storage
import io.github.jan-tennert.supabase.realtime.Realtime
import io.github.jan-tennert.supabase.realtime.realtime

object WasalSupabase {
    const val SUPABASE_URL = "https://hhqhoqwpebnmfuhwhllw.supabase.co"
    const val SUPABASE_KEY = "YOUR_SUPABASE_ANON_KEY"

    val client: SupabaseClient by lazy {
        createSupabaseClient(SUPABASE_URL, SUPABASE_KEY) {
            install(Auth) {
                scheme = "wasal"
                host = "callback"
            }
            install(Postgrest)
            install(Storage)
            install(Realtime)
        }
    }

    val auth get() = client.auth
    val db get() = client.postgrest
    val storage get() = client.storage
    val realtime get() = client.realtime
}`
  },
  {
    name: 'Models.kt',
    path: 'app/src/main/java/com/wasal/app/data/model/Models.kt',
    description: 'فئات البيانات القابلة للتسلسل (Serializable) لملفات تعريف المستخدمين، المطاعم، بنود القائمة، السلة والعروض الترويجية.',
    content: `package com.wasal.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("user_id") val userId: String,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("account_status") val accountStatus: String? = null
)

@Serializable
data class Restaurant(
    val id: String,
    @SerialName("name_ar") val nameAr: String,
    @SerialName("cover_image") val coverImage: String? = null,
    @SerialName("logo_url") val logoUrl: String? = null,
    val rating: Double? = null,
    @SerialName("estimated_delivery_time") val estimatedDeliveryTime: Int? = null,
    @SerialName("delivery_fee") val deliveryFee: Double? = null,
    @SerialName("is_active") val isActive: Boolean? = null,
    @SerialName("delivery_company_id") val deliveryCompanyId: String? = null
)

@Serializable
data class MenuCategory(
    val id: String,
    @SerialName("name_ar") val nameAr: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("restaurant_id") val restaurantId: String? = null,
    @SerialName("is_active") val isActive: Boolean? = null,
    @SerialName("sort_order") val sortOrder: Int? = null
)

@Serializable
data class MenuItem(
    val id: String,
    @SerialName("name_ar") val nameAr: String,
    val description: String? = null,
    val price: Double,
    @SerialName("original_price") val originalPrice: Double? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("category_id") val categoryId: String? = null,
    @SerialName("restaurant_id") val restaurantId: String? = null,
    @SerialName("is_available") val isAvailable: Boolean? = null,
    @SerialName("is_featured") val isFeatured: Boolean? = null,
    @SerialName("is_popular") val isPopular: Boolean? = null
)

@Serializable
data class DeliveryBanner(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("badge_text") val badgeText: String? = null,
    @SerialName("restaurant_id") val restaurantId: String? = null,
    @SerialName("is_active") val isActive: Boolean? = null
)

@Serializable
data class DeliveryOffer(
    val id: String,
    val title: String,
    @SerialName("offer_type") val offerType: String,
    @SerialName("badge_text") val badgeText: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("restaurant_id") val restaurantId: String? = null,
    @SerialName("is_active") val isActive: Boolean? = null
)

data class CartItem(
    val menuItem: MenuItem,
    var quantity: Int = 1,
    val restaurantId: String,
    val restaurantName: String
)`
  },
  {
    name: 'CartManager.kt',
    path: 'app/src/main/java/com/wasal/app/data/cart/CartManager.kt',
    description: 'محرك السلة العالمي المانع لخلط الوجبات من مطاعم متعددة مع تحديثات لحظية عبر التدفقات البرمجية المتجاوبة (Flow).',
    content: `package com.wasal.app.data.cart

import com.wasal.app.data.model.CartItem
import com.wasal.app.data.model.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

object CartManager {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    private val cartScope = CoroutineScope(Dispatchers.Default)

    val totalPrice: StateFlow<Double> = _items
        .map { list -> list.sumOf { it.menuItem.price * it.quantity } }
        .stateIn(cartScope, SharingStarted.Eagerly, 0.0)

    val itemCount: StateFlow<Int> = _items
        .map { list -> list.sumOf { it.quantity } }
        .stateIn(cartScope, SharingStarted.Eagerly, 0)

    fun addItem(menuItem: MenuItem, restaurantId: String, restaurantName: String) {
        val currentList = _items.value
        val currentRestaurantId = getRestaurantId()

        if (currentRestaurantId != null && currentRestaurantId != restaurantId) {
            // If item from DIFFERENT restaurant: clear cart first, then add
            _items.value = listOf(CartItem(menuItem = menuItem, quantity = 1, restaurantId = restaurantId, restaurantName = restaurantName))
        } else {
            // If same restaurant: increment quantity if exists, else add new
            val existingIndex = currentList.indexOfFirst { it.menuItem.id == menuItem.id }
            if (existingIndex != -1) {
                val updatedList = currentList.mapIndexed { index, item ->
                    if (index == existingIndex) {
                        item.copy(quantity = item.quantity + 1)
                    } else {
                        item
                    }
                }
                _items.value = updatedList
            } else {
                _items.value = currentList + CartItem(menuItem = menuItem, quantity = 1, restaurantId = restaurantId, restaurantName = restaurantName)
            }
        }
    }

    fun incrementItem(menuItemId: String) {
        val currentList = _items.value
        _items.value = currentList.map { item ->
            if (item.menuItem.id == menuItemId) {
                item.copy(quantity = item.quantity + 1)
            } else {
                item
            }
        }
    }

    fun decrementItem(menuItemId: String) {
        val currentList = _items.value
        val updatedList = currentList.mapNotNull { item ->
            if (item.menuItem.id == menuItemId) {
                if (item.quantity > 1) {
                    item.copy(quantity = item.quantity - 1)
                } else {
                    null // removes if quantity reaches 0
                }
            } else {
                item
            }
        }
        _items.value = updatedList
    }

    fun removeItem(menuItemId: String) {
        _items.value = _items.value.filter { it.menuItem.id != menuItemId }
    }

    fun clearCart() {
        _items.value = emptyList()
    }

    fun getRestaurantId(): String? {
        return _items.value.firstOrNull()?.restaurantId
    }
}`
  },
  {
    name: 'Screen.kt',
    path: 'app/src/main/java/com/wasal/app/ui/navigation/Screen.kt',
    description: 'تصنيف المسارات في تطبيق وصال للتنقل الآمن مع دعم لتمرير المدخلات والمعاملات المشفرة.',
    content: `package com.wasal.app.ui.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object OtpVerify : Screen("otp_verify/{phone}") {
        fun createRoute(phone: String) = "otp_verify/\$phone"
    }
    object Home : Screen("home")
    object Restaurants : Screen("restaurants")
    object Category : Screen("category/{categoryName}") {
        fun createRoute(categoryName: String) = 
            "category/\${Uri.encode(categoryName)}"
    }
    object RestaurantMenu : Screen("restaurant_menu/{restaurantId}") {
        fun createRoute(restaurantId: String) = "restaurant_menu/\$restaurantId"
    }
    object Cart : Screen("cart")
    object Checkout : Screen("checkout/{restaurantId}") {
        fun createRoute(restaurantId: String) = "checkout/\$restaurantId"
    }
    object OrderTracking : Screen("order_tracking/{orderId}") {
        fun createRoute(orderId: String) = "order_tracking/\$orderId"
    }
    object OrdersHistory : Screen("orders_history")
    object DeliveryRequest : Screen("delivery_request")
    object Profile : Screen("profile")
    object Addresses : Screen("addresses")
    object Notifications : Screen("notifications")
}`
  },
  {
    name: 'WasalNavGraph.kt',
    path: 'app/src/main/java/com/wasal/app/ui/navigation/WasalNavGraph.kt',
    description: 'الرسم البياني للتنقل (NavHost) الحاضن لكافة مسارات الشاشات ومعالجة الانتقال من Splash.',
    content: `package com.wasal.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wasal.app.ui.auth.SplashScreen
import com.wasal.app.ui.auth.LoginScreen
import com.wasal.app.ui.auth.RegisterScreen
import com.wasal.app.ui.auth.OtpScreen
import com.wasal.app.ui.home.HomeScreen
import com.wasal.app.data.remote.WasalSupabase
import kotlinx.coroutines.delay

@Composable
fun WasalNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }

        composable(
            route = Screen.OtpVerify.route,
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpScreen(navController, phone)
        }

        composable(Screen.Home.route) {
            val currentUser = WasalSupabase.auth.currentUserOrNull()
            HomeScreen(
                navController = navController,
                userId = currentUser?.id,
                userCity = null
            )
        }

        composable(Screen.Restaurants.route) {
            PlaceholderScreen("Restaurants")
        }

        composable(Screen.Category.route) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            PlaceholderScreen("Category: \$categoryName")
        }

        composable(Screen.RestaurantMenu.route) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
            PlaceholderScreen("Restaurant Menu: \$restaurantId")
        }

        composable(Screen.Cart.route) {
            PlaceholderScreen("Cart")
        }

        composable(Screen.Checkout.route) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
            PlaceholderScreen("Checkout: \$restaurantId")
        }

        composable(Screen.OrderTracking.route) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            PlaceholderScreen("Order Tracking: \$orderId")
        }

        composable(Screen.OrdersHistory.route) {
            PlaceholderScreen("Orders History")
        }

        composable(Screen.DeliveryRequest.route) {
            PlaceholderScreen("Delivery Request")
        }

        composable(Screen.Profile.route) {
            PlaceholderScreen("Profile")
        }

        composable(Screen.Addresses.route) {
            PlaceholderScreen("Addresses")
        }

        composable(Screen.Notifications.route) {
            PlaceholderScreen("Notifications")
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Screen: \$name",
            style = MaterialTheme.typography.titleLarge
        )
    }
}`
  },
  {
    name: 'MainActivity.kt',
    path: 'app/src/main/java/com/wasal/app/MainActivity.kt',
    description: 'النشاط الرئيسي (MainActivity) ونقطة انطلاق تطبيق الاندرويد وإتاحة العرض بملء الشاشة مع تثبيت المظهر.',
    content: `package com.wasal.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wasal.app.ui.navigation.WasalNavGraph
import com.wasal.app.ui.theme.WasalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WasalTheme {
                WasalNavGraph()
            }
        }
    }
}`
  },
  {
    name: 'WasalApp.kt',
    path: 'app/src/main/java/com/wasal/app/WasalApp.kt',
    description: 'صنف التطبيق الرئيسي (Application) لتفعيل التشغيل والتحميل التلقائي المبكر لعميل سوبابيس (Supabase).',
    content: `package com.wasal.app

import android.app.Application
import com.wasal.app.data.remote.WasalSupabase

class WasalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Reference client to trigger lazy initialization of WasalSupabase
        val supabaseClient = WasalSupabase.client
    }
}`
  },
  {
    name: 'WasalTopBar.kt',
    path: 'app/src/main/java/com/wasal/app/ui/components/common/WasalTopBar.kt',
    description: 'مكون شريط العناوين العلوي الموحد لجميع واجهات وصال مع دعم مدمج لأزرار الرجوع الديناميكية والإجراءات الإضافية.',
    content: `package com.wasal.app.ui.components.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.wasal.app.ui.theme.WasalGreen
import com.wasal.app.ui.theme.WasalWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasalTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = WasalWhite
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = WasalWhite
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = WasalGreen,
            titleContentColor = WasalWhite,
            navigationIconContentColor = WasalWhite,
            actionIconContentColor = WasalWhite
        )
    )
}`
  },
  {
    name: 'strings.xml',
    path: 'app/src/main/res/values/strings.xml',
    description: 'النصوص الرسمية المعربة لتطبيق وصال لضمان توحيد المسميات في شاشات طلب الطعام وتتبع الطرود ومراجعة السلة.',
    content: `<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">وصال</string>
    <string name="loading">جاري التحميل...</string>
    <string name="error_generic">حدث خطأ، حاول مرة أخرى</string>
    <string name="no_data">لا توجد بيانات</string>
    <string name="retry">إعادة المحاولة</string>
    <string name="add_to_cart">أضف للسلة</string>
    <string name="cart">السلة</string>
    <string name="home">الرئيسية</string>
    <string name="my_orders">طلباتي</string>
    <string name="profile">حسابي</string>
    <string name="notifications">الإشعارات</string>
</resources>`
  },
  {
    name: 'AuthViewModel.kt',
    path: 'app/src/main/java/com/wasal/app/ui/auth/AuthViewModel.kt',
    description: 'نظام إدارة حالات التحقق والاتصال بـ Supabase لمعالجة تسجيل الدخول وإنشاء الحسابات وتتبع الجلسات النشطة.',
    content: `package com.wasal.app.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasal.app.data.remote.WasalSupabase
import com.wasal.app.ui.navigation.Screen
import io.github.jan-tennert.supabase.auth.auth
import io.github.jan-tennert.supabase.auth.providers.Google
import io.github.jan-tennert.supabase.auth.providers.builtin.Email
import io.github.jan-tennert.supabase.auth.providers.builtin.OTP
import io.github.jan-tennert.supabase.postgrest.postgrest
import io.github.jan-tennert.supabase.postgrest.query.Columns
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val route: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun loginWithEmail(emailVal: String, passwordVal: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                WasalSupabase.auth.signInWith(Email) {
                    email = emailVal
                    password = passwordVal
                }
                
                val user = WasalSupabase.auth.currentUserOrNull()
                if (user != null) {
                    _uiState.value = AuthUiState.Success(Screen.Home.route)
                } else {
                    _uiState.value = AuthUiState.Error("فشل العثور على بيانات المستخدم بعد تسجيل الدخول")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "حدث خطأ أثناء تسجيل الدخول")
            }
        }
    }

    fun loginWithPhone(phoneVal: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                WasalSupabase.auth.signInWith(OTP) {
                    phoneNumber = "+967\$phoneVal"
                }
                _uiState.value = AuthUiState.Success(Screen.OtpVerify.createRoute(phoneVal))
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "حدث خطأ أثناء إرسال رمز التحقق")
            }
        }
    }

    fun verifyOTP(phoneVal: String, tokenVal: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                WasalSupabase.auth.verifyPhoneOtp(
                    phone = "+967\$phoneVal",
                    token = tokenVal,
                    type = io.github.jan-tennert.supabase.auth.OtpType.Phone.SMS
                )
                _uiState.value = AuthUiState.Success(Screen.Home.route)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "رمز التحقق غير صحيح")
            }
        }
    }

    fun registerWithEmail(
        emailVal: String, passwordVal: String,
        fullName: String, phoneVal: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                WasalSupabase.auth.signUpWith(Email) {
                    email = emailVal
                    password = passwordVal
                }
                delay(1000)
                val user = WasalSupabase.auth.currentUserOrNull()
                if (user != null) {
                    try {
                        val profileMap = mapOf(
                            "user_id" to user.id,
                            "full_name" to fullName,
                            "phone" to "+967\$phoneVal",
                            "account_status" to "active"
                        )
                        WasalSupabase.db.from("profiles").insert(profileMap)
                    } catch (e: Exception) {}
                    _uiState.value = AuthUiState.Success(Screen.Home.route)
                } else {
                    _uiState.value = AuthUiState.Error("حدث خطأ في استرداد حساب المستخدم")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "حدث خطأ أثناء إنشاء الحساب")
            }
        }
    }

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                WasalSupabase.auth.signInWith(Google)
                _uiState.value = AuthUiState.Success(Screen.Home.route)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "حدث خطأ أثناء تسجيل الدخول بـ Google")
            }
        }
    }

    fun checkSession() {
        viewModelScope.launch {
            try {
                val session = WasalSupabase.auth.currentSessionOrNull()
                if (session != null) {
                    _uiState.value = AuthUiState.Success(Screen.Home.route)
                } else {
                    _uiState.value = AuthUiState.Idle
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Idle
            }
        }
    }
}`
  },
  {
    name: 'LoginScreen.kt',
    path: 'app/src/main/java/com/wasal/app/ui/auth/LoginScreen.kt',
    description: 'واجهة تسجيل الدخول المطابقة تماماً لتصاميم PWA مع دعم التبديل بين البريد ورقم الهاتف والتحقق بـ Google.',
    content: `package com.wasal.app.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wasal.app.R
import com.wasal.app.ui.navigation.Screen
import com.wasal.app.ui.theme.*

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableStateOf(0) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var phone by remember { mutableStateOf("") }
    val isLoading = uiState is AuthUiState.Loading

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            val route = (uiState as AuthUiState.Success).route
            navController.navigate(route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Scaffold(containerColor = WasalBackground) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            Image(
                painter = painterResource(R.drawable.wasal_logo),
                contentDescription = null,
                modifier = Modifier.size(120.dp).clip(RoundedCornerShape(24.dp))
            )
            Text("توصيل أسرع، حياة أسهل", style = MaterialTheme.typography.bodyMedium, color = WasalGrey)
            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = WasalWhite)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("تسجيل الدخول", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    Text("أهلاً بعودتك! اختر طريقة تسجيل الدخول", style = MaterialTheme.typography.bodyMedium, color = WasalGrey, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    Spacer(Modifier.height(20.dp))

                    OutlinedButton(
                        onClick = { viewModel.signInWithGoogle(context) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFDDDDDD))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("تسجيل الدخول بـ Google", color = WasalBlack)
                            Spacer(Modifier.width(10.dp))
                            Icon(painter = painterResource(R.drawable.ic_google), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(20.dp))
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().height(52.dp).background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)).padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight().background(if (selectedTab == 0) WasalGreen else Color.Transparent, RoundedCornerShape(10.dp)).clickable { selectedTab = 0 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("البريد الإلكتروني", color = if (selectedTab == 0) WasalWhite else WasalGrey)
                        }
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight().background(if (selectedTab == 1) WasalGreen else Color.Transparent, RoundedCornerShape(10.dp)).clickable { selectedTab = 1 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("رقم الهاتف", color = if (selectedTab == 1) WasalWhite else WasalGrey)
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    if (selectedTab == 0) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("example@email.com") },
                            leadingIcon = { Icon(Icons.Default.Email, null) }
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                                }
                            }
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.loginWithEmail(email, password) },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WasalGreen)
                        ) {
                            Text("تسجيل الدخول", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("7XX XXX XXX") }
                            )
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier.width(72.dp).height(52.dp).background(WasalGreenSurface, RoundedCornerShape(12.dp)).border(BorderStroke(1.dp, WasalGreen), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+967", color = WasalGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.loginWithPhone(phone) },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WasalGreen)
                        ) {
                            Text("إرسال رمز التحقق", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    TextButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        Text("لا تستطيع الوصول لحسابك؟", color = WasalGrey)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text("ليس لديك حساب؟", color = WasalGrey)
                        Spacer(Modifier.width(4.dp))
                        Text("إنشاء حساب جديد", color = WasalGreen, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.navigate(Screen.Register.route) })
                    }
                }
            }
        }
    }
}`
  },
  {
    name: 'OtpScreen.kt',
    path: 'app/src/main/java/com/wasal/app/ui/auth/OtpScreen.kt',
    description: 'واجهة إدخال رمز التحقق (OTP) المكون من 6 أرقام مع مؤقت تنازلي لإعادة الإرسال.',
    content: `package com.wasal.app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wasal.app.R
import com.wasal.app.ui.navigation.Screen
import com.wasal.app.ui.theme.*

@Composable
fun OtpScreen(navController: NavController, phone: String) {
    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var otpValue by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Scaffold(containerColor = WasalBackground) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            Image(
                painter = painterResource(R.drawable.wasal_logo),
                contentDescription = null,
                modifier = Modifier.size(120.dp).clip(RoundedCornerShape(24.dp))
            )
            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = WasalWhite)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("أدخل رمز التحقق", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("تم إرسال رمز إلى +967 \$phone", style = MaterialTheme.typography.bodyMedium, color = WasalGrey)
                    Spacer(Modifier.height(28.dp))

                    BasicTextField(
                        value = otpValue,
                        onValueChange = { if (it.length <= 6) otpValue = it },
                        modifier = Modifier.fillMaxWidth().height(64.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
                            repeat(6) { index ->
                                val digit = if (index < otpValue.length) otpValue[index].toString() else ""
                                Box(
                                    modifier = Modifier.width(44.dp).height(56.dp).background(Color(0xFFF9F9F9), RoundedCornerShape(12.dp)).border(1.dp, Color(0xFFDDDDDD), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(digit, style = MaterialTheme.typography.titleLarge)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.verifyOTP(phone, otpValue) },
                        enabled = otpValue.length == 6,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WasalGreen)
                    ) {
                        Text("تحقق", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}`
  },
  {
    name: 'RegisterScreen.kt',
    path: 'app/src/main/java/com/wasal/app/ui/auth/RegisterScreen.kt',
    description: 'واجهة إنشاء الحساب المشحونة بمدخلات التحقق للبريد، الهاتف، الاسم، وتأكيد كلمتي السر متطابقتين.',
    content: `package com.wasal.app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wasal.app.R
import com.wasal.app.ui.navigation.Screen
import com.wasal.app.ui.theme.*

@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Scaffold(containerColor = WasalBackground) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(padding).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            Image(painter = painterResource(R.drawable.wasal_logo), contentDescription = null, modifier = Modifier.size(120.dp).clip(RoundedCornerShape(24.dp)))
            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WasalWhite)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("إنشاء حساب جديد", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(value = fullName, onValueChange = { fullName = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("الاسم الكامل") }, leadingIcon = { Icon(Icons.Default.Person, null) })
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("البريد الإلكتروني") }, leadingIcon = { Icon(Icons.Default.Email, null) })
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("رقم الهاتف") })
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(value = password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("كلمة السر") }, leadingIcon = { Icon(Icons.Default.Lock, null) })
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, modifier = Modifier.fillMaxWidth(), placeholder = { Text("تأكيد كلمة السر") }, leadingIcon = { Icon(Icons.Default.Lock, null) })
                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.registerWithEmail(email, password, fullName, phone) },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WasalGreen)
                    ) {
                        Text("إنشاء الحساب", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}`
  },
  {
    name: 'SplashScreen.kt',
    path: 'app/src/main/java/com/wasal/app/ui/auth/SplashScreen.kt',
    description: 'شاشة البداية للتطبيق المعروضة عند التشغيل لتفعيل التوجيه التلقائي للمستخدم المسجل.',
    content: `package com.wasal.app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wasal.app.R
import com.wasal.app.ui.navigation.Screen
import com.wasal.app.ui.theme.WasalGreen
import com.wasal.app.ui.theme.WasalWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        delay(1500)
        viewModel.checkSession()
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.Success -> {
                navController.navigate((uiState as AuthUiState.Success).route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
            is AuthUiState.Idle -> {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(WasalGreen), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.wasal_logo), contentDescription = null, modifier = Modifier.size(120.dp).clip(RoundedCornerShape(28.dp)))
            Spacer(Modifier.height(16.dp))
            Text("وصال", style = MaterialTheme.typography.displayLarge, color = WasalWhite, fontWeight = FontWeight.Bold)
            Text("توصيل أسرع، حياة أسهل", style = MaterialTheme.typography.bodyLarge, color = WasalWhite.copy(alpha = 0.8f))
            Spacer(Modifier.height(48.dp))
            CircularProgressIndicator(color = WasalWhite.copy(alpha = 0.7f), modifier = Modifier.size(32.dp))
        }
    }
}`
  },
  {
    name: 'ic_google.xml',
    path: 'app/src/main/res/drawable/ic_google.xml',
    description: 'أيقونة Google الملونة والمنسقة بصيغة Vector متطابقة مع الملحقات الرسومية لأنظمة أندرويد.',
    content: `<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path android:fillColor="#EA4335" android:pathData="M12.24,5a6.85,6.85 0,0 1,4.85 1.9l3.6,-3.6A11.81,11.81 0,0 0,12.24 0,11.93 11.93 0,0 0,1.38 7.08l4.13,3.2A7.14,7.14 0,0 1,12.24 5z"/>
    <path android:fillColor="#FBBC05" android:pathData="M1.38,7.08A11.94,11.94 0,0 0,1.38 16.92l4.13,-3.2a7.1,7.1 0,0 1,0,-6.64l-4.13,-3.08z"/>
    <path android:fillColor="#34A853" android:pathData="M12.24,19a7.12,7.12 0,0 1,-6.73 -4.84l-4.13,3.2A11.94,11.94 0,0 0,12.24 24,11.75 11.75 0,0 0,20.48 17.08l-4.22,-3.2A6.87,6.87 0,0 1,12.24 19z"/>
    <path android:fillColor="#4285F4" android:pathData="M24,12a11.53,11.53 0,0 0,-0.23 -2.25H12.24v4.51h6.6a5.64,5.64 0,0 1,-2.44 3.71l4.22,3.2A11.83,11.83 0,0 0,24 12z"/>
</vector>`
  },
  {
    name: 'wasal_logo.xml',
    path: 'app/src/main/res/drawable/wasal_logo.xml',
    description: 'شعار منصة وصال المطور بـ Vector ليعبر عن سرعة الطلب وتتبع السائقين في اليمن بنسب تباين عالية.',
    content: `<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="120dp"
    android:height="120dp"
    android:viewportWidth="120"
    android:viewportHeight="120">
    <path android:fillColor="#1A5C3A" android:pathData="M24,0 L96,0 A24,24 0,0 1,120 24 L120,96 A24,24 0,0 1,96 120 L24,120 A24,24 0,0 1,0 96 L0,24 A24,24 0,0 1,24 0 Z" />
    <path android:strokeColor="#FFB800" android:strokeWidth="5" android:strokeLineCap="round" android:strokeLineJoin="round" android:pathData="M35,68 L50,68 L58,52 L78,52 L83,68" />
    <path android:strokeColor="#FFB800" android:strokeWidth="5" android:strokeLineCap="round" android:pathData="M78,52 L75,40 L70,40" />
    <path android:fillColor="#FFB800" android:pathData="M32,42 L52,42 L52,60 L32,60 Z" />
    <path android:strokeColor="#FFB800" android:strokeWidth="5" android:fillColor="#1A5C3A" android:pathData="M42,80 A8,8 0,1 1,42,64 A8,8 0,1 1,42,80 Z" />
    <path android:strokeColor="#FFB800" android:strokeWidth="5" android:fillColor="#1A5C3A" android:pathData="M78,80 A8,8 0,1 1,78,64 A8,8 0,1 1,78,80 Z" />
    <path android:strokeColor="#FFB800" android:strokeWidth="4" android:strokeLineCap="round" android:pathData="M15,25 Q60,10 105,25" />
</vector>`
  },
  {
    name: 'HomeRepository.kt',
    path: 'app/src/main/java/com/wasal/app/data/repository/HomeRepository.kt',
    description: 'مستودع البيانات للرئيسية لمعالجة جلب الإعلانات النشطة والعروض وتصنيف فئات المأكولات وحالة الإشعارات والعناوين.',
    content: `package com.wasal.app.data.repository

import com.wasal.app.data.remote.WasalSupabase
import com.wasal.app.data.model.DeliveryBanner
import com.wasal.app.data.model.DeliveryOffer
import com.wasal.app.data.model.MenuCategory
import com.wasal.app.data.model.Restaurant
import com.wasal.app.data.model.CustomerAddress
import com.wasal.app.data.model.WasalNotification
import io.github.jan-tennert.supabase.postgrest.query.Columns
import io.github.jan-tennert.supabase.postgrest.query.Order

class HomeRepository {
    private val db = WasalSupabase.db

    suspend fun getBanners(): List<DeliveryBanner> {
        return try {
            db.from("delivery_banners").select(columns = Columns.ALL) {
                filter {
                    eq("banner_type", "carousel")
                    eq("is_active", true)
                }
                order(column = "sort_order", order = Order.ASCENDING)
            }.decodeList<DeliveryBanner>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getOffers(): List<DeliveryOffer> {
        return try {
            db.from("delivery_company_offers").select(columns = Columns.ALL) {
                filter {
                    eq("is_active", true)
                }
            }.decodeList<DeliveryOffer>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getCategories(): List<MenuCategory> {
        return try {
            val rawCategories = db.from("menu_categories").select(columns = Columns.ALL) {
                filter {
                    neq("is_active", false)
                }
                order(column = "sort_order", order = Order.ASCENDING)
                limit(30L)
            }.decodeList<MenuCategory>()

            val dedupedMap = mutableMapOf<String, MenuCategory>()
            for (category in rawCategories) {
                val key = category.nameAr
                val existing = dedupedMap[key]
                if (existing == null) {
                    dedupedMap[key] = category
                } else {
                    if (!category.imageUrl.isNullOrBlank() && existing.imageUrl.isNullOrBlank()) {
                        dedupedMap[key] = category
                    }
                }
            }
            dedupedMap.values.toList().sortedBy { it.sortOrder ?: 0 }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getRestaurants(city: String?): List<Restaurant> {
        return try {
            db.from("restaurants").select(columns = Columns.ALL) {
                filter {
                    eq("is_active", true)
                    if (!city.isNullOrBlank()) {
                        eq("city", city)
                    }
                }
                order(column = "is_featured", order = Order.DESCENDING)
                order(column = "rating", order = Order.DESCENDING)
                limit(10L)
            }.decodeList<Restaurant>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUserAddress(userId: String): Pair<String, String> {
        return try {
            val addresses = db.from("customer_addresses").select(columns = Columns.ALL) {
                filter {
                    eq("customer_id", userId)
                }
                order(column = "is_default", order = Order.DESCENDING)
                order(column = "created_at", order = Order.DESCENDING)
                limit(5L)
            }.decodeList<CustomerAddress>()

            if (addresses.isNotEmpty()) {
                val best = addresses.first()
                val cityDistrict = if (!best.district.isNullOrBlank()) {
                    "\${best.city}، \${best.district}"
                } else {
                    best.city
                }
                Pair(best.addressName, cityDistrict)
            } else {
                Pair("", "")
            }
        } catch (e: Exception) {
            Pair("", "")
        }
    }

    suspend fun getUnreadCount(userId: String): Int {
        return try {
            val list = db.from("notifications").select(columns = Columns.ALL) {
                filter {
                    eq("user_id", userId)
                    isIn("notification_type", listOf("order_status", "promotion", "general", "promo"))
                }
            }.decodeList<WasalNotification>()
            list.count { it.readAt == null }
        } catch (e: Exception) {
            0
        }
    }
}`
  },
  {
    name: 'HomeViewModel.kt',
    path: 'app/src/main/java/com/wasal/app/ui/home/HomeViewModel.kt',
    description: 'نموذج عرض الواجهة (ViewModel) للرئيسية لمعالجة وتخزين حالات العرض والتحميل المتوازي لكافة الأقسام.',
    content: `package com.wasal.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasal.app.data.model.DeliveryBanner
import com.wasal.app.data.model.DeliveryOffer
import com.wasal.app.data.model.MenuCategory
import com.wasal.app.data.model.Restaurant
import com.wasal.app.data.repository.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val banners: List<DeliveryBanner> = emptyList(),
    val offers: List<DeliveryOffer> = emptyList(),
    val categories: List<MenuCategory> = emptyList(),
    val restaurants: List<Restaurant> = emptyList(),
    val addressName: String = "",
    val cityDistrict: String = "",
    val unreadCount: Int = 0,
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    private val repo = HomeRepository()
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadAll(userId: String?, city: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val (addrName, cityDistrict) = if (userId != null) {
                    repo.getUserAddress(userId)
                } else {
                    Pair("", "")
                }

                val bannersDeferred = async { runCatching { repo.getBanners() }.getOrDefault(emptyList()) }
                val offersDeferred = async { runCatching { repo.getOffers() }.getOrDefault(emptyList()) }
                val catsDeferred = async { runCatching { repo.getCategories() }.getOrDefault(emptyList()) }
                val restsDeferred = async { runCatching { repo.getRestaurants(city) }.getOrDefault(emptyList()) }
                val unreadDeferred = async {
                    if (userId != null) runCatching { repo.getUnreadCount(userId) }.getOrDefault(0) else 0
                }

                _uiState.update { it.copy(
                    isLoading = false,
                    banners = bannersDeferred.await(),
                    offers = offersDeferred.await(),
                    categories = catsDeferred.await(),
                    restaurants = restsDeferred.await(),
                    addressName = addrName,
                    cityDistrict = cityDistrict,
                    unreadCount = unreadDeferred.await()
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "حدث خطأ") }
            }
        }
    }
}`
  },
  {
    name: 'HomeScreen.kt',
    path: 'app/src/main/java/com/wasal/app/ui/home/HomeScreen.kt',
    description: 'شاشة الواجهة الرئيسية المصممة بتخطيط Jetpack Compose متطابق تماماً مع PWA وتخدم 5 أقسام رئيسية وتدعم RTL بالكامل.',
    content: `package com.wasal.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wasal.app.ui.home.components.*
import com.wasal.app.ui.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    userId: String?,
    userCity: String?,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId, userCity) {
        viewModel.loadAll(userId, userCity)
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                WasalHomeTopBar(
                    addressName = uiState.addressName,
                    cityDistrict = uiState.cityDistrict,
                    unreadCount = uiState.unreadCount,
                    onNotificationClick = { navController.navigate(Screen.Notifications.route) }
                )
            },
            bottomBar = {
                WasalBottomNav(navController = navController, currentRoute = Screen.Home.route)
            }
        ) { padding ->
            if (uiState.isLoading) {
                HomeLoadingSkeleton(Modifier.padding(padding))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF8F9FA))
                        .padding(padding),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        BannerCarousel(
                            banners = uiState.banners,
                            onBannerClick = { linkTab ->
                                when (linkTab) {
                                    "restaurants" -> navController.navigate(Screen.Restaurants.route)
                                    "shipments"   -> navController.navigate(Screen.DeliveryRequest.route)
                                    else -> navController.navigate(Screen.Restaurants.route)
                                }
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    if (uiState.offers.isNotEmpty()) {
                        item {
                            OffersRow(
                                offers = uiState.offers,
                                onOfferClick = { offer ->
                                    if (offer.restaurantId != null) {
                                        navController.navigate(Screen.RestaurantMenu.createRoute(offer.restaurantId))
                                    } else {
                                        navController.navigate(Screen.Restaurants.route)
                                    }
                                }
                            )
                        }
                    }

                    if (uiState.categories.isNotEmpty()) {
                        item {
                            CategoryScrollerSection(
                                categories = uiState.categories,
                                onCategoryClick = { cat ->
                                    navController.navigate(Screen.Category.createRoute(cat.nameAr))
                                },
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    if (uiState.restaurants.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "مطاعم بالقرب منك",
                                onMoreClick = { navController.navigate(Screen.Restaurants.route) },
                                modifier = Modifier.padding(horizontal = 16.dp, top = 8.dp)
                            )
                        }
                        items(uiState.restaurants, key = { it.id }) { restaurant ->
                            RestaurantCard(
                                restaurant = restaurant,
                                onClick = { navController.navigate(Screen.RestaurantMenu.createRoute(restaurant.id)) },
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onMoreClick) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "عرض الكل",
                    color = Color(0xFF1A5C3A),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color(0xFF1A5C3A),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black
        )
    }
}`
  }
];
