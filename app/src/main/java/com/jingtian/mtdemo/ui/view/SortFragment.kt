package com.jingtian.mtdemo.ui.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.view.BaseFragment
import com.jingtian.mtdemo.bean.SortBean
import com.jingtian.mtdemo.ui.adapters.SortAdapter
import com.jingtian.mtdemo.ui.interfaces.SortInterface
import com.jingtian.mtdemo.ui.presenter.SortPresenter

class SortFragment : BaseFragment<SortPresenter>(), SortInterface.View {
    override fun getPresenter(): SortPresenter {
        return SortPresenter()
    }

    interface TabSelectionListener {
        fun click(id: Long, position: Int)
    }

    interface Add2CartListener {
        fun click(sortBean: SortBean)
    }

    private var x = 0
    private var y = 0
    private var nsvSort: NestedScrollView? = null
    private var rvMatrix = ArrayList<MutableMap<Long, RecyclerView?>>()
    private fun initRVMat() {
        for (i in 0..y) {
            rvMatrix.add(mutableMapOf())
        }
    }

    private fun showRV(i: Int?, j: Long?) {
        if ((i == null) or (j == null)) return
        val i1 = i!!
        val j1 = j!!
        Log.d("position", "$i, $j")
        Log.d("context is null:", "${context == null}")
        if (!rvMatrix[i1].containsKey(j1)) {
            context?.let {
//                Log.d("sort context", "not null")
                rvMatrix[i1][j1] = RecyclerView(it)
                mPresenter?.requestRvData(i1, j1)
            }
        }
        nsvSort?.let {
            Log.d("sort nsvSort", "not null")
            it.removeAllViews()
            it.addView(rvMatrix[i1][j1]!!)
        }
    }

    private var lvSLTAdapter: VerticalTabAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val llFragmentSort = view.findViewById<LinearLayout>(R.id.ll_sort_root)
        llFragmentSort.layoutParams =
            (llFragmentSort.layoutParams as LinearLayout.LayoutParams).apply {
                setMargins(leftMargin, topMargin + getStatusBarHeight(), leftMargin, bottomMargin)
            }
        nsvSort = view.findViewById(R.id.nsv_sort)
        val tlSort = view.findViewById<TabLayout>(R.id.tl_sort)
        val lvSortLeftTab = view.findViewById<RecyclerView>(R.id.rv_left_tab)
        lvSortLeftTab.layoutManager = LinearLayoutManager(context)
        lvSLTAdapter =
            VerticalTabAdapter(TabBean.data, view.context, object : TabSelectionListener {
                override fun click(id: Long, position: Int) {
                    Log.d("sort click", "not null, ${tlSort.selectedTabPosition}")
                    showRV(tlSort.selectedTabPosition, id)
                }
            })
        tlSort.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                showRV(tab?.position, lvSLTAdapter?.getCurrentSelection()?.id)

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        lvSortLeftTab.adapter = lvSLTAdapter
        x = TabBean.data.size
        y = 2
        initRVMat()
        showRV(0, TabBean.getFirstID())
    }

    override fun getLayout(): Int {
        return R.layout.fragment_sort
    }

    override fun provideRvData(i: Int, j: Long, data: ArrayList<SortBean>) {
        rvMatrix[i][j]?.let { rv ->
            Log.d("sort matrix", "not null")
            rv.adapter = SortAdapter(data, object : Add2CartListener {
                override fun click(sortBean: SortBean) {
                    mPresenter?.add2Cart(sortBean)
                }

            })
            rv.layoutManager = LinearLayoutManager(context)
            rv.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun add2CartSuccess() {
        Toast.makeText(context, "成功添加到购物车", Toast.LENGTH_SHORT).show()
    }

    override fun add2CartFail() {
        Toast.makeText(context, "添加到购物车失败", Toast.LENGTH_SHORT).show()
    }
}

//class VerticalTabAdapter(data:ArrayList<String>, context:Context):
//    ArrayAdapter<String>(context, R.layout.item_left_tab, data) {
//    class ViewHolder(val view: View){
//        val button: Button = view.findViewById(R.id.bt_sort_item_left_tab)
//        init {
//            view.tag = this
//        }
//    }
//
//    @SuppressLint("ViewHolder")
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val holder = if (convertView == null)
//            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_left_tab, parent, false))
//        else {
//            convertView.tag as ViewHolder
//        }
//        holder.button.text = getItem(position)
//        return holder.view
//    }
//}
data class TabBean(val name: String) {
    val id = curID

    init {
        curID++
    }

    companion object {
        var curID: Long = Long.MIN_VALUE
        val data = arrayListOf(
            TabBean("推荐"),
            TabBean("蔬菜"),
            TabBean("豆制品"),
            TabBean("水果"),
            TabBean("肉禽蛋"),
            TabBean("海鲜水产"),
            TabBean("乳制品"),
            TabBean("烘焙"),
            TabBean("营养早餐"),
            TabBean("米面"),
            TabBean("粮油"),
            TabBean("烘焙1"),
            TabBean("营养早餐1"),
            TabBean("米面1"),
            TabBean("粮油1")
        )

        fun getFirstID(): Long {
            return data[0].id
        }
    }
}

class VerticalTabAdapter(
    val data: ArrayList<TabBean>,
    val context: Context?,
    private val listener: SortFragment.TabSelectionListener
) :
    RecyclerView.Adapter<VerticalTabAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tv: TextView = view.findViewById(R.id.tv_sort_item_left_tab)
        val cv: CardView = view.findViewById(R.id.cv_left_tab)
    }

    private var currentSelection = 0
    fun getCurrentSelection(): TabBean {
        return data[currentSelection]
    }

    private fun select(position: Int) {
        if (position != currentSelection) {
            holders[position]?.apply {
                tv.setTextColor(BaseApplication.utils.getColor(R.color.orange))
                cv.setCardBackgroundColor(BaseApplication.utils.getColor(R.color.bg_gray))
            }
            holders[currentSelection]?.apply {
                tv.setTextColor(Color.BLACK)
                cv.setCardBackgroundColor(BaseApplication.utils.getColor(R.color.white))
            }
            currentSelection = position
        }
    }

    private val holders = mutableMapOf<Int, ViewHolder>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_left_tab, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holders[position] = holder
        holder.tv.setTextColor(if (position != 0) Color.BLACK else BaseApplication.utils.getColor(R.color.orange))
        holder.tv.text = item.name
        holder.tv.setOnClickListener {
            select(position)
            listener.click(item.id, position)
        }
        holder.cv.setCardBackgroundColor(
            if (position != 0) BaseApplication.utils.getColor(R.color.white) else BaseApplication.utils.getColor(
                R.color.bg_gray
            )
        )
    }

    override fun getItemCount(): Int = data.size
}
