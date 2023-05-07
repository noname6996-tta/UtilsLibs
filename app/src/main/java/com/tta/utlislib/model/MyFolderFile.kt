package com.ntduc.utils.model

data class MyFolderFile(
  var folder: MyFile = MyFile(),
  var isShowList: Boolean = false,
  var list: ArrayList<MyFile> = arrayListOf()
) {
  override fun equals(other: Any?): Boolean {
    return this.folder.title == (other as MyFolderFile).folder.title
  }
}