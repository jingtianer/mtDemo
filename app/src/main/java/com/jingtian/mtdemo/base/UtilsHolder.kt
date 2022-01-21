package com.jingtian.mtdemo.base

import android.content.Context
import com.jingtian.mtdemo.utils.AnimationUtil
import com.jingtian.mtdemo.utils.MyUtils
import com.jingtian.mtdemo.utils.SP
class UtilsHolder(private val context: Context) {
    val sp by lazy {
        SP(context)
    }
    val anims by lazy {
        AnimationUtil(context)
    }
    val utils:MyUtils by lazy {
        MyUtils(context)
    }
}