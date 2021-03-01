package com.example.woo.songstar.customDialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.example.woo.songstar.R
import com.example.woo.songstar.activities.LoginActivity
import com.example.woo.songstar.intefaces.DialogCallback
import kotlinx.android.synthetic.main.dialog_edit_name.*

class CustomEditNameDialog(context: Context, userName: String, dialogCallback: DialogCallback) : Dialog(context) {
    var userName: String = ""
    var dialogCallback: DialogCallback? = null

    init {
        this.userName = userName
        this.dialogCallback = dialogCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_name)
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        this.initView()
    }

    private fun initView() {
        this.etName.setText(this.userName)

        this.ivClose.setOnClickListener {
            this.dismiss()
        }

        this.btnSave.setOnClickListener {
            AlertDialog.Builder(this.context)
                .setTitle(context.getString(R.string.save_changes))
                .setMessage(context.getString(R.string.do_you_want_to_save_these_changes))
                .setPositiveButton(R.string.yes) { _, _ ->
                    this.dialogCallback?.submit(this@CustomEditNameDialog.etName.text.toString(), 1)
                    this.dismiss()
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}