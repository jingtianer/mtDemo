package com.jingtian.mtdemo.UI.View

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.mtdemo.Base.View.BaseActivity
import com.jingtian.mtdemo.UI.Interface.MainInterface
import com.jingtian.mtdemo.UI.Presenter.MainPresenter

import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.Utils.SetFont
import okhttp3.internal.notifyAll
import java.util.*
import kotlin.collections.ArrayList

import android.view.WindowManager





class MainActivity: BaseActivity<MainInterface.presenter>(), MainInterface.view {
    override fun getPresenter(): MainInterface.presenter {
        return MainPresenter()
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        pf_listener?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
    fun getCallback():HomeFragment.touch_event{
        return object :HomeFragment.touch_event {
            override fun register_listener(listener: GestureDetector) {
                pf_listener = listener
            }

            override fun unregister_listener() {
                pf_listener = null
            }

        }
    }
    var pf_listener:GestureDetector? = null
    private val fragments = mutableMapOf<Int, Fragment>()
    fun getInstance(i:Int):Fragment {
        if (!fragments.containsKey(i)) {
            fragments[i] = Class.forName(navi_arr[i].class_name).newInstance() as Fragment
        }
        return fragments[i]!!
    }
    fun Login() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentBars()
        val fl_main = findViewById<FrameLayout>(R.id.fl_main)
        val rc_btm_navi = findViewById<RecyclerView>(R.id.rc_btm_navi)
        rc_btm_navi.layoutManager = GridLayoutManager(this,1).apply {
            orientation = GridLayoutManager.HORIZONTAL
        }
        rc_btm_navi.adapter = navi_adapter(navi_arr, this, object :NaviItemClick {
            override fun click(position: Int) {
                supportFragmentManager.beginTransaction()
                .replace(R.id.fl_main, getInstance(position))
                .commit()
                if (position == 3) {
                    Login()
                }
            }
        })
    }
    data class navi_bean(val icon:Int, val title:String, val class_name:String) {
        var mHolder:navi_adapter.view_holder?=null
    }
    val navi_arr = arrayListOf(
        navi_bean(R.string.home, "首页","com.jingtian.mtdemo.UI.View.HomeFragment"),
        navi_bean(R.string.sort, "分类","com.jingtian.mtdemo.UI.View.SortFragment"),
        navi_bean(R.string.cart, "购物车","com.jingtian.mtdemo.UI.View.CartFragment"),
        navi_bean(R.string.mine, "我的","com.jingtian.mtdemo.UI.View.MineFragment")
    )
    interface NaviItemClick {
        fun click(position:Int)
    }
    class navi_adapter(val navi_arr:ArrayList<navi_bean>,val activity: FragmentActivity,val perform_click:NaviItemClick)
        :RecyclerView.Adapter<navi_adapter.view_holder>(){
        class view_holder(val view:View) : RecyclerView.ViewHolder(view) {
            var item_navi_icon:TextView
            var item_navi_title:TextView
            init {
                item_navi_icon = view.findViewById(R.id.item_navi_icon)
                item_navi_title = view.findViewById(R.id.item_navi_title)
            }
            fun onTouch(b:Boolean) {
                if (b) {
                    item_navi_icon.setTextColor(ContextCompat.getColor(view.context, R.color.orange))
                    item_navi_title.setTextColor(ContextCompat.getColor(view.context, R.color.orange))
                } else {
                    item_navi_icon.setTextColor(ContextCompat.getColor(view.context, R.color.black))
                    item_navi_title.setTextColor(ContextCompat.getColor(view.context, R.color.black))
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): view_holder {
            val view: View =
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.item_navi,
                        parent,
                        false
                    )
            view.layoutParams.width = getScreenWidth(activity) / navi_arr.size
            return view_holder(view)
        }


        override fun onBindViewHolder(holder: view_holder, position: Int) {
            val item = navi_arr[position]
            navi_arr[position].mHolder = holder
            SetFont.setFont(holder.item_navi_icon, holder.item_navi_title.context)
            holder.apply {
                item_navi_title.text = item.title
                item_navi_icon.setTextColor(ContextCompat.getColor(activity, R.color.black))
                item_navi_title.setTextColor(ContextCompat.getColor(activity, R.color.black))
                item_navi_icon.setText(item.icon)
                if (position == 0) {
                    click(position)
                }
                view.setOnClickListener {
                    click(position)
                }
            }
        }

        fun click(position: Int) {


            for(i in 0 until navi_arr.size) {
                navi_arr[i].mHolder?.onTouch(i == position)
            }
            //点击时改变颜色
            perform_click.click(position)
            //通知activity点击事件
        }
        override fun getItemCount(): Int = navi_arr.size
        fun getScreenWidth(context: Activity): Int{
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.windowManager.currentWindowMetrics.bounds.width()
            } else {
                context.windowManager.defaultDisplay.width
            }
        }

    }
}