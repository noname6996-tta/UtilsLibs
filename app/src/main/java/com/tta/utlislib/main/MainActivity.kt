package com.tta.utlislib.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.contextutils.showConfirmationDialog
import com.ntduc.toastutils.shortToast
import com.tta.utlislib.activity_utils.ActiUtilsActivity
import com.ntduc.utils.app_utils.activity.AppActivity
import com.tta.utlislib.color_utils.ColorUtilsActivity
import com.ntduc.utils.context_utils.ContextUtilsActivity
import com.tta.utlislib.databinding.ActivityMainBinding
import com.ntduc.utils.file_utils.activity.FileUtilsActivity
import com.ntduc.utils.fragment_utils.activity.FragmentUtilsActivity
import com.tta.utlislib.navigation_utils.activity.NavUtilsActivity
import com.tta.utlislib.Recycler_view_utils.RecyclerUtilsActivity
import com.tta.utlislib.security_utils.SecurityUtilsActivity
import com.ntduc.utils.sp_utils.SpActivity
import com.ntduc.utils.string_utils.StringUtilsActivity
import com.ntduc.utils.view_pager_2_utils.activity.ViewPager2UtilsActivity
import com.ntduc.utils.view_utils.activity.ViewUtilsActivity

class MainActivity : AppCompatActivity() {
  
  companion object {
    private const val REQUEST_PERMISSION_READ_WRITE = 100
  }
  
  private val requestPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      if (it.resultCode == RESULT_OK) {
        if (checkPermissionReadAllFile()) {
          startActivity(Intent(this, FileUtilsActivity::class.java))
        } else {
          requestPermissionReadAllFile()
        }
      }
    }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    binding.btnActivityUtils.setOnClickShrinkEffectListener {
      startActivity(Intent(this, ActiUtilsActivity::class.java))
    }
    
    binding.btnContextUtils.setOnClickShrinkEffectListener {
      startActivity(Intent(this, ContextUtilsActivity::class.java))
    }
    
    binding.btnFragmentUtils.setOnClickShrinkEffectListener {
      startActivity(Intent(this, FragmentUtilsActivity::class.java))
    }
    
    binding.btnViewUtils.setOnClickShrinkEffectListener {
      startActivity(Intent(this, ViewUtilsActivity::class.java))
    }
    
    binding.btnNavigationUtils.setOnClickShrinkEffectListener {
      startActivity(Intent(this, NavUtilsActivity::class.java))
    }
    
    binding.btnColorUtils.setOnClickShrinkEffectListener {
      startActivity(Intent(this, ColorUtilsActivity::class.java))
    }
    
    binding.btnStringUtils.setOnClickShrinkEffectListener {
      startActivity(Intent(this, StringUtilsActivity::class.java))
    }
    
    binding.btnSPUtils.setOnClickShrinkEffectListener {
      startActivity(Intent(this, SpActivity::class.java))
    }
    
    binding.btnFileUtils.setOnClickShrinkEffectListener {
      if (checkPermissionReadAllFile()) {
        startActivity(Intent(this, FileUtilsActivity::class.java))
      } else {
        requestPermissionReadAllFile()
      }
    }
    
    binding.btnSecurityUtils.setOnClickShrinkEffectListener {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        startActivity(Intent(this, SecurityUtilsActivity::class.java))
      } else {
        shortToast("Not support")
      }
    }
    
    binding.btnViewPager2Utils.setOnClickShrinkEffectListener {
      startActivity(Intent(this, ViewPager2UtilsActivity::class.java))
    }
    
    binding.btnRecyclerViewUtils.setOnClickShrinkEffectListener {
      startActivity(Intent(this, RecyclerUtilsActivity::class.java))
    }
    
    binding.btnAppUtils.setOnClickShrinkEffectListener {
      startActivity(Intent(this, AppActivity::class.java))
    }
  }
  
  private fun checkPermissionReadAllFile(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      Environment.isExternalStorageManager()
    } else {
      val write =
        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
      val read =
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
      write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
    }
  }
  
  private fun requestPermissionReadAllFile() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      openDialogAccessAllFile()
    } else {
      val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
      )
      requestPermissions(permissions, REQUEST_PERMISSION_READ_WRITE)
    }
  }
  
  private fun requestAccessAllFile() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      try {
        val uri = Uri.parse("package:" + "com.tta.utlislib")
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
        requestPermissionLauncher.launch(intent)
      } catch (e: Exception) {
        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        requestPermissionLauncher.launch(intent)
      }
    } else {
      try {
        val uri = Uri.parse("package:" + "com.tta.utlislib")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
        requestPermissionLauncher.launch(intent)
      } catch (_: Exception) {
      }
    }
  }
  
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String?>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQUEST_PERMISSION_READ_WRITE) {
      if (grantResults.isNotEmpty()
        && grantResults[0] == PackageManager.PERMISSION_GRANTED
        && grantResults[1] == PackageManager.PERMISSION_GRANTED
      ) {
        startActivity(Intent(this, FileUtilsActivity::class.java))
      } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          if (shouldShowRequestPermissionRationale(permissions[0]!!)
            && shouldShowRequestPermissionRationale(permissions[1]!!)
          ) {
            requestPermissions(
              permissions,
              REQUEST_PERMISSION_READ_WRITE
            )
          } else {
            openDialogAccessAllFile()
          }
        }
      }
    }
  }
  
  private fun openDialogAccessAllFile() {
    showConfirmationDialog(
      "Request Permission",
      "Access to read all file in your device",
      onResponse = {
        when (it) {
          true -> requestAccessAllFile()
          false -> finish()
        }
      })
  }
}