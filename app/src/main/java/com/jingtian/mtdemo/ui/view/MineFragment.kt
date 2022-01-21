package com.jingtian.mtdemo.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.view.BaseFragment
import com.jingtian.mtdemo.bean.MineBean1
import com.jingtian.mtdemo.bean.MineBean2
import com.jingtian.mtdemo.databinding.FragmentHomeBinding
import com.jingtian.mtdemo.databinding.FragmentMineBinding
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
        binding.tvMineId.text =
            if (BaseApplication.utilsHolder.sp.login) BaseApplication.utilsHolder.sp.phone
            else "暂未登录"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //login()
        view.let {
            BaseApplication.utilsHolder.utils.setFont(binding.tvMineSettings)
            BaseApplication.utilsHolder.utils.setFont(binding.tvMineNotification)
            binding.tvMineSettings.setText(R.string.settings)
            binding.tvMineNotification.setText(R.string.notification)
            binding.llMineFirst.layoutParams =
                (binding.llMineFirst.layoutParams as LinearLayout.LayoutParams).apply {
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
            binding.rvMine1.apply {
                layoutManager = GridLayoutManager(it.context, 1).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
                adapter = MineAdapter(
                    assets,
                    BaseApplication.utilsHolder.utils.getScreenWidth() - BaseApplication.utilsHolder.utils.dip2px(30f)
                )
            }
        }

    }

    override fun provideMyWalletData(wallet: ArrayList<MineBean1>) {
        view?.let {
            binding.rvMine2.apply {
                layoutManager = GridLayoutManager(this@MineFragment.activity, 1).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
                adapter = MineAdapter(
                    wallet,
                    BaseApplication.utilsHolder.utils.getScreenWidth() - BaseApplication.utilsHolder.utils.dip2px(30f)
                )
            }
        }
    }

    override fun provideMyFunctionData(function: ArrayList<MineBean2>) {
        view?.let {
            binding.rvMine3.apply {
                layoutManager = GridLayoutManager(it.context, 2).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
                adapter = MineAdapter1(
                    function,
                    BaseApplication.utilsHolder.utils.getScreenWidth() - BaseApplication.utilsHolder.utils.dip2px(30f)
                )
            }
        }
    }
    private var _binding: FragmentMineBinding? = null
    private val binding get() = _binding!!
    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?,): View? {
        _binding = FragmentMineBinding.inflate(inflater, container,false)
        return _binding?.root
    }
    override fun unViewBinding() {
        _binding = null
    }

}