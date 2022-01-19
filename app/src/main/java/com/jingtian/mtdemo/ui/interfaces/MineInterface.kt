package com.jingtian.mtdemo.ui.interfaces

import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.bean.MineBean1
import com.jingtian.mtdemo.bean.MineBean2

interface MineInterface : BaseInterface {
    interface View : BaseInterface.View {
        fun provideMyAssetData(assets: ArrayList<MineBean1>)
        fun provideMyWalletData(wallet: ArrayList<MineBean1>)
        fun provideMyFunctionData(function: ArrayList<MineBean2>)
    }

    interface Presenter : BaseInterface.Presenter {
        fun requestMyAssetData()
        fun requestMyWalletData()
        fun requestMyFunctionData()
    }
}