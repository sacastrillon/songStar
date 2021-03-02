package com.example.woo.songstar.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.woo.songstar.R
import com.example.woo.songstar.database.AppDatabase
import com.example.woo.songstar.models.Artist
import com.example.woo.songstar.models.MostListenedSong
import com.example.woo.songstar.models.Song
import com.example.woo.songstar.models.User
import com.example.woo.songstar.utils.AppSharedPreferences
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.doAsync

class OnBoardActivity : AppCompatActivity() {

    private var userList = emptyList<User>()
    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)

        if(!AppSharedPreferences.getInstance().getString(this, AppSharedPreferences.USERNAME).isNullOrEmpty()) {
            val intent = Intent(this, ArtistsActivity::class.java).apply{}
            startActivity(intent)
        } else {
            db = AppDatabase.getDatabase(this)
            ivProfile.setOnClickListener { view ->
                this.onClick(view)
            }
            this.createData()
        }
    }

    private fun onClick(view: View) {
        if(view.id == R.id.ivProfile) {
            if(AppSharedPreferences.getInstance().getString(this, AppSharedPreferences.USERNAME).isNullOrEmpty()) {
                val intent = Intent(this, LoginActivity::class.java).apply{}
                startActivity(intent)
            }
        }
    }

    private fun createData() {
        doAsync {
            val adminUser = User("Admin", "admin_user", "qwery123$", "English", "", true)
            userList = db?.userDao()?.getAll()!!
            if(userList.filter{ it.username == adminUser.username }.isEmpty()) {
                db?.userDao()?.insert(adminUser)
                this@OnBoardActivity.insertArtists()
            }
        }
    }

    private fun insertArtists() {
        doAsync {
            val theStrokes = Artist("The Strokes")
            val eagles = Artist("Eagles")
            val theBlackKeys = Artist("The Black Keys")

            db?.artistDao()?.insert(theStrokes)
            db?.artistDao()?.insert(eagles)
            db?.artistDao()?.insert(theBlackKeys)
            this@OnBoardActivity.insertSongs()
            this@OnBoardActivity.insertMostListenedSongs()
        }

    }

    private fun insertSongs() {
        doAsync {
            //Strokes
            var song = Song("What Ever Happened?", "Room On Fire", 1)
            db?.songDao()?.insert(song)
            song = Song("Reptilia", "Room On Fire", 1)
            db?.songDao()?.insert(song)
            song = Song("Automatic Stop", "Room On Fire", 1)
            db?.songDao()?.insert(song)
            song = Song("12:51", "Room On Fire", 1)
            db?.songDao()?.insert(song)
            song = Song("You Talk Way Too Much", "Room On Fire", 1)
            db?.songDao()?.insert(song)
            song = Song("Between Love & Hate", "Room On Fire", 1)
            db?.songDao()?.insert(song)
            song = Song("Meet Me in the Bathroom", "Room On Fire", 1)
            db?.songDao()?.insert(song)
            song = Song("Under Control", "Room On Fire", 1)
            db?.songDao()?.insert(song)
            song = Song("The Way It Is", "Room On Fire", 1)
            db?.songDao()?.insert(song)
            song = Song("The End Has No End", "Room On Fire", 1)
            db?.songDao()?.insert(song)
            song = Song("I Can't Win", "Room On Fire", 1)
            db?.songDao()?.insert(song)

            //Eagles
            song = Song("Take It Easy", "Hotel California", 2)
            db?.songDao()?.insert(song)
            song = Song("Take It To The Limit", "Hotel California", 2)
            db?.songDao()?.insert(song)
            song = Song("New Kid In Town", "Hotel California", 2)
            db?.songDao()?.insert(song)
            song = Song("James Dean", "Hotel California", 2)
            db?.songDao()?.insert(song)
            song = Song("Good Day In Hell", "Hotel California", 2)
            db?.songDao()?.insert(song)
            song = Song("Witchy Woman", "Hotel California", 2)
            db?.songDao()?.insert(song)
            song = Song("Funk 49", "Hotel California", 2)
            db?.songDao()?.insert(song)
            song = Song("One Of These Nights", "Hotel California", 2)
            db?.songDao()?.insert(song)
            song = Song("Hotel California", "Hotel California", 2)
            db?.songDao()?.insert(song)
            song = Song("Already Gone", "Hotel California", 2)
            db?.songDao()?.insert(song)

            //The Black Keys
            song = Song("Lonely Boy", "El Camino", 3)
            db?.songDao()?.insert(song)
            song = Song("Dead And Gone", "El Camino", 3)
            db?.songDao()?.insert(song)
            song = Song("Gold On The Ceiling", "El Camino", 3)
            db?.songDao()?.insert(song)
            song = Song("Little Black Submarines", "El Camino", 3)
            db?.songDao()?.insert(song)
            song = Song("Money Maker", "El Camino", 3)
            db?.songDao()?.insert(song)
            song = Song("Run Right Back", "El Camino", 3)
            db?.songDao()?.insert(song)
            song = Song("Sister", "El Camino", 3)
            db?.songDao()?.insert(song)
            song = Song("Hell Of A Season", "El Camino", 3)
            db?.songDao()?.insert(song)
            song = Song("Stop Stop", "El Camino", 3)
            db?.songDao()?.insert(song)
            song = Song("Nova Baby", "El Camino", 3)
            db?.songDao()?.insert(song)
            song = Song("Mind Eraser", "El Camino", 3)
            db?.songDao()?.insert(song)
        }
    }

    private fun insertMostListenedSongs() {
        doAsync {
            var song = MostListenedSong(1, 1, 4)
            db?.mostListenedSongDao()?.insert(song)
            song = MostListenedSong(5, 1, 2)
            db?.mostListenedSongDao()?.insert(song)
            song = MostListenedSong(8, 1, 1)
            db?.mostListenedSongDao()?.insert(song)

            song = MostListenedSong(10, 2, 4)
            db?.mostListenedSongDao()?.insert(song)
            song = MostListenedSong(20, 2, 2)
            db?.mostListenedSongDao()?.insert(song)
            song = MostListenedSong(15, 2, 1)
            db?.mostListenedSongDao()?.insert(song)
        }
    }
}