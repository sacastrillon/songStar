package com.example.woo.songstar.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.woo.songstar.R
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.models.User
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.doAsync

class CreateUserActivity : AppCompatActivity() {

    private var language: String = ""
    private val languages = listOf("English", "Spanish")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        this.tvTitle.text = getString(R.string.create_user)
        this.ivProfile.visibility = View.GONE
        this.ivBack.visibility = View.VISIBLE

        this.ivBack.setOnClickListener {
            this.finish()
        }

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.languages)
        this.spLanguage.adapter = arrayAdapter

        this.spLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                this@CreateUserActivity.language = this@CreateUserActivity.languages[position]
            }
        }

        this.btnSave.setOnClickListener {
            if(this.validateFields(
                    this.etName.text.toString(),
                    this.etUserName.text.toString(),
                    this.language,
                    this.etPassword.text.toString()
                )) {
                this.createUser()
            }
        }
    }

    private fun createUser() {
        val db = AppDatabase.getDatabase(this)

        doAsync {
            val user = db.userDao().getByUserName(this@CreateUserActivity.etUserName.text.toString())
            if(user.isNotEmpty()) {
                runOnUiThread {
                    Toast.makeText(this@CreateUserActivity, getString(R.string.username_already_exists), Toast.LENGTH_LONG).show()
                }
            } else {
                db.userDao().insert(User(
                    this@CreateUserActivity.etName.text.toString(),
                    this@CreateUserActivity.etUserName.text.toString(),
                    this@CreateUserActivity.etPassword.text.toString(),
                    this@CreateUserActivity.language,
                    "",
                    false
                    ))
                runOnUiThread {
                    Toast.makeText(this@CreateUserActivity, getString(R.string.user_created_successfully), Toast.LENGTH_LONG).show()
                    this@CreateUserActivity.finish()
                }
            }
        }
    }

    private fun validateFields(name: String, userName: String, language: String, password: String): Boolean {

        if(name.isEmpty()) {
            //Toast.makeText(this, getString(R.string.a_name_must_be_typed), Toast.LENGTH_LONG).show()
            return false
        }

        if(userName.isEmpty()) {
            //Toast.makeText(this, getString(R.string.a_username_must_be_typed), Toast.LENGTH_LONG).show()
            return false
        }

        if(language.isEmpty()) {
            //Toast.makeText(this, getString(R.string.a_language_must_be_selected), Toast.LENGTH_LONG).show()
            return false
        }

        if(password.isEmpty()) {
            //Toast.makeText(this, getString(R.string.a_password_must_be_typed), Toast.LENGTH_LONG).show()
            return false
        }

        if(password.length < 4) {
            //Toast.makeText(this, getString(R.string.password_needs_to_have), Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
}