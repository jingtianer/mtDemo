package com.jingtian.mtdemo.ui.interfaces

import com.jingtian.mtdemo.base.interfaces.BaseInterface

interface LoginInterface {
    interface View : BaseInterface.View {
        fun loginSuccess()
        fun loginByVcFailed(mes: String)
        fun loginByPdFailed(mes: String)
    }

    interface Presenter : BaseInterface.Presenter {
        fun loginByVc(phoneNumber: String, verificationCode: String)
        fun loginByPd(phoneNumber: String, password: String)
    }
}