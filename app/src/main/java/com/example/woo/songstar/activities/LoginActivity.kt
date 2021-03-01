package com.example.woo.songstar.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.woo.songstar.R
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.utils.AppSharedPreferences
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val db = AppDatabase.getDatabase(this)

        btnLogin.setOnClickListener {
            if(this.fieldsValidation(this.etUserName.text.toString(), this.etPassword.text.toString())) {
                doAsync {
                    val user = db.userDao().getUser(etUserName.text.toString(), etPassword.text.toString())
                    runOnUiThread {
                        if(user.isNotEmpty()) {
                            AppSharedPreferences.getInstance().putString(this@LoginActivity, AppSharedPreferences.USER_ID, user.first().id.toString())
                            AppSharedPreferences.getInstance().putString(this@LoginActivity, AppSharedPreferences.NAME, user.first().name)
                            AppSharedPreferences.getInstance().putString(this@LoginActivity, AppSharedPreferences.USERNAME, user.first().username)
                            AppSharedPreferences.getInstance().putString(this@LoginActivity, AppSharedPreferences.LANGUAGE, user.first().language)
                            AppSharedPreferences.getInstance().putString(this@LoginActivity, AppSharedPreferences.PHOTO, user.first().photo)
                            AppSharedPreferences.getInstance().putBoolean(this@LoginActivity, AppSharedPreferences.IS_ADMIN, user.first().isAdmin)
                            val intent = Intent(this@LoginActivity, ArtistsActivity::class.java).apply{}
                            startActivity(intent)
                            Toast.makeText(this@LoginActivity, "Welcome ${user.first().name}", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@LoginActivity, "User not found.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        llBack.setOnClickListener{
            this.finish()
        }
    }

    fun fieldsValidation(userName: String, password: String): Boolean {
        if(userName.isEmpty()) {
            //Toast.makeText(this, getString(R.string.a_username_must_be_typed), Toast.LENGTH_LONG).show()
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