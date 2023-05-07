package com.ntduc.fileutils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.provider.BaseColumns
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.ntduc.fileutils.constant.FileType
import com.ntduc.fileutils.model.BaseAudio
import com.ntduc.fileutils.model.BaseFile
import com.ntduc.fileutils.model.BaseImage
import com.ntduc.fileutils.model.BaseVideo
import java.io.*

fun File.mimeType(): String? = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

fun File.renameTo(context: Context, newName: String): File? {
  try {
    val pathNew =
      if (this.isDirectory)
        "${this.parentFile?.path ?: ""}/${newName}"
      else
        "${this.parentFile?.path ?: ""}/${newName}.${this.extension}"
    
    val fileNew = File(pathNew)
    if (fileNew.exists()) {
      return null
    }
    
    if (this.renameTo(fileNew)) {
      MediaScannerConnection.scanFile(
        context,
        listOf(this.path, fileNew.path).toTypedArray(),
        null,
        null
      )
      return fileNew
    }
  } catch (e: Exception) {
    e.printStackTrace()
  }
  return null
}

fun File.copyTo(
  context: Context,
  dest: File,
  overwrite: Boolean = false,
  bufferSize: Int = DEFAULT_BUFFER_SIZE
): Boolean {
  if (!this.exists() || !dest.isDirectory) return false
  val pathDest = "${dest.path}/${this.name}"
  
  if (this.isDirectory) {
    val fileDest = File(pathDest)
    if (fileDest.exists()) return false
    fileDest.mkdirs()
    if (this.listFiles() == null) return false
    
    var result = true
    for (file in this.listFiles()!!) {
      if (!file.copyTo(context, fileDest, overwrite, bufferSize)) {
        result = false
      }
    }
    return result
  } else {
    try {
      this.copyTo(File(pathDest), overwrite, bufferSize)
    } catch (e: Exception) {
      e.printStackTrace()
      return false
    }
    
    MediaScannerConnection.scanFile(context, listOf(pathDest).toTypedArray(), null, null)
    return true
  }
}

fun File.moveTo(
  context: Context,
  dest: File,
  overwrite: Boolean = false,
  bufferSize: Int = DEFAULT_BUFFER_SIZE
): Boolean {
  if (!this.exists() || !dest.isDirectory) return false
  val pathDest = "${dest.path}/${this.name}"
  
  if (this.isDirectory) {
    val fileDest = File(pathDest)
    if (fileDest.exists()) return false
    fileDest.mkdirs()
    if (this.listFiles() == null) return false
    
    var result = true
    for (file in this.listFiles()!!) {
      if (!file.moveTo(context, fileDest, overwrite, bufferSize)) {
        result = false
      }
    }
    this.delete()
    return result
  } else {
    try {
      this.copyTo(File(pathDest), overwrite, bufferSize)
      this.delete()
    } catch (e: Exception) {
      e.printStackTrace()
      return false
    }
    
    MediaScannerConnection.scanFile(
      context,
      listOf(this.path, pathDest).toTypedArray(),
      null,
      null
    )
    return true
  }
}

fun File.delete(context: Context): Boolean {
  try {
    deleteRecursively()
  } catch (e: Exception) {
    e.printStackTrace()
    return false
  }
  MediaScannerConnection.scanFile(context, listOf(this.path).toTypedArray(), null, null)
  return true
}

@SuppressLint("QueryPermissionsNeeded")
fun File.share(context: Context, authority: String) {
  if (!this.exists()) return
  
  val uri = FileProvider.getUriForFile(context, authority, this)
  val intentShareFile = Intent(Intent.ACTION_SEND)
  val titleFull = this.name
  intentShareFile.type = this.mimeType()
  intentShareFile.putExtra(Intent.EXTRA_STREAM, uri)
  intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
  val chooser = Intent.createChooser(intentShareFile, titleFull)
  val resInfoList =
    context.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
  for (resolveInfo in resInfoList) {
    val packageName = resolveInfo.activityInfo.packageName
    context.grantUriPermission(
      packageName,
      uri,
      Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
    )
  }
  context.startActivity(chooser)
}

