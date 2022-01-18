package com.jingtian.mtdemo.ui.view

import android.os.Bundle
import android.widget.TextView
import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.base.view.BaseActivity
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.utils.SetFont

class LoginActivity:BaseActivity<BaseInterface.Presenter>(), BaseInterface.View {
    override fun getPresenter(): BaseInterface.Presenter {
        return BasePresenter<LoginActivity>()
    }

    override fun getLayout(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = findViewById<TextView>(R.id.login_left_exit)
        SetFont.setFont(text, this)
        text.setText(R.string.close)
        supportFragmentManager.beginTransaction()
            .replace(R.id.login_frame, PasswordFragment())
            .commit()
        text.setOnClickListener {
            this.finish()
        }
        noBars()
    }
}