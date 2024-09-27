package com.codetech.ads.ads.interstitial

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError

interface InterstitialCallBack {
    fun onAdLoaded()
    fun onAdFailedToLoad(adError: LoadAdError)
    fun onAdDismissed()
    fun onAdFailedToShow(adError: AdError)
    fun onAdShowed()
    fun onAdImpression()
    fun onAdClicked()
    fun onAppPurchased()
    fun onAdNotReadyYet()
    fun onError(error:String)
}