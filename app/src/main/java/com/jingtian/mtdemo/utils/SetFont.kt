package com.jingtian.mtdemo.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.widget.TextView

class SetFont {
    companion object {
        fun setFont(view:TextView, context:Context) {
            // TODO: 这里是否可以加个内存缓存，不用每次都去加载
            view.typeface = Typeface.createFromAsset(context.assets, "iconfont.ttf")
        }
        fun getScreenHeight(activity: Activity):Int = activity.resources.displayMetrics.heightPixels
        fun getScreenWidth(activity: Activity):Int = activity.resources.displayMetrics.widthPixels
        fun px2dip(context:Context, pxValue:Float):Int {
            val scale = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }

        /**
         * 将dip或dp值转换成为px值，保证尺寸大小不变
         *
         * @param context
         * @param dpValue
         * @return
         */
        fun dip2px(context:Context,dpValue:Float):Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        fun getColor(activity:Activity, id:Int) :Int{
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.resources.getColor(id,null)
            } else {
                activity.resources.getColor(id)
            }
        }

    }

}