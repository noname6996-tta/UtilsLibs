package com.ntduc.contextutils

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.*
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.app.usage.UsageStatsManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.content.RestrictionsManager
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.media.projection.MediaProjectionManager
import android.media.session.MediaSessionManager
import android.media.tv.TvInputManager
import android.net.ConnectivityManager
import android.net.Uri
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.nfc.NfcManager
import android.os.*
import android.os.storage.StorageManager
import android.print.PrintManager
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.inputmethod.InputMethodManager
import android.view.textservice.TextServicesManager
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

/**
 * Get inputManager for Context.
 */
inline val Context.inputManager: InputMethodManager?
  get() = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager

/**
 * Get notificationManager for Context.
 */
inline val Context.notificationManager: NotificationManager?
  get() = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager

/**
 * Get keyguardManager for Context.
 */
inline val Context.keyguardManager: KeyguardManager?
  get() = getSystemService(KEYGUARD_SERVICE) as? KeyguardManager


/**
 * Get telephonyManager for Context.
 */
inline val Context.telephonyManager: TelephonyManager?
  get() = getSystemService(TELEPHONY_SERVICE) as? TelephonyManager


/**
 * Get devicePolicyManager for Context.
 */
inline val Context.devicePolicyManager: DevicePolicyManager?
  get() = getSystemService(DEVICE_POLICY_SERVICE) as? DevicePolicyManager


/**
 * Get connectivityManager for Context.
 */
inline val Context.connectivityManager: ConnectivityManager?
  get() = getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager


/**
 *Get alarmManager for Context.
 */
inline val Context.alarmManager: AlarmManager?
  get() = getSystemService(ALARM_SERVICE) as? AlarmManager


/**
 * Get clipboardManager for Context.
 */
inline val Context.clipboardManager: ClipboardManager?
  get() = getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager


/**
 * Get jobScheduler for Context.
 */
inline val Context.jobScheduler: JobScheduler?
  get() = getSystemService(JOB_SCHEDULER_SERVICE) as? JobScheduler


/**
 * Get notification for Context.
 */
inline fun Context.notification(
  body: NotificationCompat.Builder.() -> Unit,
  channelID: String
): Notification {
  val builder = NotificationCompat.Builder(this, channelID)
  builder.body()
  return builder.build()
}


/**
 * Get ActivityManager
 */
inline val Context.activityManager: ActivityManager
  get() = getSystemService(ACTIVITY_SERVICE) as ActivityManager

/**
 * Get AppWidgetManager
 */
inline val Context.appWidgetManager
  get() = getSystemService(APPWIDGET_SERVICE) as AppWidgetManager

/**
 * Get InputMethodManager
 */
inline val Context.inputMethodManager: InputMethodManager
  get() = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

/**
 * Get BluetoothManager
 */
inline val Context.bluetoothManager: BluetoothManager
  get() = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager

/**
 * Get AudioManager
 */
inline val Context.audioManager
  get() = getSystemService(AUDIO_SERVICE) as AudioManager

/**
 * Get BatteryManager
 */
inline val Context.batteryManager
  get() = getSystemService(BATTERY_SERVICE) as BatteryManager

/**
 * Get CameraManager
 */
inline val Context.cameraManager
  get() = getSystemService(CAMERA_SERVICE) as CameraManager

/**
 * Get Vibrator
 */
inline val Context.vibrator
  get() = getSystemService(VIBRATOR_SERVICE) as Vibrator


/**
 * Dial a number
 */
fun Context.dial(tel: String?) = startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tel")))

/**
 * Get if screen is in landscape mode
 */
val Context.isLandscape get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

/**
 * Get if screen is in portrait mode
 */
val Context.isPortrait get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

val Context.getDeviceUsableMemory: Long
  get() {
    val memoryInfo = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo)
    return memoryInfo.availMem / (1024 * 1024)
  }

inline val Context.isFinishing: Boolean
  get() = (this as? Activity)?.isFinishing ?: false

inline val Context.isRtl: Boolean
  get() = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

val Context.isTablet: Boolean
  get() {
    return this.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
  }

fun Context.getAnimatedVectorDrawable(@DrawableRes id: Int): AnimatedVectorDrawableCompat? {
  return AnimatedVectorDrawableCompat.create(this, id)
}


