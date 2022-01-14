package com.jingtian.mtdemo.UI.Refresh

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper
import androidx.core.content.res.ResourcesCompat

open class SwipDrawable(context: Context, mRefreshLayout: PullRefreshLayout)
    : RefreshDrawable(context, mRefreshLayout), Runnable {
        init {
            getBitmaps(context)
        }
    val bitmaps:ArrayList<Bitmap> = arrayListOf()
    var drawableMinddleWidth = 0
    fun getBitmaps(context: Context) {
        val bitmap_res = arrayListOf<Int>()
        for (res in bitmap_res) {
            bitmaps.add(
                (ResourcesCompat.getDrawable(
                    context.resources,
                    res,
                    null
                ) as BitmapDrawable).bitmap.apply {
                    drawableMinddleWidth = minimumWidth/2
                }
            )
        }
    }
    var mPercent = 0.0f
    val rectF = RectF()
    override fun setPercent(percent:Float) {
        mPercent = percent
        val centerX = bounds.centerX()
        rectF.left = centerX - drawableMinddleWidth * mPercent
        rectF.right = centerX + drawableMinddleWidth * mPercent
        rectF.top = -drawableMinddleWidth * 2 * mPercent
        rectF.bottom = 0f
    }

    override fun setColorSchemeColors(colorSchemeColors: IntArray?) {}

    var mOffset = 0
    override fun offsetTopAndBottom(offset: Int) {
        mOffset = offset
        invalidateSelf()
    }
    var mIsRunning = false
    val mHandler = Handler(Looper.getMainLooper())

    override fun start() {
        mIsRunning = true
        mHandler.postDelayed(this, 50)
    }

    override fun run() {
        if (mIsRunning) {
            mHandler.postDelayed(this, 50)
            invalidateSelf()
        }
    }

    override fun stop() {
        mIsRunning = false
        mHandler.hasCallbacks(this)
    }

    override fun isRunning(): Boolean {
        return mIsRunning
    }



    override fun draw(p0: Canvas) {
        var num = (System.currentTimeMillis() /50 % 11).toInt()
        val saveCount = p0.save()
        p0.translate(0f, mOffset.toFloat())
        val bitmap = bitmaps[num]
        p0.drawBitmap(bitmap, null, rectF, null)
        p0.restoreToCount(saveCount)
    }


}