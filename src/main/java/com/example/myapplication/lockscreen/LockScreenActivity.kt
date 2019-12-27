package com.example.myapplication.lockscreen

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.lockscreen.util.ButtonUnLock
import com.example.myapplication.lockscreen.util.ViewUnLock
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.igaworks.adpopcorn.IgawAdpopcorn
import com.igaworks.adpopcorn.IgawAdpopcornExtension
import com.kakao.adfit.ads.ba.BannerAdView
import kotlinx.android.synthetic.main.activty_lockscreen.*
import java.util.concurrent.locks.Lock
import kotlin.concurrent.timer
import com.example.myapplication.lockscreen.LockScreenActivity as LockScreenActivity1


const val GAME_LENGTH_MILLISECONDS = 3000L
const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
class LockScreenActivity : AppCompatActivity() {

    // Google AddMob
    private lateinit var mInterstitialAd: InterstitialAd
    private var mCountDownTimer: CountDownTimer? = null
    private var mGameIsInProgress = false
    private var mTimerMilliseconds = 0L
    private lateinit var rewardedAd:RewardedAd
    private lateinit var adView: BannerAdView

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasFocus) window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Activity FullScreen
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        setContentView(R.layout.activty_lockscreen)
        // Kick off the first play of the "game."



        //Google AddMob
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this) {}

        // Create the InterstitialAd and set it up.
        mInterstitialAd = InterstitialAd(this).apply {
            adUnitId = AD_UNIT_ID
            adListener = (object : AdListener() {
                override fun onAdLoaded() {
                    Toast.makeText(this@LockScreenActivity, "onAdLoaded()", Toast.LENGTH_SHORT).show()
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    Toast.makeText(this@LockScreenActivity,
                            "onAdFailedToLoad() with error code: $errorCode",
                            Toast.LENGTH_SHORT).show()
                }

                override fun onAdClosed() {
                }
            })
        }
        rewardedAd = RewardedAd(this, "ca-app-pub-3940256099942544/5224354917")
        val adLoadCallback = object: RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                // Ad successfully loaded.
            }
            override fun onRewardedAdFailedToLoad(errorCode: Int) {
                // Ad failed to load.
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)

        // Create the "retry" button, which triggers an interstitial between game plays.
        retry_button.visibility = View.INVISIBLE
        //retry_button.setOnClickListener { showRewardedAd() }
        retry_button.setOnClickListener {
                //오퍼월을 노출합니다.
                IgawAdpopcorn.openOfferWall(this.application);
        }

        // Kick off the first play of the "game."
        startGame()


        // Kakao Adkit
        /*adView.setClientId("DAN-urund4kq3kmt")
        adView.setAdListener(object : com.kakao.adfit.ads.AdListener {  // optional :: 광고 수신 리스너 설정

            override fun onAdLoaded() {
                // 배너 광고 노출 완료 시 호출
            }

            override fun onAdFailed(errorCode: Int) {
                // 배너 광고 노출 실패 시 호출
            }

            override fun onAdClicked() {
                // 배너 광고 클릭 시 호출
            }

        })
        // activity 또는 fragment의 lifecycle에 따라 호출
        lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                adView.resume()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                adView.pause()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                adView.destroy()
            }
        })

        adView.loadAd()*/
    }
    //////
    // Create the game timer, which counts down to the end of the level
    // and shows the "retry" button.
    private fun createTimer(milliseconds: Long) {
        mCountDownTimer?.cancel()

        mCountDownTimer = object : CountDownTimer(milliseconds, 50) {
            override fun onTick(millisUntilFinished: Long) {
                mTimerMilliseconds = millisUntilFinished
                //timer.text = "seconds remaining: ${ millisUntilFinished / 1000 + 1 }"
            }

            override fun onFinish() {
                mGameIsInProgress = false
                //timer.text = "done!"
                retry_button.visibility = View.VISIBLE
            }
        }
    }

    private fun showRewardedAd() {
        if (rewardedAd.isLoaded) {
            val activityContext: Activity = this
            val adCallback = object: RewardedAdCallback() {
                override fun onRewardedAdOpened() {
                    // Ad opened.
                }
                override fun onRewardedAdClosed() {
                    // Ad closed.
                }
                override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                    // User earned reward.
                }
                override fun onRewardedAdFailedToShow(errorCode: Int) {
                    // Ad failed to display.
                }
            }
            rewardedAd.show(activityContext, adCallback)
        }
        else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.")
        }
    }
    // Show the ad if it's ready. Otherwise toast and restart the game.
    private fun showInterstitial() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            Toast.makeText(this, "Ad wasn't loaded.", Toast.LENGTH_SHORT).show()
            startGame()
        }
    }

    // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
    private fun startGame() {
        if (!mInterstitialAd.isLoading && !mInterstitialAd.isLoaded) {
            // Create an ad request. If you're running this on a physical device, check your logcat
            // to learn how to enable test ads for it. Look for a line like this one:
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            val adRequest = AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build()

            mInterstitialAd.loadAd(adRequest)
        }

        retry_button.visibility = View.INVISIBLE
        resumeGame(GAME_LENGTH_MILLISECONDS)
    }

    private fun resumeGame(milliseconds: Long) {
        // Create a new timer for the correct length and start it.
        mGameIsInProgress = true
        mTimerMilliseconds = milliseconds
        createTimer(milliseconds)
        mCountDownTimer?.start()
    }

    // Resume the game if it's in progress.
    public override fun onResume() {
        super.onResume()

        if (mGameIsInProgress) {
            resumeGame(mTimerMilliseconds)
        }

        setButtonUnlock()
    }

    // Cancel the timer if the game is paused.
    public override fun onPause() {
        mCountDownTimer?.cancel()
        super.onPause()
    }
    /////////
    /*override fun onResume() {
        super.onResume()

        setButtonUnlock()
    }*/

    private fun setButtonUnlock() {
        swipeUnLockButton.setOnUnlockListenerRight(object : ButtonUnLock.OnUnlockListener {
            override fun onUnlock() {
                finish()
            }
        })
    }


    private fun setViewUnlock() {
        lockScreenView.x = 0f
        lockScreenView.setOnTouchListener(object : ViewUnLock(this, lockScreenView) {
            override fun onFinish() {
                finish()
                super.onFinish()
            }
        })
    }

    override fun onBackPressed() {

    }
}