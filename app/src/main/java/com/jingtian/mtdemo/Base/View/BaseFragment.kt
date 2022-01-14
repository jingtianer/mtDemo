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