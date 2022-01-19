package com.jingtian.mtdemo.ui.view

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.view.BaseFragment
import com.jingtian.mtdemo.bean.MineBean1
import com.jingtian.mtdemo.bean.MineBean2
import com.jingtian.mtdemo.ui.adapters.MineAdapter
import com.jingtian.mtdemo.ui.adapters.MineAdapter1
import com.jingtian.mtdemo.ui.interfaces.MineInterface
import com.jingtian.mtdemo.ui.presenter.MinePresenter

class MineFragment : BaseFragment<MinePresenter>(), MineInterface.View {


    override fun getPresenter(): MinePresenter {
        return MinePresenter()
    }

    override fun onResume() {
        super.onResume()
        view?.findViewById<TextView>(R.id.tv_mine_id)?.text =
            if (BaseApplication.sp.login) BaseApplication.sp.phone
            else "暂未登录"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //login()
        view.let {
            val tvMineSettings = it.findViewById<TextView>(R.id.tv_mine_settings)
            val tvMineNotification = it.findViewById<TextView>(R.id.tv_mine_notification)
            BaseApplication.utils.setFont(tvMineSettings)
            BaseApplication.utils.setFont(tvMineNotification)
            tvMineSettings.setText(R.string.settings)
            tvMineNotification.setText(R.string.notification)
            val llMineFirst = it.findViewById<LinearLayout>(R.id.ll_mine_first)
            llMineFirst.layoutParams =
                (llMineFirst.layoutParams as LinearLayout.LayoutParams).apply {
                    setMargins(
                        leftMargin,
                        getStatusBarHeight() + topMargin,
                        rightMargin,
                        bottomMargin
                    )
                }
        }

        mPresenter?.requestMyAssetData()
        mPresenter?.requestMyWalletData()
        mPresenter?.requestMyFunctionData()
    }


    override fun getLayout(): Int {
        return R.layout.fragment_mine
    }

    override fun provideMyAssetData(assets: ArrayList<MineBean1>) {
        view?.let {
            it.findViewById<RecyclerView>(R.id.rv_mine_1).apply {
                layoutManager = GridLayoutManager(it.context, 1).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
                adapter = MineAdapter(
                    assets,
                    it.context,
                    BaseApplication.utils.getScreenWidth() - BaseApplication.utils.dip2px(30f)
                )
            }
        }

    }

    override fun provideMyWalletData(wallet: ArrayList<MineBean1>) {
        view?.let {
            it.findViewById<RecyclerView>(R.id.rv_mine_2).apply {
                layoutManager = GridLayoutManager(this@MineFragment.activity, 1).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
                adapter = MineAdapter(
                    wallet,
                    it.context,
                    BaseApplication.utils.getScreenWidth() - BaseApplication.utils.dip2px(30f)
                )
            }
        }
    }

    override fun provideMyFunctionData(function: ArrayList<MineBean2>) {
        view?.let {
            it.findViewById<RecyclerView>(R.id.rv_mine_3).apply {
                layoutManager = GridLayoutManager(it.context, 2).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
                adapter = MineAdapter1(
                    function,
                    it.context,
                    BaseApplication.utils.getScreenWidth() - BaseApplication.utils.dip2px(30f)
                )
            }
        }
    }


}