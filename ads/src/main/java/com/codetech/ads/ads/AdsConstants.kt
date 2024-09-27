package com.codetech.ads.ads

import com.google.android.gms.ads.nativead.NativeAd

object AdsConstants {
    var isInterstitialShowing: Boolean = false
    var isAppOpenAdShowing: Boolean = false
    var adMobPreloadNativeAd: NativeAd? = null
    fun reset(){
        adMobPreloadNativeAd?.destroy()
        adMobPreloadNativeAd = null
    }
}