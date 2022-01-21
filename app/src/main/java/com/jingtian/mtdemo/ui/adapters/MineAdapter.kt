package com.jingtian.mtdemo.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.bean.MineBean1

class MineAdapter(
    private val data: ArrayList<MineBean1>,
    private val mWidth: Int
) : RecyclerView.Adapter<MineAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvItemMine1: TextView = view.findViewById(R.id.tv_item_mine1)
        val tvItemMine2: TextView = view.findViewById(R.id.tv_item_mine2)
        val tvItemMine3: TextView = view.findViewById(R.id.tv_item_mine3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_mine, parent, false).apply {
                layoutParams.width = mWidth / data.size
            })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            if (data[position].is_icon) {
                BaseApplication.utilsHolder.utils.setFont(tvItemMine1)
            }
            tvItemMine1.text = data[position].text1
            tvItemMine2.text = data[position].text2
            data[position].text3.let {
                tvItemMine3.text = it
            }
        }
    }

    override fun getItemCount(): Int = data.size

}