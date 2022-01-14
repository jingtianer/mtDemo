package com.jingtian.mtdemo.UI.View

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
import com.jingtian.mtdemo.Base.Interface.BaseInterface
import com.jingtian.mtdemo.Base.Presenter.BasePresenter
import com.jingtian.mtdemo.Base.View.BaseActivity
import com.jingtian.mtdemo.Base.View.BaseFragment
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.Utils.SetFont
import com.jingtian.mtdemo.Utils.SetFont.Companion.getScreenWidth

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
        for (img in img_res) {
            vf_main.addView(ImageView(context).apply {
                val bitmap = BitmapFactory.decodeResource(resources, img)
                val distW = SetFont.getScreenWidth(activity!!)*0.9
                val distH = bitmap.height * (1.0*distW/bitmap.width)
                background = Bitmap.createScaledBitmap(bitmap, distW.toInt(), distH.toInt(),false).toDrawable(resources)
            })
        }
        callback = (activity!! as MainActivity).getCallback()
        val gestureDetector = GestureDetector(context, PF_TouchListenr(vf_main, context))
        callback?.register_listener(gestureDetector)
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