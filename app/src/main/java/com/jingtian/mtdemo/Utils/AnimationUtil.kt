package com.jingtian.mtdemo.Utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.jingtian.mtdemo.R

class AnimationUtil(val context: Context) {
    fun ShakeAnimation():Animation {
        val shake = AnimationUtils.loadAnimation(context, R.anim.shake)
        //不起效
        //shake.repeatCount = 100
        return shake
    }
}