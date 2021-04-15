package com.example.woo.songstar.utils

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.woo.songstar.R
import com.example.woo.songstar.activities.ArtistsActivity
import com.example.woo.songstar.activities.FavouriteSongsActivity
import com.example.woo.songstar.activities.SongsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

object CommonBottomNavigationBar {

    fun setCommonBottomNavigationBar(activity: AppCompatActivity, bottomNavigationView: BottomNavigationView, currentSection: Int) {
        if (currentSection == 0) {
            for (i in 0 until bottomNavigationView.menu.size()) {
                bottomNavigationView.menu.getItem(i).isChecked = false
            }
        } else {
            bottomNavigationView.selectedItemId = currentSection
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.artist -> {
                    if (currentSection != R.id.artist) {
                        val intent = Intent(activity.applicationContext, ArtistsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        activity.startActivity(intent)
                        activity.overridePendingTransition(0, 0)
                        activity.finish()
                        return@setOnNavigationItemSelectedListener true
                    }
                }
                R.id.song -> {
                    if (currentSection != R.id.song) {
                        val intent =  Intent(activity.applicationContext, SongsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        activity.startActivity(intent)
                        activity.overridePendingTransition(0, 0)
                        activity.finish()
                        return@setOnNavigationItemSelectedListener true
                    }
                }

                R.id.favourite -> {
                    if (currentSection != R.id.favourite) {
                        val intent = Intent(
                            activity.applicationContext,
                            FavouriteSongsActivity::class.java
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        activity.startActivity(intent)
                        activity.overridePendingTransition(0, 0)
                        activity.finish()
                        return@setOnNavigationItemSelectedListener true
                    }
                }
            }
            false
        }
    }

}