package com.jingtian.mtdemo.base.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.R

abstract class BaseActivity<T : BaseInterface.Presenter> : FragmentActivity(), BaseInterface.View {
    fun login() {
        if (!BaseApplication.sp.login) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }

    private var mPresenter: T? = null
    abstract fun getPresenter(): T
    abstract fun getLayout(): Int
    override fun bind() {
        mPresenter = getPresenter()
        mPresenter?.bind(this)
    }

    fun setTransparentBars(){
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
    private fun getStatusBarHeight():Int {
        var statusBarHeight = -1
        //获取status_bar_height资源的ID
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }
    fun noBars() {
        val statusBar = findViewById<LinearLayout>(R.id.status_bar)
        val layoutParams: LinearLayout.LayoutParams = statusBar!!.layoutParams as LinearLayout.LayoutParams
        layoutParams.setMargins(0,getStatusBarHeight(),0,0)
        statusBar.layoutParams = layoutParams
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        val view = layoutInflater.inflate(getLayout(), null)
        setContentView(view)
        setTransparentBars()


    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.unbind()
        mPresenter = null
    }
}