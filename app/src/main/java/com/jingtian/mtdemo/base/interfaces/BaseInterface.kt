package com.jingtian.mtdemo.base.interfaces

interface BaseInterface {
    interface View {
        fun bind()
    }

    interface Presenter {
        fun bind(view: View)
        fun unbind()
    }
}