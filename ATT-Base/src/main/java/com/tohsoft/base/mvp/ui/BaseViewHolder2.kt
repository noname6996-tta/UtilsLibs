package com.tohsoft.base.mvp.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by Phong on 12/13/2017.
 */
abstract class BaseViewHolder2<T : ViewBinding>(protected var mBinding: T) :
    RecyclerView.ViewHolder(mBinding.root) {

    var currentPosition = 0
        private set

    open fun onBind(position: Int) {
        currentPosition = position
    }
}