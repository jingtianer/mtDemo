package com.jingtian.mtdemo.ui.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.base.view.BaseFragment
import kotlin.math.log

class MineFragment: BaseFragment<BaseInterface.Presenter>() {
    override fun getPresenter(): BaseInterface.Presenter {
        return BasePresenter<HomeFragment>()
    }

    override fun onResume() {
        super.onResume()
        view?.findViewById<TextView>(R.id.tv_mine_id)?.text =
            if (BaseApplication.sp.login)  BaseApplication.sp.phone
            else "暂未登录"

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login()

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

            it.findViewById<RecyclerView>(R.id.rv_mine_1).apply {
                layoutManager = GridLayoutManager(it.context, 1).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
                adapter = RvAdapter(arr1,
                    it.context,
                    BaseApplication.utils.getScreenWidth() - BaseApplication.utils.dip2px( 30f))
            }
        }



        view.let {
            it.findViewById<RecyclerView>(R.id.rv_mine_2).apply {
                layoutManager = GridLayoutManager(this@MineFragment.activity, 1).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
                adapter = RvAdapter(
                    arr2,
                    it.context,
                    BaseApplication.utils.getScreenWidth() - BaseApplication.utils.dip2px(30f)
                )
            }

            it.findViewById<RecyclerView>(R.id.rv_mine_3).apply {
                layoutManager = GridLayoutManager(it.context, 2).apply {
                    orientation = GridLayoutManager.HORIZONTAL
                }
                adapter = RvAdapter1(
                    arr3,
                    it.context,
                    BaseApplication.utils.getScreenWidth() - BaseApplication.utils.dip2px(30f)
                )
            }
        }

    }

    data class RvBean(
        val is_icon: Boolean,
        val text1: SpannableString,
        val text2: SpannableString,
        val text3: SpannableString
    )

    data class RvBeanData(val data1: String, val data2: String, val data3: String?)
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvItemMine1: TextView = view.findViewById(R.id.tv_item_mine1)
        val tvItemMine2: TextView = view.findViewById(R.id.tv_item_mine2)
        val tvItemMine3: TextView = view.findViewById(R.id.tv_item_mine3)
    }

    class RvAdapter1(
        private val data: ArrayList<RvBeanData>,
        private val context: Context,
        private val mWidth: Int
    ) : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_mine, parent, false).apply {
                    layoutParams.width = mWidth / data.size * 2
                })
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.apply {
                BaseApplication.utils.setFont(tvItemMine1)
                tvItemMine1.text = SpannableString(data[position].data1).apply {
                    setSpan(AbsoluteSizeSpan(32, true), 0, 1, 0)
                    setSpan(
                        ForegroundColorSpan(
                            BaseApplication.utils.getColor(
                                R.color.orange_secondary
                            )
                        ), 0, 1, 0
                    )
                }
                tvItemMine2.text = data[position].data2
                data[position].data3.let {
                    tvItemMine3.text = it
                }
            }
        }

        override fun getItemCount(): Int = data.size
    }

    class RvAdapter(
        private val data: ArrayList<RvBean>,
        private val context: Context,
        private val mWidth: Int
    ) : RecyclerView.Adapter<ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_mine, parent, false).apply {
                    layoutParams.width = mWidth / data.size
                })
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.apply {
                if (data[position].is_icon) {
                    BaseApplication.utils.setFont(tvItemMine1)
                }
                tvItemMine1.text = data[position].text1
                tvItemMine2.text = data[position].text2
                data[position].text3.let {
                    tvItemMine3.text = it
                }
            }
        }

        override fun getItemCount(): Int = data.size

    }
    override fun getLayout(): Int {
        return R.layout.fragment_mine
    }


}