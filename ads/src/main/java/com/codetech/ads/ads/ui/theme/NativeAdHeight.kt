package com.codetech.ads.ads.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.codetech.ads.ads.banner.BannerType
import com.codetech.ads.ads.helpers.sdp
import com.codetech.ads.ads.nativeads.NativeType

@Composable
fun getNativeHeight(nativeType: NativeType): Dp {
    return when (nativeType) {
        NativeType.BANNER -> 50.sdp // Height for BANNER
        NativeType.SMALL -> 72.sdp // Height for SMALL
        NativeType.SMALL_ADJUSTED -> 90.sdp // Height for SMALL
        NativeType.MEDIUM -> 160.sdp // Height for MEDIUM
        NativeType.LARGE -> 200.sdp // Height for LARGE
    }
}


@Composable
fun getBannerHeight(bannerType: BannerType): Dp {
    return when (bannerType) {
        BannerType.SMALL -> 50.sdp // Height for BANNER
        BannerType.ADAPTIVE -> 50.sdp // Height for SMALL
        BannerType.MEDIUM_RECTANGLE -> 160.sdp // Height for MEDIUM
        BannerType.COLLAPSIBLE_BOTTOM -> 50.sdp // Height for LARGE
        BannerType.COLLAPSIBLE_TOP ->50.sdp // Height for LARGE
    }
}