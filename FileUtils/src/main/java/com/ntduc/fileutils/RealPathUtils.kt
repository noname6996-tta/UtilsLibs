package com.ntduc.fileutils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import com.ntduc.fileutils.NanoIdUtils.randomNanoId
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.security.SecureRandom
import java.util.*

fun Uri.getRealPath(context: Context): String? {
  // DocumentProvider
  val path: String? = try {
    if (DocumentsContract.isDocumentUri(context, this)) {
      if (isExternalStorageDocument()) {
        val docId = DocumentsContract.getDocumentId(this)
        val split = docId.split(":").toTypedArray()
        val type = split[0]
        if ("primary".equals(type, ignoreCase = true)) {
          if (split.size > 1) {
            Environment.getExternalStorageDirectory().toString() + "/" + split[1]
          } else {
            Environment.getExternalStorageDirectory().toString() + "/"
          }
        } else {
          val external = context.externalMediaDirs
          for (f in external) {
            val filePath = f.absolutePath
            if (filePath.contains(type)) {
              return filePath.substring(0, filePath.indexOf("Android")) + split[1]
            }
          }
          return "storage" + "/" + docId.replace(":", "/")
        }
      } else if (isMediaDocument()) {
        getDownloadsDocumentPath(context, true)
      } else if (isRawDownloadsDocument()) {
        getDownloadsDocumentPath(context, true)
      } else if (isDownloadsDocument()) {
        getDownloadsDocumentPath(context, false)
      } else {
        loadToCacheFile(context)
      }
    } else {
      loadToCacheFile(context)
    }
  } catch (e: Exception) {
    return null
  }
  return path
}

private fun Uri.loadToCacheFile(context: Context): String? {
  try {
    val pathFile = getPathFile(context.contentResolver)
    if (checkFileExist(pathFile)) {
      return pathFile
    }
    var nameFile = getNameFile(context.contentResolver)
    if (nameFile == null || nameFile.isEmpty()) {
      return null
    }
    var suffix = ""
    if (nameFile.contains(".")) {
      try {
        suffix = nameFile.substring(nameFile.lastIndexOf("."))
        nameFile = nameFile.substring(0, nameFile.lastIndexOf("."))
      } catch (ignored: Exception) {
      }
    }
    nameFile += "_"
    if (nameFile != null) {
      if (nameFile.length < 4) {
        nameFile += randomNanoId(
          SecureRandom(), "01234".toCharArray(), 4 - nameFile.length
        )
      }
    }
    val rootDir = context.filesDir
    val containTempFileDir = File(rootDir, "Temp_folder_123123")
    if (containTempFileDir.exists()) {
      deleteRecursive(containTempFileDir)
    }
    if (!containTempFileDir.isDirectory || !containTempFileDir.exists()) {
      try {
        if (!containTempFileDir.mkdir()) {
          return null
        }
      } catch (ignored: Exception) {
        return null
      }
    }
    val newFile = nameFile?.let { File.createTempFile(it, suffix, containTempFileDir) }
    if (createFileFromStream(context, newFile)) {
      return newFile?.absolutePath
    }
  } catch (e: Exception) {
    e.printStackTrace()
  }
  return null
}

private fun Uri.getNameFile(cr: ContentResolver): String? {
  try {
    @SuppressLint("Recycle") val c = cr.query(this, null, null, null, null)
    if (c != null) {
      c.moveToFirst()
      val fileNameColumnId = c.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
      if (fileNameColumnId >= 0) {
        val attachmentFileName = c.getString(fileNameColumnId)
        return if (attachmentFileName == null || attachmentFileName.isEmpty()) null else attachmentFileName
      }
    }
  } catch (ignored: Exception) {
  }
  return null
}

private fun Uri.getPathFile(cr: ContentResolver): String? {
  try {
    @SuppressLint("Recycle") val c = cr.query(this, null, null, null, null)
    if (c != null) {
      c.moveToFirst()
      val fileNameColumnId = c.getColumnIndex(MediaStore.MediaColumns.DATA)
      if (fileNameColumnId >= 0) {
        val attachmentFileDir = c.getString(fileNameColumnId)
        return if (attachmentFileDir == null || attachmentFileDir.isEmpty()) null else attachmentFileDir
      }
    }
  } catch (e: Exception) {
  }
  return null
}

private fun Uri.createFileFromStream(context: Context, destination: File?): Boolean {
  try {
    context.contentResolver.openInputStream(this).use { ins ->
      val os: OutputStream = FileOutputStream(destination)
      val buffer = ByteArray(4096)
      var length: Int
      return if (ins != null) {
        while (ins.read(buffer).also { length = it } > 0) {
          os.write(buffer, 0, length)
        }
        os.flush()
        true
      } else {
        false
      }
    }
  } catch (ex: Exception) {
    return false
  }
}

