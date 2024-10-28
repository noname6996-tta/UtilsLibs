package com.tohsoft.base.mvp.utils.xiaomi

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

internal object Rom {
    @JvmStatic
    fun getProp(name: String): String? {
        var input: BufferedReader? = null
        return try {
            val p = Runtime.getRuntime().exec("getprop $name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            val line = input.readLine()
            input.close()
            line
        } catch (ex: IOException) {
            null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}