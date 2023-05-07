package com.tta.apputils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.tta.apputils.model.BaseApp
import java.io.File


@SuppressLint("QueryPermissionsNeeded")
fun Context.getApps(
    isSystem: Boolean = false
): List<BaseApp> {
    val apps = ArrayList<BaseApp>()
    val packs = this.packageManager.getInstalledApplications(0)

    for (pack in packs) {
        if (isSystem || !isSystemApplication(pack)) {
            val newInfo = BaseApp()
            try {
                newInfo.name = pack.loadLabel(this.packageManager).toString()
            } catch (_: Exception) {
            }
            newInfo.packageName = pack.packageName
            try {
                newInfo.icon = pack.loadIcon(this.packageManager)
            } catch (_: Exception) {
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    newInfo.category =
                        ApplicationInfo.getCategoryTitle(this, pack.category).toString()
                } catch (_: Exception) {
                }
            }
            newInfo.dataDir = pack.dataDir
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                newInfo.minSdkVersion = pack.minSdkVersion
            }
            newInfo.targetSdkVersion = pack.targetSdkVersion
            newInfo.processName = pack.processName
            newInfo.nativeLibraryDir = pack.nativeLibraryDir
            newInfo.publicSourceDir = pack.publicSourceDir
            newInfo.sourceDir = pack.sourceDir
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                newInfo.splitNames = pack.splitNames
            }
            newInfo.splitPublicSourceDirs = pack.splitPublicSourceDirs
            newInfo.splitSourceDirs = pack.splitSourceDirs
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                newInfo.storageUuid = pack.storageUuid
            }
            newInfo.taskAffinity = pack.taskAffinity
            newInfo.uid = pack.uid
            apps.add(newInfo)
        }
    }
    return apps
}

fun Activity.openApp(packageName: String) {
    var intent: Intent? = packageManager.getLaunchIntentForPackage(packageName)
    if (intent == null) {
        intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=$packageName")
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivityForResult(intent, REQUEST_CODE_OPEN_APP)
}

/*
* <uses-permission android:name="android.permission.REQUEST_DELETE_PACKAGES" />
 */
fun Activity.uninstallApp(packageName: String) {
    val intent = Intent(
        Intent.ACTION_DELETE,
        Uri.fromParts("package", packageName, null)
    )
    startActivityForResult(intent, REQUEST_CODE_UNINSTALL_APP)
}

/*
* <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
 */
fun Activity.installApk(path: String, authority: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val uri = FileProvider.getUriForFile(this, authority, File(path))
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        startActivityForResult(intent, REQUEST_CODE_INSTALL_APP)
    } else {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(File(path)), "application/vnd.android.package-archive")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivityForResult(intent, REQUEST_CODE_INSTALL_APP)
    }
}

private fun isSystemApplication(appInfo: ApplicationInfo): Boolean {
    return appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
}

const val REQUEST_CODE_OPEN_APP = 100
const val REQUEST_CODE_INSTALL_APP = 200
const val REQUEST_CODE_UNINSTALL_APP = 300