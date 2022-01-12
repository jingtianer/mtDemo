package com.jingtian.mtdemo.Base.Interface

interface BaseInterface {
    interface view {
        fun bind()
    }
    interface presenter {
        fun bind(view:BaseInterface.view)
        fun unbind()
    }
}