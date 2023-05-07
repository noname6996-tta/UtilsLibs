package com.tta.utlislib.security_utils

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.contextutils.inflater
import com.ntduc.securityutils.AndroidEncryption
import com.ntduc.securityutils.FileEncryption
import com.tta.utlislib.databinding.ActivitySecurityUtilsBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


@RequiresApi(Build.VERSION_CODES.M)
class SecurityUtilsActivity : AppCompatActivity() {
  private lateinit var binding: ActivitySecurityUtilsBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySecurityUtilsBinding.inflate(inflater)
    setContentView(binding.root)
    
    init()
  }
  
  private fun init() {
    initEvent()
  }
  
  private fun initEvent() {
    binding.btnEncrypt.setOnClickShrinkEffectListener {
      binding.txt.text = AndroidEncryption.encrypt(binding.edt.text.toString())
    }
    
    binding.btnDecrypt.setOnClickShrinkEffectListener {
      binding.edt.setText(AndroidEncryption.decrypt(binding.txt.text.toString()))
    }
    
    binding.btnEncryptFile.setOnClickShrinkEffectListener {
      FileEncryption.encryptToFile(
        "1234567891234567",
        "1234567891234567",
        FileInputStream(File("/storage/emulated/0/abc.jpg")),
        FileOutputStream(File("/storage/emulated/0/test.jpg"))
      )
    }
    
    binding.btnDecryptFile.setOnClickShrinkEffectListener {
      FileEncryption.decryptToFile(
        "1234567891234567",
        "1234567891234567",
        FileInputStream(File("/storage/emulated/0/test.jpg")),
        FileOutputStream(File("/storage/emulated/0/abcdef.jpg"))
      )
    }
  }
}