package com.simplemobiletools.launcher.dialogs

import android.app.Activity
import android.app.AlertDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.launcher.databinding.DialogRenameItemBinding
import com.simplemobiletools.launcher.extensions.homeScreenGridItemsDB
import com.simplemobiletools.launcher.models.HomeScreenGridItem

class RenameItemDialog(val activity: Activity, val item: HomeScreenGridItem, val callback: () -> Unit) {

    init {
        val binding = DialogRenameItemBinding.inflate(activity.layoutInflater)
        val view = binding.root
        binding.renameItemEdittext.setText(item.title)

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.simplemobiletools.commons.R.string.ok, null)
            .setNegativeButton(com.simplemobiletools.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(view, this, com.simplemobiletools.commons.R.string.rename) { alertDialog ->
                    alertDialog.showKeyboard(binding.renameItemEdittext)
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val newTitle = binding.renameItemEdittext.value
                        if (newTitle.isNotEmpty()) {
                            ensureBackgroundThread {
                                val result = activity.homeScreenGridItemsDB.updateItemTitle(newTitle, item.id!!)
                                if (result == 1) {
                                    callback()
                                    alertDialog.dismiss()
                                } else {
                                    activity.toast(com.simplemobiletools.commons.R.string.unknown_error_occurred)
                                }
                            }
                        } else {
                            activity.toast(com.simplemobiletools.commons.R.string.value_cannot_be_empty)
                        }
                    }
                }
            }
    }
}
