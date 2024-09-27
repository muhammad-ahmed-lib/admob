package com.codetech.ads.ads.banner

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowMetrics
import androidx.core.view.isVisible
import com.codetech.ads.ads.banner.BannerConstants.adLoaded
import com.codetech.ads.ads.banner.BannerConstants.adView
import com.codetech.ads.databinding.BannerShimerBinding
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*

object BannerConstants {
    var preloadedBanner: AdView? = null
    var adView: AdView? = null
    var adLoaded = false
}

class BannerAds(private val activity: Activity) {
    private val TAG = "BannerAdsInfo"

    fun showBannerAd(
        binding: BannerShimerBinding,
        adUnitId: String,
        isInternetConnect: Boolean = true,
        remoteConfig: Boolean = true,
        adType: BannerType,
        bannerCallBack: BannerCallBack
    ) {
        if (!isInternetConnect || !remoteConfig) {
            Log.d(TAG, "Network or remote config is not available.")
            return
        }
        when (adType) {
            BannerType.ADAPTIVE -> loadAdaptiveBanner(binding, adUnitId, bannerCallBack)
            BannerType.SMALL -> loadSmallBanner(binding, adUnitId, bannerCallBack)
            BannerType.COLLAPSIBLE_BOTTOM -> loadCollapsibleBannerBottom(binding, adUnitId, bannerCallBack)
            BannerType.COLLAPSIBLE_TOP -> loadCollapsibleBannerTop(binding, adUnitId, bannerCallBack)
            BannerType.MEDIUM_RECTANGLE -> loadMediumRectangleBanner(binding, adUnitId, bannerCallBack)
        }
    }

    private fun loadAdaptiveBanner(
        binding: BannerShimerBinding,
        adUnitId: String,
        bannerCallBack: BannerCallBack
    ) {
        binding.shimmer.startShimmer()
        if (BannerConstants.preloadedBanner != null) {
            Log.d(TAG, "Ad is already loaded, showing preloaded ad.")
            showPreloadedAd(binding, BannerType.ADAPTIVE, bannerCallBack)
            return
        }

        adView = AdView(activity)
        adView?.apply {
            this.adUnitId = adUnitId
            this.setAdSize(getBannerAdSize())
            val adRequest = AdRequest.Builder().build()
            loadAd(adRequest)
            adListener = createAdListener(false, binding, bannerCallBack)
        }
    }

    private fun loadSmallBanner(
        binding: BannerShimerBinding,
        adUnitId: String,
        bannerCallBack: BannerCallBack
    ) {
        binding.shimmer.startShimmer()
        if (BannerConstants.preloadedBanner != null) {
            showPreloadedAd(binding, BannerType.SMALL, bannerCallBack)
            return
        }

        adView = AdView(activity)
        adView?.apply {
            this.adUnitId = adUnitId
            this.setAdSize(AdSize.BANNER)
            val adRequest = AdRequest.Builder().build()
            loadAd(adRequest)
            adListener = createAdListener(false, binding, bannerCallBack)
        }
    }

    private fun loadCollapsibleBannerBottom(
        binding: BannerShimerBinding,
        adUnitId: String,
        bannerCallBack: BannerCallBack
    ) {
        binding.shimmer.startShimmer()
        if (BannerConstants.preloadedBanner != null) {
            showPreloadedAd(binding, BannerType.COLLAPSIBLE_BOTTOM, bannerCallBack)
            return
        }
        adView = AdView(activity)
        adView?.apply {
            this.adUnitId = adUnitId
            this.setAdSize(getBannerAdSize())
            loadAd(
                AdRequest.Builder().addNetworkExtrasBundle(
                    AdMobAdapter::class.java,
                    Bundle().apply {
                        putString("collapsible", "bottom")
                    }).build()
            )
            adListener = createAdListener(false, binding, bannerCallBack)
        }
    }

    private fun loadCollapsibleBannerTop(
        binding: BannerShimerBinding,
        adUnitId: String,
        bannerCallBack: BannerCallBack
    ) {
        binding.shimmer.startShimmer()
        if (BannerConstants.preloadedBanner != null) {
            showPreloadedAd(binding, BannerType.COLLAPSIBLE_TOP, bannerCallBack)
            return
        }
        adView = AdView(activity)
        adView?.apply {
            this.adUnitId = adUnitId
            this.setAdSize(getBannerAdSize())
            loadAd(
                AdRequest.Builder().addNetworkExtrasBundle(
                    AdMobAdapter::class.java,
                    Bundle().apply {
                        putString("collapsible", "top")
                    }).build()
            )
            adListener = createAdListener(false, binding, bannerCallBack)
        }
    }

