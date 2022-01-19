package com.jingtian.mtdemo.ui.presenter

import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.bean.FunctionBean
import com.jingtian.mtdemo.ui.adapters.RvCommodityAdapter
import com.jingtian.mtdemo.ui.interfaces.HomeInterface

class HomePresenter : BasePresenter<HomeInterface.View>(), HomeInterface.Presenter {
    private val functionList = arrayListOf(
        FunctionBean(R.string.daily, "日常", "#3333ff"),
        FunctionBean(R.string.ticket, "电影票", "#cc9900"),
        FunctionBean(R.string.show, "展览", "#33cccc"),
        FunctionBean(R.string.star, "收藏", "#66ff66"),
        FunctionBean(R.string.edit, "编辑", "#ff6699"),
        FunctionBean(R.string.scan, "扫码", "#0099cc"),
        FunctionBean(R.string.cart, "购物", "#cc99ff"),
        FunctionBean(R.string.position, "位置", "#ff0000")
    )
    private val imgRes = arrayListOf(
        (R.mipmap.main_vf_1),
        (R.mipmap.main_vf_2),
        (R.mipmap.main_vf_3),
        (R.mipmap.main_vf_4),
        (R.mipmap.main_vf_5)
    )

    override fun requestFunctionData() {
        mView?.provideFunctionData(functionList)
    }

    override fun requestImageData() {
        mView?.provideImageData(imgRes)
    }

    override fun requestCommodityData() {
        mView?.provideCommodityData(RvCommodityAdapter.res)
    }
}