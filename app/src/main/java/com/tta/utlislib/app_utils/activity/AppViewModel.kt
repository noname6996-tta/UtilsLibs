package com.ntduc.utils.app_utils.activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntduc.fileutils.getFiles
import com.ntduc.utils.model.MyApk
import com.ntduc.utils.model.MyApp
import com.ntduc.utils.model.MyFile
import com.tta.apputils.getApps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel : ViewModel() {
  var listAllApp: MutableLiveData<List<MyApp>> = MutableLiveData(listOf())
  var isLoadListAllApp = false
  
  fun loadAllApp(context: Context) {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val apps = context.getApps()
        val result = ArrayList<MyApp>()
        apps.forEach {
          val myApp = MyApp(
            name = it.name,
            packageName = it.packageName,
            icon = it.icon,
            category = it.category,
            dataDir = it.dataDir,
            minSdkVersion = it.minSdkVersion,
            targetSdkVersion = it.targetSdkVersion,
            nativeLibraryDir = it.nativeLibraryDir,
            processName = it.processName,
            publicSourceDir = it.publicSourceDir,
            sourceDir = it.sourceDir,
            splitNames = it.splitNames,
            splitPublicSourceDirs = it.splitPublicSourceDirs,
            splitSourceDirs = it.splitSourceDirs,
            storageUuid = it.storageUuid,
            taskAffinity = it.taskAffinity,
            uid = it.uid
          )
          result.add(myApp)
        }
        listAllApp.postValue(result)
        isLoadListAllApp = true
      }
    }
  }
  
  var listAllApk: MutableLiveData<List<MyApk>> = MutableLiveData(listOf())
  var isLoadListAllApk = false
  
  fun loadAllApk(context: Context) {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val apps = context.getFiles(types = listOf("apk"))
        val result = ArrayList<MyApk>()
        apps.forEach {
          val myFile = MyFile(
            it.title,
            it.displayName,
            it.mimeType,
            it.size,
            it.dateAdded,
            it.dateModified,
            it.data
          )
          result.add(MyApk(myFile = myFile))
        }
        listAllApk.postValue(result)
        isLoadListAllApk = true
      }
    }
  }
}