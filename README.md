Android AdMob Custom Library for both Jetpack Compose and XML

Description

A comprehensive library for managing AdMob ads in Android applications, including App Open Ads, Interstitial Ads, Banner Ads, and Native Ads. 
This library allows developers to easily implement various types of ads with minimal code.

Sample

![WhatsApp Image 2024-09-27 at 11 49 02 PM](https://github.com/user-attachments/assets/98205999-9eb4-4eb4-9868-aadd30ca62f1)

Features

App Open Ads management

Interstitial Ads management

Multiple Banner and Native Ad types


Easy integration and usage

Installation

To use this library, add the following dependency to your build.gradle file:


Copy code

      maven { url = uri("https://jitpack.io") }
      implementation ("com.github.muhammad-ahmed-lib:admob:beta-1.0")
  
Usage
Show Banner Ad
To display a banner ad, use the ShowBannerAd composable function:

kotlin
Copy code
ShowBannerAd(
    adUnit = stringResource(id = R.string.banner_id),
    bannerType = BannerType.ADAPTIVE
)
Show Interstitial Ad
To load and show an interstitial ad, use the InterstitialAds class:

Copy code

InterstitialAds(context).loadAndShow(
  
    adUnit = context.getString(R.string.interstitial_id),
    isAppPurchased = false,
    remoteConfig = true,
    isInternetConnected = true,
   onAdDismiss = {
     
        // Handle ad dismiss here
    }
)

Show App Open Ad

To manage App Open Ads, initialize the AppOpenResumeAd class:

Copy code

AppOpenResumeAd(this, getString(R.string.app_open_id))

Show Native Ad

To display a native ad, use the ShowNativeAd function:

Copy code

ShowNativeAd(
   
    adUnit = stringResource(id = R.string.native_ad_id),
    nativeType = NativeType.BANNER
)

Enum Types

Define your ad types using the following enums:


Copy code

enum class NativeType {
   
    BANNER,
    SMALL,
    SMALL_ADJUSTED,
    LARGE,
    MEDIUM
}

enum class BannerType {
  
    ADAPTIVE,
    MEDIUM_RECTANGLE,
    COLLAPSIBLE_TOP,
    COLLAPSIBLE_BOTTOM,
    SMALL
}
