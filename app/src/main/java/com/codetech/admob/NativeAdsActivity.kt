package com.codetech.admob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.codetech.admob.ui.theme.AdmobTheme
import com.codetech.ads.ads.banner.BannerType
import com.codetech.ads.ads.helpers.sdp
import com.codetech.ads.ads.nativeads.NativeType
import com.codetech.ads.ads.sample.ShowBannerAd
import com.codetech.ads.ads.sample.ShowNativeAd

class NativeAdsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdmobTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NativeUi(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun NativeUi(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    // Apply the verticalScroll modifier to make the Column scrollable
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .verticalScroll(scrollState) // Make the Column scrollable
    ) {

        ShowNativeAd(
            adUnit = stringResource(id = R.string.native_ad_id),
            nativeType = NativeType.BANNER
        )

        Spacer(modifier = Modifier.heightIn(5.sdp))
        ShowNativeAd(
            adUnit = stringResource(id = R.string.native_ad_id),
            nativeType = NativeType.SMALL
        )
        Spacer(modifier = Modifier.heightIn(5.sdp))
        ShowNativeAd(
            adUnit = stringResource(id = R.string.native_ad_id),
            nativeType = NativeType.SMALL_ADJUSTED
        )
        Spacer(modifier = Modifier.heightIn(5.sdp))
        ShowNativeAd(
            adUnit = stringResource(id = R.string.native_ad_id),
            nativeType = NativeType.MEDIUM
        )

        Spacer(modifier = Modifier.heightIn(10.sdp))

        ShowNativeAd(
            adUnit = stringResource(id = R.string.native_ad_id),
            nativeType = NativeType.LARGE
        )

    }
}
