package com.jingtian.mtdemo.utils

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.widget.TextView
import kotlin.math.abs
import kotlin.random.Random

class MyUtils(val context: Context) {

    private val random = Random(System.currentTimeMillis())
    fun getDescription(): String {
        val n = 1 + abs(random.nextInt() % 10)
        var description = ""
        for (i in 0..n) {
            description += "描述！"
        }
        return description
    }

    fun getPrice() = 100 + abs(random.nextInt() % 1000)

    private val typefaceMap = mutableMapOf<String, Typeface>()
    private fun getTypeFace(path: String): Typeface? {
        if (!typefaceMap.containsKey(path)) {
            typefaceMap[path] = Typeface.createFromAsset(context.assets, path)
        }
        return typefaceMap[path]
    }

    fun setFont(view: TextView) {
        view.typeface = getTypeFace("iconfont.ttf")
        Log.d("typeface", "${view.typeface == null}")
    }

    fun getScreenHeight(): Int =
        context.resources.displayMetrics.heightPixels

    fun getScreenWidth(): Int = context.resources.displayMetrics.widthPixels
    fun px2dip(pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将dip或dp值转换成为px值，保证尺寸大小不变
     *
     * @param dpValue
     * @return
     */
    fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun getColor(id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(id, null)
        } else {
            context.resources.getColor(id)
        }
    }

    fun getString(id: Int): String {
        return context.resources.getString(id)
    }

}