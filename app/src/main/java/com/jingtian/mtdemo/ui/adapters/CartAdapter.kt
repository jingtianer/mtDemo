package com.jingtian.mtdemo.ui.adapters

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.bean.CartBean
import com.jingtian.mtdemo.ui.view.CartFragment
import com.jingtian.mtdemo.ui.view.CartNumberPicker

class CartAdapter(
    private val activity: Activity,
    val data: ArrayList<CartBean>,
    private val listener1: CartFragment.SelectionClickListener,
    private val listener2: CartFragment.NumberClickListener
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    fun removeAt(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun selectAll() {
        for (i in 0 until data.size) {
            if (!data[i].selection)
                data[i].checkBox?.performClick()
        }
    }

    fun unselectAll() {
        for (i in 0 until data.size) {
            if (data[i].selection)
                data[i].checkBox?.performClick()
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val tvCartIcon: TextView = view.findViewById(R.id.tv_cart_icon)
        val ivPic: ImageView = view.findViewById(R.id.iv_cart_pic)
        val tvItemName: TextView = view.findViewById(R.id.tv_cart_commodity)
        val tvPrice: TextView = view.findViewById(R.id.tv_cart_price)
        val cartNumberPicker: CartNumberPicker = view.findViewById(R.id.cnp_number_picker)
        val clCartItemRoot = view.findViewById<ConstraintLayout>(R.id.cl_cart_item_root)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.item_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            data[position].checkBox = clCartItemRoot
            BaseApplication.utils.setFont(tvCartIcon)
            val item = data[position]
            tvCartIcon.setText(R.string.unchecked)
            tvCartIcon.setTextColor(BaseApplication.utils.getColor(R.color.orange_secondary))
            ivPic.setImageResource(item.pic)
            val name = activity.getString(
                R.string.tvItemCommodity,
                item.pic,
                ""
            )
            tvItemName.text = SpannableString(name).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, name.length, 0)
                setSpan(ForegroundColorSpan(Color.BLACK), 0, name.length, 0)
                setSpan(AbsoluteSizeSpan(BaseApplication.utils.dip2px(20f)), 0, name.length, 0)
            }
            val price = "￥${item.price}"
            tvPrice.text = SpannableString(price).apply {
                setSpan(
                    ForegroundColorSpan(Color.BLACK),
                    0,
                    1,
                    0
                )
                setSpan(AbsoluteSizeSpan(BaseApplication.utils.dip2px(16f)), 0, 1, 0)
                setSpan(
                    ForegroundColorSpan(BaseApplication.utils.getColor(R.color.orange)),
                    1,
                    price.length,
                    0
                )
                setSpan(AbsoluteSizeSpan(BaseApplication.utils.dip2px(22f)), 1, price.length, 0)
            }
            clCartItemRoot.setOnClickListener {
                listener1.click(tvCartIcon, item.selection, item)
                item.selection = !item.selection
            }
            cartNumberPicker.addClickListener(
                object : CartNumberPicker.OnClickListener {
                    override fun click(isLeft: Boolean) {
                        listener2.click(cartNumberPicker, item.pic)
                    }
                }
            )
        }
    }

    override fun getItemCount(): Int = data.size

}