    private fun loadMediumRectangleBanner(
        binding: BannerShimerBinding,
        adUnitId: String,
        bannerCallBack: BannerCallBack
    ) {
        binding.shimmer.startShimmer()
        binding.mediumBanner.visibility = View.INVISIBLE
        if (BannerConstants.preloadedBanner != null) {
            showPreloadedAd(binding, BannerType.MEDIUM_RECTANGLE, bannerCallBack)
            return
        }
        adView = AdView(activity)
        adView?.apply {
            this.adUnitId = adUnitId
            this.setAdSize(AdSize.MEDIUM_RECTANGLE)
            val adRequest = AdRequest.Builder().build()
            loadAd(adRequest)
            adListener = createAdListener(true, binding, bannerCallBack)
        }
    }

    private fun createAdListener(
        isMedium: Boolean,
        binding: BannerShimerBinding,
        bannerCallBack: BannerCallBack
    ): AdListener {
        return object : AdListener() {
            override fun onAdLoaded() {
                binding.shimmer.stopShimmer()
                binding.shimmer.isVisible = false
                if (isMedium) {
                    binding.mediumBanner.removeAllViews()
                    binding.mediumBanner.isVisible = true
                    binding.mediumBanner.addView(adView)
                    Log.d(TAG, "onAdLoaded: ")
                } else {
                    binding.banner.removeAllViews()
                    binding.banner.isVisible = true
                    binding.banner.addView(adView)
                }

                BannerConstants.preloadedBanner = adView
                adLoaded = true
                bannerCallBack.onAdLoaded()
                Log.d(TAG, "Banner Ad Loaded")
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                bannerCallBack.onAdFailedToLoad(error)
                adLoaded = false
                binding.shimmer.stopShimmer()
                binding.shimmer.isVisible = false
                Log.e(TAG, "Banner Ad Failed to Load: ${error.message}")
            }

            override fun onAdOpened() {
                bannerCallBack.onAdOpened()
                Log.d(TAG, "Banner Ad Clicked")
            }

            override fun onAdImpression() {
                BannerConstants.preloadedBanner = null
                bannerCallBack.onAdImpression()
                Log.d(TAG, "Banner Ad Impression Recorded")
            }

            override fun onAdClicked() {
                bannerCallBack.onAdClicked()
                Log.d(TAG, "Banner Ad Clicked")
            }

            override fun onAdClosed() {
                bannerCallBack.onAdClosed()
                Log.d(TAG, "Banner Ad Closed")
            }

            override fun onAdSwipeGestureClicked() {
                bannerCallBack.onAdSwipeGestureClicked()
                Log.d(TAG, "Ad Swipe Gesture Clicked")
            }
        }
    }

    private fun showPreloadedAd(
        binding: BannerShimerBinding,
        adType: BannerType,
        bannerCallBack: BannerCallBack
    ) {
        when (adType) {
            BannerType.ADAPTIVE, BannerType.SMALL -> {
                BannerConstants.preloadedBanner?.let { preloadedAdView ->
                    binding.shimmer.stopShimmer()
                    binding.shimmer.isVisible = false
                    val parent = preloadedAdView.parent as? ViewGroup
                    parent?.removeView(preloadedAdView)
                    binding.banner.isVisible = true
                    binding.banner.removeAllViews()
                    binding.banner.addView(preloadedAdView)
                    preloadedAdView.adListener = createAdListener(false, binding, bannerCallBack)
                }
            }
            BannerType.MEDIUM_RECTANGLE -> {
                BannerConstants.preloadedBanner?.let { preloadedAdView ->
                    binding.shimmer.stopShimmer()
                    binding.shimmer.isVisible = false
                    val parent = preloadedAdView.parent as? ViewGroup
                    parent?.removeView(preloadedAdView)
                    binding.mediumBanner.isVisible = true
                    binding.mediumBanner.removeAllViews()
                    binding.mediumBanner.addView(preloadedAdView)
                    preloadedAdView.adListener = createAdListener(true, binding, bannerCallBack)
                }
            }
            BannerType.COLLAPSIBLE_BOTTOM, BannerType.COLLAPSIBLE_TOP -> {
                BannerConstants.preloadedBanner?.let { preloadedAdView ->
                    binding.shimmer.stopShimmer()
                    binding.shimmer.isVisible = false
                    val parent = preloadedAdView.parent as? ViewGroup
                    parent?.removeView(preloadedAdView)
                    binding.banner.isVisible = true
                    binding.banner.removeAllViews()
                    binding.banner.addView(preloadedAdView)
                    preloadedAdView.adListener = createAdListener(false, binding, bannerCallBack)
                }
            }
        }
    }

    private fun getBannerAdSize(): AdSize {
        val displayMetrics = activity.resources.displayMetrics
        val adWidthPixels = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = activity.windowManager.currentWindowMetrics
            windowMetrics.bounds.width()
        } else {
            displayMetrics.widthPixels
        }
        val adWidth = (adWidthPixels / displayMetrics.density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }
}
