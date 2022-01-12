package com.jingtian.mtdemo.UI.View

import com.jingtian.mtdemo.Base.View.BaseFragment
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.UI.Interface.LoginInterface
import com.jingtian.mtdemo.UI.Presenter.LoginPresenter

class PasswordFragment:BaseFragment<LoginPresenter>(), LoginInterface.view {
    override fun getPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_password
    }
}