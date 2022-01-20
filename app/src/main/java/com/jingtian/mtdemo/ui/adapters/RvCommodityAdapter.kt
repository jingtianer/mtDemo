package com.jingtian.mtdemo.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import java.util.*

class RvCommodityAdapter(private val data: ArrayList<Int>) :
    RecyclerView.Adapter<RvCommodityAdapter.ViewHolder>() {
    companion object {
        const val VERTICAL = true
        const val HORIZONTAL = false
        fun setWaterfallFlowStyle(rv: RecyclerView, spanCount: Int, vertical: Boolean) {
            rv.apply {
                layoutManager = StaggeredGridLayoutManager(
                    spanCount,
                    if (vertical) StaggeredGridLayoutManager.VERTICAL else StaggeredGridLayoutManager.HORIZONTAL
                )

            }
        }

        fun getRes(): ArrayList<Int> {
            return arrayListOf(
                R.mipmap.commodities1,
                R.mipmap.commodities2,
                R.mipmap.commodities3,
                R.mipmap.commodities4,
                R.mipmap.commodities5,
                R.mipmap.commodities6,
                R.mipmap.commodities7,
                R.mipmap.commodities8,
                R.mipmap.commodities9,
                R.mipmap.commodities10,
                R.mipmap.commodities11,
                R.mipmap.commodities12,
                R.mipmap.commodities13,
                R.mipmap.commodities14,
                R.mipmap.commodities15,
                R.mipmap.commodities16,
                R.mipmap.commodities17,
            )
        }

//        val res: MutableList<Int> = Collections.synchronizedList(
//            arrayListOf(
//                R.mipmap.commodities1,
//                R.mipmap.commodities2,
//                R.mipmap.commodities3,
//                R.mipmap.commodities4,
//                R.mipmap.commodities5,
//                R.mipmap.commodities6,
//                R.mipmap.commodities7,
//                R.mipmap.commodities8,
//                R.mipmap.commodities9,
//                R.mipmap.commodities10,
//                R.mipmap.commodities11,
//                R.mipmap.commodities12,
//                R.mipmap.commodities13,
//                R.mipmap.commodities14,
//                R.mipmap.commodities15,
//                R.mipmap.commodities16,
//                R.mipmap.commodities17,
//            )
//        )
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val ivCommodity: ImageView = view.findViewById(R.id.iv_commodity)
        val tvItemCommodity: TextView = view.findViewById(R.id.tv_item_commodity)
        val tvItemCommodityPrice: TextView = view.findViewById(R.id.tv_item_commodity_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_commodity, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val description = BaseApplication.utils.getDescription()
        val price = BaseApplication.utils.getPrice()

        holder.apply {
            ivCommodity.setImageResource(data[position])
            tvItemCommodity.text =
                holder.view.context.getString(R.string.tvItemCommodity, position + 1, description)
            tvItemCommodityPrice.text = holder.view.context.getString(
                R.string.tvItemCommodityPrice,
                price
            )
        }
    }

    override fun getItemCount(): Int = data.size
}