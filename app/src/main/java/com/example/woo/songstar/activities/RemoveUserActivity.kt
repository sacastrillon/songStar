package com.example.woo.songstar.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woo.songstar.R
import com.example.woo.songstar.adapters.RemoveUserAdapter
import com.example.woo.songstar.database.dao.UserDao
import com.example.woo.songstar.models.User
import com.example.woo.songstar.utils.AppSharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_remove_user.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.doAsync
import javax.inject.Inject

@AndroidEntryPoint
class RemoveUserActivity : AppCompatActivity() {

    @Inject
    lateinit var userDao: UserDao

    private var users: List<User> = emptyList()
    private var adapter: RemoveUserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_user)

        this.tvTitle.text = getString(R.string.remove_user)
        this.ivProfile.visibility = View.GONE
        this.ivBack.visibility = View.VISIBLE

        this.ivBack.setOnClickListener {
            this.finish()
        }

        this.getUsers()
    }

    private fun setAdapter() {
        this.rvUsers.layoutManager = LinearLayoutManager(this)
        this.adapter = RemoveUserAdapter(this, this.users, this)
        this.rvUsers.adapter = this.adapter
    }

    private fun getUsers() {
        doAsync {
            this@RemoveUserActivity.users = this@RemoveUserActivity.userDao.getAll().filter{ it.username != AppSharedPreferences.getString(this@RemoveUserActivity, AppSharedPreferences.USERNAME)}
            runOnUiThread{
                this@RemoveUserActivity.setAdapter()
            }
        }
    }

    fun removeUser(user: User) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_user))
            .setMessage(getString(R.string.do_you_want_to_delete_this_user))
            .setPositiveButton(R.string.yes) { _, _ ->
                doAsync {
                    this@RemoveUserActivity.userDao.delete(user)
                    runOnUiThread{
                        this@RemoveUserActivity.getUsers()
                        Toast.makeText(this@RemoveUserActivity, getString(R.string.user_deleted_successfully), Toast.LENGTH_LONG).show()
                    }
                }
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}