private fun Uri.getDownloadsDocumentPath(context: Context, hasSubFolders: Boolean): String? {
  val fileName = getFilePath(context)
  val subFolderName = if (hasSubFolders) getSubFolders() else ""
  var filePath = ""
  if (fileName != null) {
    filePath = if (subFolderName != null) Environment.getExternalStorageDirectory()
      .toString() + "/Download/" + subFolderName + fileName else Environment.getExternalStorageDirectory()
      .toString() + "/Download/" + fileName
  }
  if (filePath.isNotEmpty() && checkFileExist(filePath)) {
    return filePath
  }
  val id = DocumentsContract.getDocumentId(this)
  var path: String? = null
  if (!TextUtils.isEmpty(id)) {
    if (id.startsWith("raw:")) {
      return id.replaceFirst("raw:".toRegex(), "")
    }
    val split = id.split(":".toRegex())
    var contentUri: Uri
    when (split[0]) {
      "image" -> {
        try {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
          val selection = "_id=?"
          val selectionArgs = arrayOf(
            split[1]
          )
          path = contentUri.getDataColumn(context, selection, selectionArgs)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
      "video" -> {
        try {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
          val selection = "_id=?"
          val selectionArgs = arrayOf(
            split[1]
          )
          path = contentUri.getDataColumn(context, selection, selectionArgs)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
      "audio" -> {
        try {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
          val selection = "_id=?"
          val selectionArgs = arrayOf(
            split[1]
          )
          path = contentUri.getDataColumn(context, selection, selectionArgs)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
      "document" -> {
        try {
          contentUri = MediaStore.Files.getContentUri("external")
          val selection = "_id=?"
          val selectionArgs = arrayOf(
            split[1]
          )
          path = contentUri.getDataColumn(context, selection, selectionArgs)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
      else -> {
        val contentUriPrefixesToTry = listOf(
          "content://downloads/public_downloads", "content://downloads/my_downloads"
        )
        for (contentUriPrefix in contentUriPrefixesToTry) {
          try {
            contentUri = ContentUris.withAppendedId(
              Uri.parse(contentUriPrefix), id.toLong()
            )
            val selection = "_id=?"
            val selectionArgs = arrayOf(
              split[1]
            )
            path = contentUri.getDataColumn(context, selection, selectionArgs)
          } catch (e: NumberFormatException) {
            e.printStackTrace()
          }
        }
      }
    }
  }
  return path
}

private fun Uri.getSubFolders(): String? {
  val replaceChars = this.toString().replace("%2F", "/").replace("%20", " ").replace("%3A", ":")
  // searches for "Download" to get the directory path
  // for example, if the file is inside a folder "test" in the Download folder, this method
  // returns "test/"
  val components = replaceChars.split("/").toTypedArray()
  var sub5 = ""
  var sub4 = ""
  var sub3 = ""
  var sub2 = ""
  var sub1 = ""
  if (components.size >= 2) {
    sub5 = components[components.size - 2]
  }
  if (components.size >= 3) {
    sub4 = components[components.size - 3]
  }
  if (components.size >= 4) {
    sub3 = components[components.size - 4]
  }
  if (components.size >= 5) {
    sub2 = components[components.size - 5]
  }
  if (components.size >= 6) {
    sub1 = components[components.size - 6]
  }
  return if (sub1 == "Download") {
    "$sub2/$sub3/$sub4/$sub5/"
  } else if (sub2 == "Download") {
    "$sub3/$sub4/$sub5/"
  } else if (sub3 == "Download") {
    "$sub4/$sub5/"
  } else if (sub4 == "Download") {
    "$sub5/"
  } else {
    null
  }
}

private fun Uri.getFilePath(context: Context): String? {
  val projection = arrayOf(MediaStore.Files.FileColumns.DISPLAY_NAME)
  context.contentResolver.query(
    this, projection, null, null, null
  ).use { cursor ->
    if (cursor != null && cursor.moveToFirst()) {
      val index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
      return cursor.getString(index)
    }
  }
  return null
}

private fun Uri.getDataColumn(
  context: Context, selection: String, selectionArgs: Array<String>
): String? {
  val column = "_data"
  val projection = arrayOf(
    column
  )
  var path: String? = null
  try {
    context.contentResolver.query(
      this, projection, selection, selectionArgs, null
    ).use { cursor ->
      if (cursor != null && cursor.moveToFirst()) {
        val index = cursor.getColumnIndexOrThrow(column)
        path = cursor.getString(index)
      }
    }
  } catch (e: Exception) {
    Log.e("Error", " " + e.message)
  }
  return path
}

private fun Uri.isExternalStorageDocument(): Boolean =
  "com.android.externalstorage.documents" == authority

private fun Uri.isDownloadsDocument(): Boolean =
  "com.android.providers.downloads.documents" == authority

private fun Uri.isRawDownloadsDocument(): Boolean =
  "com.android.providers.downloads.documents/document/raw" == authority

private fun Uri.isMediaDocument(): Boolean = "com.android.providers.media.documents" == authority

private fun checkFileExist(path: String?): Boolean {
  if (path == null) {
    return false
  }
  val file = File(path)
  return file.exists() && file.length() > 0
}

private fun deleteRecursive(fileOrDirectory: File) {
  try {
    if (fileOrDirectory.isDirectory) {
      for (f in Objects.requireNonNull(fileOrDirectory.listFiles())) {
        deleteRecursive(f)
      }
    }
    fileOrDirectory.delete()
  } catch (e: Exception) {
  }
}