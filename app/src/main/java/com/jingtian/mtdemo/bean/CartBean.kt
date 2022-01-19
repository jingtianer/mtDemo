package com.jingtian.mtdemo.bean

import android.widget.TextView

data class CartBean(
    val pic: Int,
    val price: Float,
    var selection: Boolean
) {
    var checkBox: TextView? = null
}