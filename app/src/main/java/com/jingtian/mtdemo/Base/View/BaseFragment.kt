package com.jingtian.mtdemo.Base.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jingtian.mtdemo.Base.Interface.BaseInterface

abstract class BaseFragment<T:BaseInterface.presenter>:BaseInterface.view, Fragment() {
    var mPresenter:T? = null
    override fun bind() {
        mPresenter?.bind(this)
    }

    abstract fun getPresenter():T
    abstract fun getLayout():Int
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
        return inflater.inflate(getLayout(), container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.unbind()
        mPresenter = null
    }
}