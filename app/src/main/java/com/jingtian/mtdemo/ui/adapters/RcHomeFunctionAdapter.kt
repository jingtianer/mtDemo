package com.jingtian.mtdemo.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.bean.FunctionBean
import kotlin.math.ceil

class RcHomeFunctionAdapter(
    private val functionList: ArrayList<FunctionBean>,
    private val spanCount: Int
) : RecyclerView.Adapter<RcHomeFunctionAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemNaviIcon: TextView = view.findViewById(R.id.item_navi_icon)
        var itemNaviTitle: TextView = view.findViewById(R.id.item_navi_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mView: View =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_navi,
                    parent,
                    false
                )
//                view.setPadding(10,10,10,10)
        mView.layoutParams.width =
            (BaseApplication.utils.getScreenWidth()) / ceil(functionList.size.toFloat() / spanCount).toInt()
        mView.layoutParams.height = (mView.layoutParams.width)
        return ViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = functionList[position]
        BaseApplication.utils.setFont(holder.itemNaviIcon)
        holder.apply {
            itemNaviTitle.text = item.title
            itemNaviIcon.setText(item.icon)
            itemNaviIcon.textSize = 36f
            itemNaviIcon.setTextColor(Color.parseColor(item.color))
        }
    }

    override fun getItemCount(): Int = functionList.size
}