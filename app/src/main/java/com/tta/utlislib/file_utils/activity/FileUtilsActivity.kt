package com.ntduc.utils.file_utils.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.fileutils.*
import com.ntduc.toastutils.shortToast
import com.tta.utlislib.databinding.ActivityFileUtilsBinding
import com.ntduc.utils.file_utils.get_all_audio.activity.GetAllAudioActivity
import com.ntduc.utils.file_utils.get_all_file.activity.GetAllFileActivity
import com.ntduc.utils.file_utils.get_all_image.activity.GetAllImageActivity
import com.ntduc.utils.file_utils.get_all_video.activity.GetAllVideoActivity
import java.io.File


class FileUtilsActivity : AppCompatActivity() {
  private lateinit var binding: ActivityFileUtilsBinding
  
  private val selectFileLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      if (it.resultCode == RESULT_OK) {
        val intent = it.data ?: return@registerForActivityResult
        binding.txt.text = intent.data?.getRealPath(this) ?: "not found"
      } else {
        shortToast("Cancel")
      }
    }
  private val selectFolderCopyFileLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      if (it.resultCode == RESULT_OK) {
        val intent = it.data ?: return@registerForActivityResult
        val docUri = DocumentsContract.buildDocumentUriUsingTree(
          intent.data,
          DocumentsContract.getTreeDocumentId(intent.data)
        )
        val uri = docUri.getRealPath(this)
        if (uri != null) {
          if (File(binding.txt.text.toString()).copyTo(
              context = this,
              dest = File(uri)
            )
          ) {
            shortToast("Copy Success")
          } else {
            shortToast("Copy Error")
          }
        }
      } else {
        shortToast("Cancel")
      }
    }
  private val selectFolderMoveFileLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      if (it.resultCode == RESULT_OK) {
        val intent = it.data ?: return@registerForActivityResult
        val docUri = DocumentsContract.buildDocumentUriUsingTree(
          intent.data,
          DocumentsContract.getTreeDocumentId(intent.data)
        )
        val uri = docUri.getRealPath(this)
        if (uri != null) {
          if (File("/storage/emulated/0/Testtttttt").moveTo(
              context = this,
              dest = File("/storage/emulated/0/Test2")
            )
          ) {
            shortToast("Move Success")
          } else {
            shortToast("Move Error")
          }
        }
      } else {
        shortToast("Cancel")
      }
    }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityFileUtilsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    binding.btnSelectFile.setOnClickShrinkEffectListener {
      selectFile()
    }
    
    binding.btnRenameFile.setOnClickShrinkEffectListener {
      if (binding.txt.text.trim().isEmpty()) {
        shortToast("Please select file")
        return@setOnClickShrinkEffectListener
      }
      
      if (binding.edtRenameFile.text.trim().isEmpty()) {
        shortToast("Please enter name file")
        return@setOnClickShrinkEffectListener
      }
      if (File(binding.txt.text.toString()).renameTo(
          this,
          binding.edtRenameFile.text.toString()
        ) != null
      ) {
        shortToast("Rename success")
        binding.txt.text = ""
        binding.edtRenameFile.setText("")
      } else {
        shortToast("Rename false")
      }
    }
    
    binding.btnCopyFile.setOnClickShrinkEffectListener {
      if (binding.txt.text.trim().isEmpty()) {
        shortToast("Please select file")
        return@setOnClickShrinkEffectListener
      }
      selectFolderCopyFile()
      Log.d("ntduc_debug", "onCreate: ${binding.txt.text}")
    }
    
    binding.btnMoveFile.setOnClickShrinkEffectListener {
      if (binding.txt.text.trim().isEmpty()) {
        shortToast("Please select file")
        return@setOnClickShrinkEffectListener
      }
      selectFolderMoveFile()
    }
    
    binding.btnDeleteFile.setOnClickShrinkEffectListener {
      if (binding.txt.text.trim().isEmpty()) {
        shortToast("Please select file")
        return@setOnClickShrinkEffectListener
      }
      
      if (File(binding.txt.text.toString()).delete(this)) {
        shortToast("Delete success")
        binding.txt.text = ""
      } else {
        shortToast("Delete Error")
      }
    }
    
    binding.btnShareFile.setOnClickShrinkEffectListener {
      if (binding.txt.text.trim().isEmpty()) {
        shortToast("Please select file")
        return@setOnClickShrinkEffectListener
      }
      
      File(binding.txt.text.toString()).share(this, "com.ntduc.utils.provider")
    }
    
    binding.btnGetAllFile.setOnClickShrinkEffectListener {
      startActivity(Intent(this, GetAllFileActivity::class.java))
    }
    
    binding.btnGetAllAudio.setOnClickShrinkEffectListener {
      startActivity(Intent(this, GetAllAudioActivity::class.java))
    }
    
    binding.btnGetAllImage.setOnClickShrinkEffectListener {
      startActivity(Intent(this, GetAllImageActivity::class.java))
    }
    
    binding.btnGetAllVideo.setOnClickShrinkEffectListener {
      startActivity(Intent(this, GetAllVideoActivity::class.java))
    }
  }
  
  private fun selectFile() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    val uri = Uri.parse(Environment.getDownloadCacheDirectory().path)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.setDataAndType(uri, "image/*")
    selectFileLauncher.launch(intent)
  }
  
  private fun selectFolderCopyFile() {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    selectFolderCopyFileLauncher.launch(intent)
  }
  
  private fun selectFolderMoveFile() {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    selectFolderMoveFileLauncher.launch(intent)
  }
}