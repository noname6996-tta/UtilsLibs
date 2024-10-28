package com.tta.utlislib.language

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.tohsoft.base.mvp.utils.language.LanguageHelperKotlin
import com.tohsoft.base.mvp.utils.language.LocaleManager
import com.tta.utlislib.R
import com.tta.utlislib.databinding.ActivityLanguageBinding

class LanguageActivity : AppCompatActivity() {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var needRecreateAfterDestroy = false

    private lateinit var activityLanguageBinding: ActivityLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleManager.setLocale(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityLanguageBinding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(activityLanguageBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        activityLanguageBinding.btn.setOnClickListener {
            showDialogLanguage()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleManager.setLocale(newBase))
    }

    private fun showDialogLanguage() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_setting_select_language, null)
        val mAlertDialog = MaterialDialog(this).customView(view = dialogView, noVerticalPadding = true, horizontalPadding = false)
        mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mAlertDialog.setOnDismissListener {
            //do something here
        }
        mAlertDialog.show() {
            cornerRadius(res = R.dimen.spacing_small)

            val listLanguage = LanguageHelperKotlin.getListLanguages(this@LanguageActivity)
            val selectedPos = listLanguage.indexOfFirst { it.isSelected }

            val recyclerView = (findViewById<View>(com.tohsoft.base.mvp.R.id.recyclerView) as RecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val adapter = ItemLanguageAdapter(listLanguage.map { it.languageDisplay }, selectedPos)
            recyclerView.adapter = adapter

            val btnCancel = findViewById<AppCompatTextView>(R.id.tv_cancel)
            val btnConfirm = findViewById<AppCompatTextView>(R.id.tv_confirm)

            btnConfirm.setOnClickListener {
                dismiss()
                val indexSelect = adapter.selectedIndex
                if (indexSelect != selectedPos) {
                    LanguageHelperKotlin.setAppLanguage(this@LanguageActivity, listLanguage[indexSelect].languageCode)
                    restartActivity("language")
                }
            }

            btnCancel.setOnClickListener { dismiss() }
        }
    }

    override fun onDestroy() {
        //Hàm này để bật lại MainActivity, sau khi nó bị hủy và có flag needRecreateAfterDestroy=true
        if (needRecreateAfterDestroy) {
            needRecreateAfterDestroy = false
            handler.postDelayed({
                LanguageHelperKotlin.restartToApplyLanguage(this, this::class.java)
            }, 1000)
        }
        super.onDestroy()
    }


    /**
     * Restart activity : Hàm này để bật flag [needRecreateAfterDestroy], và [finish] MainActivity.
     * Sau khi [onDestroy] được thực hiện hoàn thiện các tác vụ, sẽ dùng Application instance để khởi động lại chính MainActivity.
     * @param typeApply is "theme" or "language"
     */
    fun restartActivity(typeApply: String) {
        needRecreateAfterDestroy = true
        startActivity(
            Intent(
                this,
                TransferChangeLanguageActivity::class.java
            )
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                .putExtra(
                    "contentId",
                    if (typeApply == "language") R.string.msg_restart_to_change_config
                    else R.string.msg_applying_theme
                )
        )
        finish()
    }
}