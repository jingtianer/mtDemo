package com.jingtian.mtdemo.utils

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.jingtian.mtdemo.R

class AnimationUtil(val context: Context) {
    fun shakeAnimation(): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.shake)
    }

    fun getAnimation(id:Int):Animation {
        return AnimationUtils.loadAnimation(context, id)
    }

    @SuppressLint("ObjectAnimatorBinding")
    fun showUpAnimation(target: Any, offset: Int): ObjectAnimator {
        return ObjectAnimator.ofFloat(target, "translationY", offset.toFloat(), 0f)
    }

    @SuppressLint("ObjectAnimatorBinding")
    fun exitAnimation(target: Any, offset: Int): ObjectAnimator {
        return ObjectAnimator.ofFloat(target, "translationY", 0f, offset.toFloat())
    }
}