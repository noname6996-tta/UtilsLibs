package com.ntduc.utils.file_utils.get_all_image.activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntduc.datetimeutils.getDateTimeFromMillis
import com.ntduc.datetimeutils.isToday
import com.ntduc.datetimeutils.isYesterday
import com.ntduc.fileutils.getImages
import com.ntduc.utils.model.MyFile
import com.ntduc.utils.model.MyFolderImage
import com.ntduc.utils.model.MyImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class GetAllImageViewModel : ViewModel() {
  var listAllPhoto: MutableLiveData<List<MyFolderImage>> = MutableLiveData(listOf())
  var isLoadListAllPhoto = false
  
  fun loadAllPhoto(context: Context) {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val images = context.getImages()
        val temp = images.sortedWith { o1, o2 ->
          o2.dateModified!!.compareTo(o1.dateModified!!)
        }
        val result = ArrayList<MyImage>()
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
          val myImage = MyImage(myFile, it.height, it.width)
          result.add(myImage)
        }
        loadRecentPhoto(result)
      }
    }
  }
  
  private fun loadRecentPhoto(list: List<MyImage>) {
    val listRecent = filterRecentPhoto(list)
    listAllPhoto.postValue(listRecent)
    isLoadListAllPhoto = true
  }
  
  private fun filterRecentPhoto(photos: List<MyImage>): ArrayList<MyFolderImage> {
    val listFolderPhoto = ArrayList<MyFolderImage>()
    for (photo in photos) {
      val pos = checkFolderPhotoByTime(photo, listFolderPhoto)
      if (pos >= 0) {
        listFolderPhoto[pos].list.add(photo)
      } else {
        val file = File(photo.myFile!!.data!!)
        val folder = MyFile(
          getNameFolderByTime(file.lastModified()),
          getNameFolderByTime(file.lastModified()),
          "",
          file.length(),
          file.lastModified(),
          file.lastModified(),
          file.parentFile?.path ?: ""
        )
        val folderPhoto = MyFolderImage(folder)
        folderPhoto.list.add(photo)
        
        listFolderPhoto.add(folderPhoto)
      }
    }
    return listFolderPhoto
  }
  
  private fun checkFolderPhotoByTime(
    photo: MyImage,
    listFolderPhoto: ArrayList<MyFolderImage>
  ): Int {
    for (i in listFolderPhoto.indices) {
      if (getNameFolderByTime(photo.myFile!!.dateModified) == listFolderPhoto[i].folder.title) {
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