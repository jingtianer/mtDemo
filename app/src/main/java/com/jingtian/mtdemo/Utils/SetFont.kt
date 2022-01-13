package com.jingtian.mtdemo.Utils

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView

class SetFont {
    companion object {
        fun setFont(view:TextView, context:Context) {
            view.typeface = Typeface.createFromAsset(context.assets, "iconfont.ttf")
        }
    }
}