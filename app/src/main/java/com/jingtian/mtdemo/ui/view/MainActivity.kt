package com.jingtian.mtdemo.ui.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.view.BaseActivity
import com.jingtian.mtdemo.bean.NaviBean
import com.jingtian.mtdemo.databinding.ActivityLoginBinding
import com.jingtian.mtdemo.databinding.ActivityMainBinding
import com.jingtian.mtdemo.ui.adapters.MainViewPagerAdapter
import com.jingtian.mtdemo.ui.interfaces.MainInterface
import com.jingtian.mtdemo.ui.presenter.MainPresenter


class MainActivity : BaseActivity<MainInterface.Presenter>(), MainInterface.View {
    override fun getPresenter(): MainInterface.Presenter {
        return MainPresenter()
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        pfListener?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun getCallback(): HomeFragment.ViewFlipperTouchEvent {
        return object : HomeFragment.ViewFlipperTouchEvent {
            override fun registerListener(listener: GestureDetector) {
                pfListener = listener
            }

            override fun unRegisterListener() {
                pfListener = null
            }
        }
    }

    var pfListener: GestureDetector? = null
    private var pagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            (binding.rcBtmNavi.adapter as NaviAdapter).apply {
                changeSelectedItem(position)
                if (getLayoutId(position) == R.layout.fragment_mine) {
                    login()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentBars()

        binding.flMain.apply {
            adapter = MainViewPagerAdapter(supportFragmentManager, lifecycle, naviArr)
            registerOnPageChangeCallback(pagerCallback)
            offscreenPageLimit = naviArr.size - 1
            //???????????????n-1
        }

        binding.rcBtmNavi.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 1).apply {
                orientation = GridLayoutManager.HORIZONTAL
            }
            adapter = NaviAdapter(naviArr, this@MainActivity, object : NaviItemClick {
                override fun click(position: Int) {
                    binding.flMain.setCurrentItem(position, true)
//                    if (naviArr[position].id == R.layout.fragment_mine) {
//                        login()
//                    }
                }
            })
        }

    }


    private val naviArr = arrayListOf(
        NaviBean(R.string.home, "??????", R.layout.fragment_home),
        NaviBean(R.string.sort, "??????", R.layout.fragment_sort),
        NaviBean(R.string.cart, "?????????", R.layout.fragment_cart),
        NaviBean(R.string.mine, "??????", R.layout.fragment_mine)
    )

    interface NaviItemClick {
        fun click(position: Int)
    }

    class NaviAdapter(
        private val naviArr: ArrayList<NaviBean>,
        private val activity: FragmentActivity,
        private val perform_click: NaviItemClick
    ) : RecyclerView.Adapter<NaviAdapter.ViewHolder>() {
        fun getLayoutId(position: Int): Int {
            return naviArr[position].id
        }

        fun changeSelectedItem(position: Int) {
            for (i in 0 until naviArr.size) {
                naviArr[i].mHolder?.onTouch(i == position)
            }
        }

        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            var itemNaviIcon: TextView = view.findViewById(R.id.item_navi_icon)
            var itemNaviTitle: TextView = view.findViewById(R.id.item_navi_title)
            fun onTouch(b: Boolean) {
                if (b) {
                    itemNaviIcon.setTextColor(ContextCompat.getColor(view.context, R.color.orange))
                    itemNaviTitle.setTextColor(ContextCompat.getColor(view.context, R.color.orange))
                } else {
                    itemNaviIcon.setTextColor(ContextCompat.getColor(view.context, R.color.black))
                    itemNaviTitle.setTextColor(ContextCompat.getColor(view.context, R.color.black))
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View =
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.item_navi,
                        parent,
                        false
                    )
            view.layoutParams.width = getScreenWidth(activity) / naviArr.size
            return ViewHolder(view)
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = naviArr[position]
            naviArr[position].mHolder = holder
            BaseApplication.utilsHolder.utils.setFont(holder.itemNaviIcon)
            holder.apply {
                itemNaviTitle.text = item.title
                itemNaviIcon.setTextColor(ContextCompat.getColor(activity, R.color.black))
                itemNaviTitle.setTextColor(ContextCompat.getColor(activity, R.color.black))
                itemNaviIcon.setText(item.icon)
                if (item.id == R.layout.fragment_home) {
                    click(position)
                }
                view.setOnClickListener {
                    click(position)
                }
            }
        }

        private fun click(position: Int) {
            for (i in 0 until naviArr.size) {
                naviArr[i].mHolder?.onTouch(i == position)
            }
            //?????????????????????
            perform_click.click(position)
            //??????activity????????????
        }

        override fun getItemCount(): Int = naviArr.size
        private fun getScreenWidth(context: Activity): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.windowManager.currentWindowMetrics.bounds.width()
            } else {
                context.windowManager.defaultDisplay.width
            }
        }

    }

    private lateinit var binding: ActivityMainBinding
    override fun viewBinding(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }
}