@SuppressLint("QueryPermissionsNeeded")
fun File.open(context: Context, authority: String) {
  if (!this.exists()) return
  
  val uri = FileProvider.getUriForFile(context, authority, this)
  val intentOpenFile = Intent(Intent.ACTION_VIEW)
  val titleFull = this.name
  intentOpenFile.setDataAndType(uri, this.mimeType())
  intentOpenFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
  val chooser = Intent.createChooser(intentOpenFile, titleFull)
  val resInfoList =
    context.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)
  for (resolveInfo in resInfoList) {
    val packageName = resolveInfo.activityInfo.packageName
    context.grantUriPermission(
      packageName,
      uri,
      Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
    )
  }
  context.startActivity(chooser)
}

fun File.readToString(): String {
  var text: String
  FileInputStream(this).use { inpS ->
    inpS.bufferedReader().use {
      text = it.readText()
      it.close()
    }
    inpS.close()
  }
  return text
}

fun File.writeToFile(text: String) {
  FileOutputStream(this).use { outS ->
    outS.bufferedWriter().use {
      it.append(text)
      it.close()
    }
    outS.close()
  }
}

fun Context.getFiles(
  directoryPath: String = "",
  types: List<String> = listOf(*FileType.ALL)
): List<BaseFile> {
  val files = ArrayList<BaseFile>()
  
  val uri = MediaStore.Files.getContentUri("external")
  val projection = arrayOf(
    BaseColumns._ID,
    MediaStore.Files.FileColumns.TITLE,
    MediaStore.Files.FileColumns.DISPLAY_NAME,
    MediaStore.Files.FileColumns.MIME_TYPE,
    MediaStore.Files.FileColumns.SIZE,
    MediaStore.Files.FileColumns.DATE_ADDED,
    MediaStore.Files.FileColumns.DATE_MODIFIED,
    MediaStore.Files.FileColumns.DATA
  )
  var selection = ""
  for (i in types.indices) {
    if (i == 0) {
      selection = "${MediaStore.Files.FileColumns.DATA} LIKE '$directoryPath/%.${types[i]}'"
    } else {
      selection += " OR ${MediaStore.Files.FileColumns.DATA} LIKE '$directoryPath/%.${types[i]}'"
    }
  }
  
  val sortOrder = "${MediaStore.Files.FileColumns.DATA} ASC"
  
  this.contentResolver.query(
    uri,
    projection,
    selection,
    null,
    sortOrder
  )?.use { cursor ->
    val col_id = cursor.getColumnIndex(BaseColumns._ID)
    val col_title = cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE)
    val col_displayName = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
    val col_mimeType = cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
    val col_size = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)
    val col_dateAdded = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
    val col_dateModified = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)
    val col_data = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
    
    while (cursor.moveToNext()) {
      val id = cursor.getLong(col_id)
      val title = cursor.getString(col_title)
      val displayName = cursor.getString(col_displayName)
      val mimeType = cursor.getString(col_mimeType)
      val size = cursor.getLong(col_size)
      val dateAdded = cursor.getLong(col_dateAdded) * 1000
      val dateModified = cursor.getLong(col_dateModified) * 1000
      val data = cursor.getString(col_data)
      
      files.add(
        BaseFile(
          id,
          title,
          displayName,
          mimeType,
          size,
          dateAdded,
          dateModified,
          data
        )
      )
    }
    cursor.close()
  }
  return files
}

