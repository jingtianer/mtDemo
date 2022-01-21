package com.jingtian.mtdemo.ui.adapters

import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.bean.MineBean2

class MineAdapter1(
    private val data: ArrayList<MineBean2>,
    private val mWidth: Int
) : RecyclerView.Adapter<MineAdapter1.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvItemMine1: TextView = view.findViewById(R.id.tv_item_mine1)
        val tvItemMine2: TextView = view.findViewById(R.id.tv_item_mine2)
        val tvItemMine3: TextView = view.findViewById(R.id.tv_item_mine3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_mine, parent, false).apply {
                layoutParams.width = mWidth / data.size * 2
            })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            BaseApplication.utilsHolder.utils.setFont(tvItemMine1)
            tvItemMine1.text = SpannableString(data[position].data1).apply {
                setSpan(AbsoluteSizeSpan(32, true), 0, 1, 0)
                setSpan(
                    ForegroundColorSpan(
                        BaseApplication.utilsHolder.utils.getColor(
                            R.color.orange_secondary
                        )
                    ), 0, 1, 0
                )
            }
            tvItemMine2.text = data[position].data2
            data[position].data3.let {
                tvItemMine3.text = it
            }
        }
    }

    override fun getItemCount(): Int = data.size
}