package com.jingtian.mtdemo.ui.presenter

import android.util.Log
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.bean.CartBean
import com.jingtian.mtdemo.bean.SortBean
import com.jingtian.mtdemo.bean.SortBean2CartBuilder
import com.jingtian.mtdemo.ui.adapters.RvCommodityAdapter
import com.jingtian.mtdemo.ui.interfaces.CartInterface
import java.util.concurrent.ConcurrentLinkedDeque

class CartPresenter : BasePresenter<CartInterface.View>(), CartInterface.Presenter {
//    private val data = arrayListOf(
//        CartBean(
//            R.mipmap.commodities1,
//            BaseApplication.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities2,
//            BaseApplication.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities3,
//            BaseApplication.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities4,
//            BaseApplication.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities5,
//            BaseApplication.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities6,
//            BaseApplication.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities7,
//            BaseApplication.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities8,
//            BaseApplication.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities9,
//            BaseApplication.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities10,
//            BaseApplication.utils.getPrice(),
//            false
//        )
//    )
    companion object {
        private val  cartBuffer = ConcurrentLinkedDeque<CartBean>()
        fun add2Cart(sortBean: SortBean): Boolean{
            Log.d("add:", "$sortBean")
            SortBean2CartBuilder().setSortBean(sortBean).build()?.let {
                cartBuffer.add(it)
                return true
            }
            return false
        }
    }
    val data = arrayListOf<CartBean>()
    override fun requestCartData() {
        mView?.provideCartData(data)
    }

    override fun requestGuessData() {
        mView?.provideGuessData(RvCommodityAdapter.getRes())
    }

    override fun requestUpdateCart() {
        data.addAll(cartBuffer)
        mView?.provideCartUpdate(data)
        cartBuffer.clear()
    }
}