package com.jingtian.mtdemo.UI.Refresh

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

abstract class RefreshDrawable(context: Context, private val mRefreshLayout: PullRefreshLayout):Drawable(), android.graphics.drawable.Animatable, Drawable.Callback {
    fun getContext():Context {
        return mRefreshLayout.context
    }
    fun getRefreshLayout():PullRefreshLayout = mRefreshLayout

    abstract fun setPercent(percent: Float)
    abstract fun setColorSchemeColors(colorSchemeColors: IntArray?)
    abstract fun offsetTopAndBottom(offset: Int)

    override fun setAlpha(p0: Int) {}

    override fun setColorFilter(p0: ColorFilter?) {}

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun invalidateDrawable(p0: Drawable) {
        callback?.invalidateDrawable(this)
    }

    override fun scheduleDrawable(p0: Drawable, p1: Runnable, p2: Long) {
        callback?.scheduleDrawable(this,p1,p2)
    }

    override fun unscheduleDrawable(p0: Drawable, p1: Runnable) {
        callback?.unscheduleDrawable(this,p1)
    }
}