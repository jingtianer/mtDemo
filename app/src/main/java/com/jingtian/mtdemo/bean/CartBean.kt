package com.jingtian.mtdemo.bean

import androidx.constraintlayout.widget.ConstraintLayout

data class CartBean(
    var pic: Int,
    var price: Float,
    var selection: Boolean
) {
    var checkBox: ConstraintLayout? = null
    var n: Int = 0
}

class SortBean2CartBuilder {
    private var sortBean: SortBean? = null
    private var pic: Int? = null
    private var price: Float? = null
    private var n: Int? = null
    fun setSortBean(sortBean: SortBean): SortBean2CartBuilder {
        this.sortBean = sortBean
        return this
    }

    fun setN(n: Int) {
        this.n = n
    }

    fun setPic(pic: Int): SortBean2CartBuilder {
        this.pic = pic
        return this
    }

    fun setPrice(price: Float): SortBean2CartBuilder {
        this.price = price
        return this
    }

    fun build(): CartBean? {
        return (when {
            sortBean != null -> CartBean(sortBean!!.pic, sortBean!!.price, false)
            (pic != null) and (price != null) -> CartBean(pic!!, price!!, false)
            else -> null
        })?.apply {
            this@SortBean2CartBuilder.n?.let {
                this.n = it
            }
        }
    }

}