package com.jingtian.mtdemo.Net

import com.jingtian.mtdemo.Bean.BaseResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface NetInterface {

    /*
    * method : get
    * account/loginByVc
    * phoneNumber	String	是	电话号码
    * verificationCode	String	是	登录验证码
    * 备注：模拟项目暂时先写死判断规则，密码为1234，就代表密码正确，其他认为是验证码错误。
    * */
    @GET("account/LoginByVc")
    fun loginByVc(@Query("phoneNumber")phoneNumber:String,
                  @Query("verificationCode")verificationCode:String): Call<BaseResult<Any>>?

    /*
    * method : get
    * account/loginByPd
    * phoneNumber	String	是	电话号码
    * password	String	是	登录密码，理论上应该加密传输，因为模拟项目，所以暂时明文传输。
    * 备注：模拟项目暂时先写死判断规则，密码为1234，就代表密码正确，其他认为是验证码错误。
    * */
    @GET("account/LoginByPd")
    fun loginByPd(@Query("phoneNumber")phoneNumber:String,
                  @Query("password")password:String): Call<BaseResult<Any>>?
}