package com.jingtian.mtdemo.UI.View

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import com.jingtian.mtdemo.Base.Interface.BaseInterface
import com.jingtian.mtdemo.Base.Presenter.BasePresenter
import com.jingtian.mtdemo.Base.View.BaseActivity
import com.jingtian.mtdemo.R

class LoginActivity:BaseActivity<BaseInterface.presenter>(), BaseInterface.view {
    override fun getPresenter(): BaseInterface.presenter {
        return BasePresenter<LoginActivity>()
    }

    override fun getLayout(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        //没用
        findViewById<TextView>(R.id.login_left_exit).typeface = Typeface.createFromAsset(assets, "iconfont.ttf")
        supportFragmentManager.beginTransaction()
            .replace(R.id.login_frame, PasswordFragment())
            .commit()


    }
}