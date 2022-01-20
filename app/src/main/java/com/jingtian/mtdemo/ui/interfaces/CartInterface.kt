package com.jingtian.mtdemo.ui.interfaces

import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.bean.CartBean

class CartInterface : BaseInterface {
    interface View : BaseInterface.View {
        fun provideCartData(cartData: ArrayList<CartBean>)
        fun provideGuessData(commodityRes: ArrayList<Int>)
        fun provideCartUpdate(cartData: ArrayList<CartBean>)
    }

    interface Presenter : BaseInterface.Presenter {
        fun requestCartData()
        fun requestGuessData()
        fun requestUpdateCart()
    }
}