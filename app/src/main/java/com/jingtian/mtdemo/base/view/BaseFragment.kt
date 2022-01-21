package com.jingtian.mtdemo.base.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.ui.view.LoginActivity

abstract class BaseFragment<T : BaseInterface.Presenter> : BaseInterface.View, Fragment() {
    var mPresenter: T? = null
    override fun bind() {
        mPresenter?.bind(this)
    }

    fun login() {
        if (!BaseApplication.utilsHolder.sp.login) {
            val intent = Intent(this.context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            context?.startActivity(intent)
        }
    }

    fun getStatusBarHeight(): Int {
        var statusBarHeight = -1
        //获取status_bar_height资源的ID
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    abstract fun getPresenter(): T
    abstract fun getLayout(): Int
    abstract fun viewBinding(inflater: LayoutInflater, container: ViewGroup?,): View?
    abstract fun unViewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = getPresenter()
        bind()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return viewBinding(inflater, container)
    }

    override fun onDestroy() {
        super.onDestroy()
        unViewBinding()
        mPresenter?.unbind()
        mPresenter = null
    }
}