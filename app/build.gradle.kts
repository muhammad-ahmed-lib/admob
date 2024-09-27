plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.codetech.admob"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.codetech.admob"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                resValue("string", "app_id", "ca-app-pub-3940256099942544~3347511713")
                resValue("string", "banner_id", "ca-app-pub-3940256099942544/6300978111")
                resValue("string", "interstitial_id", "ca-app-pub-3940256099942544/1033173712")
                resValue("string", "app_open_id", "ca-app-pub-3940256099942544/9257395921")
                resValue("string", "native_ad_id", "ca-app-pub-3940256099942544/2247696110")
            }
            debug {

                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )

                resValue("string", "app_id", "ca-app-pub-3940256099942544~3347511713")
                resValue("string", "banner_id", "ca-app-pub-3940256099942544/6300978111")
                resValue("string", "interstitial_id", "ca-app-pub-3940256099942544/1033173712")
                resValue("string", "app_open_id", "ca-app-pub-3940256099942544/9257395921")
                resValue("string", "native_ad_id", "ca-app-pub-3940256099942544/2247696110")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("com.google.android.gms:play-services-ads:23.3.0")
    implementation ("com.google.android.ump:user-messaging-platform:3.0.0")
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    implementation ("com.google.android.gms:play-services-ads-identifier:18.1.0")
    implementation(project(":ads"))
}