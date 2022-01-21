package com.jingtian.mtdemo.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication

class CartNumberPicker(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    interface OnClickListener {
        fun click(isLeft: Boolean)
    }

    fun setNumber(n: Int) {
        if (!isInvalidNumber(n)) {
            number = n
            onNumberChanged()
        }
    }

    private fun increase() {
        number++
        numberTextView.text = "$number"
        for (listener in listeners) {
            listener.click(false)
        }
    }

    private val listeners = mutableListOf<OnClickListener>()
    fun addClickListener(listener: OnClickListener) {
        listeners.add(listener)
    }

    private var number = 0
    fun getNumber(): Int {
        return number
    }

    var isNonNegative: Boolean

    val view: View = LayoutInflater.from(context).inflate(R.layout.numberpicker, this)
    val leftButton = view.findViewById<TextView>(R.id.tv_left_bt)
    val rightButton = view.findViewById<TextView>(R.id.tv_right_bt)
    val numberTextView: TextView = view.findViewById(R.id.tv_number_bt)

    init {
        //获取xml属性
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CartNumberPicker)
        isNonNegative = ta.getBoolean(R.styleable.CartNumberPicker_non_negative, false)
        ta.recycle()
        //public void recycle ():
        //Give back a previously retrieved array, for later re-use.
        //回收TypedArray,用于后续调用时可复用之。当调用该方法后，不能再操作该变量。
        //该类没有公共的构造函数，只提供静态方法获取实例，是一个典型的单例模式。这个 array 是从一个 array pool的池中获取的。

        BaseApplication.utilsHolder.utils.setFont(leftButton)
        BaseApplication.utilsHolder.utils.setFont(rightButton)
        numberTextView.text = "$number"
        leftButton.text = context.getString(R.string.minus)
        rightButton.text = context.getString(R.string.add)
        leftButton.setOnClickListener {
            if (!isInvalidNumber(number - 1)) {
                number--
                onNumberChanged()
            }
            for (listener in listeners) {
                listener.click(true)
            }
        }
        rightButton.setOnClickListener {
            increase()
        }
    }

    private fun onNumberChanged() {
        numberTextView.text = "$number"
    }

    private fun isInvalidNumber(n: Int): Boolean {
        if (isNonNegative && n < 0) {
            this.startAnimation(BaseApplication.utilsHolder.anims.shakeAnimation())
        }
        return isNonNegative && n < 0
    }
}