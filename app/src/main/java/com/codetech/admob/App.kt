package com.codetech.admob

import android.app.Application
import com.codetech.ads.ads.AppOpenResumeAd

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        AppOpenResumeAd(this,getString(R.string.app_open_id))
    }
}