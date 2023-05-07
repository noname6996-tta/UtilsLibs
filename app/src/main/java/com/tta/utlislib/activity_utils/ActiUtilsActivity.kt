package com.tta.utlislib.activity_utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.ntduc.clickeffectutils.setOnClickShrinkEffectListener
import com.ntduc.toastutils.shortToast
import com.tta.activityutils.addSecureFlag
import com.tta.activityutils.brightness
import com.tta.activityutils.clearSecureFlag
import com.tta.activityutils.customBackButton
import com.tta.activityutils.displaySizePixels
import com.tta.activityutils.enterFullScreenMode
import com.tta.activityutils.exitFullScreenMode
import com.tta.activityutils.getNavigationBarHeight
import com.tta.activityutils.getStatusBarHeight
import com.tta.activityutils.hideBackButton
import com.tta.activityutils.hideKeyboard
import com.tta.activityutils.hideNavigationBar
import com.tta.activityutils.hideStatusBar
import com.tta.activityutils.hideToolbar
import com.tta.activityutils.lockCurrentScreenOrientation
import com.tta.activityutils.lockOrientation
import com.tta.activityutils.restart
import com.tta.activityutils.setNavigationBarColor
import com.tta.activityutils.setNavigationBarDividerColor
import com.tta.activityutils.setStatusBarColor
import com.tta.activityutils.showBackButton
import com.tta.activityutils.showKeyboard
import com.tta.activityutils.showNavigationBar
import com.tta.activityutils.showStatusBar
import com.tta.activityutils.showToolbar
import com.tta.activityutils.unlockScreenOrientation
import com.tta.utlislib.databinding.ActivityActiUtilsBinding

class ActiUtilsActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityActiUtilsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    binding.btnHideNavigationBar.setOnClickShrinkEffectListener {
      hideNavigationBar()
    }
    binding.btnShowNavigationBar.setOnClickShrinkEffectListener {
      showNavigationBar()
    }
    binding.btnGetNavigationBarHeight.setOnClickShrinkEffectListener {
      shortToast("NavigationBar Height: $getNavigationBarHeight px")
    }
    binding.btnHideStatusBar.setOnClickShrinkEffectListener {
      hideStatusBar()
    }
    binding.btnShowStatusBar.setOnClickShrinkEffectListener {
      showStatusBar()
    }
    binding.btnEnterFullScreenMode.setOnClickShrinkEffectListener {
      enterFullScreenMode()
    }
    binding.btnExitFullScreenMode.setOnClickShrinkEffectListener {
      exitFullScreenMode()
    }
    binding.btnAddSecureFlag.setOnClickShrinkEffectListener {
      addSecureFlag()
    }
    binding.btnClearSecureFlag.setOnClickShrinkEffectListener {
      clearSecureFlag()
    }
    binding.btnShowKeyboard.setOnClickShrinkEffectListener {
      showKeyboard(binding.edtBrightness)
    }
    binding.btnHideKeyboard.setOnClickShrinkEffectListener {
      hideKeyboard()
    }
    binding.btnBrightness.setOnClickShrinkEffectListener {
      brightness = binding.edtBrightness.text.toString().toFloat()
    }
    binding.btnGetStatusBarHeight.setOnClickShrinkEffectListener {
      shortToast("StatusBar Height: $getStatusBarHeight px")
    }
    binding.btnDisplaySizePixels.setOnClickShrinkEffectListener {
      shortToast("Display Size: ${displaySizePixels.x} px, ${displaySizePixels.y} px")
    }
    binding.btnSetStatusBarColor.setOnClickShrinkEffectListener {
      setStatusBarColor(com.tta.utlislib.R.color.teal_200)
    }
    binding.btnSetNavigationBarColor.setOnClickShrinkEffectListener {
      setNavigationBarColor(com.tta.utlislib.R.color.teal_200)
    }
    binding.btnSetNavigationBarDividerColor.setOnClickShrinkEffectListener {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        setNavigationBarDividerColor(com.tta.utlislib.R.color.teal_200)
      }
    }
    binding.btnRestart.setOnClickShrinkEffectListener {
      restart()
    }
    
    binding.btnSleepDuration.setOnClickShrinkEffectListener {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (Settings.System.canWrite(this)) {
          val sleepDuration = 5000
          shortToast("Sleep after: ${sleepDuration}ms")
        } else {
          try {
            val uri = Uri.parse("package:" + "com.tta.utlislib")
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, uri)
            startActivity(intent)
          } catch (e: Exception) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            startActivity(intent)
          }
        }
      }
    }
    
    binding.btnLockOrientation.setOnClickShrinkEffectListener {
      lockOrientation()
    }
    
    binding.btnUnlockScreenOrientation.setOnClickShrinkEffectListener {
      unlockScreenOrientation()
    }
    
    binding.btnLockCurrentScreenOrientation.setOnClickShrinkEffectListener {
      lockCurrentScreenOrientation()
    }
    
    binding.btnShowBackButton.setOnClickShrinkEffectListener {
      showBackButton()
    }
    
    binding.btnHideBackButton.setOnClickShrinkEffectListener {
      hideBackButton()
    }
    
    binding.btnShowToolbar.setOnClickShrinkEffectListener {
      showToolbar()
    }
    
    binding.btnHideToolbar.setOnClickShrinkEffectListener {
      hideToolbar()
    }
    
    binding.btnCustomBackButton.setOnClickShrinkEffectListener {
      customBackButton(com.tta.utlislib.R.drawable.ic_launcher_foreground)
    }
  }
}