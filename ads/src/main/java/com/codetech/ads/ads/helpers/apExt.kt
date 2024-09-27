package com.codetech.ads.ads.helpers

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.codetech.ads.R
import com.codetech.ads.ads.AdsConstants
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

private var adDialog: AlertDialog? = null
fun Context.showAdDialog(loadingMessage: String = "", isShouldCancel: Boolean = true) {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.alterads_loading, null)
    val tv = dialogView.findViewById<TextView>(R.id.tv_ad_load)
    val builder = AlertDialog.Builder(this)
        .setView(dialogView)
        .setCancelable(isShouldCancel)
    adDialog = builder.create()
    if (loadingMessage != "") {
        tv.text = loadingMessage
    }
    adDialog?.window?.decorView?.setBackgroundResource(android.R.color.transparent)
    adDialog?.show()
}

fun Context.dismissAdDialog() {
    adDialog?.let {
        if (it.isShowing) {
            it.dismiss()
        }
    }
    adDialog = null
}


// Setters for showing state
fun Context.setAppOpenAdShowing(showOpenAd: Boolean) {
    AdsConstants.isAppOpenAdShowing = showOpenAd
}

fun Context.setInterstitialAdShowing(showInterstitialAd: Boolean) {
    AdsConstants.isInterstitialShowing = showInterstitialAd
}

// Extension properties for getting state
val Context.isAppOpenAdShowing: Boolean
    get() =  AdsConstants.isAppOpenAdShowing

val Context.isInterstitialAdShowing: Boolean
    get() =  AdsConstants.isInterstitialShowing





val Int.sdp: Dp
    @Composable
    get() = this.sdpGetSize()


val Int.ssp: TextUnit
    @Composable get() = this.sspGetTextUnit()


@Composable
fun Int.sdpGetSize(): Dp {
    val minValue = kotlin.math.min(getScreenHeight(), getScreenWidth())
    val ratio = minValue / 300.0
    return (this * ratio).dp
}

@Composable
fun Int.sspGetTextUnit(): TextUnit {
    return this.textSdpSize(density = LocalDensity.current)
}

@Composable
private fun Int.textSdpSize(density: Density): TextUnit = with(density) {
    this@textSdpSize.sdp.toSp()
}

@Composable
private fun getScreenHeight(): Int {
    val configuration = LocalConfiguration.current
    return configuration.screenHeightDp
}

@Composable
private fun getScreenWidth(): Int {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp
}


