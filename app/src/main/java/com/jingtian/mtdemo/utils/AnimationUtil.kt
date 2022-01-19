package com.jingtian.mtdemo.utils

import android.content.Context
import android.view.animation.*
import com.jingtian.mtdemo.R

class AnimationUtil(val context: Context) {
    fun shakeAnimation(): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.shake)
    }

    fun showUpAnimation(): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.show_up)
    }

    fun exitAnimation(): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.exit_down)
    }
}