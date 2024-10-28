package com.tohsoft.base.mvp.utils.language

data class LanguageDetail(
    val languageCode : String,
    val languageDisplay : String,
    var isSelected : Boolean = false
)
