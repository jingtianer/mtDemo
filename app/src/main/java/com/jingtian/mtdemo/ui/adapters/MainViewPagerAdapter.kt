package com.jingtian.mtdemo.ui.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jingtian.mtdemo.BuildConfig
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.bean.NaviBean
import com.jingtian.mtdemo.ui.view.CartFragment
import com.jingtian.mtdemo.ui.view.HomeFragment
import com.jingtian.mtdemo.ui.view.MineFragment
import com.jingtian.mtdemo.ui.view.SortFragment

class MainViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val ids: List<NaviBean>
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragments: MutableMap<Int, Fragment> = mutableMapOf()
    private fun getInstance(i: Int): Fragment {
        if (!fragments.containsKey(i)) {
            if (BuildConfig.DEBUG) {
                Log.d("create fragment", "$i")
            }
            //fragments[i] = Class.forName(naviArr[i].class_name).newInstance() as Fragment
            fragments[i] = when (i) {
                R.layout.fragment_home -> {
                    HomeFragment()
                }
                R.layout.fragment_sort -> {
                    SortFragment()
                }
                R.layout.fragment_cart -> {
                    CartFragment()
                }
                R.layout.fragment_mine -> {
                    MineFragment()
                }
                else -> {
                    HomeFragment()
                }
            }
        }
        return fragments[i]!!
    }

    override fun getItemCount(): Int = ids.size

    override fun getItemId(position: Int): Long {
        return ids[position].id.toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragments.containsKey(itemId.toInt())

    }

    override fun createFragment(position: Int): Fragment {
        return getInstance(ids[position].id)
    }
}