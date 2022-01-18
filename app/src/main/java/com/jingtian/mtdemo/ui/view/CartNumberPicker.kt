package com.jingtian.mtdemo.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication

class CartNumberPicker(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private var number = 0
    fun getNumber(): Int {
        return number
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.numberpicker, this)
        val leftButton = view.findViewById<TextView>(R.id.tv_left_bt)
        val rightButton = view.findViewById<TextView>(R.id.tv_right_bt)
        val numberTextView = view.findViewById<TextView>(R.id.tv_number_bt)
        BaseApplication.utils.setFont(leftButton)
        BaseApplication.utils.setFont(rightButton)
        numberTextView.text = "$number"
        leftButton.text = context.getString(R.string.minus)
        rightButton.text = context.getString(R.string.add)
        leftButton.setOnClickListener {
            number--
            numberTextView.text = "$number"
        }
        rightButton.setOnClickListener {
            number++
            numberTextView.text = "$number"
        }
    }
}