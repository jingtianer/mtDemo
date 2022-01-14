package com.jingtian.mtdemo.Base.View

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import com.jingtian.mtdemo.Base.Interface.BaseInterface
import com.jingtian.mtdemo.R

abstract class BaseActivity<T:BaseInterface.presenter>:FragmentActivity(), BaseInterface.view {
    var mPresenter:T? = null
    abstract fun getPresenter():T
    abstract fun getLayout():Int
    override fun bind() {
        mPresenter = getPresenter()
        mPresenter?.bind(this)
    }

    fun setTransparentBars(){
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    fun getStatusBarHeight():Int {
        var statusBarHeight = -1;
        //获取status_bar_height资源的ID
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight
    }
    fun noBars() {
        val status_bar = findViewById<LinearLayout>(R.id.status_bar)
        val lp: LinearLayout.LayoutParams = status_bar!!.getLayoutParams() as LinearLayout.LayoutParams
        lp.setMargins(0,getStatusBarHeight(),0,0)
        status_bar.layoutParams = lp
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