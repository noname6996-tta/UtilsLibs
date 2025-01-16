package com.ntduc.viewutils.zoomimage_view

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(AutoResetMode.UNDER, AutoResetMode.OVER, AutoResetMode.ALWAYS, AutoResetMode.NEVER)
annotation class AutoResetMode {
    object Parser {
        @AutoResetMode
        fun fromInt(value: Int): Int {
            return when (value) {
                OVER -> OVER
                ALWAYS -> ALWAYS
                NEVER -> NEVER
                else -> UNDER
            }
        }
    }

    companion object {
        const val UNDER: Int = 0
        const val OVER: Int = 1
        const val ALWAYS: Int = 2
        const val NEVER: Int = 3
    }
}