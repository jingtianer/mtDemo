package com.jingtian.mtdemo.Base

import android.app.Application
import com.jingtian.mtdemo.Utils.SP
import kotlin.properties.Delegates

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        sp = SP(this)
    }

    companion object {
        var sp by Delegates.notNull<SP>()
    }
}