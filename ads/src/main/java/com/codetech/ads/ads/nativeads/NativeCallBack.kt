package com.codetech.admobads.ads.nativeads

import com.google.android.gms.ads.AdError

interface NativeCallBack {
    fun onAdFailedToLoad(adError:AdError)
    fun onAdLoaded()
    fun onAdImpression()
    fun onPreloaded()
    fun onAdClicked()
}