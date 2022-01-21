package com.jingtian.mtdemo.base

import android.app.Application
import com.jingtian.mtdemo.utils.AnimationUtil
import com.jingtian.mtdemo.utils.MyUtils
import com.jingtian.mtdemo.utils.SP
import kotlin.properties.Delegates

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        utilsHolder = UtilsHolder(applicationContext)
    }

    companion object {
        var utilsHolder by Delegates.notNull<UtilsHolder>()
    }
}