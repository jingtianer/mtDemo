package com.jingtian.mtdemo.utils

import android.content.Context
import android.view.animation.*
import com.jingtian.mtdemo.R

class AnimationUtil(val context: Context) {
    fun ShakeAnimation():Animation {
        val shake = AnimationUtils.loadAnimation(context, R.anim.shake)
        return shake
    }
}