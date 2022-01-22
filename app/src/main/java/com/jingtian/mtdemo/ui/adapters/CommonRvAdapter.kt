package com.jingtian.mtdemo.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class CommonRvAdapter<E:Any, V: ViewBinding>: RecyclerView.Adapter<CommonRvAdapter.CommonRvHolder<V>>() {
    open class CommonRvHolder<V:ViewBinding>(val binding: V) :RecyclerView.ViewHolder(binding.root)
    open var data:ArrayList<E> = arrayListOf()
        set(value) {
            field = value
            notifyItemRangeChanged(0, field.size)
        }
    fun insertItem(item:E) {
        data.add(item)
        notifyItemInserted(data.size - 1)
    }
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonRvHolder<V>

    abstract override fun onBindViewHolder(holder: CommonRvHolder<V>, position: Int)

    override fun getItemCount(): Int = data.size
}