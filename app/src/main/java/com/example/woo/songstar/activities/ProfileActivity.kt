package com.example.woo.songstar.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.woo.songstar.R
import com.example.woo.songstar.customDialogs.CustomChangePasswordDialog
import com.example.woo.songstar.customDialogs.CustomEditLanguageDialog
import com.example.woo.songstar.customDialogs.CustomEditNameDialog
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.intefaces.DialogCallback
import com.example.woo.songstar.utils.AppSharedPreferences
import com.example.woo.songstar.utils.CircleImage
import com.example.woo.songstar.utils.CommonBottomNavigationBar
import com.example.woo.songstar.utils.PermissionUtility
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.doAsync
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class ProfileActivity : AppCompatActivity() {

    private var preferences = AppSharedPreferences()
    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        this.initView()
    }

    private fun initView() {
        CommonBottomNavigationBar.getInstance().setCommonBottomNavigationBar(this, this.bottomBarMenu, 0)
        this.preferences = AppSharedPreferences.getInstance()
        this.db = AppDatabase.getDatabase(this@ProfileActivity)
        this.tvTitle.text = getString(R.string.profile)
        this.ivProfile.visibility = View.GONE
        if(preferences.getBoolean(this, AppSharedPreferences.IS_ADMIN)) {
            this.cvAdminOptions.visibility = View.VISIBLE
        }

        this.setAvatar()

        this.tvName.text = preferences.getString(this, AppSharedPreferences.NAME)
        this.etLanguage.setText(preferences.getString(this, AppSharedPreferences.LANGUAGE))

        this.rlProfilePic.setOnClickListener {
            if (PermissionUtility.checkPermissionIsGrant(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                this.openGalleryDialog()
            } else {
                PermissionUtility.isPermissionGranted(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            }
        }

        this.ivEditName.setOnClickListener {
            CustomEditNameDialog(this, this.tvName.text.toString(), object : DialogCallback {
                override fun submit(data: String, i: Int) {
                    if(i == 1) {
                        this@ProfileActivity.updateUserName(data)
                    }
                }
            }).show()
        }

        this.ivEditLanguage.setOnClickListener {
            CustomEditLanguageDialog(this, this.etLanguage.text.toString(), object: DialogCallback {
                override fun submit(data: String, i: Int) {
                    if(i == 1) {
                        this@ProfileActivity.updateLanguage(data)
                    }
                }
            }).show()
        }

        this.tvChangePassword.setOnClickListener {
            CustomChangePasswordDialog(this, object: DialogCallback {
                override fun submit(data: String, i: Int) {
                    if(i == 1) {
                        Toast.makeText(this@ProfileActivity, getString(R.string.password_updated_successfully), Toast.LENGTH_LONG).show()
                    }
                }
            }).show()
        }

        this.tvAddUsers.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            this.startActivity(intent)
        }

        this.tvRemoveUsers.setOnClickListener {
            val intent = Intent(this, RemoveUserActivity::class.java)
            this.startActivity(intent)
        }

        this.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(getString(R.string.do_you_want_to_logout))
                .setPositiveButton(R.string.yes) { _, _ ->
                    this.preferences.clearAllPreferences(this)
                    val intent = Intent(this, LoginActivity::class.java).apply{
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    this.startActivity(intent)
                    this.finish()
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setAvatar() {
        val uri = Uri.fromFile(getFileStreamPath(preferences.getString(this, AppSharedPreferences.USERNAME)))
        val picture = File(uri.path.toString())
        if(picture.exists()) {
            val image: ImageDecoder.Source = ImageDecoder.createSource(this.contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(image)
            this.ivProfilePic.setImageDrawable(CircleImage(bitmap))
        }
    }

    private fun openGalleryDialog() {
        val items = arrayOf("Photo", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.select_a_mode))
            .setItems(items) { _, which ->
                when(which) {
                    0 -> {
                        if(PermissionUtility.checkPermissionIsGrant(this, Manifest.permission.CAMERA)) {
                            val openGallery = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            this.startActivityForResult(openGallery, PHOTO_MODE)
                        } else {
                            PermissionUtility.isPermissionGranted(this, arrayOf(Manifest.permission.CAMERA), 1)
                        }
                    }
                    1 -> {
                        if(PermissionUtility.checkPermissionIsGrant(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            val openGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            openGallery.type = "image/*"
                            this.startActivityForResult(openGallery, GALLERY_MODE)
                        } else {
                            PermissionUtility.isPermissionGranted(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2)
                        }
                    }
                }
            }
        builder.create()
        builder.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            0 -> this.openGalleryDialog()
            1 -> {
                val openGallery = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                this.startActivityForResult(openGallery, PHOTO_MODE)
            }
            2 -> {
                val openGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                openGallery.type = "image/*"
                this.startActivityForResult(openGallery, GALLERY_MODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_MODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            ivProfilePic.setImageDrawable(CircleImage(imageBitmap))
            this.saveImage(imageBitmap)
            this.setAvatar()
        } else if(requestCode == GALLERY_MODE && resultCode == RESULT_OK) {
            val inputStream: InputStream? = contentResolver.openInputStream(data?.data!!)
            val bufferedInputStream = BufferedInputStream(inputStream)
            val imageBitmap = BitmapFactory.decodeStream(bufferedInputStream)
            ivProfilePic.setImageDrawable(CircleImage(imageBitmap))
            this.saveImage(imageBitmap)
            this.setAvatar()
        }
    }

    private fun updateUserName(name: String) {
        doAsync {
            try{
                val user = this@ProfileActivity.db?.userDao()?.getByUserName(this@ProfileActivity.preferences.getString(this@ProfileActivity, AppSharedPreferences.USERNAME))?.first()
                this@ProfileActivity.db?.userDao()?.updateName(name, user?.username.toString())
                runOnUiThread{
                    this@ProfileActivity.tvName.text = name
                    this@ProfileActivity.preferences.putString(this@ProfileActivity, AppSharedPreferences.NAME, name)
                }
            } catch (e: Exception) {
                runOnUiThread{
                    Toast.makeText(this@ProfileActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateLanguage(language: String) {

        doAsync {
            try{
                val user = this@ProfileActivity.db?.userDao()?.getByUserName(this@ProfileActivity.preferences.getString(this@ProfileActivity, AppSharedPreferences.USERNAME))?.first()
                this@ProfileActivity.db?.userDao()?.updateLanguage(language, user?.username.toString())
                runOnUiThread{
                    this@ProfileActivity.etLanguage.setText(language)
                    this@ProfileActivity.preferences.putString(this@ProfileActivity, AppSharedPreferences.LANGUAGE, language)
                }
            } catch (e: Exception) {
                runOnUiThread{
                    Toast.makeText(this@ProfileActivity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveImage(image: Bitmap) {
        try {
            val fileName = this.preferences.getString(this, AppSharedPreferences.USERNAME)
            val fileOutputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val PHOTO_MODE = 0
        const val GALLERY_MODE = 1
    }
}