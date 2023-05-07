package com.ntduc.utils.model

data class MyFolderAudio(
  var folder: MyFile = MyFile(),
  var isShowList: Boolean = false,
  var list: ArrayList<MyAudio> = arrayListOf()
) {
  override fun equals(other: Any?): Boolean {
    return this.folder.title == (other as MyFolderAudio).folder.title
  }
}