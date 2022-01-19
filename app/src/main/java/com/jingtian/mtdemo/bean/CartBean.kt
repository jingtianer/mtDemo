package com.jingtian.mtdemo.bean

import androidx.constraintlayout.widget.ConstraintLayout

data class CartBean(
    val pic: Int,
    val price: Float,
    var selection: Boolean
) {
    var checkBox: ConstraintLayout? = null
}