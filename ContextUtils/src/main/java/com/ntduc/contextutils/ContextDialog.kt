package com.ntduc.contextutils

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.showDialog(title: String, msg: String): AlertDialog =
  AlertDialog.Builder(this).setTitle(title).setMessage(msg).show()

fun Context.showConfirmationDialog(
  title: String,
  msg: String,
  onResponse: (result: Boolean) -> Unit,
  positiveText: String? = "Yes",
  negativeText: String? = "No",
  cancelable: Boolean = false
) {
  val dialog = AlertDialog.Builder(this).setTitle(title).setMessage(msg).setCancelable(cancelable)
  if (positiveText != null) {
    dialog.setPositiveButton(positiveText) { _, _ -> onResponse(true) }
  }
  if (negativeText != null) {
    dialog.setNegativeButton(negativeText) { _, _ -> onResponse(false) }
  }
  dialog.show()
}

fun Context.showSinglePicker(
  title: String,
  choices: Array<String>,
  onResponse: (index: Int) -> Unit,
  checkedItemIndex: Int = -1
): AlertDialog = AlertDialog.Builder(this).setTitle(title)
  .setSingleChoiceItems(choices, checkedItemIndex) { dialog, which ->
    onResponse(which)
    dialog.dismiss()
  }.show()

fun Context.showMultiPicker(
  title: String,
  choices: Array<String>,
  onResponse: (index: Int, isChecked: Boolean) -> Unit,
  checkedItems: BooleanArray? = null,
  positiveText: String = "Done"
): AlertDialog = AlertDialog.Builder(this).setTitle(title)
  .setMultiChoiceItems(choices, checkedItems) { _, which, isChecked ->
    onResponse(
      which, isChecked
    )
  }.setPositiveButton(positiveText, null).show()