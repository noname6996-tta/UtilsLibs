package com.ntduc.contextutils

import android.Manifest
import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils.isEmpty
import android.util.DisplayMetrics
import android.view.LayoutInflater
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

//Check PIP
val Context.supportsPictureInPicture: Boolean
  get() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && packageManager.hasSystemFeature(
      PackageManager.FEATURE_PICTURE_IN_PICTURE
    )
  }

val Context.isLocationEnabled: Boolean
  get() = (getSystemService(Context.LOCATION_SERVICE) as LocationManager?)?.isProviderEnabled(
    LocationManager.NETWORK_PROVIDER
  ) ?: false

val Context.deviceID
  @SuppressLint("HardwareIds") get() = Settings.Secure.getString(
    this.contentResolver,
    Settings.Secure.ANDROID_ID
  )

/**
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 
 * 0 = no connection info available
 * 1 = mobile data
 * 2 = wifi
 * 3 = vpn
 */
@SuppressLint("MissingPermission")
@IntRange(from = 0, to = 3)
fun Context.getConnectionType(): Int {
  var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi; 3: vpn
  val cm = connectivityManager
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    cm?.run {
      getNetworkCapabilities(cm.activeNetwork)?.run {
        when {
          hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
            result = 2
          }
          hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
            result = 1
          }
          hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
            result = 3
          }
        }
      }
    }
  } else {
    cm?.run {
      activeNetworkInfo?.run {
        when (type) {
          ConnectivityManager.TYPE_WIFI -> {
            result = 2
          }
          ConnectivityManager.TYPE_MOBILE -> {
            result = 1
          }
          ConnectivityManager.TYPE_VPN -> {
            result = 3
          }
        }
      }
    }
  }
  return result
}

/**
 * 0 = no telephony manager available
 * 1 = unknown telephony
 * 2 = 2g internet
 * 3 = 3g internet
 * 4 = 4g internet
 * 5 = 5g internet
 */
@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("SwitchIntDef")
@IntRange(from = 0, to = 5)
@RequiresPermission(READ_PHONE_STATE)
fun Context.deviceNetworkType(): Int {
  val NO_TELEPHONY = 0
  val UNKNOWN = 1
  val NET2G = 2
  val NET3G = 3
  val NET4G = 4
  val NET5G = 5
  val telephonyManager = telephonyManager ?: return NO_TELEPHONY
  
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && telephonyManager.dataNetworkType == TelephonyManager.NETWORK_TYPE_NR) { //New Radio
    return NET5G
  }
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
    when (telephonyManager.dataNetworkType) {
      TelephonyManager.NETWORK_TYPE_IWLAN      //Industrial Wireless Local Area Network, transfer IP data between a mobile device and operator’s core network through a Wi-Fi access
      -> return NET4G
      TelephonyManager.NETWORK_TYPE_TD_SCDMA   //3G Time division synchronous code division multiple access, China-Mobile standard
      -> return NET3G
      TelephonyManager.NETWORK_TYPE_GSM        // Global System for Mobile Communications, standard for 2g
      -> return NET2G
    }
  }
  
  return when (telephonyManager.dataNetworkType) {
    TelephonyManager.NETWORK_TYPE_GPRS,     //2G(2.5) General Packet Radio Service 114 kbps
    TelephonyManager.NETWORK_TYPE_EDGE,     //2G(2.75G) Enhanced Data Rate for GSM Evolution 384 kbps
    TelephonyManager.NETWORK_TYPE_CDMA,     //2G Code Division Multiple Access  ~ 14-64 kbps
    TelephonyManager.NETWORK_TYPE_1xRTT,    //2G CDMA2000 1xRTT (RTT - Radio transmission technology) 144 kbps,
    TelephonyManager.NETWORK_TYPE_IDEN      //2G Integrated Dispatch Enhanced Networks (part of 2G from Wikipedia)  ~25 kbps
    -> NET2G
    
    TelephonyManager.NETWORK_TYPE_UMTS,     //3G WCDMA 3G Universal Mobile Telecommunication System  ~ 400-7000 kbps
    TelephonyManager.NETWORK_TYPE_EVDO_0,   //3G (EVDO CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4 mbps belong 3G
    TelephonyManager.NETWORK_TYPE_EVDO_A,   //3G 1.8mbps - 3.1mbps belong 3G ~ 3.5G
    TelephonyManager.NETWORK_TYPE_EVDO_B,   //3G EV-DO Rev.B 14.7Mbps down 3.5G
    TelephonyManager.NETWORK_TYPE_HSDPA,    //3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
    TelephonyManager.NETWORK_TYPE_HSUPA,    //3.5G High Speed Uplink Packet Access 1.4 - 5.8 mbps
    TelephonyManager.NETWORK_TYPE_HSPA,     //3G (part HSDPA,HSUPA) High Speed Packet Access    ~ 700-1700 kbps
    TelephonyManager.NETWORK_TYPE_EHRPD,    //3G CDMA2000 to LTE 4G Evolved High Rate Packet Data   ~ 1-2 Mbps
    TelephonyManager.NETWORK_TYPE_HSPAP     //3G HSPAP is faster than HSDPA  ~ 10-20 Mbps
    -> NET3G
    
    TelephonyManager.NETWORK_TYPE_LTE       //4G Long Term Evolution FDD-LTE and TDD-LTE, 3G transition, upgraded LTE Advanced is 4G ~ 10+ Mbps
    -> NET4G
    else -> UNKNOWN
  }
}

