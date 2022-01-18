package com.jingtian.mtdemo.ui.view

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
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.base.view.BaseFragment
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.utils.SetFont
import com.jingtian.mtdemo.utils.SetFont.Companion.getScreenWidth
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.random.Random

class HomeFragment: BaseFragment<BaseInterface.Presenter>() {
    override fun getPresenter(): BaseInterface.Presenter {
        return BasePresenter<HomeFragment>()
    }
    data class FunctionBean(val icon:Int, val title:String, val color:String)

    class ViewHolder(view:View) :RecyclerView.ViewHolder(view) {
        var itemNaviIcon:TextView = view.findViewById(R.id.item_navi_icon)
        var itemNaviTitle:TextView = view.findViewById(R.id.item_navi_title)
    }
    private var callback:ViewFlipperTouchEvent? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            val homeSearchBar = it.findViewById<LinearLayout>(R.id.home_search_bar)
            homeSearchBar.layoutParams =
                (homeSearchBar.layoutParams as LinearLayout.LayoutParams).apply {
                    setMargins(leftMargin,getStatusBarHeight() + 45,rightMargin,bottomMargin)
                }
            val tvSearchBar = it.findViewById<TextView>(R.id.tv_search_bar)
            SetFont.setFont(tvSearchBar,it)
            //vfMain.startFlipping()
            val rcHomeFunction = it.findViewById<RecyclerView>(R.id.rc_home_function)
            val spanCount = 2
            rcHomeFunction.layoutManager = GridLayoutManager(context, spanCount).apply {
                orientation = GridLayoutManager.HORIZONTAL
            }
            val arr = arrayListOf(
                FunctionBean(R.string.daily,"日常", "#3333ff"),
                FunctionBean(R.string.ticket,"电影票", "#cc9900"),
                FunctionBean(R.string.show,"展览", "#33cccc"),
                FunctionBean(R.string.star,"收藏", "#66ff66"),
                FunctionBean(R.string.edit,"编辑", "#ff6699"),
                FunctionBean(R.string.scan,"扫码", "#0099cc"),
                FunctionBean(R.string.cart,"购物", "#cc99ff"),
                FunctionBean(R.string.position,"位置", "#ff0000")
            )
            rcHomeFunction.adapter = object : RecyclerView.Adapter<ViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                    val mView: View =
                        LayoutInflater.from(parent.context)
                            .inflate(
                                R.layout.item_navi,
                                parent,
                                false
                            )
//                view.setPadding(10,10,10,10)
                    mView.layoutParams.width = (getScreenWidth(it)) / ceil(arr.size.toFloat() / spanCount).toInt()
                    mView.layoutParams.height = (view.layoutParams.width)
                    return ViewHolder(mView)
                }

                override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                    val item = arr[position]
                    SetFont.setFont(holder.itemNaviIcon, holder.itemNaviTitle.context)
                    holder.apply {
                        itemNaviTitle.text = item.title
                        itemNaviIcon.setText(item.icon)
                        itemNaviIcon.textSize = 36f
                        itemNaviIcon.setTextColor(Color.parseColor(item.color))
                    }
                }
                override fun getItemCount(): Int = arr.size
            }
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.let {
            val vfMain = it.findViewById<ViewFlipper>(R.id.vf_main)
            // TODO: 图片都放drawable下了，drawable/drawable-xxhdpi等目录什么区别?
            val imgRes = arrayListOf(
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
            // TODO: 在onStart这个方法里进行View的初始化，是否会多次执行导致数据冗余等问题？
            for (img in imgRes) {
                vfMain.addView(ImageView(context).apply {
                    val bitmap = BitmapFactory.decodeResource(resources, img)
                    val distW = getScreenWidth(it)*0.95
                    val distH = bitmap.height * (1.0*distW/bitmap.width)
                    background = Bitmap.createScaledBitmap(bitmap, distW.toInt(), distH.toInt(),false).toDrawable(resources)
                })
            }
            vfMain.startFlipping()
            callback = (it as MainActivity).getCallback()
            val gestureDetector = GestureDetector(context, ViewFlipperTouchListener(vfMain, context))
            callback?.registerListener(gestureDetector)
            val rvCommodity = it.findViewById<RecyclerView>(R.id.rv_commodity)
            rvCommodity.apply {
                layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL).apply {

                }
                adapter = RvCommodityAdapter(it, commodityRes)
            }
        }

    }
    class RvCommodityAdapter(private val activity: Activity, private val commodityRes:ArrayList<Int>): RecyclerView.Adapter<RvCommodityAdapter.ViewHolder>() {
        class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
            val ivCommodity:ImageView = view.findViewById(R.id.iv_commodity)
            val tvItemCommodity:TextView = view.findViewById(R.id.tv_item_commodity)
            val tvItemCommodityPrice:TextView = view.findViewById(R.id.tv_item_commodity_price)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_commodity,parent,false).apply {
            })
        }
        private val random = Random(System.currentTimeMillis())
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val n = 1 + abs(random.nextInt() % 10)
            var discription = ""
            for (i in 0..n) {
                discription += "描述！"
            }
            holder.apply {
                ivCommodity.setImageResource(commodityRes[position])
                tvItemCommodity.text = activity.getString(R.string.tvItemCommodity, position+1, discription)
                tvItemCommodityPrice.text = activity.getString(R.string.tvItemCommodityPrice, 100+abs(random.nextInt() % 1000))
            }
        }

        override fun getItemCount(): Int = commodityRes.size
    }
    override fun onDestroy() {
        super.onDestroy()
        callback?.unRegisterListener()
    }
    class ViewFlipperTouchListener(private val vfMain:ViewFlipper, val context:Context?): GestureDetector.SimpleOnGestureListener() {
        private val minMove = 200
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if ((e1 == null) or (e2 == null)) return true
            if(e1!!.x - e2!!.x > minMove){
                vfMain.setInAnimation(context,R.anim.horizontal_in)
                vfMain.setOutAnimation(context, R.anim.horizontal_out)
                vfMain.showNext()
            }else if(e2.x - e1.x > minMove){
                vfMain.setInAnimation(context,R.anim.horizontal_in)
                vfMain.setOutAnimation(context, R.anim.horizontal_out)
                vfMain.showPrevious()
            }
            return true
        }
    }
    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    interface ViewFlipperTouchEvent {
        fun registerListener(listener:GestureDetector)
        fun unRegisterListener()
    }
}