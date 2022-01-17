package com.jingtian.mtdemo.UI.View

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.mtdemo.Base.BaseApplication
import com.jingtian.mtdemo.Base.Interface.BaseInterface
import com.jingtian.mtdemo.Base.Presenter.BasePresenter
import com.jingtian.mtdemo.Base.View.BaseFragment
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.Utils.SetFont

class MineFragment: BaseFragment<BaseInterface.presenter>() {
    override fun getPresenter(): BaseInterface.presenter {
        return BasePresenter<HomeFragment>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tv_mine_id  = activity!!.findViewById<TextView>(R.id.tv_mine_id)
        tv_mine_id.text = BaseApplication.sp.phone
        val tv_mine_settings = activity!!.findViewById<TextView>(R.id.tv_mine_settings)
        val tv_mine_notification = activity!!.findViewById<TextView>(R.id.tv_mine_notification)
        SetFont.setFont(tv_mine_settings,activity!!)
        SetFont.setFont(tv_mine_notification,activity!!)
        tv_mine_settings.setText(R.string.settings)
        tv_mine_notification.setText(R.string.notification)
        val ll_mine_first = activity!!.findViewById<LinearLayout>(R.id.ll_mine_first)
        ll_mine_first.layoutParams = (ll_mine_first.layoutParams as LinearLayout.LayoutParams).apply {
            setMargins(leftMargin, getStatusBarHeight()+topMargin,rightMargin,bottomMargin)
        }

        activity!!.findViewById<RecyclerView>(R.id.rv_mine_1).apply {
            layoutManager = GridLayoutManager(this@MineFragment.activity, 1).apply {
                orientation = GridLayoutManager.HORIZONTAL
            }
            adapter = RvAdapter(arrayListOf(
                RvBean(
                    true,
                    SpannableString(resources.getString(R.string.red_bao)).apply {
                        setSpan(AbsoluteSizeSpan(32,true),0,1,0)
                        setSpan(ForegroundColorSpan(Color.RED), 0,1,0)
                    },
                    SpannableString("红包"),
                    SpannableString("0个未使用").apply {
                        setSpan(AbsoluteSizeSpan(24,true), 0,1,0)
                        setSpan(ForegroundColorSpan(resources.getColor(R.color.orange_secondary1, null)), 0,1,0)
                    }
                ),
                RvBean(
                    true,
                    SpannableString(resources.getString(R.string.coupon)).apply {
                        setSpan(AbsoluteSizeSpan(32,true),0,1,0)
                        setSpan(ForegroundColorSpan(Color.BLUE), 0,1,0)
                    },
                    SpannableString("津贴"),
                    SpannableString("5个未使用").apply {
                        setSpan(AbsoluteSizeSpan(24,true), 0,1,0)
                        setSpan(ForegroundColorSpan(resources.getColor(R.color.orange_secondary1, null)), 0,1,0)
                    }
                ),
                RvBean(
                    true,
                    SpannableString(resources.getString(R.string.allowance)).apply {
                        setSpan(AbsoluteSizeSpan(32,true),0,1,0)
                        setSpan(ForegroundColorSpan(resources.getColor(R.color.orange, null)), 0,1,0)
                    },
                    SpannableString("代金券"),
                    SpannableString("18个未使用").apply {
                        setSpan(AbsoluteSizeSpan(24,true), 0,2,0)
                        setSpan(ForegroundColorSpan(resources.getColor(R.color.orange_secondary1, null)), 0,2,0)
                    }
                )
            ),activity!!, SetFont.getScreenWidth(activity!!) - SetFont.dip2px(activity!!, 30f))
        }


        var arr = arrayListOf<RvBean>()

        var data = arrayListOf(
            RvBeanData("4000","借钱","最高12期免息"),
            RvBeanData("28元","买单","免费领红包"),
            RvBeanData("165元","美食享优惠","随机最高减")
        )
        for (i in data) {
            arr.add(
                RvBean(false,
                    SpannableString(i.data1).apply {
                        setSpan(AbsoluteSizeSpan(20,true),0,i.data1.length,0)
                        setSpan(ForegroundColorSpan(Color.BLACK), 0,i.data1.length,0)
                    },
                    SpannableString(i.data2).apply {
                        setSpan(AbsoluteSizeSpan(16,true),0,i.data2.length,0)
                        setSpan(ForegroundColorSpan(Color.BLACK), 0,i.data2.length,0)
                    },
                    SpannableString(i.data3).apply {
                        setSpan(AbsoluteSizeSpan(12,true), 0,i.data3!!.length,0)
                    })
            )
        }
        activity!!.findViewById<RecyclerView>(R.id.rv_mine_2).apply {
            layoutManager = GridLayoutManager(this@MineFragment.activity, 1).apply {
                orientation = GridLayoutManager.HORIZONTAL
            }
            adapter = RvAdapter(arr ,activity!!, SetFont.getScreenWidth(activity!!) - SetFont.dip2px(activity!!, 30f))
        }

        activity!!.findViewById<RecyclerView>(R.id.rv_mine_3).apply {
            layoutManager = GridLayoutManager(this@MineFragment.activity, 2).apply {
                orientation = GridLayoutManager.HORIZONTAL
            }
            adapter = RvAdapter1(arrayListOf(
                RvBeanData(resources.getString(R.string.my_local),"我的位置",null),
                RvBeanData(resources.getString(R.string.foot_print),"我的足迹",null),
                RvBeanData(resources.getString(R.string.my_likes),"我的收藏",null),
                RvBeanData(resources.getString(R.string.acknowledge_log),"答谢记录",null),
                RvBeanData(resources.getString(R.string.comments),"我的评论",null),
                RvBeanData(resources.getString(R.string.my_assistant),"我的客服",null),
                RvBeanData(resources.getString(R.string.commercial),"理财",null),
                RvBeanData(resources.getString(R.string.more),"更多",null),
            ), activity!!, SetFont.getScreenWidth(activity!!) - SetFont.dip2px(activity!!, 30f))
        }
    }
    data class RvBean(val is_icon:Boolean, val text1:SpannableString,val text2:SpannableString,val text3:SpannableString)
    data class RvBeanData(val data1:String, val data2:String, val data3:String?)
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val tv_item_mine1:TextView = view.findViewById(R.id.tv_item_mine1)
        val tv_item_mine2:TextView = view.findViewById(R.id.tv_item_mine2)
        val tv_item_mine3:TextView = view.findViewById(R.id.tv_item_mine3)
    }
    class RvAdapter1(val data:ArrayList<RvBeanData>, val activity:Activity, val mWidth:Int):RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_mine,parent,false).apply {
                layoutParams.width = mWidth / data.size * 2
            })
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.apply {
                SetFont.setFont(tv_item_mine1,activity)
                tv_item_mine1.text = SpannableString(data[position].data1).apply {
                    setSpan(AbsoluteSizeSpan(32,true),0,1,0)
                    setSpan(ForegroundColorSpan(activity!!.resources.getColor(R.color.orange_secondary, null)), 0,1,0)
                }
                tv_item_mine2.text = data[position].data2
                data[position].data3.let {
                    tv_item_mine3.text = it
                }
            }
        }

        override fun getItemCount(): Int = data.size
    }
    class RvAdapter(val data:ArrayList<RvBean>, val activity:Activity, val mWidth:Int):RecyclerView.Adapter<ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_mine,parent,false).apply {
                layoutParams.width = mWidth / data.size
            })
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.apply {
                if (data[position].is_icon) {
                    SetFont.setFont(tv_item_mine1, activity)
                }
                tv_item_mine1.text = data[position].text1
                tv_item_mine2.text = data[position].text2
                data[position].text3.let {
                    tv_item_mine3.text = it
                }
            }
        }

        override fun getItemCount(): Int = data.size

    }
    override fun getLayout(): Int {
        return R.layout.fragment_mine
    }
}