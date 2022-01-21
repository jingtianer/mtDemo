package com.jingtian.mtdemo.ui.presenter

import android.util.Log
import com.jingtian.mtdemo.BuildConfig
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
//            BaseApplication.utilsHolder.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities2,
//            BaseApplication.utilsHolder.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities3,
//            BaseApplication.utilsHolder.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities4,
//            BaseApplication.utilsHolder.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities5,
//            BaseApplication.utilsHolder.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities6,
//            BaseApplication.utilsHolder.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities7,
//            BaseApplication.utilsHolder.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities8,
//            BaseApplication.utilsHolder.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities9,
//            BaseApplication.utilsHolder.utils.getPrice(),
//            false
//        ),
//        CartBean(
//            R.mipmap.commodities10,
//            BaseApplication.utilsHolder.utils.getPrice(),
//            false
//        )
//    )
    companion object {
        private val cartBuffer = ConcurrentLinkedDeque<CartBean>()
        val data = arrayListOf<CartBean>()
        fun add2Cart(sortBean: SortBean): Boolean {
            if (BuildConfig.DEBUG) {
                Log.d("add:", "$sortBean")
            }
            var f = cartBuffer.find {
                it.pic == sortBean.pic
            }
            if (f == null) {
                f = data.find {
                    it.pic == sortBean.pic
                }
            }
            f?.let {
                f.n++
                return true
            }
            SortBean2CartBuilder().setSortBean(sortBean).build()?.let {
                it.n++
                cartBuffer.add(it)
                return true
            }
            return false
        }
    }


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