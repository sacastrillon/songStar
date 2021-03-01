package com.example.woo.songstar.customDialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.example.woo.songstar.R
import com.example.woo.songstar.intefaces.DialogCallback
import kotlinx.android.synthetic.main.dialog_edit_language.*

class CustomEditLanguageDialog(context: Context, language: String, dialogCallback: DialogCallback) : Dialog(context) {

    private var language: String = ""
    private var dialogCallback: DialogCallback? = null
    private val languages = listOf("English", "Spanish")

    init {
        this.language = language
        this.dialogCallback = dialogCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_language)
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        this.initView()
    }

    private fun initView() {
        val arrayAdapter = ArrayAdapter(this.context, android.R.layout.simple_list_item_1, this.languages)
        this.spLanguage.adapter = arrayAdapter
        val index = this.languages.indexOf(this.language)
        this.spLanguage.setSelection(index)

        this.spLanguage.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                this@CustomEditLanguageDialog.language = this@CustomEditLanguageDialog.languages[position]
            }
        }

        this.ivCloseLanguage.setOnClickListener {
            this.dismiss()
        }

        this.btnSave.setOnClickListener {
            AlertDialog.Builder(this.context)
                .setTitle(context.getString(R.string.save_changes))
                .setMessage(context.getString(R.string.do_you_want_to_save_these_changes))
                .setPositiveButton(R.string.yes) { _, _ ->
                    this.dialogCallback?.submit(this@CustomEditLanguageDialog.language, 1)
                    this.dismiss()
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}