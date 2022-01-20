package com.jingtian.mtdemo.ui.adapters

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
import com.jingtian.mtdemo.bean.SortBean
import com.jingtian.mtdemo.ui.view.SortFragment

class SortAdapter(
    val data: ArrayList<SortBean>,
    val listener: SortFragment.Add2CartListener
) : RecyclerView.Adapter<SortAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val ivPic: ImageView = view.findViewById(R.id.iv_sort_pic)
        val tvItemName: TextView = view.findViewById(R.id.tv_sort_commodity)
        val tvPrice: TextView = view.findViewById(R.id.tv_sort_price)
        val clCartItemRoot: ConstraintLayout = view.findViewById(R.id.cl_sort_item_root)
        val tvSortAdd2cart: TextView = view.findViewById(R.id.tv_sort_add2cart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_sort, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            val item = data[position]
            ivPic.setImageResource(item.pic)
            val name = view.context.getString(
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
            BaseApplication.utils.setFont(tvSortAdd2cart)
            tvSortAdd2cart.text = BaseApplication.utils.getString(R.string.add2cart)
            clCartItemRoot.setOnClickListener {
                listener.click(data[position])
            }
        }
    }

    override fun getItemCount(): Int = data.size

}