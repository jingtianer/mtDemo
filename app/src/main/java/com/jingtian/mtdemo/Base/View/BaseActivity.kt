package com.jingtian.mtdemo.Base.View

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.jingtian.mtdemo.Base.Interface.BaseInterface

abstract class BaseActivity<T:BaseInterface.presenter>:FragmentActivity(), BaseInterface.view {
    var mPresenter:T? = null
    abstract fun getPresenter():T
    abstract fun getLayout():Int
    override fun bind() {
        mPresenter = getPresenter()
        mPresenter?.bind(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        val view = layoutInflater.inflate(getLayout(), null)
        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.unbind()
        mPresenter = null
    }
}