/*
<permission android:name="android.permission.QUERY_ALL_PACKAGES" />

<queries>
    <intent>
        <action android:name="android.intent.action.MAIN" />
    </intent>
</queries>
 */
@SuppressLint("QueryPermissionsNeeded")
inline fun Context.openEmail(
  title: String = "Select an email client", onCantHandleAction: () -> Unit = {}
) {
  try {
    val emailClientNames = ArrayList<String>()
    val emailClientPackageNames = ArrayList<String>()
    // finding list of email clients that support send email
    val intent = Intent(
      Intent.ACTION_SENDTO, Uri.fromParts("mailto", "abc@gmail.com", null)
    )
    val packages = packageManager.queryIntentActivities(intent, 0)
    if (packages.isNotEmpty()) {
      for (resolveInfo in packages) {
        // finding the package name
        val packageName = resolveInfo.activityInfo.packageName
        emailClientNames.add(resolveInfo.loadLabel(packageManager).toString())
        emailClientPackageNames.add(packageName)
      }
      // a selection dialog  for the email clients
      val builder = AlertDialog.Builder(this)
      builder.setTitle(title)
      builder.setItems(emailClientNames.toTypedArray()) { _, which ->
        // on click we launch the right package
        val theIntent = packageManager.getLaunchIntentForPackage(emailClientPackageNames[which])
        startActivity(theIntent)
      }
      val dialog = builder.create()
      dialog.show()
    }
  } catch (e: ActivityNotFoundException) {
    // Show error message
    onCantHandleAction()
  }
}

inline fun Context.sendEmail(
  myEmail: String,
  subject: String,
  text: String,
  title: String = "Send an e-mail...",
  onCantHandleAction: () -> Unit = {}
) {
  val selectorIntent = Intent(Intent.ACTION_SENDTO)
  selectorIntent.data = Uri.parse("mailto:")
  
  val i = Intent(Intent.ACTION_SEND)
//    i.type = "message/rfc822"
  i.selector = selectorIntent
  i.putExtra(Intent.EXTRA_EMAIL, arrayOf(myEmail))
  i.putExtra(Intent.EXTRA_TEXT, text)
  i.putExtra(Intent.EXTRA_SUBJECT, subject)
  
  try {
    startActivity(Intent.createChooser(i, title))
  } catch (ex: ActivityNotFoundException) {
    onCantHandleAction()
  }
}

