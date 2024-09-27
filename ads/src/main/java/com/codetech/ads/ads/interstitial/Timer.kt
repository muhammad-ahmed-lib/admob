package com.codetech.ads.ads.interstitial

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object TimerManager {
    private var counter = 0
    private val _timer = MutableLiveData(counter)
    val observeTimer: LiveData<Int>
        get() = _timer
    private val handler = Handler(Looper.getMainLooper())

    // Runnable task to be repeated every InterstitialConstants.adTimer milliseconds
    private val adTimerRunnable = Runnable {
        InterstitialConstants.isShouldShowAds = true
    }

    // Runnable task to be repeated every second
    private val timerObserveRunnable = object : Runnable {
        override fun run() {
            counter++
            _timer.postValue(counter)
            // Reschedule itself to run again after 1 second (1000 milliseconds)
            handler.postDelayed(this, 1000)
        }
    }

    private fun startTimer() {
        // Reset counter when starting the timer
        counter = 0
        _timer.postValue(counter)

        // Start both runnables
        handler.postDelayed(adTimerRunnable, InterstitialConstants.adTimer)
        handler.postDelayed(timerObserveRunnable, 1000)
    }

    fun stopTimer() {
        // Stop the runnables and reset the state
        handler.removeCallbacks(adTimerRunnable)
        handler.removeCallbacks(timerObserveRunnable)
        InterstitialConstants.isShouldShowAds = false
        counter = 0
        _timer.postValue(counter)
    }

    fun onAdDismissed() {
        // Stop showing ads and restart the timer
        InterstitialConstants.isShouldShowAds = false
        startTimer()
    }
}
