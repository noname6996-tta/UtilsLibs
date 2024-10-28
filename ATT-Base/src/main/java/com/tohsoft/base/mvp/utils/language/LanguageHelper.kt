package com.tohsoft.base.mvp.utils.language

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.telephony.TelephonyManager
import com.tohsoft.base.mvp.R
import com.tohsoft.base.mvp.utils.language.LocaleManager.getLanguage
import com.tohsoft.base.mvp.utils.language.LocaleManager.setNewLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Language helper kotlin : Hỗ trợ phần lõi logic : Xử lý lấy danh sách các ngôn ngữ, detect ngôn ngữ hiện tại của ứng dụng.
 * Sử dụng Coroutine thay thế Rx.
 * by PhiNM */
object LanguageHelper {
    private const val URL_GET_COUNTRY_CODE_BY_IP = "http://gsp1.apple.com/pep/gcc"
    private const val DEFAULT_DETECT_LANGUAGE = "en"
    private const val KEY_COUNTRY_CODE_BY_IP = "country_code_by_ip"

    /**
     * Device language  : Hàm này trả về kết quả callback là ngôn ngữ của device.
     */
    private fun detectUserLanguage(context: Context) : String {
        val languageCodeBySim = getLanguageFromCountry(countryBySim(context))
        if (!languageCodeBySim.isNullOrEmpty()) return languageCodeBySim
        val languageCodeByIP = getLanguageFromCountry(getCountryCodeByIP(context))
        return if (!languageCodeByIP.isNullOrEmpty()) languageCodeByIP
        else DEFAULT_DETECT_LANGUAGE
    }

    /**
     * Lấy lại CountryCode theo API. Nếu đã request trước đó thì bỏ qua, không xử lý gì cả.
     */
    fun syncDetectLanguageByIp(context : Context, lifecycleScope: CoroutineScope) {
        if (getCountryCodeByIP(context).isNotEmpty()) return
        lifecycleScope.launch(Dispatchers.IO) {
            countryByIp(context)
        }
    }

    private fun getAppLanguageCode(context: Context) = getLanguage(context)

    fun getAppLanguageDisplay(context: Context) =
        if (getLanguage(context) == LocaleManager.MODE_AUTO) context.getString(R.string.lbl_auto)
        else getLanguageDisplayName(getAppLanguageCode(context))

    fun setAppLanguage(context: Context, languageCode: String) {
        setNewLocale(context, languageCode)
    }

    fun getListLanguages(context: Context): List<LanguageDetail> {
        val appLanguage = getAppLanguageCode(context)
        val autoItem = LanguageDetail(LocaleManager.MODE_AUTO, context.getString(R.string.lbl_auto))
        val deviceItem = detectUserLanguage(context).let { LanguageDetail(it, getLanguageDisplayName(it)) }
        val enItem = LanguageDetail("en", getLanguageDisplayName("en"))

        val languagesList =
            context.resources.getStringArray(R.array.key_language_support).toMutableList().apply {
                sortBy {
                    toDisplayCase(getLanguageDisplayName(it))
                }
            }
                .filter { it != deviceItem.languageCode && it != enItem.languageCode } //Bỏ language EN vs Device User ra.
                .map {
                    LanguageDetail(it, getLanguageDisplayName(it))
                }.toMutableList().apply {
                    add(0, enItem)
                    if (enItem.languageCode != deviceItem.languageCode) add(0, deviceItem)
                    add(0, autoItem)
                }
        languagesList.first { it.languageCode == appLanguage }.isSelected = true
        return languagesList
    }

    private fun getLanguageDisplayName(languageCode : String) : String {
        val spk = languageCode.split("-".toRegex()).toTypedArray()
        val locale = if (spk.size > 1) {
            Locale(spk[0], spk[1])
        } else {
            Locale(languageCode)
        }
        return toDisplayCase(locale.getDisplayName(locale))
    }

    private suspend fun countryByIp(context: Context): String {
        val countryCode = getCountryCodeByIP(context)
        if (countryCode.isNotEmpty()) return countryCode
        return try {
            val response = withContext(Dispatchers.IO) {
                NetworkCall().makeServiceCall(URL_GET_COUNTRY_CODE_BY_IP)
            }
            if (!response.isNullOrEmpty()) {
                setCountryCodeByIP(context, response)
            }
            response?.lowercase(Locale.US) ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun countryBySim(context: Context): String {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simCountry: String? = tm.simCountryIso
            if (simCountry != null && simCountry.length == 2) {
                return simCountry.lowercase(Locale.US)
            } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) {
                val networkCountry = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) {
                    return networkCountry.lowercase(Locale.US)
                }
            }
            return ""
        } catch (_: Exception) {
            return ""
        }
    }

    private fun getLanguageFromCountry(countryCode: String): String? {
        val locales = Locale.getAvailableLocales()
        val countryLanguageMap: HashSet<String> = HashSet()

        for (locale in locales) {
            val country = locale.country
            val language = locale.language
            if (country.isNotEmpty() && language.isNotEmpty() && country.equals(countryCode, true)) {
                countryLanguageMap.add(language)
            }
        }
        return if (countryLanguageMap.size==0) null else countryLanguageMap.first()
    }

    @Deprecated("Hàm này không chính xác lắm, dùng hàm getLanguageFromCountry(countryCode)")
    private fun getLanguageFromCountry(context: Context, country: String): String {
        val countryCode = context.resources.getStringArray(R.array.CountryCodes)
        val languageCodeSupport =
            context.resources.getStringArray(R.array.key_language_support).asList()
        for (s in countryCode) {
            val lg = s.split("_".toRegex()).toTypedArray()
            if (lg.size > 1 && lg[0].equals(
                    country,
                    ignoreCase = true
                ) && languageCodeSupport.contains(lg[0])
            ) {
                return lg[0]
            }
        }
        for (s in countryCode) {
            val lg = s.split("_".toRegex()).toTypedArray()
            if (lg.size > 1 && lg[1].equals(
                    country,
                    ignoreCase = true
                ) && languageCodeSupport.contains(lg[0])
            ) {
                return lg[0]
            }
        }
        return ""
    }

    private fun toDisplayCase(s: String): String {
        val actionableDelimiters = " '-/"
        val sb = StringBuilder()
        var capNext = true
        for (c in s.toCharArray()) {
            val char = if (capNext) Character.toUpperCase(c) else Character.toLowerCase(c)
            sb.append(char)
            capNext = actionableDelimiters.indexOf(char) >= 0
        }
        return sb.toString()
    }

    // Không thấy dùng các hàm phía dưới này nữa
    lateinit var sharedPreferences: SharedPreferences
    fun init(context: Context) {
        sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun getCountryCodeByIP(context: Context): String {
        return sharedPreferences.getString(KEY_COUNTRY_CODE_BY_IP, "") ?: ""
    }

    private fun setCountryCodeByIP(context: Context, value: String) {
        sharedPreferences.edit().putString(KEY_COUNTRY_CODE_BY_IP, value).apply()
    }
}