fun Context.getAudios(
  directoryPath: String = "",
  types: List<String> = listOf(*FileType.AUDIO)
): List<BaseAudio> {
  val audios = ArrayList<BaseAudio>()
  
  val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
  val projection = arrayOf(
    BaseColumns._ID,
    MediaStore.Audio.AudioColumns.TITLE,
    MediaStore.Audio.AudioColumns.DISPLAY_NAME,
    MediaStore.Audio.AudioColumns.MIME_TYPE,
    MediaStore.Audio.AudioColumns.SIZE,
    MediaStore.Audio.AudioColumns.DATE_ADDED,
    MediaStore.Audio.AudioColumns.DATE_MODIFIED,
    MediaStore.Audio.AudioColumns.DATA,
    MediaStore.Audio.AudioColumns.ALBUM,
    MediaStore.Audio.AudioColumns.ARTIST,
    MediaStore.Audio.AudioColumns.DURATION
  )
  var selection = ""
  for (i in types.indices) {
    if (i == 0) {
      selection = "${MediaStore.Audio.AudioColumns.DATA} LIKE '$directoryPath/%.${types[i]}'"
    } else {
      selection += " OR ${MediaStore.Audio.AudioColumns.DATA} LIKE '$directoryPath/%.${types[i]}'"
    }
  }
  
  val sortOrder = "${MediaStore.Audio.AudioColumns.DATA} ASC"
  
  this.contentResolver.query(
    uri,
    projection,
    selection,
    null,
    sortOrder
  )?.use { cursor ->
    val col_id = cursor.getColumnIndex(BaseColumns._ID)
    val col_title = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)
    val col_displayName = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
    val col_mimeType = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.MIME_TYPE)
    val col_size = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE)
    val col_dateAdded = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED)
    val col_dateModified = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_MODIFIED)
    val col_data = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)
    val col_album = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM)
    val col_artist = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)
    val col_duration = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)
    
    while (cursor.moveToNext()) {
      val id = cursor.getLong(col_id)
      val title = cursor.getString(col_title)
      val displayName = cursor.getString(col_displayName)
      val mimeType = cursor.getString(col_mimeType)
      val size = cursor.getLong(col_size)
      val dateAdded = cursor.getLong(col_dateAdded) * 1000
      val dateModified = cursor.getLong(col_dateModified) * 1000
      val data = cursor.getString(col_data)
      val album = cursor.getString(col_album)
      val artist = cursor.getString(col_artist)
      val duration = cursor.getLong(col_duration)
      
      audios.add(
        BaseAudio(
          id,
          title,
          displayName,
          mimeType,
          size,
          dateAdded,
          dateModified,
          data,
          album,
          artist,
          duration
        )
      )
    }
    cursor.close()
  }
  return audios
}

fun Context.getImages(
  directoryPath: String = "",
  types: List<String> = listOf(*FileType.IMAGE)
): List<BaseImage> {
  val images = ArrayList<BaseImage>()
  
  val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
  val projection = arrayOf(
    BaseColumns._ID,
    MediaStore.Images.ImageColumns.TITLE,
    MediaStore.Images.ImageColumns.DISPLAY_NAME,
    MediaStore.Images.ImageColumns.MIME_TYPE,
    MediaStore.Images.ImageColumns.SIZE,
    MediaStore.Images.ImageColumns.DATE_ADDED,
    MediaStore.Images.ImageColumns.DATE_MODIFIED,
    MediaStore.Images.ImageColumns.DATA,
    MediaStore.Images.ImageColumns.HEIGHT,
    MediaStore.Images.ImageColumns.WIDTH
  )
  var selection = ""
  for (i in types.indices) {
    if (i == 0) {
      selection = "${MediaStore.Images.ImageColumns.DATA} LIKE '$directoryPath/%.${types[i]}'"
    } else {
      selection += " OR ${MediaStore.Images.ImageColumns.DATA} LIKE '$directoryPath/%.${types[i]}'"
    }
  }
  
  val sortOrder = "${MediaStore.Images.ImageColumns.DATA} ASC"
  
  this.contentResolver.query(
    uri,
    projection,
    selection,
    null,
    sortOrder
  )?.use { cursor ->
    val col_id = cursor.getColumnIndex(BaseColumns._ID)
    val col_title = cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE)
    val col_displayName = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
    val col_mimeType = cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE)
    val col_size = cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)
    val col_dateAdded = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED)
    val col_dateModified = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED)
    val col_data = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
    val col_height = cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT)
    val col_width = cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH)
    
    while (cursor.moveToNext()) {
      val id = cursor.getLong(col_id)
      val title = cursor.getString(col_title)
      val displayName = cursor.getString(col_displayName)
      val mimeType = cursor.getString(col_mimeType)
      val size = cursor.getLong(col_size)
      val dateAdded = cursor.getLong(col_dateAdded) * 1000
      val dateModified = cursor.getLong(col_dateModified) * 1000
      val data = cursor.getString(col_data)
      val height = cursor.getLong(col_height)
      val width = cursor.getLong(col_width)
      
      images.add(
        BaseImage(
          id,
          title,
          displayName,
          mimeType,
          size,
          dateAdded,
          dateModified,
          data,
          height,
          width
        )
      )
    }
    cursor.close()
  }
  return images
}

