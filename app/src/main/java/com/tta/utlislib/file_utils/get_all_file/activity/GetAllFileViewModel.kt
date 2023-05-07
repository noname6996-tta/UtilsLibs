package com.ntduc.utils.file_utils.get_all_file.activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntduc.datetimeutils.getDateTimeFromMillis
import com.ntduc.datetimeutils.isToday
import com.ntduc.datetimeutils.isYesterday
import com.ntduc.fileutils.getFiles
import com.ntduc.utils.model.MyFile
import com.ntduc.utils.model.MyFolderFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class GetAllFileViewModel : ViewModel() {
  var listAllFile: MutableLiveData<List<MyFolderFile>> = MutableLiveData(listOf())
  var isLoadListAllFile = false
  
  fun loadAllFile(context: Context) {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val images = context.getFiles()
        val temp = images.sortedWith { o1, o2 ->
          o2.dateModified!!.compareTo(o1.dateModified!!)
        }
        val result = ArrayList<MyFile>()
        temp.forEach {
          val myFile = MyFile(
            it.title,
            it.displayName,
            it.mimeType,
            it.size,
            it.dateAdded,
            it.dateModified,
            it.data
          )
          result.add(myFile)
        }
        loadRecentFile(result)
      }
    }
  }
  
  private fun loadRecentFile(list: List<MyFile>) {
    val listRecent = filterRecentFile(list)
    listAllFile.postValue(listRecent)
    isLoadListAllFile = true
  }
  
  private fun filterRecentFile(files: List<MyFile>): ArrayList<MyFolderFile> {
    val listFolderFile = ArrayList<MyFolderFile>()
    for (file in files) {
      val pos = checkFolderPhotoByTime(file, listFolderFile)
      if (pos >= 0) {
        listFolderFile[pos].list.add(file)
      } else {
        val f = File(file.data!!)
        val folder = MyFile(
          getNameFolderByTime(f.lastModified()),
          getNameFolderByTime(f.lastModified()),
          "",
          f.length(),
          f.lastModified(),
          f.lastModified(),
          f.parentFile?.path ?: ""
        )
        val folderFile = MyFolderFile(folder)
        folderFile.list.add(file)
        
        listFolderFile.add(folderFile)
      }
    }
    return listFolderFile
  }
  
  private fun checkFolderPhotoByTime(
    file: MyFile,
    listFolderFile: ArrayList<MyFolderFile>
  ): Int {
    for (i in listFolderFile.indices) {
      if (getNameFolderByTime(file.dateModified) == listFolderFile[i].folder.title) {
        return i
      }
    }
    return -1
  }
  
  private fun getNameFolderByTime(time: Long?): String {
    if (time == null) return "Unknown"
    
    val date = Date(time)
    return if (date.isToday()) {
      "Today"
    } else if (date.isYesterday()) {
      "Yesterday"
    } else {
      getDateTimeFromMillis(time, "MMM dd", Locale.ENGLISH)
    }
  }
}