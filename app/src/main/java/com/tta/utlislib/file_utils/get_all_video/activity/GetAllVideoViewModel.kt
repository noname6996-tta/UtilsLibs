package com.ntduc.utils.file_utils.get_all_video.activity

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntduc.datetimeutils.getDateTimeFromMillis
import com.ntduc.datetimeutils.isToday
import com.ntduc.datetimeutils.isYesterday
import com.ntduc.fileutils.getVideos
import com.ntduc.utils.model.MyFile
import com.ntduc.utils.model.MyFolderVideo
import com.ntduc.utils.model.MyVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class GetAllVideoViewModel : ViewModel() {
  var listAllVideo: MutableLiveData<List<MyFolderVideo>> = MutableLiveData(listOf())
  var listUri: MutableLiveData<ArrayList<Uri>> = MutableLiveData(arrayListOf())
  var isLoadListAllVideo = false
  
  fun loadAllVideo(context: Context) {
    listUri.value = arrayListOf()
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val videos = context.getVideos()
        val temp = videos.sortedWith { o1, o2 ->
          o2.dateModified!!.compareTo(o1.dateModified!!)
        }
        val result = ArrayList<MyVideo>()
        val uris = ArrayList<Uri>()
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
          val myVideo = MyVideo(
            myFile,
            it.height,
            it.width,
            it.album,
            it.artist,
            it.duration,
            it.bucketID,
            it.bucketDisplayName,
            it.resolution
          )
          result.add(myVideo)
          
          uris.add(FileProvider.getUriForFile(context, "com.ntduc.utils.provider", File(it.data!!)))
        }
        listUri.postValue(uris)
        loadRecentVideo(result)
      }
    }
  }
  
  private fun loadRecentVideo(list: List<MyVideo>) {
    val listRecent = filterRecentVideo(list)
    listAllVideo.postValue(listRecent)
    isLoadListAllVideo = true
  }
  
  private fun filterRecentVideo(videos: List<MyVideo>): ArrayList<MyFolderVideo> {
    val listFolderVideo = ArrayList<MyFolderVideo>()
    for (video in videos) {
      val pos = checkFolderVideoByTime(video, listFolderVideo)
      if (pos >= 0) {
        listFolderVideo[pos].list.add(video)
      } else {
        val file = File(video.myFile!!.data!!)
        val folder = MyFile(
          getNameFolderByTime(file.lastModified()),
          getNameFolderByTime(file.lastModified()),
          "",
          file.length(),
          file.lastModified(),
          file.lastModified(),
          file.parentFile?.path ?: ""
        )
        val folderVideo = MyFolderVideo(folder)
        folderVideo.list.add(video)
        
        listFolderVideo.add(folderVideo)
      }
    }
    return listFolderVideo
  }
  
  private fun checkFolderVideoByTime(
    video: MyVideo,
    listFolderVideo: ArrayList<MyFolderVideo>
  ): Int {
    for (i in listFolderVideo.indices) {
      if (getNameFolderByTime(video.myFile!!.dateModified) == listFolderVideo[i].folder.title) {
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