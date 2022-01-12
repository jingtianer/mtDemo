package com.jingtian.mtdemo.UI.View

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.jingtian.mtdemo.Base.Interface.BaseInterface
import com.jingtian.mtdemo.Base.Presenter.BasePresenter
import com.jingtian.mtdemo.Base.View.BaseActivity
import com.jingtian.mtdemo.BuildConfig.DEBUG
import com.jingtian.mtdemo.R
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
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).let {
            it.show(WindowInsetsCompat.Type.systemBars())
        //点击屏幕显示bars
        //it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
        }
    }
    fun setBackGround() {
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        window.setBackgroundDrawableResource(R.drawable.splash_launcher)
    }
    fun startMainActivity() {
        Log.d("start main activity","start main activity")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    val timer = Timer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemBars()
        setBackGround()
        splash_delay = 4 //延迟时间
        val sp_delay = findViewById<TextView>(R.id.sp_delay)
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    Log.d("delay","${splash_delay}s")
                    sp_delay.text = "${splash_delay}s"
                    if (splash_delay == 0) {
                        startMainActivity()
                        this.cancel()
                    }
                    splash_delay--
                }

            }
        }, 0, 1000)
        //倒计时
        findViewById<TextView>(R.id.tv_sp_skip).setOnClickListener {
            startMainActivity()
            timer.cancel()
        }
        //点击跳过按钮

    }

    override fun getLayout(): Int {
        return R.layout.activity_splash
    }

    override fun getPresenter(): BaseInterface.presenter {
        return BasePresenter<SplashActivity>()
    }
}