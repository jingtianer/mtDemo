package com.jingtian.mtdemo.ui.view

import com.jingtian.mtdemo.base.interfaces.BaseInterface
import com.jingtian.mtdemo.base.presenter.BasePresenter
import com.jingtian.mtdemo.base.view.BaseFragment
import com.jingtian.mtdemo.R

class SortFragment: BaseFragment<BaseInterface.Presenter>() {
    override fun getPresenter(): BaseInterface.Presenter {
        return BasePresenter<HomeFragment>()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_sort
    }
}