fun Context.getVideos(
  directoryPath: String = "",
  types: List<String> = listOf(*FileType.VIDEO)
): List<BaseVideo> {
  val videos = ArrayList<BaseVideo>()
  
  val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
  val projection = arrayOf(
    BaseColumns._ID,
    MediaStore.Video.VideoColumns.TITLE,
    MediaStore.Video.VideoColumns.DISPLAY_NAME,
    MediaStore.Video.VideoColumns.MIME_TYPE,
    MediaStore.Video.VideoColumns.SIZE,
    MediaStore.Video.VideoColumns.DATE_ADDED,
    MediaStore.Video.VideoColumns.DATE_MODIFIED,
    MediaStore.Video.VideoColumns.DATA,
    MediaStore.Video.VideoColumns.HEIGHT,
    MediaStore.Video.VideoColumns.WIDTH,
    MediaStore.Video.VideoColumns.ALBUM,
    MediaStore.Video.VideoColumns.ARTIST,
    MediaStore.Video.VideoColumns.DURATION,
    MediaStore.Video.VideoColumns.BUCKET_ID,
    MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
    MediaStore.Video.VideoColumns.RESOLUTION
  )
  var selection = ""
  for (i in types.indices) {
    if (i == 0) {
      selection = "${MediaStore.Video.VideoColumns.DATA} LIKE '$directoryPath/%.${types[i]}'"
    } else {
      selection += " OR ${MediaStore.Video.VideoColumns.DATA} LIKE '$directoryPath/%.${types[i]}'"
    }
  }
  
  val sortOrder = "${MediaStore.Video.VideoColumns.DATA} ASC"
  
  this.contentResolver.query(
    uri,
    projection,
    selection,
    null,
    sortOrder
  )?.use { cursor ->
    val col_id = cursor.getColumnIndex(BaseColumns._ID)
    val col_title = cursor.getColumnIndex(MediaStore.Video.VideoColumns.TITLE)
    val col_displayName = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)
    val col_mimeType = cursor.getColumnIndex(MediaStore.Video.VideoColumns.MIME_TYPE)
    val col_size = cursor.getColumnIndex(MediaStore.Video.VideoColumns.SIZE)
    val col_dateAdded = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_ADDED)
    val col_dateModified = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_MODIFIED)
    val col_data = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
    val col_height = cursor.getColumnIndex(MediaStore.Video.VideoColumns.HEIGHT)
    val col_width = cursor.getColumnIndex(MediaStore.Video.VideoColumns.WIDTH)
    val col_album = cursor.getColumnIndex(MediaStore.Video.VideoColumns.ALBUM)
    val col_artist = cursor.getColumnIndex(MediaStore.Video.VideoColumns.ARTIST)
    val col_duration = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)
    val col_bucketID = cursor.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_ID)
    val col_bucketDisplayName =
      cursor.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME)
    val col_resolution = cursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION)
    
    while (cursor.moveToNext()) {
      val id = cursor.getLong(col_id)
      val title = cursor.getString(col_title)
      val displayName = cursor.getString(col_displayName)
      val mimeType = cursor.getString(col_mimeType)
      val size = cursor.getLong(col_size)
      val dateAdded = cursor.getLong(col_dateAdded) * 1000
      val dateModified = cursor.getLong(col_dateModified) * 1000
      val data = cursor.getString(col_data)
      val height = cursor.getLong(col_height)
      val width = cursor.getLong(col_width)
      val album = cursor.getString(col_album)
      val artist = cursor.getString(col_artist)
      val duration = cursor.getLong(col_duration)
      val bucketID = cursor.getLong(col_bucketID)
      val bucketDisplayName = cursor.getString(col_bucketDisplayName)
      val resolution = cursor.getString(col_resolution)
      
      videos.add(
        BaseVideo(
          id,
          title,
          displayName,
          mimeType,
          size,
          dateAdded,
          dateModified,
          data,
          height,
          width,
          album,
          artist,
          duration,
          bucketID,
          bucketDisplayName,
          resolution
        )
      )
    }
    cursor.close()
  }
  return videos
}