package com.jingtian.mtdemo.ui.interfaces

import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.bean.SortBean

interface SortInterface : BaseInterface {
    interface View : BaseInterface.View {
        fun provideRvData(i: Int, j: Long, data: ArrayList<SortBean>)
        fun add2CartSuccess()
        fun add2CartFail()
    }

    interface Presenter : BaseInterface.Presenter {
        fun requestRvData(i: Int, j: Long)
        fun add2Cart(sortBean: SortBean)
    }
}