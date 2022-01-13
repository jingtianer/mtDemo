package com.jingtian.mtdemo.UI.View

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.jingtian.mtdemo.Base.Interface.BaseInterface
import com.jingtian.mtdemo.Base.Presenter.BasePresenter
import com.jingtian.mtdemo.Base.View.BaseActivity
import com.jingtian.mtdemo.BuildConfig.DEBUG
import com.jingtian.mtdemo.R
import org.xmlpull.v1.XmlPullParser
import java.lang.Exception
import java.util.*

class SplashActivity:BaseActivity<BaseInterface.presenter>(){

    var splash_delay= 4
    fun hideSystemBars() {
        //已经废弃
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            //it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
        }
    }
    fun showSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let {
            it.show(WindowInsetsCompat.Type.systemBars())
        //点击屏幕显示bars
        //it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
        }
    }
    fun setBackGround() {
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        //window.setBackgroundDrawableResource(R.drawable.splash_launcher)
    }
    fun startMainActivity() {
        Log.d("start main activity","start main activity")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    var timer:Timer? = null
    fun set_splash_delay(d:Int) {
        splash_delay = d
    }
    class timer_task(val activity: SplashActivity, var splash_delay:Int):TimerTask() {
        override fun run() {
            activity.runOnUiThread {
                val sp_delay = activity.findViewById<TextView>(R.id.sp_delay)
                Log.d("delay","${splash_delay}s")
                sp_delay.text = "${splash_delay}s"
                if (splash_delay == 0) {
                    activity.startMainActivity()
                    this.cancel()
                }
                splash_delay--
                activity.set_splash_delay(splash_delay)
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemBars()
        setBackGround()
        //倒计时
        findViewById<TextView>(R.id.tv_sp_skip).setOnClickListener {
            startMainActivity()
            timer?.cancel()
        }
        //点击跳过按钮

    }


    var data = 0
    fun set_data(d:Int) {
        data = d
    }
    class splash_task(val activity: SplashActivity, val timer:Timer, var data:Int): TimerTask() {
        override fun run() {
            val cl_splash = activity.findViewById<ConstraintLayout>(R.id.cl_splash)
            val clipDrable = cl_splash.background
            if (data >= 10800) {
                timer.cancel()
            }else {
                clipDrable.setLevel(data)
                data += 200
                activity.set_data(data)
            }

        }
    }
    var splash_timer:Timer? = null
    val splash_period = (1000/50).toLong()
    fun start_splash() {
        val cl_splash = findViewById<ConstraintLayout>(R.id.cl_splash)
        cl_splash.setBackgroundResource(R.drawable.vertical_unfold)
//        val handler = Handler.Callback {
//            if (it.what == what) {
//                clipDrable.setLevel(data)
//                data += 200
//            }
//            true
//        }
        //handler不会用
        splash_timer?.schedule(splash_task(this, splash_timer!!, data),0,splash_period)

    }
    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun getPresenter(): BaseInterface.presenter {
        return BasePresenter<SplashActivity>()
    }

    override fun onPause() {
        timer?.cancel()
        splash_timer?.cancel()
        timer = null
        splash_timer = null
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        splash_timer = Timer()
        timer?.scheduleAtFixedRate(timer_task(this, splash_delay), 0, 1000)
        start_splash()
    }
}