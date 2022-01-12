package com.jingtian.mtdemo.Base.Presenter

import com.jingtian.mtdemo.Base.Interface.BaseInterface

open class BasePresenter<T:BaseInterface.view> : BaseInterface.presenter{
    var mView:T? = null
    override fun bind(view:BaseInterface.view) {
        mView = view as T
    }

    override fun unbind() {
        mView = null
    }

}