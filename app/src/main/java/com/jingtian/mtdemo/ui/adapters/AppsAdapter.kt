package com.jingtian.mtdemo.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.databinding.ItemAppsBinding
import com.jingtian.mtdemo.ui.view.SearchActivity

class AppsAdapter(private val uninstallButton: SearchActivity.UninstallButton) :
    CommonRvAdapter<SearchActivity.AppBean, ItemAppsBinding>() {

    var userAppCount = 0
    var requestUninstallItem: SearchActivity.AppBean? = null
    fun deletionResult(isUninstalled: Boolean) {
        if (isUninstalled) {
            requestUninstallItem?.let {
                val position = data.indexOf(it)
                data.removeAt(position)
                notifyItemRemoved(position)
                requestUninstallItem = null
                userAppCount--
            }
        }
    }

    override fun insertItem(item: SearchActivity.AppBean) {
        if (item.type == SearchActivity.AppBean.Type.USER_APP) {
            data.add(userAppCount, item)
            notifyItemInserted(userAppCount)
            userAppCount++
        } else {
            data.add(item)
            notifyItemInserted(data.size - 1)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommonRvHolder<ItemAppsBinding> {
        return CommonRvHolder(
            ItemAppsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommonRvHolder<ItemAppsBinding>, position: Int) {
        holder.binding.apply {
            data[position].let { item ->
                tvAppName.text = item.label
                ivAppsIcon.background = item.icon
                tvAppPackageName.text = item.applicationInfo.name
                when (item.type) {
                    SearchActivity.AppBean.Type.SYSTEM_APP -> {
                        tvUninstallable.text = "不可卸載"
                        tvUninstallBt.visibility = View.GONE
                    }
                    SearchActivity.AppBean.Type.USER_APP -> {
                        tvUninstallable.visibility = View.GONE
                        BaseApplication.utilsHolder.utils.setFont(tvUninstallBt)
                        tvUninstallable.setTextColor(
                            BaseApplication.utilsHolder.utils.getColor(
                                R.color.orange
                            )
                        )
                    }
                    else -> {
                        tvUninstallable.text = "其他应用"
                        tvUninstallBt.visibility = View.GONE
                    }
                }
                tvUninstallBt.setOnClickListener {
                    uninstallButton.clickForResult(item.applicationInfo.packageName)
                    requestUninstallItem = item
                }
            }

        }
    }
}