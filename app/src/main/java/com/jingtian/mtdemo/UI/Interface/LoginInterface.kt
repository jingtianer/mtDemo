package com.jingtian.mtdemo.UI.Interface

import com.jingtian.mtdemo.Base.Interface.BaseInterface

interface LoginInterface {
    interface view : BaseInterface.view{
        fun login_success()
        fun loginByVc_fail(mes:String)
        fun loginByPd_fail(mes:String)
    }
    interface presenter : BaseInterface.presenter{
        fun loginByVc(phoneNumber:String, verificationCode:String)
        fun loginByPd(phoneNumber:String, password:String)
    }
}