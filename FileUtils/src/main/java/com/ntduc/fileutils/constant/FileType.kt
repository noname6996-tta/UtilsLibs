package com.ntduc.fileutils.constant

object FileType {
  val IMAGE_JPG = arrayOf(
    "jpg"
  )
  val IMAGE_PNG = arrayOf(
    "png"
  )
  val IMAGE_GIF = arrayOf(
    "gif"
  )
  val IMAGE_BMP = arrayOf(
    "bmp"
  )
  val IMAGE_HEIF = arrayOf(
    "heif"
  )
  val IMAGE = arrayOf(
    *IMAGE_JPG, *IMAGE_PNG, *IMAGE_GIF, *IMAGE_BMP, *IMAGE_HEIF
  )
  val VIDEO_MP4 = arrayOf(
    "mp4"
  )
  val VIDEO_MKV = arrayOf(
    "kmv"
  )
  val VIDEO_AVI = arrayOf(
    "avi"
  )
  val VIDEO_WMV = arrayOf(
    "wmv"
  )
  val VIDEO_3GP = arrayOf(
    "3gp"
  )
  val VIDEO = arrayOf(
    *VIDEO_MP4, *VIDEO_MKV, *VIDEO_AVI, *VIDEO_WMV, *VIDEO_3GP
  )
  val AUDIO_MP3 = arrayOf(
    "mp3"
  )
  val AUDIO_M4A = arrayOf(
    "m4a"
  )
  val AUDIO_AMR = arrayOf(
    "amr"
  )
  val AUDIO_OGG = arrayOf(
    "ogg"
  )
  val AUDIO_AAC = arrayOf(
    "aac"
  )
  val AUDIO = arrayOf(
    *AUDIO_MP3, *AUDIO_M4A, *AUDIO_AMR, *AUDIO_OGG, *AUDIO_AAC
  )
  
  val DOCUMENT_PDF = arrayOf(
    "pdf"
  )
  
  val DOCUMENT_TXT = arrayOf(
    "txt"
  )
  
  val DOCUMENT_DOC = arrayOf(
    "doc", "docx"
  )
  
  val DOCUMENT_XLS = arrayOf(
    "xls", "xlsx"
  )
  
  val DOCUMENT_PPT = arrayOf(
    "ppt", "pptx"
  )
  
  val DOCUMENT = arrayOf(
    *DOCUMENT_PDF, *DOCUMENT_TXT, *DOCUMENT_DOC, *DOCUMENT_XLS, *DOCUMENT_PPT
  )
  
  val COMPRESSED_ZIP = arrayOf(
    "zip"
  )
  
  val COMPRESSED_7ZIP = arrayOf(
    "7z"
  )
  
  val COMPRESSED_RAR = arrayOf(
    "rar"
  )
  
  val COMPRESSED = arrayOf(
    *COMPRESSED_ZIP, *COMPRESSED_7ZIP, *COMPRESSED_RAR
  )
  
  val APK = arrayOf(
    "apk"
  )
  
  val ALL = arrayOf(
    *IMAGE, *VIDEO, *AUDIO, *DOCUMENT, *COMPRESSED, *APK
  )
}