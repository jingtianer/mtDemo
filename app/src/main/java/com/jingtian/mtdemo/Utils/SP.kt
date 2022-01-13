package com.jingtian.mtdemo.Utils

import android.content.Context

class SP(val context: Context) {
    var login by SharedPreferencesUtil<Boolean>(context, "login", false)
    var phone by SharedPreferencesUtil<String>(context, "phone", "")
}