package com.codetech.ads.ads.nativeads

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.codetech.admobads.ads.nativeads.NativeCallBack
import com.codetech.ads.R
import com.codetech.ads.ads.AdsConstants.adMobPreloadNativeAd
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AdmobNativeAds {

    private val AD_TAG = "AdmobNativeAdsInfo"
    private var adMobNativeAd: NativeAd? = null
    private var isLoading = false
    fun preLoadNativeAd(
        activity: Activity?,
        isInternetConnected: Boolean,
        isFirstTime:Boolean=false,
        adUnit: String
    ) {
        activity?.let { mActivity ->
            try {
                if (isInternetConnected) {
                    val id= adUnit
                    Log.d(AD_TAG, "preLoadNativeAd: $id")
                    // reuse of preloaded native ad
                    // if miss first native then use it next
                    if (adMobPreloadNativeAd != null) {
                        adMobNativeAd = adMobPreloadNativeAd
                        adMobPreloadNativeAd = null
                        Log.d(AD_TAG, "admob native onAdLoaded")
                        return
                    }
                    if (adMobNativeAd == null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val builder: AdLoader.Builder = AdLoader.Builder(
                                mActivity,
                                id
                            )
                            val adLoader =
                                builder.forNativeAd { unifiedNativeAd: NativeAd? ->
                                    if (!mActivity.isDestroyed && !mActivity.isFinishing) {
                                        adMobNativeAd = unifiedNativeAd
                                    } else {
                                        unifiedNativeAd?.destroy()
                                        return@forNativeAd
                                    }
                                }
                                    .withAdListener(object : AdListener() {

                                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                            Log.e(
                                                AD_TAG,
                                                "admob native onAdFailedToLoad: " + loadAdError.message
                                            )
                                            adMobNativeAd = null
                                            super.onAdFailedToLoad(loadAdError)
                                        }

                                        override fun onAdLoaded() {
                                            super.onAdLoaded()
                                            Log.d(AD_TAG, "admob native onAdLoaded pre fun")
                                        }

                                    }).withNativeAdOptions(
                                        com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                                            .setAdChoicesPlacement(
                                                com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT
                                            ).build()
                                    )
                                    .build()
                            adLoader.loadAd(AdRequest.Builder().build())

                        }
                    } else {
                        Log.e(AD_TAG, "Native is already loaded")
                    }
                }

            } catch (ignore: Exception) {

            }
        }
    }

    fun loadNativeAds(
        activity: Activity?,
        adsPlaceHolder: FrameLayout,
        adUnit: String,
        adEnable: Int,
        isAppPurchased: Boolean,
        nativeType: NativeType,
        isInternetConnected: Boolean,
        callBack: NativeCallBack
    ) {
        val handlerException = CoroutineExceptionHandler { _, throwable ->
            Log.e("adStatus", "${throwable.message}")
        }
        activity?.let { mActivity ->
            try {
                if (isInternetConnected && adEnable != 0 && !isAppPurchased && adUnit.isNotEmpty()) {
                    adsPlaceHolder.visibility = View.VISIBLE

                    // reuse of preloaded native ad
                    // if miss first native then use it next
                    if (adMobPreloadNativeAd != null) {
                        adMobNativeAd = adMobPreloadNativeAd
                        adMobPreloadNativeAd = null
                        Log.d(AD_TAG, "adMobPreloadNativeAd")
                        callBack.onPreloaded()
                        displayNativeAd(mActivity, adsPlaceHolder,nativeType)
                        return
                    }
                    if (adMobNativeAd == null) {
                        CoroutineScope(Dispatchers.IO + handlerException).launch {
                            val builder: AdLoader.Builder =
                                AdLoader.Builder(mActivity, adUnit)
                            val adLoader =
                                builder.forNativeAd { unifiedNativeAd: NativeAd? ->
                                    if (!mActivity.isDestroyed && !mActivity.isFinishing) {
                                        adMobNativeAd = unifiedNativeAd
                                        adMobPreloadNativeAd = unifiedNativeAd
                                    } else {
                                        unifiedNativeAd?.destroy()
                                        return@forNativeAd
                                    }
                                }
                                    .withAdListener(object : AdListener() {
                                        override fun onAdImpression() {
                                            super.onAdImpression()
                                            Log.d(AD_TAG, "admob native onAdImpression")
                                            callBack.onAdImpression()
                                            adMobNativeAd = null
                                            adMobPreloadNativeAd = null
                                        }

                                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                            Log.e(
                                                AD_TAG,
                                                "admob native onAdFailedToLoad: " + loadAdError.message
                                            )
                                            callBack.onAdFailedToLoad(loadAdError)
                                            adsPlaceHolder.visibility = View.GONE
                                            adMobNativeAd = null
                                            super.onAdFailedToLoad(loadAdError)
                                        }

                                        override fun onAdLoaded() {
                                            super.onAdLoaded()
                                            Log.d(AD_TAG, "admob native onAdLoaded")
                                            callBack.onAdLoaded()
                                            displayNativeAd(mActivity, adsPlaceHolder, nativeType)

                                        }

                                    }).withNativeAdOptions(
                                        com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                                            .setAdChoicesPlacement(
                                                com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT
                                            ).build()
                                    )
                                    .build()
                            adLoader.loadAd(AdRequest.Builder().build())

                        }
                    } else {
                        Log.e(AD_TAG, "Native is already loaded")
                        callBack.onPreloaded()
                        displayNativeAd(mActivity, adsPlaceHolder, nativeType)
                    }

                } else {
                    adsPlaceHolder.visibility = View.GONE
                    Log.e(
                        AD_TAG,
                        "adEnable = $adEnable, isAppPurchased = $isAppPurchased, isInternetConnected = $isInternetConnected"
                    )
                }

            } catch (ex: Exception) {
                adsPlaceHolder.visibility = View.GONE
                Log.e(AD_TAG, "${ex.message}")

            }
        }
    }

    @SuppressLint("InflateParams")
    private fun displayNativeAd(
        activity: Activity?,
        adMobNativeContainer: FrameLayout,
        nativeType: NativeType,
    ) {
        activity?.let { mActivity ->
            try {
                adMobNativeAd?.let { ad ->
                    val inflater = LayoutInflater.from(mActivity)

                    val adView: NativeAdView = when (nativeType) {
                        NativeType.BANNER -> inflater.inflate(
                            R.layout.admob_native_banner,
                            null
                        ) as NativeAdView

                        NativeType.MEDIUM -> inflater.inflate(
                            R.layout.admob_native_medium,
                            null
                        ) as NativeAdView

                        NativeType.SMALL -> inflater.inflate(
                            R.layout.admob_native_small,
                            null
                        ) as NativeAdView

                        NativeType.SMALL_ADJUSTED -> inflater.inflate(
                            R.layout.admob_native_small_adjusted,
                            null
                        ) as NativeAdView

                        NativeType.LARGE -> inflater.inflate(
                            R.layout.admob_native_large,
                            null
                        ) as NativeAdView

                    }
                    if (nativeType == NativeType.LARGE) {
                        val mediaView: MediaView = adView.findViewById(R.id.media_view)
                        adView.mediaView = mediaView
                        adView.starRatingView = adView.findViewById(R.id.ad_stars)
                        if (ad.starRating != null) {
                            (adView.starRatingView as RatingBar).rating = ad.starRating!!.toFloat()
                            adView.starRatingView?.visibility = View.VISIBLE
                        } else {
                            adView.starRatingView?.visibility = View.INVISIBLE
                        }
                    }
                    val viewGroup: ViewGroup? = adView.parent as ViewGroup?
                    viewGroup?.removeView(adView)

                    adMobNativeContainer.removeAllViews()
                    adMobNativeContainer.addView(adView)


                    if (nativeType == NativeType.MEDIUM) {
                        val mediaView: MediaView = adView.findViewById(R.id.media_view)
                        adView.mediaView = mediaView
                    }

                    // Set other ad assets.
                    adView.headlineView = adView.findViewById(R.id.ad_headline)
                    adView.bodyView = adView.findViewById(R.id.ad_body)
                    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
                    adView.iconView = adView.findViewById(R.id.ad_app_icon)
                    val iconCard = adView.findViewById<CardView>(R.id.adIconCard)
                    val shimmer = adView.findViewById<ShimmerFrameLayout>(R.id.shimmer)
                    val constraintLayout = adView.findViewById<ConstraintLayout>(R.id.data_panel)

                    //Headline
                    adView.headlineView?.let { headline ->
                        (headline as TextView).text = ad.headline
                        headline.isSelected = true
                    }

                    //Body
                    adView.bodyView?.let { bodyView ->
                        if (ad.body == null) {
                            bodyView.visibility = View.INVISIBLE
                        } else {
                            bodyView.visibility = View.VISIBLE
                            (bodyView as TextView).text = ad.body
                        }

                    }

                    //Call to Action
                    adView.callToActionView?.let { ctaView ->
                        if (ad.callToAction == null) {
                            ctaView.visibility = View.INVISIBLE
                        } else {
                            ctaView.visibility = View.VISIBLE
                            (ctaView as TextView).text = ad.callToAction
                        }

                    }

                    //Icon
                    adView.iconView?.let { iconView ->
                        if (ad.icon == null) {
                            iconView.visibility = View.GONE
                            iconCard.visibility = View.GONE
                        } else {
                            (iconView as ImageView).setImageDrawable(ad.icon?.drawable)
                            iconView.visibility = View.VISIBLE
                            iconCard.visibility = View.VISIBLE
                        }

                    }

                    adView.advertiserView?.let { adverView ->

                        if (ad.advertiser == null) {
                            adverView.visibility = View.GONE
                        } else {
                            (adverView as TextView).text = ad.advertiser
                            adverView.visibility = View.GONE
                        }
                    }
                    shimmer.stopShimmer()
                    shimmer.visibility=View.GONE
                    constraintLayout.visibility=View.VISIBLE

                    adView.setNativeAd(ad)
                    adMobNativeAd = null
                }
            } catch (ex: Exception) {
                Log.e(AD_TAG, "displayNativeAd: ${ex.message}")
            }
        }
    }

}