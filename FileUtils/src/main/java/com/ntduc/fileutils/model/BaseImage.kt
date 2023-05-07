package com.ntduc.fileutils.model

open class BaseImage(
  id: Long? = null,
  title: String? = null,
  displayName: String? = null,
  mimeType: String? = null,
  size: Long? = null,
  dateAdded: Long? = null,
  dateModified: Long? = null,
  data: String? = null,
  var height: Long? = null,
  var width: Long? = null
) : BaseFile(id, title, displayName, mimeType, size, dateAdded, dateModified, data)