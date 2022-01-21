package com.jingtian.mtdemo.ui.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.base.view.BaseActivity
import com.jingtian.mtdemo.databinding.ActivityLoginBinding
import com.jingtian.mtdemo.databinding.ActivitySearchBinding

class LoginActivity : BaseActivity<BaseInterface.Presenter>(), BaseInterface.View {
    override fun getPresenter(): BaseInterface.Presenter {
        return BasePresenter<LoginActivity>()
    }

    override fun getLayout(): Int {
        return R.layout.activity_login
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.utilsHolder.utils.setFont(binding.loginLeftExit)
        binding.loginLeftExit.setText(R.string.close)
        supportFragmentManager.beginTransaction()
            .replace(R.id.login_frame, PasswordFragment())
            .commit()
        binding.loginLeftExit.setOnClickListener {
            this.finish()
        }
        noBars()
    }
    private lateinit var binding:ActivityLoginBinding
    override fun viewBinding(): View {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        return binding.root
    }
}