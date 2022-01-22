package com.jingtian.mtdemo.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.jingtian.mtdemo.BuildConfig
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.base.view.BaseActivity
import com.jingtian.mtdemo.databinding.*
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
        if (BuildConfig.DEBUG) {
            Log.d("start main activity", "start main activity")
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private var timer: Timer? = null
    fun setSplashDelay(d: Int) {
        splashDelay = d
    }

    class SplashTimerTask(val handler: Handler) :
        TimerTask() {
        override fun run() {
            handler.sendEmptyMessage(FROM_SPLASH_TIMING)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hideSystemBars()
        setBackGround()
        //倒计时
        binding.tvSpSkip.setOnClickListener {
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

    companion object {
        const val FROM_SPLASH = 0
        const val FROM_SPLASH_TIMING = 1
    }
    var accelerate = 0
    private val splashHandler = Handler(Looper.myLooper()!!) {
        if (it.what == FROM_SPLASH) {
            if (data >= 10800) {
                splashTimer?.cancel()
            } else {
                binding.clSplash.background.level = data
                data += 200
                setData(data)
            }
        } else if (it.what == FROM_SPLASH_TIMING) {
            val tvDelay = binding.spDelay
            if (BuildConfig.DEBUG) {
                Log.d("delay", "${splashDelay}s")
            }
            tvDelay.text = resources.getString(R.string.tvDelay, splashDelay)
            if (splashDelay == 0) {
                startMainActivity()
                timer?.cancel()
            }
            splashDelay--
            setSplashDelay(splashDelay)
        }
        return@Handler true
    }
    class SplashTask(
        private val splashHandler:Handler
    ) : TimerTask() {
        override fun run() {
            splashHandler.sendEmptyMessage(FROM_SPLASH)

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
        splashTimer?.schedule(SplashTask(splashHandler), 0, splashPeriod)

    }

    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun getPresenter(): BaseInterface.Presenter {
        return BasePresenter<SplashActivity>()
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        splashTimer?.cancel()
        timer = null
        splashTimer = null
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        splashTimer = Timer()
        timer?.scheduleAtFixedRate(SplashTimerTask(splashHandler), 0, 1000)
        startSplash()
    }

    private lateinit var binding: ActivitySplashBinding
    override fun viewBinding(): View {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        return binding.root
    }

}