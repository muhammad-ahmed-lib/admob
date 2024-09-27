package com.codetech.ads.ads.interstitial

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import com.codetech.ads.ads.interstitial.InterstitialConstants.isShouldShowAds
import com.codetech.ads.ads.helpers.dismissAdDialog
import com.codetech.ads.ads.helpers.isAppOpenAdShowing
import com.codetech.ads.ads.helpers.setInterstitialAdShowing
import com.codetech.ads.ads.helpers.showAdDialog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


object InterstitialConstants {
    var isShouldShowAds = true
    var preloadedInterstitialAd: InterstitialAd? = null
    var adTimer: Long = 10000
}

class InterstitialAds (private val activity: Activity) {
    private val TAG = "AdmobInterstitialAdInfo"
    private var interstitialAd: InterstitialAd? = null

    fun loadAndShow(
        adUnit: String,
        isAppPurchased: Boolean,
        isInternetConnected: Boolean,
        remoteConfig: Boolean,
        loadingMessage: String = "Ad is Loading",
        callBack: InterstitialCallBack
    ) {

        if (isAppPurchased){
            callBack.onAppPurchased()
            return
        }

        if (activity.isAppOpenAdShowing||!isShouldShowAds){
            callBack.onAdNotReadyYet()
            return
        }

        if (!isInternetConnected&&!remoteConfig){
            Log.d(TAG, "loadAndShow: disabled internet$isInternetConnected remote$remoteConfig")
            callBack.onError("internet error or remote config")
            return
        }
        
        activity.showAdDialog(loadingMessage,isShouldCancel = true)
        activity.showAdDialog(
            loadingMessage = loadingMessage
        )
        InterstitialAd.load(activity, adUnit,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    callBack.onAdLoaded()
                    interstitialAd = ad
                    activity.dismissAdDialog()
                    activity.setInterstitialAdShowing(true)
                    interstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                interstitialAd = null
                                activity.setInterstitialAdShowing(false)
                                activity.dismissAdDialog()
                                callBack.onAdDismissed()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                activity.dismissAdDialog()
                                activity.setInterstitialAdShowing(false)
                                callBack.onAdFailedToShow(adError)
                            }

                            override fun onAdShowedFullScreenContent() {
                                callBack.onAdShowed()
                            }

                            override fun onAdImpression() {
                                super.onAdImpression()
                                callBack.onAdImpression()
                            }

                            override fun onAdClicked() {
                                super.onAdClicked()
                                callBack.onAdClicked()
                            }
                        }
                    interstitialAd?.show(activity)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    activity.dismissAdDialog()
                    callBack.onAdFailedToLoad(adError)
                    Log.d(TAG, "onAdFailedToLoad: ${adError.message}")
                }
            })
    }


    fun loadAndShow(
        adUnit: String,
        loadingMessage: String = "Ad is Loading",
        isInternetConnected: Boolean=true,
        remoteConfig: Boolean=true,
        isAppPurchased:Boolean=false,
        onAdDismiss: () -> Unit
    ) {
        if (isAppPurchased){
            onAdDismiss()
            return
        }

        if (activity.isAppOpenAdShowing) {
            onAdDismiss()
            return
        }
        if (!isInternetConnected&&!remoteConfig){
            Log.d(TAG, "loadAndShow: disabled internet$isInternetConnected remote$remoteConfig")
            onAdDismiss()
            return
        }

        if (InterstitialConstants.preloadedInterstitialAd !=null){
            showPreloadedAd(activity, onAdDismiss)
            return
        }

        activity.showAdDialog(loadingMessage,isShouldCancel = true)

        InterstitialAd.load(activity, adUnit,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    activity.setInterstitialAdShowing(true)
                    interstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                interstitialAd = null
                                activity.dismissAdDialog()
                                activity.setInterstitialAdShowing(false)
                                onAdDismiss()
                                Log.d(TAG, "onAdDismissedFullScreenContent: ")
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                activity.dismissAdDialog()
                                activity.setInterstitialAdShowing(false)
                                onAdDismiss()
                                Log.d(TAG, "onAdFailedToShowFullScreenContent: ")
                            }

                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "onAdShowedFullScreenContent: ")
                            }

                            override fun onAdImpression() {
                                super.onAdImpression()
                                Log.d(TAG, "onAdImpression: ")
                            }

                            override fun onAdClicked() {
                                super.onAdClicked()
                                Log.d(TAG, "onAdClicked: ")
                            }
                        }
                    interstitialAd?.show(activity)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    activity.dismissAdDialog()
                    onAdDismiss()
                    Log.d(TAG, "onAdFailedToLoad: ${adError.message}")
                }
            })
    }

    private fun showPreloadedAd(
        activity: Activity,
        onAdDismiss: () -> Unit
    ) {

        if (InterstitialConstants.preloadedInterstitialAd != null) {
            activity.setInterstitialAdShowing(true)
            InterstitialConstants.preloadedInterstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        activity.setInterstitialAdShowing(false)
                        TimerManager.onAdDismissed()
                        InterstitialConstants.preloadedInterstitialAd =null
                        onAdDismiss()
                        Log.d(TAG, "onAdDismissedFullScreenContent: ")
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        activity.setInterstitialAdShowing(false)
                        TimerManager.onAdDismissed()
                        InterstitialConstants.preloadedInterstitialAd =null
                        Log.d(TAG, "onAdFailedToShowFullScreenContent: ")
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "onAdShowedFullScreenContent: ")
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                        InterstitialConstants.preloadedInterstitialAd =null
                        Log.d(TAG, "onAdImpression: ")
                    }

                    override fun onAdClicked() {
                        super.onAdClicked()
                        Log.d(TAG, "onAdClicked: ")
                    }
                }
            InterstitialConstants.preloadedInterstitialAd?.show(activity)
        } else {
            TimerManager.onAdDismissed()
            onAdDismiss()
        }
    }

}