package com.jingtian.mtdemo.ui.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import com.jingtian.mtdemo.R
import com.jingtian.mtdemo.base.BaseApplication
import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.base.view.BaseActivity
import com.jingtian.mtdemo.databinding.ActivitySearchBinding
import java.util.*

class SearchActivity: BaseActivity<BaseInterface.Presenter>(), BaseInterface.View {
    override fun getPresenter(): BaseInterface.Presenter {
        return BasePresenter<SearchActivity>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.llSearchSearchBarRoot.apply {
            setPadding(paddingLeft, paddingTop + getStatusBarHeight(),paddingRight, paddingBottom)
        }

        BaseApplication.utilsHolder.utils.setFont(binding.tvSearchSearchBar)




        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        binding.etSearchSearchBar.postDelayed({
            runOnUiThread {
                binding.etSearchSearchBar.apply{
                    isFocusable = true
                    isFocusableInTouchMode = true
                    requestFocus()
                    findFocus()
                }
                val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(binding.etSearchSearchBar,  InputMethodManager.SHOW_IMPLICIT)
            }
        },300)
    }

    override fun onResume() {
        super.onResume()

    }
    override fun getLayout(): Int {
        return R.layout.activity_search
    }
    private lateinit var binding:ActivitySearchBinding
    override fun viewBinding(): View {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        return binding.root
    }
}