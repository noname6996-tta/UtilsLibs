package com.ntduc.utils.model

data class MyFolderImage(
  var folder: MyFile = MyFile(),
  var isShowList: Boolean = false,
  var list: ArrayList<MyImage> = arrayListOf()
) {
  override fun equals(other: Any?): Boolean {
    return this.folder.title == (other as MyFolderImage).folder.title
  }
}