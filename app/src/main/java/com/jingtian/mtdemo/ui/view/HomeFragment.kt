package com.jingtian.mtdemo.ui.view

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.view.BaseFragment
import com.jingtian.mtdemo.bean.FunctionBean
import com.jingtian.mtdemo.databinding.FragmentCartBinding
import com.jingtian.mtdemo.databinding.FragmentHomeBinding
import com.jingtian.mtdemo.ui.adapters.RcHomeFunctionAdapter
import com.jingtian.mtdemo.ui.adapters.RvCommodityAdapter
import com.jingtian.mtdemo.ui.interfaces.HomeInterface
import com.jingtian.mtdemo.ui.presenter.HomePresenter

class HomeFragment : BaseFragment<HomePresenter>(), HomeInterface.View {


    private var callback: ViewFlipperTouchEvent? = null


    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.let {
            binding.homeSearchBar.layoutParams =
                (binding.homeSearchBar.layoutParams as LinearLayout.LayoutParams).apply {
                    setMargins(leftMargin, getStatusBarHeight() + 45, rightMargin, bottomMargin)
                }
            BaseApplication.utilsHolder.utils.setFont(binding.tvSearchBar)
            binding.homeSearchBar.setOnClickListener {
                val intent = Intent(this.context, SearchActivity::class.java)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    this.activity, binding.homeSearchBar, ViewCompat.getTransitionName(binding.homeSearchBar)
                ).toBundle())
            }
        }

        mPresenter?.requestFunctionData()
        mPresenter?.requestImageData()
        mPresenter?.requestCommodityData()
    }

    private fun getImg(img: Int): Bitmap? {
        view?.let {
            val bitmap = BitmapFactory.decodeResource(resources, img)
            val distW = BaseApplication.utilsHolder.utils.getScreenWidth() * 0.95
            val distH = bitmap.height * (1.0 * distW / bitmap.width)
            return Bitmap.createScaledBitmap(bitmap, distW.toInt(), distH.toInt(), false)
        }
        return null

    }

    override fun onStart() {
        //此时activity已经创建了
        if (activity != null) {binding.homeSearchBar
            binding.vfMain.let {
                callback = (activity as MainActivity).getCallback()
                val gestureDetector =
                    GestureDetector(context, ViewFlipperTouchListener(it, context))
                callback?.registerListener(gestureDetector)
            }
        }
        super.onStart()
        //此时fragment对用户可见
    }

    override fun onDetach() {
        super.onDetach()
        callback?.unRegisterListener()
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
            if (e1!!.x - e2!!.x > minMove) {
                vfMain.setInAnimation(context, R.anim.horizontal_in)
                vfMain.setOutAnimation(context, R.anim.horizontal_out)
                vfMain.showNext()
            } else if (e2.x - e1.x > minMove) {
                vfMain.setInAnimation(context, R.anim.horizontal_in)
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
        fun registerListener(listener: GestureDetector)
        fun unRegisterListener()
    }

    override fun getPresenter(): HomePresenter = HomePresenter()

    override fun provideFunctionData(functionList: ArrayList<FunctionBean>) {
        view?.let {
            val spanCount = 2
            binding.rcHomeFunction.layoutManager = GridLayoutManager(context, spanCount).apply {
                orientation = GridLayoutManager.HORIZONTAL
            }
            binding.rcHomeFunction.adapter = RcHomeFunctionAdapter(functionList, spanCount)
        }
    }

    override fun provideImageData(imgRes: ArrayList<Int>) {
        view?.let {
            for (img in imgRes) {
                binding.vfMain.addView(ImageView(it.context).apply {
                    setImageBitmap(getImg(img))
                })
            }
            binding.vfMain.startFlipping()
        }
    }

    override fun provideCommodityData(commodities: ArrayList<Int>) {
        view?.let {
            RvCommodityAdapter.setWaterfallFlowStyle(binding.rvCommodity, 2, RvCommodityAdapter.VERTICAL)
            binding.rvCommodity.adapter = RvCommodityAdapter(commodities)
        }
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?,): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container,false)
        return _binding?.root
    }
    override fun unViewBinding() {
        _binding = null
    }
}