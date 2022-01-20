package com.jingtian.mtdemo.base.presenter

import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.net.NetConstants
import com.jingtian.mtdemo.net.NetInterface

open class BasePresenter<T : BaseInterface.View> : BaseInterface.Presenter {
    var mView: T? = null
    var mInterface: NetInterface = NetConstants.getInterface()
    override fun bind(view: BaseInterface.View) {
        mView = view as T
    }

    override fun unbind() {
        mView = null
    }

}