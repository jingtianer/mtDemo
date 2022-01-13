package com.jingtian.mtdemo.Bean
/*
* "code": 602,    // 约定602错误码，代表验证码错误。
* "data": null,
* "msg": "wrong verification code",
* "count": 0,
* "success": false
*
* */
class BaseResult<T>(val code:Int?, val data:T?, val msg:String?, val count:Int?, val success:Boolean?) {}