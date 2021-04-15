package com.example.woo.songstar.customDialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.woo.songstar.R
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.intefaces.DialogCallback
import com.example.woo.songstar.utils.AppSharedPreferences
import kotlinx.android.synthetic.main.dialog_change_password.*
import kotlinx.android.synthetic.main.dialog_edit_name.btnSave
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread

class CustomChangePasswordDialog(context: Context, dialogCallback: DialogCallback) : Dialog(context) {

    var dialogCallback: DialogCallback? = null
    private var db: AppDatabase? = null

    init {
        this.dialogCallback = dialogCallback
        this.db = AppDatabase.getDatabase(this.context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_change_password)
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        this.initView()
    }

    private fun initView() {
        this.ivClosePassword.setOnClickListener {
            this.dismiss()
        }

        this.btnSave.setOnClickListener {
            AlertDialog.Builder(this.context)
                .setTitle(context.getString(R.string.save_changes))
                .setMessage(context.getString(R.string.do_you_want_to_save_these_changes))
                .setPositiveButton(R.string.yes) { _, _ ->
                    this.setNewPassword()
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setNewPassword() {
        doAsync {
            try {
                val user = db?.userDao()?.getUser(
                    AppSharedPreferences.getString(this@CustomChangePasswordDialog.context,
                        AppSharedPreferences.USERNAME)!!, this@CustomChangePasswordDialog.etOldPassword.text.toString())
                if(user?.isNotEmpty()!!) {
                    db?.userDao()?.updatePassword(this@CustomChangePasswordDialog.etNewPassword.text.toString(),
                       AppSharedPreferences.getString(this@CustomChangePasswordDialog.context,
                            AppSharedPreferences.USERNAME)!!)
                    context.runOnUiThread {
                        this@CustomChangePasswordDialog.dialogCallback?.submit("", 1)
                        this@CustomChangePasswordDialog.dismiss()
                    }
                } else {
                    context.runOnUiThread {
                        Toast.makeText(context, getString(R.string.current_password_dont_match), Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                context.runOnUiThread {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}