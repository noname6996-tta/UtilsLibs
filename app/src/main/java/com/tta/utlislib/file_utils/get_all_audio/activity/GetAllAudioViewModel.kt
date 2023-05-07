package com.ntduc.utils.file_utils.get_all_audio.activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntduc.datetimeutils.getDateTimeFromMillis
import com.ntduc.datetimeutils.isToday
import com.ntduc.datetimeutils.isYesterday
import com.ntduc.fileutils.getAudios
import com.ntduc.utils.model.MyAudio
import com.ntduc.utils.model.MyFile
import com.ntduc.utils.model.MyFolderAudio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class GetAllAudioViewModel : ViewModel() {
  var listAllAudio: MutableLiveData<List<MyFolderAudio>> = MutableLiveData(listOf())
  var isLoadListAllAudio = false
  
  fun loadAllAudio(context: Context) {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val audios = context.getAudios()
        val temp = audios.sortedWith { o1, o2 ->
          o2.dateModified!!.compareTo(o1.dateModified!!)
        }
        val result = ArrayList<MyAudio>()
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
          val myAudio = MyAudio(myFile, it.album, it.artist, it.duration)
          result.add(myAudio)
        }
        loadRecentAudio(result)
      }
    }
  }
  
  private fun loadRecentAudio(list: List<MyAudio>) {
    val listRecent = filterRecentAudio(list)
    listAllAudio.postValue(listRecent)
    isLoadListAllAudio = true
  }
  
  private fun filterRecentAudio(audios: List<MyAudio>): ArrayList<MyFolderAudio> {
    val listFolderAudio = ArrayList<MyFolderAudio>()
    for (audio in audios) {
      val pos = checkFolderAudioByTime(audio, listFolderAudio)
      if (pos >= 0) {
        listFolderAudio[pos].list.add(audio)
      } else {
        val file = File(audio.myFile!!.data!!)
        val folder = MyFile(
          getNameFolderByTime(file.lastModified()),
          getNameFolderByTime(file.lastModified()),
          "",
          file.length(),
          file.lastModified(),
          file.lastModified(),
          file.parentFile?.path ?: ""
        )
        val folderAudio = MyFolderAudio(folder)
        folderAudio.list.add(audio)
        
        listFolderAudio.add(folderAudio)
      }
    }
    return listFolderAudio
  }
  
  private fun checkFolderAudioByTime(
    audio: MyAudio,
    listFolderAudio: ArrayList<MyFolderAudio>
  ): Int {
    for (i in listFolderAudio.indices) {
      if (getNameFolderByTime(audio.myFile!!.dateModified) == listFolderAudio[i].folder.title) {
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