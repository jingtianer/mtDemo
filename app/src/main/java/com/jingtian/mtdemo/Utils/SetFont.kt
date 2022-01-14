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
    }

}