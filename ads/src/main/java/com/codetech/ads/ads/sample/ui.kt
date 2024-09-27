package com.codetech.ads.ads.sample

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.codetech.admobads.ads.nativeads.NativeCallBack
import com.codetech.ads.R
import com.codetech.ads.ads.banner.BannerAds
import com.codetech.ads.ads.banner.BannerCallBack
import com.codetech.ads.ads.banner.BannerType
import com.codetech.ads.ads.nativeads.AdmobNativeAds
import com.codetech.ads.ads.nativeads.NativeType
import com.codetech.ads.ads.ui.theme.getBannerHeight
import com.codetech.ads.ads.ui.theme.getNativeHeight
import com.codetech.ads.ads.ui.theme.shimmerBrush
import com.codetech.ads.databinding.BannerShimerBinding
import com.google.android.gms.ads.AdError

@Composable
fun ShowBannerAd(adUnit: String, bannerType: BannerType) {
    val activity = LocalContext.current as Activity
    val showShimmer = remember { mutableStateOf(true) }
    val bannerAds = BannerAds(activity)

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(getBannerHeight(bannerType = bannerType))
            .background(
                shimmerBrush(
                    targetValue = 1300f,
                    showShimmer = showShimmer.value
                )
            ),
        factory = { context ->
            val binding = BannerShimerBinding.inflate(LayoutInflater.from(context))
            bannerAds.showBannerAd(
                binding = binding,
                adUnitId = adUnit,
                adType = bannerType,
                isInternetConnect = true,
                remoteConfig = true,
                bannerCallBack = object : BannerCallBack {
                    override fun onAdFailedToLoad(adError: AdError) {
                        showShimmer.value = false
                        Log.e("BannerAd", "Ad Failed to Load: ${adError.message}")
                    }

                    override fun onAdLoaded() {
                        showShimmer.value = false
                        Log.d("BannerAd", "Ad Loaded Successfully")
                    }

                    override fun onAdOpened() {}
                    override fun onAdImpression() {}
                    override fun onAdClosed() {}
                    override fun onAdClicked() {}
                    override fun onAdSwipeGestureClicked() {}
                }
            )
            binding.root
        },
        update = {}
    )
}


@Composable
fun ShowNativeAd(adUnit: String, nativeType: NativeType) {
    val activity = LocalContext.current as Activity
    val showShimmer = remember { mutableStateOf(true) }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(getNativeHeight(nativeType = nativeType))
            .background(
                shimmerBrush(
                    targetValue = 1300f,
                    showShimmer = showShimmer.value
                )
            ), // Set background to shimmer
        factory = { context ->
            val container = LayoutInflater.from(context)
                .inflate(R.layout.native_container, null, false) as FrameLayout

            // Load the native ads into the inflated view
            AdmobNativeAds.loadNativeAds(
                activity = activity,
                isInternetConnected = true,
                adsPlaceHolder = container,
                adUnit = adUnit,
                adEnable = 1,
                isAppPurchased = false,
                nativeType = nativeType,
                callBack = object : NativeCallBack {
                    override fun onAdFailedToLoad(adError: AdError) {
                        // Stop and hide shimmer on ad load failure
                        showShimmer.value = false
                    }

                    override fun onAdLoaded() {
                        // Stop shimmer and update UI with ad
                        showShimmer.value = false

                        // Update the NativeAdView here (e.g., populate views with ad data)
                    }

                    override fun onAdImpression() {
                        // Handle ad impression
                    }

                    override fun onPreloaded() {
                        // Handle preloaded ad
                    }

                    override fun onAdClicked() {
                        // Handle ad click
                    }
                }
            )

            container // Return the inflated view
        }
    )

}



