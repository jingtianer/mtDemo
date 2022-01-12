package com.jingtian.mtdemo.Utils

import android.content.Context

class SP(val context: Context) {
    val login by SharedPreferencesUtil<Boolean>(context, "login", false)
}