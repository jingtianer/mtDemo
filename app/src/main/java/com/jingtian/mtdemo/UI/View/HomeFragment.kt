package com.jingtian.mtdemo.UI.View

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jingtian.mtdemo.Base.Interface.BaseInterface
import com.jingtian.mtdemo.Base.Presenter.BasePresenter
import com.jingtian.mtdemo.Base.View.BaseActivity
import com.jingtian.mtdemo.Base.View.BaseFragment
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.Utils.SetFont
import com.jingtian.mtdemo.Utils.SetFont.Companion.getScreenWidth
import kotlin.math.abs
import kotlin.random.Random

class HomeFragment: BaseFragment<BaseInterface.presenter>() {
    override fun getPresenter(): BaseInterface.presenter {
        return BasePresenter<HomeFragment>()
    }
    data class function_bean(val icon:Int, val title:String, val color:String) {
    }

    class viewHolder(view:View) :RecyclerView.ViewHolder(view) {
        var item_navi_icon:TextView
        var item_navi_title:TextView
        init {
            item_navi_icon = view.findViewById(R.id.item_navi_icon)
            item_navi_title = view.findViewById(R.id.item_navi_title)
        }
    }
    var callback:touch_event? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ll_home_root = activity!!.findViewById<LinearLayout>(R.id.home_search_bar)
        ll_home_root.layoutParams =
            (ll_home_root.layoutParams as LinearLayout.LayoutParams).apply {
                setMargins(leftMargin,getStatusBarHeight() + 45,rightMargin,bottomMargin)
            }
        val tv_search_bar = activity!!.findViewById<TextView>(R.id.tv_search_bar)
        SetFont.setFont(tv_search_bar,activity!!)
        //vf_main.startFlipping()
        val rc_home_function = activity!!.findViewById<RecyclerView>(R.id.rc_home_function)
        val spanCount = 5
        rc_home_function.layoutManager = GridLayoutManager(context, spanCount).apply {
            orientation = GridLayoutManager.VERTICAL
        }
        val arr = arrayListOf(
            function_bean(R.string.daily,"日常", "#3333ff"),
            function_bean(R.string.ticket,"电影票", "#cc9900"),
            function_bean(R.string.show,"展览", "#33cccc"),
            function_bean(R.string.star,"收藏", "#66ff66"),
            function_bean(R.string.edit,"编辑", "#ff6699"),
            function_bean(R.string.scan,"扫码", "#0099cc"),
            function_bean(R.string.cart,"购物", "#cc99ff"),
            function_bean(R.string.position,"位置", "#ff0000")
        )
        rc_home_function.adapter = object : RecyclerView.Adapter<viewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
                val view: View =
                    LayoutInflater.from(parent.context)
                        .inflate(
                            R.layout.item_navi,
                            parent,
                            false
                        )
//                view.setPadding(10,10,10,10)
                view.layoutParams.width = (getScreenWidth(this@HomeFragment.activity!!)) / spanCount
                view.layoutParams.height = (view.layoutParams.width).toInt()
                return viewHolder(view)
            }

            override fun onBindViewHolder(holder: viewHolder, position: Int) {
                val item = arr[position]
                SetFont.setFont(holder.item_navi_icon, holder.item_navi_title.context)
                holder.apply {
                    item_navi_title.text = item.title
                    item_navi_icon.setText(item.icon)
                    item_navi_icon.textSize = 36f
                    item_navi_icon.setTextColor(Color.parseColor(item.color))
                }
            }

            override fun getItemCount(): Int = arr.size


        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val vf_main = activity!!.findViewById<ViewFlipper>(R.id.vf_main)
        val img_res = arrayListOf(
            R.drawable.main_vf_1,
            R.drawable.main_vf_2,
            R.drawable.main_vf_3,
            R.drawable.main_vf_4,
            R.drawable.main_vf_5)
        val commodityRes = arrayListOf(
            R.drawable.commodities1,
            R.drawable.commodities2,
            R.drawable.commodities3,
            R.drawable.commodities4,
            R.drawable.commodities5,
            R.drawable.commodities6,
            R.drawable.commodities7,
            R.drawable.commodities8,
            R.drawable.commodities9,
            R.drawable.commodities10,
            R.drawable.commodities11,
            R.drawable.commodities12,
            R.drawable.commodities13,
            R.drawable.commodities14,
            R.drawable.commodities15,
            R.drawable.commodities16,
            R.drawable.commodities17,
        )
        for (img in img_res) {
            vf_main.addView(ImageView(context).apply {
                val bitmap = BitmapFactory.decodeResource(resources, img)
                val distW = SetFont.getScreenWidth(activity!!)*0.95
                val distH = bitmap.height * (1.0*distW/bitmap.width)
                background = Bitmap.createScaledBitmap(bitmap, distW.toInt(), distH.toInt(),false).toDrawable(resources)
            })
        }
        vf_main.startFlipping()
        callback = (activity!! as MainActivity).getCallback()
        val gestureDetector = GestureDetector(context, PF_TouchListenr(vf_main, context))
        callback?.register_listener(gestureDetector)
        val rv_commodity = activity!!.findViewById<RecyclerView>(R.id.rv_commodity)
        rv_commodity.apply {
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL).apply {

            }
            adapter = RvCommodityAdapter(activity!!, commodityRes)
        }
    }
    class RvCommodityAdapter(val activity: Activity, val commodityRes:ArrayList<Int>): RecyclerView.Adapter<RvCommodityAdapter.ViewHolder>() {
        class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
            val iv_commodity:ImageView
            val tv_item_commodity:TextView
            val tv_item_commodity_price:TextView
            init {
                iv_commodity = view.findViewById(R.id.iv_commodity)
                tv_item_commodity = view.findViewById(R.id.tv_item_commodity)
                tv_item_commodity_price=view.findViewById(R.id.tv_item_commodity_price)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_commodity,parent,false).apply {
            })
        }
        val random = Random(System.currentTimeMillis())
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val n = 1 + abs(random.nextInt() % 10)
            var discription = ""
            for (i in 0..n) {
                discription += "描述！"
            }
            holder.apply {
                iv_commodity.setImageResource(commodityRes[position])
                tv_item_commodity.text = "商品${position}" + discription
                tv_item_commodity_price.text = "＄${100+abs(random.nextInt() % 1000)}"
            }
        }

        override fun getItemCount(): Int = commodityRes.size
    }
    override fun onDestroy() {
        super.onDestroy()
        callback?.unregister_listener()
    }
    class PF_TouchListenr(val vf_main:ViewFlipper, val context:Context?): GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val MIN_MOVE = 200
            if ((e1 == null) or (e2 == null)) return true
            if(e1!!.getX() - e2!!.getX() > MIN_MOVE){
                vf_main.setInAnimation(context,R.anim.horizontal_in)
                vf_main.setOutAnimation(context, R.anim.horizontal_out)
                vf_main.showNext()
            }else if(e2.getX() - e1.getX() > MIN_MOVE){
                vf_main.setInAnimation(context,R.anim.horizontal_in)
                vf_main.setOutAnimation(context, R.anim.horizontal_out)
                vf_main.showPrevious()
            }
            return true
        }
    }
    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    interface touch_event {
        fun register_listener(listener:GestureDetector)
        fun unregister_listener()
    }
}