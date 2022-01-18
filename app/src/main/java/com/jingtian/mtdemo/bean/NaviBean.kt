package com.jingtian.mtdemo.bean

import com.jingtian.mtdemo.ui.view.MainActivity

data class NaviBean(val icon: Int, val title: String, val id: Int) {
    var mHolder: MainActivity.NaviAdapter.ViewHolder? = null
}