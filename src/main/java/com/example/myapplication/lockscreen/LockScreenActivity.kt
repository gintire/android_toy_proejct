package com.example.myapplication.lockscreen

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.lockscreen.util.ButtonUnLock
import com.example.myapplication.lockscreen.util.ViewUnLock
import kotlinx.android.synthetic.main.activty_lockscreen.*

class LockScreenActivity : AppCompatActivity() {
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
    }

    override fun onResume() {
        super.onResume()

        setButtonUnlock()
    }

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