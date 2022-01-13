package com.jingtian.mtdemo.UI.Presenter

import com.jingtian.mtdemo.Base.Presenter.BasePresenter
import com.jingtian.mtdemo.Bean.BaseResult
import com.jingtian.mtdemo.UI.Interface.LoginInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter: BasePresenter<LoginInterface.view>(), LoginInterface.presenter {
    override fun loginByVc(phoneNumber: String, verificationCode: String) {
        mInterface.loginByVc(phoneNumber, verificationCode)?.enqueue(object :Callback<BaseResult<Any>> {
            override fun onResponse(
                call: Call<BaseResult<Any>>,
                response: Response<BaseResult<Any>>
            ) {
                if (response.body()?.code == 0) {
                    mView?.login_success()
                } else if (response.body()?.code == 602) {
                    mView?.loginByVc_fail(response.body()?.msg + "")
                } else {
                    mView?.loginByVc_fail("服务器异常")
                }
            }

            override fun onFailure(call: Call<BaseResult<Any>>, t: Throwable) {
                mView?.loginByVc_fail("其他错误")
            }

        })
    }

    override fun loginByPd(phoneNumber: String, password: String) {
        mInterface.loginByPd(phoneNumber, password)?.enqueue(object :Callback<BaseResult<Any>> {
            override fun onResponse(
                call: Call<BaseResult<Any>>,
                response: Response<BaseResult<Any>>
            ) {
                if (response.body()?.code == 0) {
                    mView?.login_success()
                } else if (response.body()?.code == 601) {
                    mView?.loginByPd_fail(response.body()?.msg + "")
                } else {
                    mView?.loginByPd_fail("服务器异常")
                }
            }

            override fun onFailure(call: Call<BaseResult<Any>>, t: Throwable) {
                mView?.loginByPd_fail("网络错误")
            }

        })
    }
}