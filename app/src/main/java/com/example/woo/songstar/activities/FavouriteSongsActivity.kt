package com.example.woo.songstar.activities

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.example.woo.songstar.R
import com.example.woo.songstar.adapters.FavouriteSongAdapter
import com.example.woo.songstar.adapters.MostListenedSongsAdapter
import com.example.woo.songstar.database.dao.ArtistDao
import com.example.woo.songstar.database.dao.FavoriteSongDao
import com.example.woo.songstar.database.dao.MostListenedSongDao
import com.example.woo.songstar.intefaces.ApiInterface
import com.example.woo.songstar.models.Artist
import com.example.woo.songstar.models.ItunesAPIResponse
import com.example.woo.songstar.models.Song
import com.example.woo.songstar.models.SongDetails
import com.example.woo.songstar.utils.AppConstants
import com.example.woo.songstar.utils.AppSharedPreferences
import com.example.woo.songstar.utils.CircleImage
import com.example.woo.songstar.utils.CommonBottomNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_artists.bottomBarMenu
import kotlinx.android.synthetic.main.activity_favourite_songs.*
import kotlinx.android.synthetic.main.activity_songs.rvSongs
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FavouriteSongsActivity : AppCompatActivity() {

    @Inject
    lateinit var favouriteDao: FavoriteSongDao
    @Inject
    lateinit var artistDao: ArtistDao
    @Inject
    lateinit var mostListenedSongDao: MostListenedSongDao
    @Inject
    lateinit var apiInterface: ApiInterface

    private var favouriteSongs: List<Song> = emptyList()
    private var adapter: FavouriteSongAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var artists: List<Artist> = emptyList()
    private var mostListenedSongs: List<Song> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_songs)
        this.initView()
        this.getFavouriteSongs()
    }

    private fun initView() {
        tvTitle.text = getString(R.string.favourite_songs)
        CommonBottomNavigationBar.setCommonBottomNavigationBar(this, bottomBarMenu, R.id.favourite)

        val uri = Uri.fromFile(
            getFileStreamPath(
                AppSharedPreferences.getString(this, AppSharedPreferences.USERNAME)
            )
        )
        val picture = File(uri.path!!)
        if (picture.exists()) {
            doAsync {
                val image: ImageDecoder.Source =
                    ImageDecoder.createSource(this@FavouriteSongsActivity.contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(image)
                this@FavouriteSongsActivity.ivProfile.setImageDrawable(CircleImage(bitmap))
            }
        }

        this.ivProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            overridePendingTransition(0, 0)
            this.finish()
        }
    }

    private fun setAdapter() {
        this.linearLayoutManager = LinearLayoutManager(this)
        this.rvSongs.layoutManager = this.linearLayoutManager
        this.adapter = FavouriteSongAdapter(this, this.favouriteSongs, this)
        this.rvSongs.adapter = this.adapter
    }

    private fun setMostListenedAdapter() {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val snapHelper: SnapHelper = PagerSnapHelper()
        this.rvMostListenedSongs.layoutManager = linearLayoutManager
        val mostListenedSongsAdapter = MostListenedSongsAdapter(this, this.mostListenedSongs, this)
        this.rvMostListenedSongs.adapter = mostListenedSongsAdapter
        this.rvMostListenedSongs.onFlingListener = null
        snapHelper.attachToRecyclerView(this.rvMostListenedSongs)
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)
        this.rvMostListenedSongs.addItemDecoration(dividerItemDecoration)
    }

    private fun getFavouriteSongs() {
        doAsync {
            this@FavouriteSongsActivity.favouriteSongs = this@FavouriteSongsActivity.favouriteDao.getFavouriteSongsByUserId(
                AppSharedPreferences.getString(this@FavouriteSongsActivity, AppSharedPreferences.USER_ID)!!.toInt())
            this@FavouriteSongsActivity.mostListenedSongs = this@FavouriteSongsActivity.mostListenedSongDao.getMostListenedSongsByUser(
                AppSharedPreferences.getString(this@FavouriteSongsActivity, AppSharedPreferences.USER_ID)!!.toInt())
            this@FavouriteSongsActivity.artists = this@FavouriteSongsActivity.artistDao.getAll()
            runOnUiThread{
                this@FavouriteSongsActivity.setAdapter()
                if(this@FavouriteSongsActivity.mostListenedSongs.isNotEmpty()) {
                    this@FavouriteSongsActivity.llMostListenedSongs.visibility = View.VISIBLE
                    this@FavouriteSongsActivity.setMostListenedAdapter()
                }
            }
        }
    }

    fun removeFromFavourites(song: Song) {
        doAsync {
            val favouriteSong = this@FavouriteSongsActivity.favouriteDao.getFavoriteSong(
                AppSharedPreferences.getString(this@FavouriteSongsActivity, AppSharedPreferences.USER_ID)!!.toInt(), song.id)
            if(favouriteSong.isNotEmpty()) {
                this@FavouriteSongsActivity.favouriteDao.delete(favouriteSong.first())
                this@FavouriteSongsActivity.favouriteSongs = this@FavouriteSongsActivity.favouriteDao.getFavouriteSongsByUserId(
                    AppSharedPreferences.getString(this@FavouriteSongsActivity, AppSharedPreferences.USER_ID)!!.toInt())
            }
            runOnUiThread{
                Toast.makeText(this@FavouriteSongsActivity, getString(R.string.favourite_removed_successfully), Toast.LENGTH_LONG).show()
                this@FavouriteSongsActivity.adapter?.update(this@FavouriteSongsActivity.favouriteSongs)
            }
        }
    }

    fun openSongDetails(song: Song) {
        doAsync{
            val artist = this@FavouriteSongsActivity.artistDao.getArtistById(song.artistId).first()
            val param = artist.name.toLowerCase(Locale.getDefault()).replace(" ", "+") + "+" + song.name.toLowerCase(
                Locale.getDefault()).replace(" ", "+")
            val call = this@FavouriteSongsActivity.apiInterface.search(param).execute()
            uiThread {
                if(call.isSuccessful) {
                    val response = call.body() as ItunesAPIResponse
                    if(response.results.size > 0) {
                        this@FavouriteSongsActivity.goToDetails(response.results.filter { it.artistName.isNotEmpty()
                                && it.trackName.isNotEmpty() && it.image.isNotEmpty() }.first())
                    } else {
                        Toast.makeText(this@FavouriteSongsActivity, getString(R.string.not_found), Toast.LENGTH_LONG).show()
                    }
                } else {
                    try {
                        Toast.makeText(this@FavouriteSongsActivity, call.errorBody().toString(), Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@FavouriteSongsActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }

    private fun goToDetails(songDetails: SongDetails) {
        val intent = Intent(this, SongDetailsActivity::class.java).apply {
            putExtra(AppConstants.SONG_DETAILS, songDetails)
        }
        startActivity(intent)
    }

    fun getArtistById(artistId: Int): Artist {
        return artists.filter { it.id == artistId }.first()
    }
}