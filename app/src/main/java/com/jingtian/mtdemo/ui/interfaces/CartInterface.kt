package com.jingtian.mtdemo.ui.interfaces

import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.bean.CartBean

class CartInterface : BaseInterface {
    interface View : BaseInterface.View {
        fun provideCartData(cartData: ArrayList<CartBean>)
        fun provideGuessData(commodityRes: ArrayList<Int>)
    }

    interface Presenter : BaseInterface.Presenter {
        fun requestCartData()
        fun requestGuessData()
    }
}