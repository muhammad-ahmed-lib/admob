package com.codetech.ads.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.codetech.ads.ads.AdsConstants.isAppOpenAdShowing
import com.codetech.ads.ads.helpers.isAppOpenAdShowing
import com.codetech.ads.ads.helpers.isInterstitialAdShowing
import com.codetech.ads.ads.helpers.setAppOpenAdShowing
import com.google.android.gms.ads.*
import com.google.android.gms.ads.appopen.AppOpenAd

import java.util.Date

class AppOpenResumeAd(private val appContext: Application,private val adUnit:String) : Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appContext.registerActivityLifecycleCallbacks(this)
        appOpenAdManager = AppOpenAdManager()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        currentActivity?.let { appOpenAdManager.showAdIfAvailable(it) }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        if (!isAppOpenAdShowing) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    private inner class AppOpenAdManager {

        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        private var loadTime: Long = 0

        fun loadAd(context: Context) {
            if (isLoadingAd || isAdAvailable()) return

            isLoadingAd = true
            val adRequest = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                adUnit,
                adRequest,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                        Log.d(LOG_TAG, "Ad successfully loaded")
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingAd = false
                        Log.e(LOG_TAG, "Failed to load ad: ${loadAdError.message}")
                    }
                }
            )
        }

        private fun wasLoadTimeLessThanNHoursAgo(hours: Long): Boolean {
            val timeElapsed = Date().time - loadTime
            val millisecondsPerHour = 3600000
            return timeElapsed < millisecondsPerHour * hours
        }

        private fun isAdAvailable(): Boolean {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        fun showAdIfAvailable(
            activity: Activity,
            onShowAdCompleteListener: OnShowAdCompleteListener? = null
        ) {
            if (appContext.isAppOpenAdShowing || appContext.isInterstitialAdShowing) {
                Log.d(LOG_TAG, "Another ad is already showing.")
                return
            }

            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "Ad is not available.")
                onShowAdCompleteListener?.onShowAdComplete()
                loadAd(activity)
                return
            }

            appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    appContext.setAppOpenAdShowing(false)
                    onShowAdCompleteListener?.onShowAdComplete()
                    loadAd(activity)
                    Log.d(LOG_TAG, "Ad dismissed.")
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    appContext.setAppOpenAdShowing(false)
                    onShowAdCompleteListener?.onShowAdComplete()
                    Log.e(LOG_TAG, "Failed to show ad: ${adError.message}")
                }

                override fun onAdShowedFullScreenContent() {
                    appContext.setAppOpenAdShowing(true)
                    Log.d(LOG_TAG, "Ad showed successfully.")
                }
            }
            appOpenAd?.show(activity)
        }
    }
        companion object {
        private const val LOG_TAG = "MyApplication"
    }
}