val Context.networkOperatorName: String?
  get() = telephonyManager?.networkOperatorName

val Context.accessibilityManager
  get() = getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager?

val Context.accountManager
  get() = getSystemService(ACCOUNT_SERVICE) as AccountManager?


val Context.appOpsManager
  get() = getSystemService(APP_OPS_SERVICE) as AppOpsManager?

val Context.usageStatsManager
  @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1) get() = getSystemService(USAGE_STATS_SERVICE) as? UsageStatsManager

val Context.captioningManager
  get() = getSystemService(CAPTIONING_SERVICE) as CaptioningManager?


val Context.consumerIrManager
  get() = getSystemService(CONSUMER_IR_SERVICE) as ConsumerIrManager?

val Context.displayManager
  get() = getSystemService(DISPLAY_SERVICE) as DisplayManager?

val Context.downloadManager
  get() = getSystemService(DOWNLOAD_SERVICE) as DownloadManager?

val Context.dropBoxManager
  get() = getSystemService(DROPBOX_SERVICE) as DropBoxManager?


val Context.launcherApps
  get() = getSystemService(LAUNCHER_APPS_SERVICE) as LauncherApps?

val Context.layoutInflater
  get() = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?

val Context.locationManager
  get() = getSystemService(LOCATION_SERVICE) as LocationManager?

val Context.mediaProjectionManager
  get() = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?

val Context.mediaRouter
  get() = getSystemService(MEDIA_ROUTER_SERVICE) as MediaRouter?

val Context.mediaSessionManager
  get() = getSystemService(MEDIA_SESSION_SERVICE) as MediaSessionManager?

val Context.nfcManager
  get() = getSystemService(NFC_SERVICE) as NfcManager?

val Context.nsdManager
  get() = getSystemService(NSD_SERVICE) as NsdManager?

val Context.powerManager
  get() = getSystemService(POWER_SERVICE) as PowerManager?

val Context.printManager
  get() = getSystemService(PRINT_SERVICE) as PrintManager?

val Context.restrictionsManager
  get() = getSystemService(RESTRICTIONS_SERVICE) as RestrictionsManager?

val Context.searchManager
  get() = getSystemService(SEARCH_SERVICE) as SearchManager?

val Context.sensorManager
  get() = getSystemService(SENSOR_SERVICE) as SensorManager?

val Context.storageManager
  get() = getSystemService(STORAGE_SERVICE) as StorageManager?

val Context.telecomManager
  get() = getSystemService(TELECOM_SERVICE) as TelecomManager?


val Context.textServicesManager
  get() = getSystemService(TEXT_SERVICES_MANAGER_SERVICE) as TextServicesManager?

val Context.tvInputManager
  get() = getSystemService(TV_INPUT_SERVICE) as TvInputManager?

val Context.uiModeManager
  get() = getSystemService(UI_MODE_SERVICE) as UiModeManager?

val Context.usbManager
  get() = getSystemService(USB_SERVICE) as UsbManager?

val Context.userManager
  get() = getSystemService(USER_SERVICE) as UserManager?

val Context.wallpaperManager
  @SuppressLint("ServiceCast") get() = getSystemService(WALLPAPER_SERVICE) as WallpaperManager?

val Context.wifiP2pManager
  get() = getSystemService(WIFI_P2P_SERVICE) as WifiP2pManager?

val Context.wifiManager
  get() = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager?

val Context.windowManager
  get() = getSystemService(WINDOW_SERVICE) as WindowManager?

inline val Context.configuration: Configuration
  get() = resources.configuration

inline val Context.isOneHanded: Boolean
  get() {
    return isPortrait && configuration.smallestScreenWidthDp < 600
  }


fun Context.isPermissionGranted(permission: String): Boolean =
  ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.arePermissionsGranted(vararg permissions: String): Boolean = permissions.all {
  ContextCompat.checkSelfPermission(
    this,
    it
  ) == PackageManager.PERMISSION_GRANTED
}


val Context.getExitReason
  @RequiresApi(Build.VERSION_CODES.R) get() = activityManager.getHistoricalProcessExitReasons(
    packageName,
    0,
    1
  )

@RequiresApi(Build.VERSION_CODES.R)
fun Context.getExitReasons(pid: Int = 0, maxRes: Int = 1) =
  activityManager.getHistoricalProcessExitReasons(packageName, pid, maxRes)