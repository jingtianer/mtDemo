package com.jingtian.mtdemo.utils

import android.content.Context
import com.jingtian.mtdemo.BuildConfig.DEBUG

class SP(val context: Context) {
    var login by SharedPreferencesUtil<Boolean>(context, "login", false)
    var phone by SharedPreferencesUtil<String>(context, "phone", if (DEBUG) "" else "15383511227")
}