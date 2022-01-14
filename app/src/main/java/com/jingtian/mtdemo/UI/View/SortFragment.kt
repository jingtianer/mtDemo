package com.jingtian.mtdemo.UI.View

import com.jingtian.mtdemo.Base.Interface.BaseInterface
import com.jingtian.mtdemo.Base.Presenter.BasePresenter
import com.jingtian.mtdemo.Base.View.BaseFragment
import com.jingtian.mtdemo.R

class SortFragment: BaseFragment<BaseInterface.presenter>() {
    override fun getPresenter(): BaseInterface.presenter {
        return BasePresenter<HomeFragment>()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_sort
    }
}