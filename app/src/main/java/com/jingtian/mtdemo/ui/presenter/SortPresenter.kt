package com.jingtian.mtdemo.ui.presenter

import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.bean.CartBean
import com.jingtian.mtdemo.bean.SortBean
import com.jingtian.mtdemo.ui.interfaces.SortInterface

class SortPresenter : BasePresenter<SortInterface.View>(), SortInterface.Presenter {
    override fun requestRvData(i: Int, j: Long) {
        mView?.provideRvData(
            i, j, arrayListOf(
                SortBean(R.mipmap.commodities1, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities2, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities3, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities4, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities5, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities6, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities7, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities8, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities9, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities10, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities11, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities12, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities13, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities14, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities15, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities16, BaseApplication.utils.getPrice()),
                SortBean(R.mipmap.commodities17, BaseApplication.utils.getPrice())
        ))
    }

    override fun add2Cart(sortBean: SortBean) {
        if(CartPresenter.add2Cart(sortBean)) {
            mView?.add2CartSuccess()
        } else {
            mView?.add2CartFail()
        }
    }
}