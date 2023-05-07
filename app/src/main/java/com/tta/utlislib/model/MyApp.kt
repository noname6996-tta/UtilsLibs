package com.ntduc.utils.model

import android.graphics.drawable.Drawable
import java.util.*

open class MyApp(
  var name: String? = null,
  var packageName: String? = null,
  var icon: Drawable? = null,
  var category: String? = null,
  var dataDir: String? = null,
  var minSdkVersion: Int? = null,
  var targetSdkVersion: Int? = null,
  var nativeLibraryDir: String? = null,
  var processName: String? = null,
  var publicSourceDir: String? = null,
  var sourceDir: String? = null,
  var splitNames: Array<String>? = null,
  var splitPublicSourceDirs: Array<String>? = null,
  var splitSourceDirs: Array<String>? = null,
  var storageUuid: UUID? = null,
  var taskAffinity: String? = null,
  var uid: Int? = null
)