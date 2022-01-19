package com.jingtian.mtdemo.ui.presenter

import android.graphics.Color
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.bean.MineBean1
import com.jingtian.mtdemo.bean.MineBean2
import com.jingtian.mtdemo.ui.interfaces.MineInterface

class MinePresenter : BasePresenter<MineInterface.View>(), MineInterface.Presenter {
    val function = arrayListOf(
        MineBean2(BaseApplication.utils.getString(R.string.my_local), "我的位置", null),
        MineBean2(BaseApplication.utils.getString(R.string.foot_print), "我的足迹", null),
        MineBean2(BaseApplication.utils.getString(R.string.my_likes), "我的收藏", null),
        MineBean2(BaseApplication.utils.getString(R.string.acknowledge_log), "答谢记录", null),
        MineBean2(BaseApplication.utils.getString(R.string.comments), "我的评论", null),
        MineBean2(BaseApplication.utils.getString(R.string.my_assistant), "我的客服", null),
        MineBean2(BaseApplication.utils.getString(R.string.commercial), "理财", null),
        MineBean2(BaseApplication.utils.getString(R.string.more), "更多", null),
    )
    private val wallet = arrayListOf<MineBean1>()

    init {
        val data2 = arrayListOf(
            MineBean2("4000", "借钱", "最高12期免息"),
            MineBean2("28元", "买单", "免费领红包"),
            MineBean2("165元", "美食享优惠", "随机最高减")
        )
        for (i in data2) {
            wallet.add(
                MineBean1(false,
                    SpannableString(i.data1).apply {
                        setSpan(AbsoluteSizeSpan(20, true), 0, i.data1.length, 0)
                        setSpan(ForegroundColorSpan(Color.BLACK), 0, i.data1.length, 0)
                    },
                    SpannableString(i.data2).apply {
                        setSpan(AbsoluteSizeSpan(16, true), 0, i.data2.length, 0)
                        setSpan(ForegroundColorSpan(Color.BLACK), 0, i.data2.length, 0)
                    },
                    SpannableString(i.data3).apply {
                        setSpan(AbsoluteSizeSpan(12, true), 0, i.data3!!.length, 0)
                    })
            )
        }
    }

    private val assets = arrayListOf(
        MineBean1(
            true,
            SpannableString(BaseApplication.utils.getString(R.string.red_bao)).apply {
                setSpan(AbsoluteSizeSpan(32, true), 0, 1, 0)
                setSpan(ForegroundColorSpan(Color.RED), 0, 1, 0)
            },
            SpannableString("红包"),
            SpannableString("0个未使用").apply {
                setSpan(AbsoluteSizeSpan(24, true), 0, 1, 0)
                setSpan(
                    ForegroundColorSpan(
                        BaseApplication.utils.getColor(
                            R.color.orange_secondary1
                        )
                    ), 0, 1, 0
                )
            }
        ),
        MineBean1(
            true,
            SpannableString(BaseApplication.utils.getString(R.string.coupon)).apply {
                setSpan(AbsoluteSizeSpan(32, true), 0, 1, 0)
                setSpan(ForegroundColorSpan(Color.BLUE), 0, 1, 0)
            },
            SpannableString("津贴"),
            SpannableString("5个未使用").apply {
                setSpan(AbsoluteSizeSpan(24, true), 0, 1, 0)
                setSpan(
                    ForegroundColorSpan(
                        BaseApplication.utils.getColor(
                            R.color.orange_secondary1
                        )
                    ), 0, 1, 0
                )
            }
        ),
        MineBean1(
            true,
            SpannableString(BaseApplication.utils.getString(R.string.allowance)).apply {
                setSpan(AbsoluteSizeSpan(32, true), 0, 1, 0)
                setSpan(
                    ForegroundColorSpan(
                        BaseApplication.utils.getColor(
                            R.color.orange_secondary1
                        )
                    ), 0, 1, 0
                )
            },
            SpannableString("代金券"),
            SpannableString("18个未使用").apply {
                setSpan(AbsoluteSizeSpan(24, true), 0, 2, 0)
                setSpan(
                    ForegroundColorSpan(
                        BaseApplication.utils.getColor(
                            R.color.orange_secondary1
                        )
                    ), 0, 2, 0
                )
            }
        )
    )

    override fun requestMyAssetData() {
        mView?.provideMyAssetData(assets)
    }

    override fun requestMyWalletData() {
        mView?.provideMyWalletData(wallet)
    }

    override fun requestMyFunctionData() {
        mView?.provideMyFunctionData(function)
    }
}