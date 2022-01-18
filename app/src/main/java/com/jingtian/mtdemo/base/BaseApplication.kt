package com.jingtian.mtdemo.base

import android.app.Application
import com.jingtian.mtdemo.utils.AnimationUtil
import com.jingtian.mtdemo.utils.SP
import kotlin.properties.Delegates

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        sp = SP(this)
        anims = AnimationUtil(this)

    }

    companion object {
        var sp by Delegates.notNull<SP>()
        var anims by Delegates.notNull<AnimationUtil>()
    }
}