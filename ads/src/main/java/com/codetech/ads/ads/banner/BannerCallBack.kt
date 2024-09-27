package com.codetech.ads.ads.banner

import com.google.android.gms.ads.AdError

interface BannerCallBack {
    fun onAdFailedToLoad(adError:AdError)
    fun onAdLoaded()
    fun onAdImpression()
    fun onAdClicked()
    fun onAdClosed()
    fun onAdOpened()
    fun onAdSwipeGestureClicked()
}