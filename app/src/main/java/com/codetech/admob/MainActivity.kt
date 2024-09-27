package com.codetech.admob

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.codetech.admob.ui.theme.AdmobTheme
import com.codetech.ads.ads.banner.BannerType
import com.codetech.ads.ads.interstitial.InterstitialAds
import com.codetech.ads.ads.sample.ShowBannerAd
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        enableEdgeToEdge()
        setContent {
            AdmobTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val context = LocalContext.current as Activity
    val selectedItem = remember { mutableStateOf(BannerType.ADAPTIVE) }

    Column(modifier = modifier.padding(top = 20.dp)) {
        Button(
            onClick = {
                context.startActivity(Intent(context, NativeAdsActivity::class.java))
            },
            modifier = Modifier.padding(16.dp) // Optional: Add a modifier for styling
        ) {
            Text(text = "View Native Ads")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                InterstitialAds(context).loadAndShow(
                    adUnit = context.getString(R.string.interstitial_id),
                    isAppPurchased = false,
                    remoteConfig = true,
                    isInternetConnected = true,
                    onAdDismiss = {
                        // Handle ad dismiss here
                    }
                )
            },
            modifier = Modifier.padding(16.dp) // Optional: Add a modifier for styling
        ) {
            Text(text = "Show Interstitial Ad")
        }

        Spacer(modifier = Modifier.height(10.dp))



        Spacer(modifier = Modifier.height(10.dp))
        ShowBannerAd(
            adUnit = stringResource(id = R.string.banner_id),
            bannerType = selectedItem.value
        )
    }
}

