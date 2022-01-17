package com.jingtian.mtdemo.Utils

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

class SetFont {
    companion object {
        fun setFont(view:TextView, context:Context) {
            view.typeface = Typeface.createFromAsset(context.assets, "iconfont.ttf")
        }
        fun getScreenHeight(activity: Activity):Int = activity.resources.displayMetrics.heightPixels
        fun getScreenWidth(activity: Activity):Int = activity.resources.displayMetrics.widthPixels
        fun px2dip(context:Context, pxValue:Float):Int {
            val scale = context.resources.displayMetrics.density;
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
            val scale = context.resources.displayMetrics.density;
            return (dpValue * scale + 0.5f).toInt()
        }
    }

}