package com.jingtian.mtdemo.ui.view

import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.base.view.BaseFragment
import com.jingtian.mtdemo.R

class CartFragment: BaseFragment<BaseInterface.Presenter>() {
    override fun getPresenter(): BaseInterface.Presenter {
        return BasePresenter<HomeFragment>()
    }

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
            if (map.containsKey(item.pic)) {
                totalPrice += item.price * map[item.pic]!!
                Log.d("价格", "$totalPrice")
            }
        }
        cartTotal?.text = "$totalPrice"

    }

    var isBottomBarShown = false
    private fun bottomBarShowUp() {
        if (isBottomBarShown) return
        bottomBar?.apply {
            startAnimation(BaseApplication.anims.showUpAnimation())
        }
        isBottomBarShown = true
    }

    private fun bottomBarExitDown() {
        bottomBar?.apply {
            startAnimation(BaseApplication.anims.exitAnimation())
        }
        isBottomBarShown = false
    }

    private val map = mutableMapOf<Int, Int>()
    private val listener2 = object : NumberClickListener {
        override fun click(view: CartNumberPicker, id: Int) {
            map[id] = view.getNumber()
            Log.d("total", "${map[id]}")
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

    private var bottomBar: ConstraintLayout? = null
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
            bottomBar = it.findViewById(R.id.cl_cart_bottom)
            bottomBarExitDown()
            tvSelectAll = it.findViewById(R.id.tv_cart_select_all)
            val rvCart = it.findViewById<RecyclerView>(R.id.rv_cart_cart)
            tvSelectAll?.apply {
                BaseApplication.utils.setFont(this)
                text = BaseApplication.utils.getString(R.string.unchecked)
                setOnClickListener {
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
        fun click(view: CartNumberPicker, position: Int)
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
                while (adapter.data.size > 0) {
                    adapter.removeAt(0)
                }
                selectedItem.clear()
                map.clear()
                selectCount = 0
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
                    adapter.removeAt(adapter.data.indexOf(item))
                }
                selectedItem.clear()
                map.clear()
                selectCount = 0
                disableButton(tvLabelRight)
                updateTotal()
            }
        }
        disableButton(tvLabelRight)
        enableButton(tvLabelLeft)
    }

    var mCartAdapter: CartAdapter? = null
    override fun getPresenter(): CartPresenter = CartPresenter()
    override fun provideCartData(cartData: ArrayList<CartBean>) {
        view?.let {
            mCartAdapter = CartAdapter(requireActivity(), cartData, listener1, listener2)
            val rvCart = it.findViewById<RecyclerView>(R.id.rv_cart_cart)
            rvCart?.apply {
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }
                adapter = mCartAdapter
            }
        }
    }

    override fun provideGuessData(commodityRes: ArrayList<Int>) {
        view?.let {
            val rvCartGuess = it.findViewById<RecyclerView>(R.id.rv_cart_guess)
            RvCommodityAdapter.setWaterfallFlowStyle(rvCartGuess, 2, RvCommodityAdapter.VERTICAL)
            rvCartGuess.adapter = RvCommodityAdapter(requireActivity(), commodityRes)
        }
    }

}
