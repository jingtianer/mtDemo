package com.jingtian.mtdemo.UI.Refresh

import android.content.Context
import android.graphics.Canvas

class SwipLoadDrawable(context: Context, mRefreshLayout: PullRefreshLayout)
    :SwipDrawable(context, mRefreshLayout) {
    override fun setPercent(percent: Float) {
        super.setPercent(percent)
        val bottom = bounds.bottom
        rectF.top = bottom * 1.0f
        rectF.bottom = bottom + drawableMinddleWidth * 2 * mPercent
    }

    override fun draw(p0: Canvas) {
        val num = (System.currentTimeMillis() / 50 % 11).toInt()
        val saveCount = p0.save()
        p0.translate(0f, mOffset.toFloat())
        val bitmap = bitmaps[num]
        p0.drawBitmap(bitmap, null, rectF, null)
        p0.restoreToCount(saveCount)
    }
}