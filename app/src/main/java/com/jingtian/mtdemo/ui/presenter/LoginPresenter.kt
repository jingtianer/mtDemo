package com.jingtian.mtdemo.ui.presenter

import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.bean.BaseResult
import com.jingtian.mtdemo.net.NetConstants.Companion.LOGIN_BY_PD_FAIL
import com.jingtian.mtdemo.net.NetConstants.Companion.LOGIN_BY_VC_FAIL
import com.jingtian.mtdemo.net.NetConstants.Companion.LOGIN_SUCCESS
import com.jingtian.mtdemo.ui.interfaces.LoginInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter : BasePresenter<LoginInterface.View>(), LoginInterface.Presenter {
    override fun loginByVc(phoneNumber: String, verificationCode: String) {
        mInterface.loginByVc(phoneNumber, verificationCode)
            ?.enqueue(object : Callback<BaseResult<Any>> {
                override fun onResponse(
                    call: Call<BaseResult<Any>>,
                    response: Response<BaseResult<Any>>
                ) {
                    when (response.body()?.code) {
                        LOGIN_SUCCESS -> {
                            mView?.loginSuccess()
                        }
                        LOGIN_BY_VC_FAIL -> {
                            mView?.loginByVcFailed(response.body()?.msg + "")
                        }
                        else -> {
                            mView?.loginByVcFailed("服务器异常")
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResult<Any>>, t: Throwable) {
                    mView?.loginByVcFailed("其他错误")
                }

            })
    }

    override fun loginByPd(phoneNumber: String, password: String) {
        mInterface.loginByPd(phoneNumber, password)?.enqueue(object : Callback<BaseResult<Any>> {
            override fun onResponse(
                call: Call<BaseResult<Any>>,
                response: Response<BaseResult<Any>>
            ) {
                when (response.body()?.code) {
                    LOGIN_SUCCESS -> {
                        mView?.loginSuccess()
                    }
                    LOGIN_BY_PD_FAIL -> {
                        mView?.loginByPdFailed(response.body()?.msg + "")
                    }
                    else -> {
                        mView?.loginByPdFailed("服务器异常")
                    }
                }
            }

            override fun onFailure(call: Call<BaseResult<Any>>, t: Throwable) {
                mView?.loginByPdFailed("其他错误")
            }

        })
    }
}