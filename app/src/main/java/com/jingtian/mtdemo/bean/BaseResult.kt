package com.jingtian.mtdemo.bean

class BaseResult<T>(
    var msg: String?,
    var code: Int?,
    var data: T?,
    var success: Boolean?,
    var count: Int?
)