inline fun Context.sendEmail(
  emails: Array<String>,
  subject: String,
  text: String,
  title: String = "Send an e-mail...",
  onCantHandleAction: () -> Unit = {}
) {
  val selectorIntent = Intent(Intent.ACTION_SENDTO)
  selectorIntent.data = Uri.parse("mailto:")
  
  val i = Intent(Intent.ACTION_SEND)
//    i.type = "message/rfc822"
  i.selector = selectorIntent
  i.putExtra(Intent.EXTRA_EMAIL, emails)
  i.putExtra(Intent.EXTRA_TEXT, text)
  i.putExtra(Intent.EXTRA_SUBJECT, subject)
  
  try {
    startActivity(Intent.createChooser(i, title))
  } catch (ex: ActivityNotFoundException) {
    onCantHandleAction()
  }
}

@SuppressLint("QueryPermissionsNeeded")
inline fun Context.shareText(
  text: String, subject: String = "", onCantHandleAction: () -> Unit = {}
) {
  val intent = Intent(Intent.ACTION_SEND)
  intent.type = "text/plain"
  intent.putExtra(Intent.EXTRA_SUBJECT, subject)
  intent.putExtra(Intent.EXTRA_TEXT, text)
  
  if (intent.resolveActivity(packageManager) != null) {
    startActivity(Intent.createChooser(intent, null))
  } else {
    onCantHandleAction()
  }
}

@SuppressLint("QueryPermissionsNeeded")
inline fun Context.sendSMS(number: String, text: String = "", onCantHandleAction: () -> Unit = {}) {
  val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
  intent.putExtra("sms_body", text)
  if (intent.resolveActivity(packageManager) != null) {
    startActivity(Intent.createChooser(intent, null))
  } else {
    onCantHandleAction()
  }
}

fun Context.watchYoutube(id: String) {
  val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
  val webIntent = Intent(
    Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$id")
  )
  try {
    this.startActivity(appIntent)
  } catch (ex: ActivityNotFoundException) {
    this.startActivity(webIntent)
  }
}

@SuppressLint("QueryPermissionsNeeded")
fun Context?.openGoogleMaps(address: String?) {
  this ?: return
  if (isEmpty(address)) return
  
  val gmmIntentUri = Uri.parse("geo:0,0?q=${address?.trim()}")
  val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
  mapIntent.`package` = "com.google.android.apps.maps"
  
  if (mapIntent.resolveActivity(packageManager) != null) {
    startActivity(mapIntent)
  }
}

fun Context.copyToClipboard(text: String) {
  val clipboard = clipboardManager
  val clip = ClipData.newPlainText("label", text)
  clipboard?.setPrimaryClip(clip)
}

fun Context.getTextFromClipboard(): CharSequence {
  val clipData = clipboardManager?.primaryClip
  if (clipData != null && clipData.itemCount > 0) return clipData.getItemAt(0).coerceToText(this)
  
  return ""
}

fun Context.getUriFromClipboard(): Uri? {
  val clipData = clipboardManager?.primaryClip
  if (clipData != null && clipData.itemCount > 0) return clipData.getItemAt(0).uri
  
  return null
}

fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)

fun Context.getDrawableCompat(drawable: Int) = ContextCompat.getDrawable(this, drawable)

//Chỉnh thời gian tắt màn hình
inline var Context.sleepDuration: Int
  @RequiresPermission(Manifest.permission.WRITE_SETTINGS) set(value) {
    Settings.System.putInt(
      this.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, value
    )
  }
  get() {
    return try {
      Settings.System.getInt(
        this.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT
      )
    } catch (e: Settings.SettingNotFoundException) {
      e.printStackTrace()
      -123
    }
  }

inline val Context.displayWidth: Int
  get() = resources.displayMetrics.widthPixels

inline val Context.displayHeight: Int
  get() = resources.displayMetrics.heightPixels

inline val Context.displayMetrics: DisplayMetrics
  get() = resources.displayMetrics

inline val Context.inflater: LayoutInflater
  get() = LayoutInflater.from(this)

val Context.isDarkTheme
  get() = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
    Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> false
    else -> true
  }

fun Context.restartApp() {
  val restartIntent = packageManager.getLaunchIntentForPackage(packageName)!!
  restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
  startActivity(restartIntent)
}
