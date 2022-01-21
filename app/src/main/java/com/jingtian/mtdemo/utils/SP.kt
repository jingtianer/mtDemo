package com.jingtian.mtdemo.utils

import android.content.Context
import com.jingtian.mtdemo.BuildConfig

class SP(val context: Context) {
    var login by SharedPreferencesUtil<Boolean>(context, "login", false)
    var phone by SharedPreferencesUtil<String>(context, "phone", if (BuildConfig.DEBUG) "" else "15383511227")
}