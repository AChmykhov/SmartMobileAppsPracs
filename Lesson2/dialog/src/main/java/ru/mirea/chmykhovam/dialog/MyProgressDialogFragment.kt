package ru.mirea.chmykhovam.dialog

import android.app.ProgressDialog
import android.content.Context

class MyProgressDialogFragment(context: Context) : ProgressDialog(context) {
    override fun show() {
        ProgressDialog.show(context, "MIREA progress dialog", "This is progress dialog", true, true)
    }
}