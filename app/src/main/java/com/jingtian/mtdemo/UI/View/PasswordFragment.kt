package com.jingtian.mtdemo.UI.View

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.jingtian.mtdemo.Base.View.BaseFragment
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.UI.Interface.LoginInterface
import com.jingtian.mtdemo.UI.Presenter.LoginPresenter

class PasswordFragment:BaseFragment<LoginPresenter>(), LoginInterface.view {
    override fun getPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    var is_password = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tv_fgpwd_change = activity!!.findViewById<TextView>(R.id.tv_fgpwd_change)
        val tv_fgpwd_loginhint = activity!!.findViewById<TextView>(R.id.tv_fgpwd_loginhint)
        val ll_pwd = activity!!.findViewById<LinearLayout>(R.id.ll_pwd)
        val fgpwd_divider2 = activity!!.findViewById<View>(R.id.fgpwd_divider2)
        is_password = true
        tv_fgpwd_change.setOnClickListener {
            if (is_password) {
                ll_pwd.visibility = View.GONE
                tv_fgpwd_loginhint.visibility = View.VISIBLE
                fgpwd_divider2.visibility = View.GONE
                tv_fgpwd_change.setText("使用密码")
            } else {
                ll_pwd.visibility = View.VISIBLE
                tv_fgpwd_loginhint.visibility = View.GONE
                fgpwd_divider2.visibility = View.VISIBLE
                tv_fgpwd_change.setText("使用验证码")
            }
            is_password = !is_password
        }
    }
    override fun getLayout(): Int {
        return R.layout.fragment_password
    }
}