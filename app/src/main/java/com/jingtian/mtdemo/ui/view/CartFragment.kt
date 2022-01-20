package com.jingtian.mtdemo.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.view.BaseFragment
import com.jingtian.mtdemo.bean.CartBean
import com.jingtian.mtdemo.ui.adapters.CartAdapter
import com.jingtian.mtdemo.ui.adapters.RvCommodityAdapter
import com.jingtian.mtdemo.ui.interfaces.CartInterface
import com.jingtian.mtdemo.ui.presenter.CartPresenter


class CartFragment : BaseFragment<CartPresenter>(), CartInterface.View {

    override fun getLayout(): Int {
        return R.layout.fragment_cart
    }

    private var selectCount = 0
    fun incSelectCount() {
        if (selectCount == 0) {
            enableButton(tvLabelRight)
            bottomBarShowUp()
        }

        selectCount++
        if (isSelectAll()) {
            tvSelectAll?.text = BaseApplication.utils.getString(R.string.checked)
        }
    }

    fun decSelectionCount() {
        if (selectCount - 1 == 0) {
            disableButton(tvLabelRight)
            bottomBarExitDown()
        }
        tvSelectAll?.text = BaseApplication.utils.getString(R.string.unchecked)
        selectCount--
    }

    val selectedItem = mutableListOf<CartBean>()
    private val listener1 = object : SelectionClickListener {
        override fun click(view: TextView, selection: Boolean, bean: CartBean) {
            if (selection) {
                view.setText(R.string.unchecked)
                decSelectionCount()
                selectedItem.remove(bean)
            } else {
                view.setText(R.string.checked)
                if (!selectedItem.contains(bean)) {
                    selectedItem.add(bean)
                }
                incSelectCount()
            }
            updateTotal()
        }

    }

    fun updateTotal() {
        var totalPrice = 0f
        for (item in selectedItem) {
            totalPrice += item.price * item.n
            Log.d("价格", "$totalPrice")
        }
        cartTotal?.text = "$totalPrice"

    }

    var isBottomBarShown = true
    private fun bottomBarShowUp() {
        if (isBottomBarShown) return
        bottomBar?.apply {
//            startAnimation(BaseApplication.anims.showUpAnimation())
            BaseApplication.anims.showUpAnimation(this, height).start()
        }
        isBottomBarShown = true
    }

    private fun bottomBarExitDown(delay: Long = 0) {
        bottomBar?.apply {
//            startAnimation(BaseApplication.anims.exitAnimation().apply {
//                startOffset = delay
//            })
            BaseApplication.anims.exitAnimation(this, height).apply {
                startDelay = delay
                start()
            }
        }
        isBottomBarShown = false
    }

    private val map = mutableMapOf<Int, Int>()
    private val listener2 = object : NumberClickListener {
        override fun click() {
            updateTotal()
        }
    }


    private fun disableButton(textView: TextView?) {
        textView?.apply {
            visibility = View.INVISIBLE
            isClickable = false
        }
    }

    private fun enableButton(textView: TextView?) {
        textView?.apply {
            visibility = View.VISIBLE
            isClickable = true
        }
    }

    private var bottomBar: LinearLayout? = null
    private var tvLabelLeft: TextView? = null
    private var tvLabelRight: TextView? = null
    private var tvSelectAll: TextView? = null
    private var cartTotal: TextView? = null
    private fun isSelectAll() = selectCount == mCartAdapter?.itemCount
    private fun getToolbar(view: View) = view.findViewById<LinearLayout>(R.id.ll_toolbar_root)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.let {
            val llCartFirst = it.findViewById<LinearLayout>(R.id.ll_cart_first)
            llCartFirst.apply {
                setPadding(
                    paddingLeft,
                    paddingTop + getStatusBarHeight(),
                    paddingRight,
                    paddingBottom
                )
            }
            cartTotal = it.findViewById(R.id.cart_total)
            //请求recyclerview的数据
            mPresenter?.requestGuessData()
            mPresenter?.requestCartData()
            //初始化按钮与点击操作
            initButtons(it)
            bottomBar = it.findViewById(R.id.ll_cart_bottom)
            tvSelectAll = it.findViewById(R.id.tv_cart_select_all)
            val rvCart = it.findViewById<RecyclerView>(R.id.rv_cart_cart)
            tvSelectAll?.apply {
                BaseApplication.utils.setFont(this)
                text = BaseApplication.utils.getString(R.string.unchecked)
                it.findViewById<ConstraintLayout>(R.id.cl_cart_select_all)?.setOnClickListener {
                    if (isBottomBarShown) {
                        text = if (isSelectAll()) {
                            (rvCart.adapter as CartAdapter).unselectAll()
                            BaseApplication.utils.getString(R.string.unchecked)
                        } else {
                            (rvCart.adapter as CartAdapter).selectAll()
                            BaseApplication.utils.getString(R.string.checked)
                        }
                    }
                }
            }

        }
    }

    interface SelectionClickListener {
        fun click(view: TextView, selection: Boolean, bean: CartBean)
    }

    interface NumberClickListener {
        fun click()
    }


    private fun initButtons(it: View) {
        val toolbar = getToolbar(it)
        val tvTitle = toolbar.findViewById<TextView>(R.id.tv_label_title)
        tvTitle.text = "购物车"
        tvLabelLeft = toolbar.findViewById(R.id.tv_label_left)
        tvLabelRight = toolbar.findViewById(R.id.tv_label_right)
        val rvCart = it.findViewById<RecyclerView>(R.id.rv_cart_cart)
        tvLabelLeft?.apply {
            text = "清空"
            setOnClickListener {
                val adapter = rvCart.adapter as CartAdapter
                while (adapter.itemCount > 0) {
                    adapter.removeAt(0)
                }
                selectedItem.clear()
                map.clear()
                selectCount = 0
                bottomBarExitDown()
                updateTotal()
                disableButton(tvLabelRight)
            }
            disableButton(tvLabelRight)
            selectCount = 0
        }
        tvLabelRight?.apply {
            text = "删除"
            setOnClickListener {
                val adapter = rvCart.adapter as CartAdapter
                for (item in selectedItem) {
                    adapter.removeAt(adapter.indexOf(item))
                }
                selectedItem.clear()
                map.clear()
                selectCount = 0
                updateTotal()
                bottomBarExitDown()
                disableButton(tvLabelRight)
            }
        }
        disableButton(tvLabelRight)
        enableButton(tvLabelLeft)
    }

    var mCartAdapter: CartAdapter? = null
    override fun getPresenter(): CartPresenter = CartPresenter()
    override fun provideCartData(cartData: ArrayList<CartBean>) {
        mCartAdapter?.let {
            for (item in cartData) {
                it.add(item)
            }
        }
    }

    override fun provideGuessData(commodityRes: ArrayList<Int>) {
        view?.let {
            val rvCartGuess = it.findViewById<RecyclerView>(R.id.rv_cart_guess)
            RvCommodityAdapter.setWaterfallFlowStyle(rvCartGuess, 2, RvCommodityAdapter.VERTICAL)
            rvCartGuess.adapter = RvCommodityAdapter(commodityRes)
        }
    }

    override fun provideCartUpdate(cartData: ArrayList<CartBean>) {
        view?.let {
            mCartAdapter = CartAdapter(cartData, listener1, listener2)
            val rvCart = it.findViewById<RecyclerView>(R.id.rv_cart_cart)
            rvCart?.apply {
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }
                adapter = mCartAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.requestUpdateCart()
        if (selectCount == 0 && isBottomBarShown)
            bottomBarExitDown(200)
    }
}
