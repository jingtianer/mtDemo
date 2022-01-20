package com.jingtian.mtdemo.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.base.view.BaseActivity
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<BaseInterface.Presenter>() {

    private var splashDelay = 4
    fun hideSystemBars() {
        //已经废弃
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.statusBarColor = Color.TRANSPARENT
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
        }
    }

    fun showSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            window.decorView
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    private fun setBackGround() {
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        //window.setBackgroundDrawableResource(R.drawable.splash_launcher)
    }

    fun startMainActivity() {
        Log.d("start main activity", "start main activity")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private var timer: Timer? = null
    fun setSplashDelay(d: Int) {
        splashDelay = d
    }

    class SplashTimerTask(private val activity: SplashActivity, private var splashDelay: Int) :
        TimerTask() {
        override fun run() {
            activity.runOnUiThread {
                val tvDelay = activity.findViewById<TextView>(R.id.sp_delay)
                Log.d("delay", "${splashDelay}s")
                tvDelay.text = activity.resources.getString(R.string.tvDelay, splashDelay)
                if (splashDelay == 0) {
                    activity.startMainActivity()
                    this.cancel()
                }
                splashDelay--
                activity.setSplashDelay(splashDelay)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hideSystemBars()
        setBackGround()
        //倒计时
        findViewById<TextView>(R.id.tv_sp_skip).setOnClickListener {
            startMainActivity()
            timer?.cancel()
        }
        //点击跳过按钮
        noBars()
    }


    private var data = 0
    fun setData(d: Int) {
        data = d
    }

    class SplashTask(
        private val activity: SplashActivity,
        private val timer: Timer,
        private var data: Int
    ) : TimerTask() {
        override fun run() {
            val clSplash = activity.findViewById<ConstraintLayout>(R.id.cl_splash)
            val clipDrawable = clSplash.background
            if (data >= 10800) {
                timer.cancel()
            } else {
                activity.runOnUiThread {
                    clipDrawable.level = data
                    data += 200
                    activity.setData(data)
                }
            }

        }
    }

    private var splashTimer: Timer? = null
    private val splashPeriod = (1000 / 50).toLong()
    private fun startSplash() {
        val clSplash = findViewById<ConstraintLayout>(R.id.cl_splash)
        clSplash.setBackgroundResource(R.drawable.vertical_unfold)
//        val handler = Handler.Callback {
//            if (it.what == what) {
//                clipDrable.setLevel(data)
//                data += 200
//            }
//            true
//        }
        //handler不会用
        splashTimer?.schedule(SplashTask(this, splashTimer!!, data), 0, splashPeriod)

    }

    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun getPresenter(): BaseInterface.Presenter {
        return BasePresenter<SplashActivity>()
    }

    override fun onPause() {
        timer?.cancel()
        splashTimer?.cancel()
        timer = null
        splashTimer = null
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        splashTimer = Timer()
        timer?.scheduleAtFixedRate(SplashTimerTask(this, splashDelay), 0, 1000)
        startSplash()
    }
}