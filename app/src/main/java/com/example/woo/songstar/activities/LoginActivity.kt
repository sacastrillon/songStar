package com.example.woo.songstar.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.woo.songstar.R
import com.example.woo.songstar.database.dao.UserDao
import com.example.woo.songstar.utils.AppSharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            if(this.fieldsValidation(this.etUserName.text.toString(), this.etPassword.text.toString())) {
                doAsync {
                    val user = this@LoginActivity.userDao.getUser(etUserName.text.toString(), etPassword.text.toString())
                    runOnUiThread {
                        if(user.isNotEmpty()) {
                            AppSharedPreferences.putString(this@LoginActivity, AppSharedPreferences.USER_ID, user.first().id.toString())
                            AppSharedPreferences.putString(this@LoginActivity, AppSharedPreferences.NAME, user.first().name)
                            AppSharedPreferences.putString(this@LoginActivity, AppSharedPreferences.USERNAME, user.first().username)
                            AppSharedPreferences.putString(this@LoginActivity, AppSharedPreferences.LANGUAGE, user.first().language)
                            AppSharedPreferences.putString(this@LoginActivity, AppSharedPreferences.PHOTO, user.first().photo)
                            AppSharedPreferences.putBoolean(this@LoginActivity, AppSharedPreferences.IS_ADMIN, user.first().isAdmin)
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