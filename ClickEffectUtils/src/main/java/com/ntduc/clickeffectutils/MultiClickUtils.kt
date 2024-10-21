package com.ntduc.clickeffectutils

import android.os.SystemClock

/*
How to use

    override fun onClick(v: View?) {
        if (!MultiClickUtils.instance?.isAvailableClick(300)!!) {
            return
        }
        when (v?.id) {
            mBinding.btnSave.id -> handleSaveProfile()
            mBinding.ivBack.id -> onBackPressed()
            mBinding.rlAvatar.id -> handleStartPickImage()
            mBinding.tvChangePassword.id -> handleChangePassword()
        }
    }

*/


class MultiClickUtils private constructor() {

    private var mLastClickTime: Long
    val isAvailableClick: Boolean
        get() {
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - mLastClickTime < 500) {
//                Timber.d("Can not apply click!")
                return false
            }
            mLastClickTime = currentTime
//            Timber.d("Apply click!")
            return true
        }

    fun isAvailableClick(timeDelay: Long): Boolean {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - mLastClickTime < timeDelay) {
//            Timber.d("Can not apply click!")
            return false
        }
        mLastClickTime = currentTime
//        Timber.d("Apply click!")
        return true
    }

    companion object {
        var instance: MultiClickUtils? = null
            get() {
                if (field == null) field = MultiClickUtils()
                return field
            }
            private set
    }

    init {
        mLastClickTime = -1
    }
}