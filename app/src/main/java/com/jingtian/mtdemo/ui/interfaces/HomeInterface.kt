package com.jingtian.mtdemo.ui.interfaces

import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.bean.FunctionBean

interface HomeInterface : BaseInterface {
    interface View : BaseInterface.View {
        fun provideFunctionData(functionList: ArrayList<FunctionBean>)
        fun provideImageData(imgRes: ArrayList<Int>)
        fun provideCommodityData(commodities: ArrayList<Int>)
    }

    interface Presenter : BaseInterface.Presenter {
        fun requestFunctionData()
        fun requestImageData()
        fun requestCommodityData()
    }
}