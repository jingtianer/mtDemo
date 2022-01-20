package com.jingtian.mtdemo.ui.view

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
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


    private var callback: ViewFlipperTouchEvent? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.let {
            val homeSearchBar = it.findViewById<LinearLayout>(R.id.home_search_bar)
            homeSearchBar.layoutParams =
                (homeSearchBar.layoutParams as LinearLayout.LayoutParams).apply {
                    setMargins(leftMargin,getStatusBarHeight() + 45,rightMargin,bottomMargin)
                }
            val tvSearchBar = it.findViewById<TextView>(R.id.tv_search_bar)
            BaseApplication.utils.setFont(tvSearchBar)


        }

        mPresenter?.requestFunctionData()
        mPresenter?.requestImageData()
        mPresenter?.requestCommodityData()
    }

    private fun getImg(img: Int): Bitmap? {
        view?.let {
            val bitmap = BitmapFactory.decodeResource(resources, img)
            val distW = BaseApplication.utils.getScreenWidth() * 0.95
            val distH = bitmap.height * (1.0 * distW / bitmap.width)
            return Bitmap.createScaledBitmap(bitmap, distW.toInt(), distH.toInt(), false)
        }
        return null

    }


    override fun onStart() {
        //此时activity已经创建了
        if (activity != null) {
            val vfMain = view?.findViewById<ViewFlipper>(R.id.vf_main)
            vfMain?.let {
                callback = (activity as MainActivity).getCallback()
                val gestureDetector =
                    GestureDetector(context, ViewFlipperTouchListener(it, context))
                callback?.registerListener(gestureDetector)
            }
        }
        super.onStart()
        //此时fragment对用户可见
    }

        override fun getItemCount(): Int = commodityRes.size
    }

    class ViewFlipperTouchListener(private val vfMain: ViewFlipper, val context: Context?) :
        GestureDetector.SimpleOnGestureListener() {
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

    override fun getPresenter(): HomePresenter = HomePresenter()

    override fun provideFunctionData(functionList: ArrayList<FunctionBean>) {
        view?.let {
            val rcHomeFunction = it.findViewById<RecyclerView>(R.id.rc_home_function)
            val spanCount = 2
            rcHomeFunction.layoutManager = GridLayoutManager(context, spanCount).apply {
                orientation = GridLayoutManager.HORIZONTAL
            }
            rcHomeFunction.adapter = RcHomeFunctionAdapter(functionList, spanCount)
        }
    }

    override fun provideImageData(imgRes: ArrayList<Int>) {
        view?.let {
            val vfMain = it.findViewById<ViewFlipper>(R.id.vf_main)
            for (img in imgRes) {
                vfMain.addView(ImageView(it.context).apply {
                    setImageBitmap(getImg(img))
                })
            }
            vfMain.startFlipping()
        }
    }

    override fun provideCommodityData(commodities: ArrayList<Int>) {
        view?.let {
            val rvCommodity = it.findViewById<RecyclerView>(R.id.rv_commodity)
            RvCommodityAdapter.setWaterfallFlowStyle(rvCommodity, 2, RvCommodityAdapter.VERTICAL)
            rvCommodity.adapter = RvCommodityAdapter(commodities)
        }
    }
}