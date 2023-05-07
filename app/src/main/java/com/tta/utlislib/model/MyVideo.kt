package com.ntduc.utils.model

open class MyVideo(
  var myFile: MyFile? = null,
  var height: Long? = null,
  var width: Long? = null,
  var album: String? = null,
  var artist: String? = null,
  var duration: Long? = null,
  var bucketID: Long? = null,
  var bucketDisplayName: String? = null,
  var resolution: String? = null
)