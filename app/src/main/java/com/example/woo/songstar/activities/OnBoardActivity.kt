package com.example.woo.songstar.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.woo.songstar.R
import com.example.woo.songstar.database.dao.ArtistDao
import com.example.woo.songstar.database.dao.MostListenedSongDao
import com.example.woo.songstar.database.dao.SongDao
import com.example.woo.songstar.database.dao.UserDao
import com.example.woo.songstar.models.Artist
import com.example.woo.songstar.models.MostListenedSong
import com.example.woo.songstar.models.Song
import com.example.woo.songstar.models.User
import com.example.woo.songstar.utils.AppSharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.doAsync
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardActivity : AppCompatActivity() {

    @Inject
    lateinit var songDao: SongDao
    @Inject
    lateinit var artistDao: ArtistDao
    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var mostListenedSongDao: MostListenedSongDao

    private var userList = emptyList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)

        if(!AppSharedPreferences.getString(this, AppSharedPreferences.USERNAME).isNullOrEmpty()) {
            val intent = Intent(this, ArtistsActivity::class.java).apply{}
            startActivity(intent)
        } else {
            ivProfile.setOnClickListener { view ->
                this.onClick(view)
            }
            this.createData()
        }
    }

    private fun onClick(view: View) {
        if(view.id == R.id.ivProfile) {
            if(AppSharedPreferences.getString(this, AppSharedPreferences.USERNAME).isNullOrEmpty()) {
                val intent = Intent(this, LoginActivity::class.java).apply{}
                startActivity(intent)
            }
        }
    }

    private fun createData() {
        doAsync {
            val adminUser = User("Admin", "admin_user", "qwery123$", "English", "", true)
            userList = this@OnBoardActivity.userDao.getAll()
            if(userList.filter{ it.username == adminUser.username }.isEmpty()) {
                this@OnBoardActivity.userDao.insert(adminUser)
                this@OnBoardActivity.insertArtists()
            }
        }
    }

    private fun insertArtists() {
        doAsync {
            val theStrokes = Artist("The Strokes")
            val eagles = Artist("Eagles")
            val theBlackKeys = Artist("The Black Keys")

            this@OnBoardActivity.artistDao.insert(theStrokes)
            this@OnBoardActivity.artistDao.insert(eagles)
            this@OnBoardActivity.artistDao.insert(theBlackKeys)
            this@OnBoardActivity.insertSongs()
            this@OnBoardActivity.insertMostListenedSongs()
        }

    }

    private fun insertSongs() {
        doAsync {
            //Strokes
            var song = Song("What Ever Happened?", "Room On Fire", 1)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Reptilia", "Room On Fire", 1)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Automatic Stop", "Room On Fire", 1)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("12:51", "Room On Fire", 1)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("You Talk Way Too Much", "Room On Fire", 1)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Between Love & Hate", "Room On Fire", 1)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Meet Me in the Bathroom", "Room On Fire", 1)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Under Control", "Room On Fire", 1)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("The Way It Is", "Room On Fire", 1)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("The End Has No End", "Room On Fire", 1)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("I Can't Win", "Room On Fire", 1)
            this@OnBoardActivity.songDao.insert(song)

            //Eagles
            song = Song("Take It Easy", "Hotel California", 2)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Take It To The Limit", "Hotel California", 2)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("New Kid In Town", "Hotel California", 2)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("James Dean", "Hotel California", 2)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Good Day In Hell", "Hotel California", 2)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Witchy Woman", "Hotel California", 2)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Funk 49", "Hotel California", 2)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("One Of These Nights", "Hotel California", 2)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Hotel California", "Hotel California", 2)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Already Gone", "Hotel California", 2)
            this@OnBoardActivity.songDao.insert(song)

            //The Black Keys
            song = Song("Lonely Boy", "El Camino", 3)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Dead And Gone", "El Camino", 3)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Gold On The Ceiling", "El Camino", 3)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Little Black Submarines", "El Camino", 3)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Money Maker", "El Camino", 3)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Run Right Back", "El Camino", 3)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Sister", "El Camino", 3)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Hell Of A Season", "El Camino", 3)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Stop Stop", "El Camino", 3)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Nova Baby", "El Camino", 3)
            this@OnBoardActivity.songDao.insert(song)
            song = Song("Mind Eraser", "El Camino", 3)
            this@OnBoardActivity.songDao.insert(song)
        }
    }

    private fun insertMostListenedSongs() {
        doAsync {
            var song = MostListenedSong(1, 1, 4)
            this@OnBoardActivity.mostListenedSongDao.insert(song)
            song = MostListenedSong(5, 1, 2)
            this@OnBoardActivity.mostListenedSongDao.insert(song)
            song = MostListenedSong(8, 1, 1)
            this@OnBoardActivity.mostListenedSongDao.insert(song)

            song = MostListenedSong(10, 2, 4)
            this@OnBoardActivity.mostListenedSongDao.insert(song)
            song = MostListenedSong(20, 2, 2)
            this@OnBoardActivity.mostListenedSongDao.insert(song)
            song = MostListenedSong(15, 2, 1)
            this@OnBoardActivity.mostListenedSongDao.insert(song)
        }
    }
}